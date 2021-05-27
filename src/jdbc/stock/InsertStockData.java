package jdbc.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class InsertStockData {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, NumberFormatException, IOException {
		String sqlServer = "jdbc:mysql://192.168.23.27:3306/kopoctc?useSSL=false";
		String uid = "root";
		String pw = "kopoctc";

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(sqlServer, uid, pw);

		String QueryTxt = "INSERT INTO stockdailyprice (bsop_date, shrn_iscd, stck_prpr, stck_oprc, stck_hgpr, stck_lwpr, acml_vol, acml_tr_pbmn) VALUES ('?', '?', ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(QueryTxt);

		File file = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\StockDailyPrice.csv"); // 데이터 파일을 불러온다.
		BufferedReader br = new BufferedReader(new FileReader(file)); // 버퍼 리더인스턴스를 생성한다.
		String readtxt;

		int LineCnt = 0; // line 수를 세는 변수를 선언한다.
		conn.setAutoCommit(false);
		System.out.printf("start time: %dms\n", System.currentTimeMillis()); // 각 항목이 db에 잘 들어갔는지 확인하기 위해 프린트해본다.

		while ((readtxt = br.readLine()) != null) { // 파일의 1줄씩 읽어들인다. 파일이 끝나서 null 값이 나올 때까지.
			String[] field = readtxt.replace("\"", "").split(","); // tab으로 구분된 파일을 각각의 필드로 나누어 배열로 만든다.
			try {
				pstmt.setDate(1, stringToDate(field[1]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			pstmt.setString(2, field[2]);
			pstmt.setInt(3, Integer.parseInt(field[3]));
			pstmt.setInt(4, Integer.parseInt(field[4]));
			pstmt.setInt(5, Integer.parseInt(field[5]));
			pstmt.setInt(6, Integer.parseInt(field[6]));
			pstmt.setLong(7, Long.parseLong(field[11]));
			pstmt.setLong(8, Long.parseLong(field[12]));

			System.out.printf("[%d] addBatch OK\n", LineCnt);

			pstmt.addBatch(QueryTxt);
			pstmt.clearParameters();
			LineCnt++;

			try {
				if (LineCnt == 10000) {
					pstmt.executeBatch();
					conn.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				pstmt.executeBatch();
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn.commit();
			conn.setAutoCommit(true);

			System.out.printf("end time: %dms\ntotal : %d\n", System.currentTimeMillis(), LineCnt);
			br.close();
			pstmt.close();
			conn.close();
		}
	}

	static java.sql.Date stringToDate(String data) throws ParseException {
		String dayDate;
		try { // sql.Date는 정해진 형식으로 년, 월, 일이 모두 들어가야한다.
			dayDate = data.substring(0, 4) + "-" + data.substring(4, 6) + "-" + data.substring(6); // 따라서 csv파일의 날짜 데이터를
																									// 가져와서 포맷에 맞게
		} catch (Exception e) { // yyyy-MM-dd로 맞춘다.
			dayDate = "0000-00-00"; // 만약 null값이 나올 경우 데이터를 1900-01-01로 넣어주었다.
		} // 나중에 처리를 하게 된다면 2000년 이후의 데이터만
		java.sql.Date sqlDate = java.sql.Date.valueOf(dayDate); // select해야 한다. // parsing된 util.Date객체에서 시간 정보를 가져와

		return sqlDate; // sql.Date로 반환한다.
	}

}
