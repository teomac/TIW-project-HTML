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
	
	public Registration() {
		super();
	}
	
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
		String email=null;
		
		try {
			username = request.getParameter("username");
			password = request.getParameter("password");
			name = request.getParameter("name");
	    	surname = request.getParameter("surname");
	    	email=request.getParameter("email");
	    	employee = Boolean.parseBoolean(request.getParameter("isEmployee"));
	    	
			
			if (username == null || password == null || name == null || surname == null || employee == null || email==null) {
				throw new Exception("Missing or empty credential value");
			}

		}catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credentials");
			return;
		}
		
		try {
		if(username.length()<8 || password.length()<8) {
			throw new Exception("Username or password length is too small or too high. Min = 8 characters, Max = 32 characters");
		}}catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username or password length is too small or too high. Min = 8 characters, Max = 32 characters");
			return;
		}
		
		
    	UserDAO userDAO = new UserDAO(connection);
    	boolean isFree = false;
    	
    	try {
    		isFree = userDAO.isUsernameFree(username);
    	}catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error checking username");
			return;
		} 
    	
    	try {
    	if(!isFree) {
    		throw new Exception("Username already taken");
    	}}catch (Exception e) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username already taken");
    		return;

    	}
    	
    	boolean isEmailValid = userDAO.isEmailValid(email);
    	  
    	try {
    	if(!isEmailValid) {
    		throw new Exception("Email format not valid");
    	}}catch (Exception e) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email format not valid");
    		return;
    	}
    	
    	
    	try {
    		userDAO.createCredentials(username, name, surname, password, employee, email);
    	} catch (Exception e) {
    		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to insert credential in DB");
    		return;
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