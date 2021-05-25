package jdbc;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - examtable1
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Examtable1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// 실행할 jdbc 드라이버를 로드한다.
																				// Class 클래스는 JVM에서 동작할 클래스들의
																				// 정보를 묘사하는 일종의 메타 클래스다.
		Connection conn = DriverManager.getConnection(							// DriverManager: 로드된 드라이버를 통해서 Connection을 활성화해주는 객체
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");	// mysql이 있는 ip:port/DBname, uid, password
		Statement stmt = conn.createStatement();								// Statement: SQL을 실행하는 객체
		stmt.execute("create table examtable2(" 								// examtable2라는 테이블을 생성하는 sql명령문
					+ "name varchar(20), " 										// varchar: 가변길이 문자열. 여기서는 최대 50byte까지 넣을 수 있다.
					+ "studentid int not null primary key, "					// studentid는 primary key로 설정하고 null값을 허용하지 않는다.
					+ "kor int, " 												// 한국어는 int 데이터를 받는다.
					+ "eng int, " 												// 영어는 int 데이터를 받는다.
					+ "mat int)" 												// 수학은 int 데이터를 받는다.
					+ "DEFAULT CHARSET=utf8;");									// charset을 utf8로 설정한다.

		stmt.close();															// 역순으로 각 클래스를 닫아준다.
		conn.close();															// Statement를 먼저 닫고, Connection을 닫는다.
	}
}
