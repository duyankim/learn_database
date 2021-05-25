package jdbc.Parkinglot;

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
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		
		File f = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\한국교통안전공단_전국공영주차장정보_20191224-3.csv");	// 데이터 파일을 불러온다.
		BufferedReader br = new BufferedReader(new FileReader(f));						// 버퍼 리더인스턴스를 생성한다.
		
		String readtxt;																	// 한 줄을 먼저 읽는다.
		if ((readtxt = br.readLine()) == null) {										// 첫 번째 줄에 데이터가 없다면
			System.out.printf("빈 파일입니다\n");											// 파일은 비어있는 것이다.
			return;																		// 반환값은 없다.
		}
		String[] field_name=readtxt.split(",");											// 필드명을 배열로 받는다.
		
		int LineCnt = 0;																// line 수를 세는 변수를 선언한다.
		while((readtxt = br.readLine()) != null) {										// 
			String[] field = readtxt.replace("\"","").replace("'","").split(",");
			String QueryTxt = null;
			String[] fieldData = {field[0], field[1], field[2], field[3], field[4], field[5], field[6], field[7], field[8], field[16], field[22]};
				
			try {
				if (field[3] != null) {
					QueryTxt = makeQueryTxt(fieldData);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				readtxt = readtxt + br.readLine();
				field = readtxt.split(",");
				QueryTxt = makeQueryTxt(fieldData);
				LineCnt -= 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt.execute(QueryTxt);
			System.out.printf("%d번째 항목 Insert OK \n[%s]\n", LineCnt, QueryTxt);
			
			LineCnt++;
		}
		br.close();

		stmt.close();
		conn.close();
	}	

	static String makeQueryTxt (String[] field) throws ParseException {
		List<String> field_name = new ArrayList<>(
				Arrays.asList("lot_id", "lot_name", "longitude", "latitude", "lot_acc_type"
				,"lot_type", "lot_addr_land", "lot_addr_road", "lot_cnt", "fee", "region_id"));
		
		StringBuffer values = new StringBuffer();		
		String fields;
		String QueryTxt = null;
		List<Integer> removeIdx = new ArrayList<>();
		
		try {
			for (int idx = 0; idx < 11; idx++) {
				if (field[idx] == null || field[idx].isBlank()) {
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
		
		fields = "insert into public_parking_lot(" + String.join(", ", field_name) + ") values(";
		QueryTxt = fields + values.substring(0, values.length()-2) + ");";
		return QueryTxt;
	}
	
	

}
