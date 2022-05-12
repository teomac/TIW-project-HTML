package it.polimi.tiw.controllers;

import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet{
	private final static long serialVersionUID = 1L;
	
	private Connection connection = null;
	
	public LoginServlet() {
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			String path = null;
			
			try {
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				
			}catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali mancanti o incomplete");
			}
			
			UserDAO userDAO = new UserDAO(connection);
			User user = new User();
			user = null;
			
			try {
				user = userDAO.checkCredentials(username, password);
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Non è stato possibile controllare le credenziali");
				return;
			}
			
			
			if (user == null) {
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMsg", "Username o password errati");
				path = "/index.html";
				templateEngine.process(path, ctx, response.getWriter());
			} else {
				request.getSession().setAttribute("user", user);
				path = getServletContext().getContextPath() + "/Home";
				response.sendRedirect(path);
			}

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

}