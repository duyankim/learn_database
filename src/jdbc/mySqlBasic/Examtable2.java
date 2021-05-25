package jdbc;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - examtable2
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Examtable2 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
																				// 주어진 접속상에서 SQL문을 실행하기 위한 컨테이너로써 동작한다.
		stmt.execute("delete from examtable;");									// execute로 examtable의 데이터를 지우는 sql명령어를 실행한다.
																				// executeQuery 메소드는 하나의 result set을 만드는 SQL문들에서 사용한다.
		stmt.close();															// executeQuery는 ResultSet객체를 반환한다. SELECT문에서 사용한다.
		conn.close();															// execute는 boolean값을 반환한다. INSERT, UPDATE, DELETE문에서 사용한다.
	}
}
