package jdbc.FreeWifi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FreeWifi3_2 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();
		
		double lat = 37.3860521;
		double lng = 127.1214038;
		
		String QueryTxt;
		
		QueryTxt = "select * from freewifi";
		
		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt = 0;
		while(rset.next()) {
			System.out.printf("*(%d)***********************************\n", iCnt++);
			System.out.printf("설치장소명:       %s\n", rset.getString(1));
			System.out.printf("설치장소상세:     %s\n", rset.getString(2));
			System.out.printf("설치시도명:       %s\n", rset.getString(3));
			System.out.printf("설치시군구명:     %s\n", rset.getString(4));
			System.out.printf("설치시설구분:     %s\n", rset.getString(5));
			System.out.printf("서비스제공사명:   %s\n", rset.getString(6));
			System.out.printf("와이파이SSID:     %s\n", rset.getString(7));
			System.out.printf("설치년월:         %s\n", rset.getString(8));
			System.out.printf("소재지도로명주소: %s\n", rset.getString(9));
			System.out.printf("소재지지번주소:   %s\n", rset.getString(10));
			System.out.printf("관리기관명:       %s\n", rset.getString(11));
			System.out.printf("관리기관전화번호:  %s\n", rset.getString(12));
			System.out.printf("위도:              %s\n", rset.getString(13));
			System.out.printf("경도:              %s\n", rset.getString(14));
			System.out.printf("데이터기준일자:    %s\n", rset.getString(15));
		}
	}

}
