package cinema.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

class TestCinema {
	private static DataSource ds;
	
	@BeforeAll
	static void configDataSource() {
		String url = "jdbc:postgresql://localhost:5432/postgres";
		var pgDs = new PGSimpleDataSource();
		pgDs.setURL(url);
		pgDs.setUser("cinema");
		pgDs.setPassword("password");
		ds = pgDs;
	}

	@Test
	void testLecture() throws SQLException {
		String sql = "select titre, annee, duree from film";
		try ( // resources
				Connection conn = ds.getConnection();
				Statement st = conn.createStatement();
				ResultSet res = st.executeQuery(sql);
		) { // debut : utilisation resources
			
			while (res.next()) {
				String titre = res.getString("titre");
				int annee = res.getInt(2);
				int duree = res.getInt("duree");
				System.out.println("Film : " + titre + "(" + annee 
						+ ", " + duree + ")");
			}
		} 	// fin utilisation  resources
			// auto : res.close(), st.close(), conn.close()
	}
	
	@Test
	void testEcriture() throws SQLException {
		String sql = "insert into film (titre, annee, duree, num_real) "
				+ "values ('Once Upon a Time... in Hollywood', 2019, 161, 14)";
		try ( // resources
				Connection conn = ds.getConnection();
				Statement st = conn.createStatement();
		) {
			int nbRowInserted = st.executeUpdate(sql);
			System.out.println("Film ajouté : " + nbRowInserted);
		}
	}
	
	@Test
	void testEcritureFromHuman() throws SQLException {
		// data from form
		String titre = "Andy";
		int annee = 2019;
		int duree = 91;
		int num_real = 12;
		
		// request with 4 placeholders
		String sql = "insert into film (titre, annee, duree, num_real) "
				+ "values (?,?,?,?)";
		try ( // resources
				Connection conn = ds.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
		) {
			st.setString(1, titre);
			st.setInt(2, annee);
			st.setInt(3, duree);
			st.setInt(4, num_real);
			int nbRowInserted = st.executeUpdate();
			System.out.println("Film ajouté : " + nbRowInserted);
		}
	}
	
}
