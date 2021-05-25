package jdbc.Parkinglot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Parkinglot3 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();
		
		double lat = 37.3860521;
		double lng = 127.1214038;
		
		String QueryTxt;
		QueryTxt = String.format("select * from public_parking_lot where "
				+"SQRT( POWER( latitude-%f,2) + POWER (longitude-%f,2 ) ) = "
				+"(select MIN( SQRT( POWER( latitude-%f,2) + POWER (longitude-%f,2 ) ) ) from public_parking_lot);"
				, lat, lng, lat, lng);

		ResultSet rset = stmt.executeQuery(QueryTxt);
		int iCnt = 0;
		while(rset.next()) {
			System.out.printf("*(%d)***********************************\n", iCnt++);
			System.out.printf("주차장관리번호:  %s\n", rset.getString(1));
			System.out.printf("주차장명:       %s\n", rset.getString(2));
			System.out.printf("경도:          %s\n", rset.getString(3));
			System.out.printf("위도:          %s\n", rset.getString(4));
			System.out.printf("주차장구분:     %s\n", rset.getString(5));
			System.out.printf("주차장유형:     %s\n", rset.getString(6));
			System.out.printf("주차장지번주소:  %s\n", rset.getString(7));
			System.out.printf("주차장도로명주소:%s\n", rset.getString(8));
			System.out.printf("주차구획수:     %s\n", rset.getString(9));
			System.out.printf("요금정보:       %s\n", rset.getString(10));
			System.out.printf("지역코드:       %s\n", rset.getString(11));
		}
	}
}
