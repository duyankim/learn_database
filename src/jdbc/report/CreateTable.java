package jdbc.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreateTable {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");								// JDBC 드라이버를 로드한다.
		Connection conn = DriverManager.getConnection(							// 드라이버를 통해서 데이터베이스와 연결한다.
				"jdbc:mysql://192.168.23.27:3306/kopoctc", "root", "kopoctc");
		Statement stmt = conn.createStatement();								// Statement 객체는 SQL문을 데이터베이스로 전송하는데 사용한다.

		ArrayList<OneRec> reportData = dataSet();
		
		stmt.execute(makeTable());
		stmt.execute(insertData(reportData));
		stmt.close();										
		conn.close();							
	}
	
	static public String makeTable() {
		String QueryTxt = "create table grade_report(" 						
				+ "stu_id int not null primary key, " 	
				+ "stu_name varchar(300), "					
				+ "kor int, "						
				+ "eng int, " 								
				+ "mat int)" 					
				+ "DEFAULT CHARSET=utf8;";
		return QueryTxt;
	}
	
	static public String insertData(ArrayList<OneRec> reportData) {
		StringBuffer sb = new StringBuffer();
		String QueryTxt = "insert into grade_report("
				+ "stu_id, stu_name, kor, eng, mat)"
				+ " values ";
		for (int i = 0; i < reportData.size(); i++) {
			OneRec person = reportData.get(i);
			sb.append(String.format("(%s, '%s', %s, %s, %s), ",
					person.studentId() ,person.name(), person.kor(), person.eng(), person.mat()));
		}
		QueryTxt += sb.substring(0, sb.length()-2) + ";";
		return QueryTxt;
	}

	public static ArrayList<OneRec> dataSet() {
		ArrayList<OneRec> ArrayOneRec = new ArrayList<OneRec>();	
		int person = 1000;
		
		for (int i = 0; i < person; i++) {					
			String name = String.format("홍길동%03d", i);													// 이름 만들기
			int kor = (int)(Math.random() * 100);															// 국어 점수 만들기
			int eng = (int)(Math.random() * 100);															// 수학 점수 만들기
			int mat = (int)(Math.random() * 100);															// 영어 점수 만들기
			ArrayOneRec.add(new OneRec(i, name, kor, eng, mat));										// 하나의 OneRec클래스 생성 후 ArrayList에 집어넣음
		}
		return ArrayOneRec;
	}

}
