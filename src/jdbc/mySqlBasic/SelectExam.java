package jdbc;

import java.io.UnsupportedEncodingException;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - examtable4
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectExam {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		ResultSet rset = stmt.executeQuery("select * from examtable");			// select문은 executeQuery 메소드로 ResultSet 객체를 생성한다.
		
		System.out.printf("  이름 학번  국어 영어 수학\n");							// 필드명을 프린트한다.
		while(rset.next()) {													// ResultSet의 다음 열이 있을 경우 while문을 돈다.
			System.out.printf("%4s %6d %3d %3d %3d \n", 						// 이름 행은 getString으로, 나머지 행은 getInt로 값을 가져와 프린트한다.
					rset.getString(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getInt(5));
		}
		rset.close();															// ResultSet 인스턴스를 닫는다.
		stmt.close();															// Statement 인스턴스를 닫는다.
		conn.close();															// Connection 인스턴스를 닫는다.
	}
}

