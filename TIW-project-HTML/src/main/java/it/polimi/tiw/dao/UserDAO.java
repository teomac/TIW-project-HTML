package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.beans.User;

public class UserDAO {
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}

	public User checkCredentials(String username, String password) throws SQLException {
		String query = "SELECT  id, username, name, surname, email FROM user  WHERE username = ? AND password =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setId(result.getInt("id"));
					user.setUsername(result.getString("username"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setEmail(result.getString("email"));
					return user;
				}
			}
		}
	}
	
	public void createCredentials(String username, String name, String surname, String email, String password) throws SQLException{
		String query = "INSERT INTO TIW.user (username, name, surname, email, password) VALUES (?, ?, ?, ?, ?)";
		
		try(PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setString(1, username);
			pstatement.setString(2, name);
			pstatement.setString(3, surname);
			pstatement.setString(4, email);
			pstatement.setString(5, password);
			pstatement.executeUpdate();
		}
	}
}
