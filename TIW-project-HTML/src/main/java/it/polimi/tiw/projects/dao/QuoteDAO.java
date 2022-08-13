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
					quote.setClientUsername(username);
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
	
	
	public List<Quote> findQuotesByEmployee(String username) throws SQLException{
		List<Quote> quotes = new ArrayList<Quote>();

		String query = "SELECT * FROM quote WHERE employee = ? ORDER BY id DESC";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Quote quote = new Quote();
					quote.setEmployeeUsername(username);
					quote.setClientUsername(result.getString("client"));
					quote.setProductID(result.getInt("product"));
					quote.setQuoteID(result.getInt("id"));
					quote.setPrice(result.getDouble("price"));
					quotes.add(quote);
				}
			}
		}
		return quotes;
	}
	
	public List<Quote> findFreeQuotes() throws SQLException{
		
		List<Quote> freequotes = new ArrayList<Quote>();
		
		String query = "SELECT * FROM quote WHERE employee = 'null'";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			try (ResultSet result = pstatement.executeQuery();) {
				while(result.next()) {
					Quote quote = new Quote();
				quote.setClientUsername(result.getString("client"));
				quote.setProductID(result.getInt("product"));
				quote.setQuoteID(result.getInt("id"));
				quote.setPrice(result.getDouble("price"));
				freequotes.add(quote);
					}
				
				}
			}
		return freequotes;
	}
	
	public void addPriceToQuote(int price, int id, String employee) throws SQLException{
		
		String query = "SELECT * FROM quote WHERE id = ? (INSERT INTO (price, employee) VALUES (?, ?))";
		
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id);
			pstatement.setInt(2, price);
			pstatement.setString(3, employee);
			pstatement.executeUpdate();
		}
		
	}
	
	
	
	
	public Quote findQuoteDetails(int quoteID) throws SQLException{
		Quote q = null;
		String query = "SELECT * FROM quote WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1,  quoteID);
			try (ResultSet result = pstatement.executeQuery();){
				if (result.next()) {
					q= new Quote();
					q.setQuoteID(quoteID);
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
	
	
	public void createQuote(int product, String client) throws SQLException{
		String query = "INSERT INTO quote (product, client) VALUES (?, ?)";
		
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			pstatement.setInt(1, product);
			pstatement.setString(2,  client);
			pstatement.executeUpdate();
			return;
		}
	}

	
	
}
		
