package cinema.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
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
		Connection conn = ds.getConnection();
		Statement st = conn.createStatement();
		ResultSet res = st.executeQuery(sql);
		while (res.next()) {
			String titre = res.getString("titre");
			int annee = res.getInt(2);
			int duree = res.getInt("duree");
			System.out.println("Film : " + titre + "(" + annee 
					+ ", " + duree + ")");
		}
	}
	
}