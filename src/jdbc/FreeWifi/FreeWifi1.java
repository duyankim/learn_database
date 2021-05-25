package jdbc.FreeWifi;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - freewifi1
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FreeWifi1 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.

		stmt.execute("create table freewifi(" 									// 테이블명 freewifi 테이블 생성
				+ "inst_place varchar(100), " 									// 설치 장소명
				+ "inst_place_detail varchar(300), "								// 설치 장소상세
				+ "inst_city varchar(50), " 									// 설치 시도명
				+ "inst_country varchar(50), " 									// 설치 시군구명
				+ "inst_place_flag varchar(50), "								// 설치 시설 구분
				+ "service_provider varchar(50), " 								// 서비스 제공사명
				+ "wifi_ssid varchar(100), " 									// 와이파이 SSID
				+ "inst_date date, "											// 설치 년월
				+ "place_addr_road varchar(200), " 								// 소재지 도로명주소
				+ "place_addr_land varchar(200), " 								// 소재지 지번주소
				+ "manage_office varchar(50), "									// 관리기관명
				+ "manage_office_phone varchar(50), " 							// 관리기관 전화번호
				+ "latitude double, " 											// 위도 (더블형)
				+ "longitude double, " 											// 경도 (더블형)
				+ "write_date date)"											// 데이터 기준일자 (date)
				+ "DEFAULT CHARSET=utf8;");										// utf-8로 저장

		stmt.close();															// Statement 인스턴스 닫음
		conn.close();															// Connection 인스턴스 닫음
	}
}

