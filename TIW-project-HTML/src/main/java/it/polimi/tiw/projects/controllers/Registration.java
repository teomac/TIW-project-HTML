package it.polimi.tiw.projects.controllers;

import it.polimi.tiw.projects.dao.UserDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.io.IOException;
import java.sql.*;


@WebServlet("/Registration")
public class Registration extends HttpServlet{
	private final static long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	
    @Override
    public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String username=null;
		String password=null;
		String name=null;
		String surname=null;
		Boolean employee = null;
		
		try {
			username = request.getParameter("username");
			password = request.getParameter("password");
			name = request.getParameter("name");
	    	surname = request.getParameter("surname");
	    	employee = Boolean.parseBoolean(request.getParameter("isEmployee"));
	    	
			
			if (username == null || password == null || name == null || surname == null || employee == null) {
				throw new Exception("Missing or empty credential value");
			}

		}catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credentials");
		}
		
		if(username.length()<8 || password.length()<8) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username or password length is too small or too high . Min = 8 characters, Max = 32 characters");
		}
		
		
    	UserDAO userDAO = new UserDAO(connection);
    	boolean isFree = false;
    	
    	try {
    		isFree = userDAO.isUsernameFree(username);
    	}catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error checking username");
		} 
    	
    	if(!isFree) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username already taken");
    	}
    	
    	
    	
    	try {
    		userDAO.createCredentials(username, name, surname, password, employee);
    	} catch (Exception e) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to insert credential in DB");
    	}
    	
    	String loginpath = getServletContext().getContextPath() + "/loginPage.html";
		response.sendRedirect(loginpath);		
	}
    
    @Override
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}