package jdbc.mySqlBasic;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - ResultSet
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// 실행할 jdbc 드라이버를 로드한다.
																				// Class 클래스는 JVM에서 동작할 클래스들의
																				// 정보를 묘사하는 일종의 메타 클래스다.
		Connection conn = DriverManager.getConnection(							// DriverManager: 로드된 드라이버를 통해서 Connection을 활성화해주는 객체
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");	// mysql이 있는 ip:port/DBname, uid, password
		Statement stmt = conn.createStatement();								// Statement: SQL을 실행하는 객체
		ResultSet rset = stmt.executeQuery("show databases;");					// ResultSet : query를 실행하고 반환되는 결과
		while (rset.next()) {													// rset에 다음 값이 존재한다면
			System.out.println("값 : " + rset.getString(1));						// rset의 값을 가져온다.
		}
		rset.close();															// resultset 인스턴스를 닫는다
		stmt.close();															// statement 인스턴스를 닫는다
		conn.close();															// connection 인스턴스를 닫는다
	}
}

