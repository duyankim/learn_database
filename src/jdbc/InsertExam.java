package jdbc;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - examtable3
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertExam {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");																		// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(																	// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();																		// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('효민', 209901, 95, 100, 95);");  	// execute 메소드로 table에 데이터를 INSERT한다.
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('보람', 209902, 95, 95, 95);");		// execute 메소드로 table에 데이터를 INSERT한다.
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('은정', 209903, 100, 100, 100);");	// execute 메소드로 table에 데이터를 INSERT한다.
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('지연', 209904, 100, 95, 90);");	// execute 메소드로 table에 데이터를 INSERT한다.
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('소연', 209905, 80, 100, 70);");	// execute 메소드로 table에 데이터를 INSERT한다.
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('큐리', 209906, 100, 100, 70);");	// execute 메소드로 table에 데이터를 INSERT한다.
		stmt.execute("insert into examtable (name, studentid, kor, eng, mat) values ('화영', 209907, 70, 70, 70);");		// execute 메소드로 table에 데이터를 INSERT한다.
		
		stmt.close();																									// Statemet 인스턴스를 닫는다.
		conn.close();																									// Connection 인스턴스를 닫는다.
	}
}
