package stock2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class StockQuery {

	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		
		String QueryTxt = "insert into stockdailyprice (shrn_iscd, bsop_date, stck_oprc, stck_hgpr,"
				+ "stck_prpr, acml_vol, acml_tr_pbmn) values(?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement pstmt = conn.prepareStatement(QueryTxt);
		
		File f = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\StockDailyPrice.csv");
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String readTxt ="";
		int errorCnt = 0;
		String errorTxt = null;
		int LineCnt = 0;
		
		conn.setAutoCommit(false);
		long startTime = System.currentTimeMillis();
		
		while ((readTxt = br.readLine()) != null) {
			String[] field = readTxt.split(",");
			
			pstmt.setString(1, field[2]);
			pstmt.setString(2, field[1]);
			pstmt.setString(3, field[4]);
			pstmt.setString(4, field[5]);
			pstmt.setString(5, field[3]);
			pstmt.setString(6, field[11]);
			pstmt.setString(7, field[12]);
			pstmt.addBatch();
			System.out.printf("%d번째 항목 addBatch OK\n", LineCnt);
			pstmt.clearParameters();
			LineCnt++;
			
			try {
				if (LineCnt % 1000 == 0) {
					pstmt.executeBatch();
					conn.commit();
				}
			} catch (Exception e) {
				errorTxt += Arrays.toString(field) + "\n";
				errorCnt++;
				e.printStackTrace();
			}
		}
		try {
			pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		conn.commit();
		conn.setAutoCommit(true);
		long endTime = System.currentTimeMillis();
		
		System.out.printf("Insert End\n");
		System.out.printf("total : %d\n", LineCnt);
		System.out.printf("time : %d\n", endTime-startTime);
		System.out.printf("error: %s\n", errorTxt);
		System.out.printf("errorCnt: %d\n",errorCnt);
		
		br.close();
		pstmt.close();
		conn.close();
	}
}
