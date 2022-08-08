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
					user.setEmployee(result.getBoolean("employee"));
					return user;
				}
			}
		}
	}
	
	
	
	public void createCredentials(String username, String name, String surname, String password, Boolean employee) throws SQLException{
		String query = "INSERT INTO user (username, name, surname, password, employee) VALUES (LOWER(?), ?, ?, ?, ?)";
		
		try(PreparedStatement pstatement = con.prepareStatement(query);){
			pstatement.setString(1, username);
			pstatement.setString(2, name);
			pstatement.setString(3, surname);
			pstatement.setString(4, password);
			pstatement.setBoolean(5,  employee);
			pstatement.executeUpdate();
		}
	}
	
	
		public boolean isUsernameFree(String username) throws SQLException{
        String query = "SELECT 1 FROM user WHERE LOWER (username)= LOWER(?)";
        try (PreparedStatement pstatement = con.prepareStatement(query);) {
            pstatement.setString(1, StringEscapeUtils.escapeJava(username));
            try (ResultSet result = pstatement.executeQuery();) {
                // no results, credential check failed
                return !result.isBeforeFirst();
            }
        }
    }
	
}