package jdbc.mySqlBasic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("select * from examtable;");
		
		while (rset.next()) {
			System.out.println("이름 : " + rset.getString(1));
			System.out.println("학번 : " + rset.getInt(2));
			System.out.println("국어 : " + rset.getInt(3));
			System.out.println("영어 : " + rset.getInt(4));
			System.out.println("수학 : " + rset.getInt(5));
			System.out.println("=============================");
		}
		rset.close();
		stmt.close();
		conn.close();
	}
}
