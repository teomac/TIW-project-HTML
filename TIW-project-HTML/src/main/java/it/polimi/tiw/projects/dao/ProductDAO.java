package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
	private Connection connection;
	
	public ProductDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Product> list() throws SQLException{
		List<Product> products = new ArrayList<>();
		
		String SQLQuery = "SELECT * FROM testdb.products";
		
		try(PreparedStatement statement = connection.prepareStatement(SQLQuery);
				ResultSet resultSet = statement.executeQuery();
				){
			while(resultSet.next()) {
				Product product = new Product(resultSet.getInt("productCode"), resultSet.getString("productName"), resultSet.getString("productImage"), resultSet.getString("productImage));
				products.add(product);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return products;
	}

}
