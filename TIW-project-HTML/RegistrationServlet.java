package it.polimi.tiw.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;

@WebServlet("/registrationServlet")
public class RegistrationServlet extends HttpServlet{
	private final static long serialVersionUID = 1L;
	private Connection con = null;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws ServletException {
		con = ConnectionHandler.getConnection(getServletContext());
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.te = new TemplateEngine();
		this.te.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		
		try {
			String driver = servletContext.getInitParameter("dbDriver");
			String url = servletContext.getInitParameter("dbUrl");
			String user = servletContext.getInitParameter("dbUser");
			String password = servletContext.getInitParameter("dbPassword");
			
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			throw new UnavailableException("Impossibile caricare il db driver");
		} catch (SQLException e) {
			throw new UnavailableException("Impossibile connettersi al database");
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = "/WEB-INF/registration.html";
		ServletContext servletContext = getServletContext();
		
		final WebContext webContext = new WebContext(request, response, servletContext, request.getLocale());
        /*Alert alert;
        if(request.getSession().getAttribute("registerResult")==null){
            alert = new Alert(false, Alert.DANGER, "");
        } else {
            alert = (Alert) request.getSession().getAttribute("registerResult");
        }*/

        request.getSession().setAttribute("registerResult", alert);
        webContext.setVariable("errorMessage", request.getSession().getAttribute("registerResult"));
        templateEngine.process(path, webContext, response.getWriter());
        /*if(alert.isDismissible()) alert.hide();*/
	}
	
	@Override
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
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Utente già registrato");
				return;
			}
			
			if(!userDAO.isEmailFree(email)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email già associata ad un utente");
				return;
			}
			if(!userDAO.isUsernameFree(username)){
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username già in uso");
				return;
			}
		
		}
		
		/*TODO Confimation message*/
	}
}
