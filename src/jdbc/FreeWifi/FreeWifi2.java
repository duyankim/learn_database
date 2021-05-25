package jdbc.FreeWifi;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - freewifi2
 * kopo03 김도연
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FreeWifi2 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");													// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(												// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();													// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		
		File f = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\전국무료와이파이표준데이터7.txt");	// 데이터 파일을 불러온다.
		BufferedReader br = new BufferedReader(new FileReader(f));									// 버퍼 리더인스턴스를 생성한다.
		String readtxt;																				// 한 줄을 먼저 읽는다.
		if ((readtxt = br.readLine()) == null) {													// 첫 번째 줄에 데이터가 없다면
			System.out.printf("빈 파일입니다\n");														// 파일은 비어있는 것이다.
			return;																					// 반환값은 없다.
		}
		String[] field_name=readtxt.split(",");														// 필드명을 배열로 받는다.
		
		int LineCnt = 0;																			// line 수를 세는 변수를 선언한다.
		while((readtxt = br.readLine()) != null) {													// 파일의 1줄씩 읽어들인다. 파일이 끝나서 null 값이 나올 때까지. 
			String[] field = readtxt.replace("\"","").split("\t");									// tab으로 구분된 파일을 각각의 필드로 나누어 배열로 만든다.
			String QueryTxt = null;																	// 쿼리 텍스트를 넣을 String 변수를 선언해준다.
				
			try {																					// try-catch 문으로 예외 처리를 해준다.
				if (field[3] != null) {																// 필드 값 중간에 들어간 개행문자로 인해서
					QueryTxt = String.format("insert into freewifi("								// 배열이 제대로 만들어지지 않았을 경우를 대비해 if 문을 사용한다.
								+ "inst_place, inst_place_detail, inst_city, inst_country, inst_place_flag,"	// freewifi table에 데이터를 넣는 쿼리를 쓴다.
								+ "service_provider, wifi_ssid, inst_date, place_addr_road, place_addr_land,"	// 먼저 insert into table명 명령어를 쓴다.
								+ "manage_office, manage_office_phone, latitude, longitude, write_date)"		// 그 다음 해당 데이터의 필드 명을 적는다.
								+ "values ("																	// 필드의 값들을 넣을 때는 values가 붙어야 한다.
								+ "'%s', '%s', '%s', '%s', '%s',"												// varchar데이터를 insert할 때는 
								+ "'%s', '%s', '%s', '%s', '%s',"												// '로 문자를 감싸야 한다.
								+ "'%s', '%s', %s, %s, '%s');", 												// int 타입인 lat, lng는 '로 감싸지 않는다.
								field[0], field[1], field[2], field[3], field[4],								// 해당 데이터 포멧에 맞추어 쿼리문을 쓰기 위해
								field[5], field[6], stringToDate(field[7]), field[8], field[9],					// String의 format내장 함수를 사용한다.
								field[10], field[11], field[12], field[13], stringToDate(field[14]));			// stringToDate는 sql.Date를 반환한다.
					
				}
			} catch (ArrayIndexOutOfBoundsException e) {											// 만약 배열의 인덱스가 모자랄 경우 예외처리를 한다.
				readtxt = readtxt + br.readLine();													// 필드 속의 , 때문에 한 줄이 통째로 읽히지 않은 경우로,
				field = readtxt.split("\t");														// 바로 다음 라인을 읽어들여서 문제의 라인에 붙여준다.
				LineCnt -= 1;																		// 다시 field에 배열을 만들고, 라인이 중복 카운트되지 않도록 한다.
			} catch (Exception e) {																	// 그 외의 모든 예외는 Exception이 받는다.
				e.printStackTrace();																// 예외가 발생할 경우 예외를 콘솔에 출력한다.
			}
			stmt.execute(QueryTxt);																	// statement가 string에 담은 쿼리문을 실행시킨다.
			System.out.printf("%d번째 항목 Insert OK \n[%s]\n", LineCnt, QueryTxt);					// 각 항목이 db에 잘 들어갔는지 확인하기 위해 프린트해본다.
			LineCnt++;																				// 이 과정을 한 번 반복할 때마다 라인이 1줄 는다.
		}
		br.close();																					// BufferedReader 인스턴스를 닫는다.
		stmt.close();																				// Statement 인스턴스를 닫는다.
		conn.close();																				// Connection 인스턴스를 닫는다.
	}	
	
	static java.sql.Date stringToDate(String data) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");								// 자바에는 Date포맷이 두가지 있는데,
		Date parsed;																				// 하나는 java.util.Date이고, 다른 하나는 java.sql.Date다.
		try {																						// sql.Date는 정해진 형식으로 년, 월, 일이 모두 들어가야한다.
			parsed = format.parse(data);															// 따라서 csv파일의 날짜 데이터를 가져와서 포맷에 맞게
		} catch (Exception e) {																		// yyyy-MM-dd로 맞춘다.
			parsed = format.parse("1900-01-01");													// 만약 null값이 나올 경우 데이터를 1900-01-01로 넣어주었다.
		}																							// 나중에 처리를 하게 된다면 2000년 이후의 데이터만
		java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());								// select해야 한다.
		sqlDate = null;																				// parsing된 util.Date객체에서 시간 정보를 가져와
        return sqlDate;																				// sql.Date로 반환한다.
	}

}
