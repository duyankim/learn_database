package jdbc.Parkinglot;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - 주차장 데이터
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Parkinglot1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.

		stmt.execute("create table public_parking_lot(" 						// 테이블명 public_parking_log 테이블 생성
				+ "lot_id int not null primary key, " 							// 주차장 관리번호
				+ "lot_name varchar(300), "										// 주차장 이름
				+ "longitude double, " 											// 경도
				+ "latitude double, " 											// 위도
				+ "lot_acc_type varchar(50), "									// 주차장 구분(공영, 민영 등)
				+ "lot_type varchar(50), " 										// 주차장 유형(노상, 노외 등)
				+ "lot_addr_land varchar(300), " 								// 주차장 지번주소
				+ "lot_addr_road varchar(300), "								// 주차장 도로명주소
				+ "lot_cnt varchar(50), " 										// 주차 구획수	
				+ "fee varchar(50), " 											// 요금 (무료, 혼합, 금액 등)
				+ "region_id int)"												// 지역 코드
				+ "DEFAULT CHARSET=utf8;");										// utf-8로 저장
		stmt.close();															// Statement 인스턴스 닫음
		conn.close();															// Connection 인스턴스 닫음
	}
}
