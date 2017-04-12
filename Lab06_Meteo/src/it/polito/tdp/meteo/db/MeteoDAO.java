package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;

import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		return null;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		final String sql = "SELECT AVG(umidita) AS umidita_media "+
							"FROM situazione "+ 
							"WHERE localita=? "+
							"AND month(data)=? "+
							"GROUP BY ?";
		
		double umidita=0;

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, localita);
			/*String meseConZero;
			if(mese<10)
				meseConZero="0"+mese;
			else
				meseConZero=""+mese;
			st.setString(2, "2013-"+meseConZero+"-01");
			st.setString(3, "2013-"+meseConZero+"-31");*/
			st.setInt(2, mese);
			st.setString(3, localita);
			
			ResultSet rs = st.executeQuery();
			
			if (rs.next()) {
				umidita=rs.getDouble("umidita_media");
				conn.close();
			    return umidita;
			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
			return 0.0;
		}
	
	public List<String> getElencoLocalita(){
		final String sql="SELECT DISTINCT localita "+
						  "FROM situazione";
		List<String> citta=new LinkedList<String>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				citta.add(rs.getString("localita"));
			}
			
			conn.close();
		    return citta;
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public SimpleCity getElencoSimpleCity(int mese, int giorno, String localita){
		final String sql="SELECT localita, umidita "+
				  	"FROM situazione "+
				  	"WHERE month(data)=? "+
				  	"AND day(data)=? "+
				  	"AND localita=?";
		
		SimpleCity sc=null;

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, mese);
			st.setInt(2, giorno+1);
			st.setString(3, localita);
	
			ResultSet rs = st.executeQuery();
	
			while (rs.next()) {
				sc=new SimpleCity(rs.getString("localita"), rs.getInt("umidita"));
			}
			conn.close();
			return sc;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
