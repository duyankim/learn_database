package jdbc.stock;

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
		stmt.execute(makeTable());
		stmt.close();										
		conn.close();							
	}
	
	static public String makeTable() {
		String QueryTxt = "create table stockdailyprice(" 						
				+ "bsop_date varchar(20) not null, " 	
				+ "shrn_iscd varchar(20) not null, "					
				+ "stck_prpr int, "						
				+ "stck_oprc int, " 
				+ "stck_hgpr int, "						
				+ "stck_lwpr int, " 
				+ "acml_vol double, "						 
				+ "acml_tr_pbmn double, "
				+ "primary key (bsop_date, shrn_iscd))" 					
				+ "DEFAULT CHARSET=utf8;";
		return QueryTxt;
	}
}