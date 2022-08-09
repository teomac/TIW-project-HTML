package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SOptionsDAO {

	private Connection connection;
	
	public SOptionsDAO(Connection c) {
		this.connection=c;
	}
	
	public void updateSOptions(int optionID, String client) throws SQLException {
	
	String query2 = "SELECT id FROM quote WHERE client = ? ORDER BY id DESC";
	int quoteID=0;
	try (PreparedStatement pstatement = connection.prepareStatement(query2)){
		pstatement.setString(1,  client);
		try (ResultSet result = pstatement.executeQuery();){
			if (result.next()) {
			   quoteID = result.getInt("id");
		}
	}
		}
	
	String query3 = "INSERT INTO selectedOptions (optionID, quoteID) VALUES (?, ?)";
	try(PreparedStatement pstatement = connection.prepareStatement(query3);){
		pstatement.setInt(1, optionID);
		pstatement.setInt(2, quoteID);
		pstatement.executeUpdate();
		return;
	}
}	
}
