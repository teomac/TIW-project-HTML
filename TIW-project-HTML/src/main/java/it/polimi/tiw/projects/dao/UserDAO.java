package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringEscapeUtils;
import it.polimi.tiw.projects.beans.User;

public class UserDAO {
	private Connection con;

	public UserDAO(Connection connection) {
		this.con = connection;
	}

	public User checkCredentials(String username, String password) throws SQLException {
		String query = "SELECT * FROM user WHERE LOWER(username) = LOWER(?) AND password = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					User user = new User();
					user.setUsername(result.getString("username"));
					user.setName(result.getString("name"));
					user.setSurname(result.getString("surname"));
					user.setEmail(result.getString("email"));
					user.setEmployee(result.getBoolean("employee"));
					return user;
				}
			}
		}
	}
	
	
	
	public void createCredentials(String username, String name, String surname, String email, String password, Boolean employee) throws SQLException{
		String query = "INSERT INTO user (username, name, surname, email, password, employee) VALUES (?, ?, ?, ?, ?, ?)";
		
		try(PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setString(1, username);
			pstatement.setString(2, name);
			pstatement.setString(3, surname);
			pstatement.setString(4, email);
			pstatement.setString(5, password);
			pstatement.setBoolean(6,  employee);
			pstatement.executeUpdate();
		}
	}
	
	public boolean isUsernameFree(String username) throws SQLException{
        String query = "SELECT 1 FROM user WHERE username= ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, StringEscapeUtils.escapeJava(username));
            try (ResultSet result = pstatement.executeQuery();) {
                // no results, credential check failed
                return !result.isBeforeFirst();
            }
        }
    }
	
	
	public boolean alreadyExists(String username, String email) throws SQLException{
        String query = "SELECT 1 FROM user WHERE username= ? AND email= ?";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, StringEscapeUtils.escapeJava(username));
            pstatement.setString(2, StringEscapeUtils.escapeJava(email));
            try (ResultSet result = pstatement.executeQuery();) {
                // no results, credential check failed
                return result.isBeforeFirst();
            }
        }
    }
}
