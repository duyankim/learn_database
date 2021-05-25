package jdbc.Parkinglot;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - 주차장 데이터
 * kopo03 김도연
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parkinglot2 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");										// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(									// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();										// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		
		File f = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\한국교통안전공단_전국공영주차장정보_20191224-3.csv");	// 데이터 파일을 불러온다.
		BufferedReader br = new BufferedReader(new FileReader(f));						// 버퍼 리더인스턴스를 생성한다.
		
		String readtxt;																	// 한 줄을 먼저 읽는다.
		if ((readtxt = br.readLine()) == null) {										// 첫 번째 줄에 데이터가 없다면
			System.out.printf("빈 파일입니다\n");											// 파일은 비어있는 것이다.
			return;																		// 반환값은 없다.
		}
		String[] field_name=readtxt.split(",");											// 필드명을 배열로 받는다.
		
		int LineCnt = 0;																// line 수를 세는 변수를 선언한다.
		while((readtxt = br.readLine()) != null) {										// 파일의 1줄씩 읽어들인다. 
			String[] field = readtxt.replace("\"","").replace("'","").split(",");		// 콤마로 구분된 파일을 각각의 필드로 나누어 배열로 만든다.
			String QueryTxt = null;														// 쿼리 텍스트를 넣을 String 변수를 선언해준다.
			String[] fieldData = {field[0], field[1], field[2], field[3], field[4], 	// 필요한 필드의 데이터만 넣은 배열을 만들었다.
					field[5], field[6], field[7], field[8], field[16], field[22]};
				
			try {																		// try-catch 문으로 예외 처리를 해준다.
				if (field[3] != null) {													// 필드 값 중간에 들어간 개행문자로 인해서
					QueryTxt = makeQueryTxt(fieldData);									// makeQueryTxt는 쿼리문을 만드는 함수다.
				}
			} catch (ArrayIndexOutOfBoundsException e) {								// 만약 배열의 인덱스가 모자랄 경우 예외처리를 한다.
				readtxt = readtxt + br.readLine();										// 필드 속의 , 때문에 한 줄이 통째로 읽히지 않은 경우로,
				field = readtxt.split(",");												// 바로 다음 라인을 읽어들여서 문제의 라인에 붙여준다.
				QueryTxt = makeQueryTxt(fieldData);										// 새로 만든 배열을 makeQueryTxt 함수에 인자로 전달한다.
				LineCnt -= 1;															// 라인이 중복 카운트되지 않도록 한다.
			} catch (Exception e) {														// 그 외의 모든 예외는 Exception이 받는다.
				e.printStackTrace();													// 예외가 발생할 경우 예외를 콘솔에 출력한다.
			}
			stmt.execute(QueryTxt);														// 만들어진 쿼리문을 실행한다.
			System.out.printf("%d번째 항목 Insert OK \n[%s]\n", LineCnt, QueryTxt);		// 실행이 완료되면 프린트한다.
			LineCnt++;																	// 라인을 1씩 증가시킨다.
		}
		br.close();																		// BufferedReader 인스턴스를 닫는다.
		stmt.close();																	// Statement 인스턴스를 닫는다.
		conn.close();																	// Connection 인스턴스를 닫는다.
	}	

	static String makeQueryTxt (String[] field) throws ParseException {
		List<String> field_name = new ArrayList<>(										// 필드 이름을 넣은 ArrayList를 만들었다.
				Arrays.asList("lot_id", "lot_name", "longitude", "latitude", "lot_acc_type"	// 가변 배열이기 때문에 Array를 사용하지 않았다.
				,"lot_type", "lot_addr_land", "lot_addr_road", "lot_cnt", "fee", "region_id"));
		
		StringBuffer values = new StringBuffer();										// 가변 문자열일 경우 String보다 StringBuffer가 좋다.																// 필드 
		String QueryTxt = null;															// 쿼리문을 넣을 문자열을 선언한다.
		List<Integer> removeIdx = new ArrayList<>();									// 삭제할 필드의 인덱스를 저장하는 ArrayList다.
		try {																			// null값을 넣으려면 insert할 때 해당 null값의 필드를 빼고 insert하면 된다.
			for (int idx = 0; idx < 11; idx++) {										// 따라서 빈칸이거나 데이터가 올바르지 않은 필드를 제외시키기 위해서
				if (field[idx] == null || field[idx].isBlank()) {						//
					removeIdx.add(idx);
				} else {
					if (idx == 0 || idx == 2 || idx == 3 || idx == 10 && field[idx] != null) {
						values.append(field[idx] + ", ");
					} else if(field[idx] != null) {
						values.append("'" + field[idx] + "', ");
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		for (int i : removeIdx) {
			field_name.remove(i);
		}
		
		String fields = "insert into public_parking_lot(" + String.join(", ", field_name) + ") values(";
		QueryTxt = fields + values.substring(0, values.length()-2) + ");";
		return QueryTxt;
	}
	
	

}
