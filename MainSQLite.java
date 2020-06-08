import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Student implements Comparable<Student> {
	private int id;
	static int contor = 0;
	private String nume;
	private double notaMate;
	private double notaRo;
	private double medie;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Student [id=");
		builder.append(id);
		builder.append(", nume=");
		builder.append(nume);
		builder.append(", notaMate=");
		builder.append(notaMate);
		builder.append(", notaRo=");
		builder.append(notaRo);
		builder.append(", medie=");
		builder.append(medie);
		builder.append("]");
		return builder.toString();
	}
	
	public Student(String nume, double notaMate, double notaRo) {
		this.id = contor ++;
		this.nume = nume;
		this.notaMate = notaMate;
		this.notaRo = notaRo;
		this.medie = (notaMate + notaRo)/2;
	}

	@Override
	public int compareTo(Student o) {
		if(medie == o.medie)
			return 0;
		if(medie < o.medie)
			return -1;
		else
			return 1;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public double getNotaMate() {
		return notaMate;
	}

	public void setNotaMate(double notaMate) {
		this.notaMate = notaMate;
	}

	public double getNotaRo() {
		return notaRo;
	}

	public void setNotaRo(double notaRo) {
		this.notaRo = notaRo;
	}

	public double getMedie() {
		return medie;
	}

	public void setMedie(double medie) {
		this.medie = medie;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}


public class MainSQLite {
	static List<Student> lst = null;
	
	public static void main(String[] args) {
		
		Student s1 = new Student("Daniel", 8,9);
		Student s2 = new Student("Cornel", 3, 6);
		Student s3 = new Student("Ana", 8, 5);
		Student s4 = new Student("Dorin",  9, 9);
		Student s5 = new Student("George", 3, 5);
		
		lst = new ArrayList<>();
		lst.add(s1);
		lst.add(s2);
		lst.add(s3);
		lst.add(s4);
		lst.add(s5);
		for(Iterator<Student> it = lst.listIterator(); it.hasNext();)
			System.out.println(it.next());
		
		lst.sort((o1,o2)-> o1.compareTo(o2));
		System.out.println("Sorted:");
		for(Student x : lst) {
			System.out.println(x);
		}
		
		Connection c = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c=DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("SQLite DB Connected!");
			c.setAutoCommit(false);
			createDBTable(c);
			insertDBTable(c, lst);
			selectDBTable(c);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void createDBTable(Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		String sqlDropTable = "drop table STUDS";
		String sqlCreateTable = "create table STUDS " +
		"(ID INT PRIMARY KEY NOT NULL," + "NAME TEXT NOT NULL, NOTA_MATE REAL, NOTA_RO REAL, MEDIE REAL)";
		stmt.executeUpdate(sqlDropTable);
		stmt.executeUpdate(sqlCreateTable);
		
		stmt.close();
		c.commit();
		System.out.println("Table created.");
	}
	
	public static void insertDBTable(Connection c, List<Student> lst) throws SQLException {
		String sql = null;
		PreparedStatement ps = c.prepareStatement("INSERT INTO STUDS(ID,NAME,NOTA_MATE,NOTA_RO,MEDIE) VALUES "
				+ "(?,?,?,?,?)");
		for(Student x : lst) {
			ps.setInt(1, x.getId());
			ps.setString(2, x.getNume());
			ps.setDouble(3, x.getNotaMate());
			ps.setDouble(4, x.getNotaRo());
			ps.setDouble(5, x.getMedie());
			ps.executeUpdate();
		}
		System.out.println("Inserted students in pula.");
		ps.close();
		c.commit();
		
	}
	
	
	public static void selectDBTable(Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		String sqlSet = "select * from STUDS ORDER BY ID";
		ResultSet rs = stmt.executeQuery(sqlSet);
		while(rs.next()) {
			int id = rs.getInt("ID");
			String nume = rs.getString("NAME");
			double notaMate = rs.getDouble("NOTA_MATE");
			double notaRo = rs.getDouble("NOTA_RO");
			double media = rs.getDouble("MEDIE");
			System.out.println(id + " " + nume + " " + notaMate + " " + notaRo + " " + media);
		}
		rs.close();
		stmt.close();
	}
}

