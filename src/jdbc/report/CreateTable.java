package jdbc.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.

		stmt.execute("create table grade_report(" 						// 테이블명 freewifi 테이블 생성
				+ "lot_id int not null primary key, " 					// 설치 장소명
				+ "lot_name varchar(300), "										// 설치 장소상세
				+ "longitude double, " 											// 설치 시도명
				+ "latitude double, " 											// 설치 시군구명
				+ "lot_acc_type varchar(50), "									// 설치 시설 구분
				+ "lot_type varchar(50), " 										// 서비스 제공사명
				+ "lot_addr_land varchar(300), " 								// 와이파이 SSID
				+ "lot_addr_road varchar(300), "								// 설치 년월
				+ "lot_cnt varchar(50), " 										// 소재지 도로명주소	
				+ "fee varchar(50), " 											// 경도 (더블형)
				+ "region_id int)"										// 데이터 기준일자 (date)
				+ "DEFAULT CHARSET=utf8;");										// utf-8로 저장
		stmt.close();															// Statement 인스턴스 닫음
		conn.close();							

	}

}
