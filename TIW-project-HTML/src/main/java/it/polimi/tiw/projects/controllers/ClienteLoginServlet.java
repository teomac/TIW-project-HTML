package it.polimi.tiw.projects.controllers;

import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;



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

import it.polimi.tiw.projects.beans.*;
import it.polimi.tiw.projects.dao.*;
import it.polimi.tiw.projects.utils.*;

@WebServlet("/ClienteLoginServlet")
public class ClienteLoginServlet extends HttpServlet{
	private final static long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	
	public ClienteLoginServlet() {
		super();
	}
	

	public void init() throws ServletException {
		connection = ConnectionHandler.getConnection(getServletContext());
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			String path = null;
			String username=null;
			String password=null;
			
			try {
				username = request.getParameter("username");
				password = request.getParameter("password");
				
			}catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali mancanti o incomplete");
			}
			
			ClienteDAO clienteDAO = new ClienteDAO(connection);
			Cliente cliente = new Cliente();
			cliente = null;
			
			try {
				cliente = clienteDAO.checkCredentials(username, password);
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Non è stato possibile controllare le credenziali");
				return;
			}
			
			
			if (cliente == null) {
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("errorMsg", "Username o password errati");
				path = "/index.html";
				templateEngine.process(path, ctx, response.getWriter());
			} else {
				request.getSession().setAttribute("cliente", cliente);
				path = getServletContext().getContextPath() + "/HomePageCliente";
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
