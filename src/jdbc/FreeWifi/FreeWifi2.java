package jdbc.FreeWifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class FreeWifi2 {

	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		
		File f = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\전국무료와이파이표준데이터7.txt");	// 데이터 파일을 불러온다.
		BufferedReader br = new BufferedReader(new FileReader(f));						// 버퍼 리더인스턴스를 생성한다.
		
		String readtxt;																	// 한 줄을 먼저 읽는다.
		if ((readtxt = br.readLine()) == null) {										// 첫 번째 줄에 데이터가 없다면
			System.out.printf("빈 파일입니다\n");											// 파일은 비어있는 것이다.
			return;																		// 반환값은 없다.
		}
		String[] field_name=readtxt.split(",");											// 필드명을 배열로 받는다.
		
		int LineCnt = 0;																// line 수를 세는 변수를 선언한다.
		while((readtxt = br.readLine()) != null) {										// 
			String[] field = readtxt.replace("\"","").split("\t");
			String QueryTxt=null;
				
			try {
				if (field[3] != null) {
					
					QueryTxt = String.format("insert into freewifi("
								+ "inst_place, inst_place_detail, inst_city, inst_country, inst_place_flag,"
								+ "service_provider, wifi_ssid, inst_date, place_addr_road, place_addr_land,"
								+ "manage_office, manage_office_phone, latitude, longitude, write_date)"
								+ "values ("
								+ "'%s', '%s', '%s', '%s', '%s',"
								+ "'%s', '%s', '%s', '%s', '%s',"
								+ "'%s', '%s', %s, %s, '%s');", 
								field[0], field[1], field[2], field[3], field[4],
								field[5], field[6], stringToDate(field[7]), field[8], field[9],
								field[10], field[11], field[12], field[13], stringToDate(field[14]));
					
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				readtxt = readtxt + br.readLine();
				field = readtxt.split("\t");
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
	
	static java.sql.Date stringToDate(String data) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed;
		try {
			parsed = format.parse(data);
		} catch (Exception e) {
			parsed = format.parse("1900-01-01");
		}
		java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
		sqlDate = null;
        return sqlDate;
	}

}
