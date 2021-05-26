package jdbc.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PrintReport {
	static final int person = 1000;																		// 몇 개의 인덱스를 만들지 설정한다.
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");													// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(												// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();													// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.
		ResultSet rset = null;
		printFormat(rset, stmt);
		stmt.close();																				// Statement 인스턴스를 닫는다.
		conn.close();																				// Connection 인스턴스를 닫는다.
	}
	
	public static void printFormat(ResultSet rset, Statement stmt) throws SQLException {
		int maxLines = 30;																					// 한 페이지에 나올 라인 수를 넣을 변수 선언
		int pages = 0;																						// 총 몇 페이지가 나올건지 계산할 변수 선언
		pages = person / maxLines == 0 ? 																	// 만약 학생수가 라인수로 나누어떨어지지 않으면
				person / maxLines : (int)(person / maxLines) + 1;											// 나눈 값에 1을 더해서 페이지 수를 정해준다.
		
		for (int page = 0; page < pages; page++) {															// 페이지 수만큼 배열을 돈다
			int restLines = person - maxLines * page;														// 앞으로 프린트해야 할 라인수를 계산한다.
			int thisPageLines = restLines > maxLines ? maxLines : restLines;								// 이번 페이지에 몇 라인이 들어가야하는지 계산한다.
			int begin = page * maxLines;																	// 이번 페이지의 리스트가 시작할 배열 인덱스와
			int end = begin + thisPageLines;																// 리스트의 마지막 배열 인덱스를 구한다.
			int allPageLines = person - restLines + thisPageLines;											// 이 페이지를 포함해 지금까지 출력한 라인수 누적을 구한다.
			
			HeaderPrint(page+1); 																			// 헤더인쇄
			rset = stmt.executeQuery("select *, kor+eng+mat, (kor+eng+mat)/3 "
					+ "from grade_report limit " + begin + "," + end +";");
			while (rset.next()) {
				ItemPrint(rset);
			}

			rset = stmt.executeQuery("select sum(kor), sum(eng), sum(mat), sum(kor+eng+mat), sum(kor+eng+mat/3), "
					+ "avg(kor), avg(eng), avg(mat), avg(kor+eng+mat), avg(kor+eng+mat/3) "
					+ "from (select * from grade_report limit " + begin + "," + end + ") as page" + (page+1) + ";");
			while (rset.next()) {
				thisPageSumPrint(rset);
			}
			
			rset = stmt.executeQuery("select sum(kor), sum(eng), sum(mat), sum(kor+eng+mat), sum(kor+eng+mat/3), "
					+ "avg(kor), avg(eng), avg(mat), avg(kor+eng+mat), avg(kor+eng+mat/3) "
					+ "from (select * from grade_report limit 0," + allPageLines + ") as page" + (page+1) + ";");
			while (rset.next()) {
				thisPageSumPrint(rset);
			}
		}
	}
	
	public static String timeStamp() {																		// 현재 시간을 가져오는 메소드
		Calendar k03_cal = Calendar.getInstance();															// 캘린더 클래스 불러와서 인스턴스 생성
		SimpleDateFormat k03_sdf = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss");								// 날짜 출력 포멧 지정
		return k03_sdf.format(k03_cal.getTime());															// 초까지 출력하는 날짜 형식
	}
	
	public static void HeaderPrint(int page) {																// 헤더를 출력하는 메소드
		String k03_Date = timeStamp();																		// 현재시간을 string변수에 할당한다.
		System.out.printf(" %26s\n\n", "성적집계표");															// 성적집계표 출력 시작
		System.out.printf(" PAGE: %d%42s\n", page, "출력일자 : " + k03_Date);									// 출력일자 프린트
		System.out.printf("=======================================================\n");						// 구분선 출력
		System.out.printf(" %-4s %-4s%6s%6s%6s%6s%7s\n", "번호", "이름", "국어", "영어", "수학", "총점", "평균");	// 필드명 출력
		System.out.printf("=======================================================\n");						// 구분선 출력
	}
	
	public static void ItemPrint(ResultSet rset) throws SQLException {										// 각 아이템을 출력하는 메소드
		System.out.printf(" %03d   %-6s%6d %7d %7d %7d %8.1f\n", 											// 각 항목 프린트
				rset.getInt(1), rset.getString(2), rset.getInt(3), 
				rset.getInt(4), rset.getInt(5), rset.getInt(6), rset.getDouble(7));
	}

	public static void thisPageSumPrint(ResultSet rset) throws SQLException {												// 합계를 출력하는 메소드다.
		System.out.printf("=======================================================\n");						// 구분선 출력
		System.out.printf(" %s\n", "현재페이지");																// 현재 페이지 부분을 프린트한다.
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.1f\n", "합계", " ", 								// 정렬을 맞추어서 프린트 하면서
				rset.getInt(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getDouble(5));														// 평균 부분에는 합계를 3으로 나누어 표현했다.
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.1f\n", "평균", " ", 								// 국영수합계의 총점에 대한 평균을 계산하였다.
				rset.getInt(6), rset.getInt(7), rset.getInt(8), rset.getInt(9), rset.getDouble(10));									// 총점에 대한 합계를 3으로 나누어 평균의 평균을 계산하였다.
	}
	
	public static void allPageSumPrint(ResultSet rset) throws SQLException {										// 평균을 출력하는 메소드다.
		System.out.printf("=======================================================\n");						// 구분선을 프린트한다.
		System.out.printf(" %s\n", "누적페이지");	
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.1f\n", "합계", " ", 								// 국영수합계의 총점에 대한 평균을 계산하였다.
				rset.getInt(1), rset.getInt(2), rset.getInt(3), rset.getInt(4), rset.getDouble(5));									// 총점에 대한 합계를 3으로 나누어 평균의 평균을 계산하였다.
		System.out.printf(" %-4s%7s  %6d  %6d  %6d  %6d %8.1f\n", "평균", " ", 								// 국영수합계의 총점에 대한 평균을 계산하였다.
				rset.getInt(6), rset.getInt(7), rset.getInt(8), rset.getInt(9), rset.getDouble(10));									// 총점에 대한 합계를 3으로 나누어 평균의 평균을 계산하였다.
		System.out.println();
	}
}
