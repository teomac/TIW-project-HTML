package it.polimi.tiw.projects.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.projects.beans.Quote;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.QuoteDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/AddPrice")
public class AddPrice extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	public AddPrice() {
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
		if (session.isNew() || session.getAttribute("user") == null || ((User) session.getAttribute("user")).getEmployee()==false) {
			response.sendRedirect(loginpath);
			return;}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		
		String employeeUser;
		double price = 0;
		int quoteID = 0;

		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		employeeUser = user.getUsername();
		
		/*try {*/
			price = Double.parseDouble(request.getParameter("price"));
			quoteID = Integer.parseInt(request.getParameter("quoteID"));
		
		QuoteDAO quoteDao = new QuoteDAO(connection);
		
		Quote quote = new Quote();
		
		try {
			if(price != 0 && quoteID != 0) {
				quote= quoteDao.findQuoteDetails(quoteID);
			}
			else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Price and quote ID cannot be zero");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(quote.getEmployeeUsername()==null && quote.getPrice()==0.0) {
				quoteDao.addPriceToQuote(price, quoteID, employeeUser);
			}
			else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Quote already priced");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// return the user to the right view
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/HomeEmployee";
		response.sendRedirect(path);
	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
