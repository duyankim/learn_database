package jdbc.FreeWifi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - freewifi1
 * kopo03 김도연
 */

public class FreeWifiMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();
		
		stmt.execute("drop table freewifi;");
		stmt.close();
		conn.close();
	}

}
