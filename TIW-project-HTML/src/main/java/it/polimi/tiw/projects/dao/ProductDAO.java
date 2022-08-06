package it.polimi.tiw.projects.dao;

import java.sql.Connection;

public class ProductDAO {
	private Connection connection;
	
	public ProductDAO(Connection connection) {
		this.connection = connection;
	}

}
