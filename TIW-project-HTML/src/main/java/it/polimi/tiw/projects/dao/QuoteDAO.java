package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.projects.beans.Option;
import it.polimi.tiw.projects.beans.Quote;

public class QuoteDAO {
	
	private Connection connection;
	
	public QuoteDAO(Connection c) {
		this.connection = c;
	}
	
	public List<Quote> findQuotesByClient(String username) throws SQLException{
		List<Quote> quotes = new ArrayList<Quote>();

		String query = "SELECT * FROM quote WHERE client = ? ORDER BY id DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Quote quote = new Quote();
					quote.setClientUsername(result.getString("client"));
					quote.setEmployeeUsername(result.getString("employee"));
					quote.setProductID(result.getInt("product"));
					quote.setQuoteID(result.getInt("id"));
					quote.setPrice(result.getDouble("price"));
					quotes.add(quote);
				}
			}
		}
		return quotes;
	}
	
	
	
	
	public Quote findQuoteDetails(int quoteID) throws SQLException{
		Quote q = null;
		String query = "SELECT * FROM quote WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1,  quoteID);
			try (ResultSet result = pstatement.executeQuery();){
				if (result.next()) {
					q= new Quote();
					q.setQuoteID(result.getInt("id"));
					q.setProductID(result.getInt("product"));
					q.setPrice(result.getDouble("price"));
					q.setEmployeeUsername(result.getString("employee"));
					q.setClientUsername(result.getString("client"));}
			}
		}
		return q;
		
	}

	public List<Option> findQuoteOptions(int quoteID) throws SQLException{
		List<Option> selectedOptions = new ArrayList<Option>();
		
		String query = "SELECT * FROM selectedOptions WHERE quoteID = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1,  quoteID);
			try (ResultSet result = pstatement.executeQuery();){
				while (result.next()) {
					Option option = new Option();
					option.setOptionID(result.getInt("optionID"));
					selectedOptions.add(option);
				}
			}
		}
		return selectedOptions;
	}
}
