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
import it.polimi.tiw.projects.beans.Product;
import it.polimi.tiw.projects.beans.Quote;
import it.polimi.tiw.projects.beans.User;
import it.polimi.tiw.projects.dao.OptionDAO;
import it.polimi.tiw.projects.dao.ProductDAO;
import it.polimi.tiw.projects.dao.QuoteDAO;
import it.polimi.tiw.projects.utils.ConnectionHandler;

@WebServlet("/HomeClient")
public class GoToHomepageClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;

	public GoToHomepageClient() {
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
		// If the user is not logged in (not present in session), or the user is an Employee, redirect to the login
		String loginpath = getServletContext().getContextPath() + "/loginPage.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null || ((User) session.getAttribute("user")).getEmployee()==true) {
			response.sendRedirect(loginpath);
			return;
		}
		User user = (User) session.getAttribute("user");
		
		// get and check params
				Integer productCode = null;
				try {
					productCode = Integer.parseInt(request.getParameter("productCode"));
				} catch (NumberFormatException | NullPointerException e) {
					// only for debugging e.printStackTrace();
					productCode=null;
				}		
				
		QuoteDAO quotesDAO = new QuoteDAO(connection);
		List<Quote> quotes = new ArrayList<Quote>();

		try {
			quotes = quotesDAO.findQuotesByClient(user.getUsername());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover quotes");
			return;
		}

		ProductDAO productDAO = new ProductDAO(connection);
		List<Product> products = new ArrayList<Product>();
		try {
			products = productDAO.list();
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover products");
			return;
		}
		;
		OptionDAO optionDao = new OptionDAO(connection);
		List<Option> productOptions =  new ArrayList<Option>();
		if(!(productCode==null)) {
			try {
				productOptions= optionDao.findAvailableOptions(productCode);
			}catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover option details");
				return;}	
		}
		String productMessage = null;
		
		if(productCode!=null) {
	
		for(Product p: products) {
			if(p.getProductCode()==productCode) {
				productMessage=p.getProductName();
			}
		}}
		
		//check if product does not exists
		try {
			if(productMessage==null && productCode!=null) {
				throw new Exception ("Not existing product");}
			}catch(Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not existing product");
				return;
			}
		
		
		String message = null;
		if(productCode==null){
			message = null;
		}
		else if(productOptions.isEmpty()) {
			message = "No options available for this product.";
		}
		else {
			message = "Please select at least one option for your "+productMessage;
		}
		
		// Redirect to the Home page and add quotes to the parameters
		String path = "/WEB-INF/HomepageClient.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("quotes", quotes);
		ctx.setVariable("products", products);
		ctx.setVariable("productOptions", productOptions);
		ctx.setVariable("message", message);
		templateEngine.process(path, ctx, response.getWriter());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
