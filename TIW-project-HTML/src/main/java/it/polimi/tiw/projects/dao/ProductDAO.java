package it.polimi.tiw.projects.dao;

import it.polimi.tiw.projects.beans.*;

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
		
		String SQLQuery = "SELECT * FROM product";
		
		try(PreparedStatement statement = connection.prepareStatement(SQLQuery);
				ResultSet resultSet = statement.executeQuery();
				){
			while(resultSet.next()) {
				Product product = new Product(resultSet.getInt("code"), resultSet.getString("name"), resultSet.getString("image"));
				products.add(product);
			}}
		
		return products;
	}
	
	public Product findProductDetails(int productCode) throws SQLException {
		Product p = new Product();
		
		String SQLQuery = "SELECT * FROM product WHERE code = ?";
		
		try(PreparedStatement pstatement = connection.prepareStatement(SQLQuery)){
			pstatement.setInt(1,  productCode);
				try(ResultSet result = pstatement.executeQuery();){
					if (result.next()) {
							Product product = new Product(productCode, result.getString("name"), result.getString("image"));
							p=product;
					}
				}
		}
		
		return p;
	}

}