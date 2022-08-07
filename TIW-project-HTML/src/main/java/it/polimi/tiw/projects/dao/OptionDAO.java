package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Option;

public class OptionDAO {
	
	private Connection connection;
	
	public OptionDAO(Connection c) {
		this.connection=c;
	}
	
	public List<Option> findAvailableOptions(int productID) throws SQLException{
		List<Option> availableOptions = new ArrayList<Option>();
		
		String query = "SELECT * from avOptions WHERE productID = ? ORDER BY id DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, productID);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Option option = new Option();
					option.setOptionID(result.getInt("id"));
					option.setProductID(result.getInt("productID"));
					option.setInSale(result.getBoolean("inSale"));
					option.setName(result.getString("name"));
					availableOptions.add(option);
				}
			}
		}
		return availableOptions;
	}
	
	
	
	public Option findOptionDetails(int optionID) throws SQLException{
		Option o = null;
		String query = "SELECT * FROM avOptions WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, optionID);
			try (ResultSet result = pstatement.executeQuery();) {
				if(result.next()) {
					o = new Option();
					o.setOptionID(result.getInt("id"));
					o.setName(result.getString("name"));
					o.setInSale(result.getBoolean("inSale"));
					o.setProductID(result.getInt("productID"));
					}
				}
		}
		return o;
	}

}
