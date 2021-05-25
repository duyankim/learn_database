package jdbc.FreeWifi;

/* 2021-05-24
 * 데이터베이스 프로그래밍 2강 - freewifi3
 * kopo03 김도연
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FreeWifi3 {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		
		double lat = 37.3860521;												// 위도 값을 선언한다.
		double lng = 127.1214038;												// 경도 값을 선언한다.
		
		String QueryTxt;														// 쿼리 텍스트를 넣을 String을 선언한다.
		QueryTxt = String.format("select * from freewifi where "				// 쿼리문을 만들기 쉽게 String.format을 이용한다.
				+"SQRT( POWER( latitude-%f,2) + POWER (longitude-%f,2 ) ) = "	// freewifi table에서 선택한 데이터를 열람하는 쿼리문이다.
				+"(select MIN( "												// 피타고라스 공식을 사용해서 직선거리를 구한다.
				+ "SQRT( POWER( latitude-%f,2) + POWER (longitude-%f,2 ) ) "	// select * from table명 where 직선거리 = select 최소값(직선거리) from table명
				+ ") from freewifi);"											// 현위치의 위도 경도 값을 사용해서 구한다.
				, lat, lng, lat, lng);
		ResultSet rset = stmt.executeQuery(QueryTxt);							// executeQuery 메소드로 ResultSet 객체를 생성하여 실행한다.
		int iCnt = 0;															// 줄을 세기 위한 변수를 만든다.
		while(rset.next()) {													// 만약 ResultSet객체에 다음 데이터가 있다면 (마지막 줄이 아니라면)
			System.out.printf("*(%d)***********************************\n", iCnt++);	// 라인 수와 구분선을 프린트하고,
			System.out.printf("설치장소명:       %s\n", rset.getString(1));				// ResultSet의 getString메소드로 각 필드의 값을 불러온다.
			System.out.printf("설치장소상세:     %s\n", rset.getString(2));				// 각 필드는 순서가 있는 데이터라는 점에서
			System.out.printf("설치시도명:       %s\n", rset.getString(3));				// 배열과 유사한 것 같지만
			System.out.printf("설치시군구명:     %s\n", rset.getString(4));				// 배열과 달리 첫 번째 인덱스가 0이 아닌 1부터 시작한다.
			System.out.printf("설치시설구분:     %s\n", rset.getString(5));				// 각각의 필드를 프린트한다.
			System.out.printf("서비스제공사명:   %s\n", rset.getString(6));					// 각각의 필드를 프린트한다.
			System.out.printf("와이파이SSID:     %s\n", rset.getString(7));				// 각각의 필드를 프린트한다.
			System.out.printf("설치년월:         %s\n", rset.getString(8));				// 각각의 필드를 프린트한다.
			System.out.printf("소재지도로명주소: %s\n", rset.getString(9));					// 각각의 필드를 프린트한다.
			System.out.printf("소재지지번주소:   %s\n", rset.getString(10));				// 각각의 필드를 프린트한다.
			System.out.printf("관리기관명:       %s\n", rset.getString(11));				// 각각의 필드를 프린트한다.
			System.out.printf("관리기관전화번호:  %s\n", rset.getString(12));				// 각각의 필드를 프린트한다.
			System.out.printf("위도:              %s\n", rset.getString(13));			// 각각의 필드를 프린트한다.
			System.out.printf("경도:              %s\n", rset.getString(14));			// 각각의 필드를 프린트한다.
			System.out.printf("데이터기준일자:    %s\n", rset.getString(15));				// 각각의 필드를 프린트한다.
		}
	}

}
