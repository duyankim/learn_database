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
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class StockQuery {

	public static void main(String[] args) {
		StockQuery stock = new StockQuery();
	}
	
	static {
        try { 
        	Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	public StockQuery(){
		try {
		String sqlServer = "jdbc:mysql://192.168.23.27:3306/kopoctc?useSSL=false";
		String uid = "root";
		String pw = "kopoctc";		
		Connection conn = DriverManager.getConnection(sqlServer, uid, pw);
		String QueryTxt = "INSERT INTO stockdailyprice (bsop_date, shrn_iscd, stck_prpr, stck_oprc, stck_hgpr, stck_lwpr, acml_vol, acml_tr_pbmn) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(QueryTxt);
		
		File file = new File("C:\\Users\\kim\\Desktop\\과제\\데이터베이스\\StockDailyPrice.csv");	
		BufferedReader br = new BufferedReader(new FileReader(file));									
		String readtxt;
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.printf("start time: %s\n", sdf.format(System.currentTimeMillis()));					
		
		int errorCnt = 0;
		String errortxt="";
		int LineCnt = 0;
		long startTime = System.currentTimeMillis();
		
		conn.setAutoCommit(false);

		while((readtxt = br.readLine()) != null) {													 
			String[] field = readtxt.split(",");	
			pstmt.setString(1, field[1]);
			pstmt.setString(2, field[2]);
			pstmt.setString(3, field[3]);
			pstmt.setString(4, field[4]);
			pstmt.setString(5, field[5]);
			pstmt.setString(6, field[6]);
			pstmt.setString(7, field[11]);
			pstmt.setString(8, field[12]);
			pstmt.addBatch();
			System.out.printf("%d번째 항목 addBatch OK\n",LineCnt);
			pstmt.clearParameters();
			LineCnt++;
			
			try {
				if (LineCnt%10000==0) {
					pstmt.executeBatch();
					conn.commit();
				}
			} catch(Exception e) {
				errortxt += Arrays.toString(field) + "\n";
				errorCnt++;
				e.printStackTrace();
			}
		}
		
		try {		
			pstmt.executeBatch();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		conn.commit();
		conn.setAutoCommit(true);
		
		long endTime = System.currentTimeMillis();
		
		System.out.printf("Insert End\n");
		System.out.printf("total : %d\n", LineCnt);
		System.out.printf("time : %d\n", endTime-startTime);
		System.out.printf("error: %s\n", errortxt);
		System.out.printf("errorCnt: %d\n",errorCnt);
		
		br.close();
		pstmt.close();
		conn.close();
	} catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch(SQLException e) {
        e.printStackTrace();
    }
	}
}