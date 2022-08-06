package it.polimi.tiw.projects.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.dao.*;
import it.polimi.tiw.projects.utils.ConnectionHandler;


@WebServlet("/Registration")
public class Registration extends HttpServlet{
	private final static long serialVersionUID = 1L;
	private Connection con = null;
	private TemplateEngine templateEngine;
	
	public void init() throws ServletException {
		con = ConnectionHandler.getConnection(getServletContext());
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		
		try {
			String driver = ((ServletContext) templateResolver).getInitParameter("dbDriver");
			String url = ((ServletContext) templateResolver).getInitParameter("dbUrl");
			String user = ((ServletContext) templateResolver).getInitParameter("dbUser");
			String password = ((ServletContext) templateResolver).getInitParameter("dbPassword");
			
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			throw new UnavailableException("Couldn't connect to db");
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = "/WEB-INF/registration.html";
		ServletContext servletContext = getServletContext();
		
		final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
        Alert alert;
        if(request.getSession().getAttribute("registerResult")==null){
            alert = new Alert(false, Alert.DANGER, "");
        } else {
            alert = (Alert) request.getSession().getAttribute("registerResult");
        }

        request.getSession().setAttribute("registerResult", alert);
        webContext.setVariable("errorMessage", request.getSession().getAttribute("registerResult"));
        templateEngine.process(path, webContext, response.getWriter());
        if(alert.isDismissible()) alert.hide();
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> paramList = new ArrayList<>(Arrays.asList("username", "name", "surname", "email", "password"));
		if(!Utility.paramExists(request, response, paramList) || Utility.paramIsEmpty(request, response, paramList)) return;
		
		String username = request.getParameter("username");
		String username = request.getParameter("name");
		String username = request.getParameter("surname");
		String username = request.getParameter("email");
		String username = request.getParameter("password");
		
		UserDAO userDAO = new UserDAO(con);
		
		try {
			if(userDAO.alreadyExists(username, email)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Utente gi� registrato");
				return;
			}
			
			if(!userDAO.isEmailFree(email)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email gi� associata ad un utente");
				return;
			}
			if(!userDAO.isUsernameFree(username)){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username gi� in uso");
				return;
			}
		
		}
		
		//TODO Confimation message
	}
}