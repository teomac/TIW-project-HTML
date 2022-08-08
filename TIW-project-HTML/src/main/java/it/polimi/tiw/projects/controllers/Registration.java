package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.dao.UserDAO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;


@WebServlet("/Registration")
public class Registration extends HttpServlet{
	private final static long serialVersionUID = 1L;
	private Connection con = null;
	
    @Override
    public void init() throws ServletException {

        try {

            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Impossibile caricare dbDriver");
        } catch (SQLException e) {
            throw new UnavailableException("Impossibile connettersi");
        }
    }
	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	HttpSession session = request.getSession(true);
    	String path = null;
    	
    	UserDAO userDAO = new UserDAO(con);
    	
    	String name = null, surname = null, username = null, password = null, employee;
    	
    	name = request.getParameter(name);
    	surname = request.getParameter(surname);
    	username = request.getParameter(username);
    	password = request.getParameter(password);
    	/*employee = request.getParameter("isEmployee");*/
    	
    	try {
    		userDAO.createCredentials(username, name, surname, password, false);
			path = getServletContext().getContextPath() + "/loginPage.html";
			response.sendRedirect(path);
    		
    		
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
		
		//TODO Confirmation message
	}
}