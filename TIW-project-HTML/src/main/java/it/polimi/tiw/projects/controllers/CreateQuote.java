package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.Option;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.OptionDAO;
import it.polimi.tiw.projects.dao.QuoteDAO;
import it.polimi.tiw.projects.dao.SOptionsDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/CreateQuote")

public class CreateQuote extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public CreateQuote() {
		super();
	}

	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
		connection = ConnectionHandler.getConnection(getServletContext());
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/loginPage.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;}
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String[] selectedOptions = null;
		
		try {
			selectedOptions=request.getParameterValues("option[]");
		}catch (NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		try {
		if(selectedOptions==null) {
			throw new Exception ("No option selected");
		}}catch(Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No option selected");
			return;
		}
				
		List<Integer> options = new ArrayList<Integer>();
		
		for(int i=0; i<selectedOptions.length; i++) {
			options.add(Integer.parseInt(selectedOptions[i]));
		}
	
		QuoteDAO quoteDao = new QuoteDAO(connection);
		OptionDAO optionDao = new OptionDAO(connection);
		int productID=0;
		
		Option opt = new Option();
		
		try {
			opt = optionDao.findOptionDetails(options.get(0));
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quote details");
			return;
		}
		
		productID = opt.getProductID();

		try {
			quoteDao.createQuote(productID, user.getUsername());
			}catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create new quote");
				return;
			}
		
		SOptionsDAO sOptionsDao = new SOptionsDAO(connection);
		for(int o: options) {
			try {
				sOptionsDao.updateSOptions(o, user.getUsername());
				}catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to update selected options in db");
					return;
				}
		}
		
		// Redirect to the result page
				String path = "/WEB-INF/QuoteResult.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("message", "Operation successfull");
				templateEngine.process(path, ctx, response.getWriter());
		
		
		
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
