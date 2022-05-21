package it.polimi.tiw.projects.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringEscapeUtils;
import it.polimi.tiw.projects.beans.Cliente;

public class ClienteDAO {
	private Connection con;

	public ClienteDAO(Connection connection) {
		this.con = connection;
	}

	public Cliente checkCredentials(String username, String password) throws SQLException {
		String query = "SELECT  username, name, surname, email FROM user  WHERE username = ? AND password = ?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) // no results, credential check failed
					return null;
				else {
					result.next();
					Cliente cliente = new Cliente();
					cliente.setUsername(result.getString("username"));
					cliente.setName(result.getString("name"));
					cliente.setSurname(result.getString("surname"));
					cliente.setEmail(result.getString("email"));
					return cliente;
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
