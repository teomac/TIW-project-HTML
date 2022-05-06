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



@WebServlet(name="login")
public class CheckLogin extends HttpServlet{
	private final static long serialVersionUID = 1L;
	
	private Connection connection = null;
	
	/*public void init() throws ServletException{
		connection = ConnectionHandler.getConnection(getServletContext());
	}
	
	public CheckLogin(){
		super();
	}*/

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			String path = null;
		
		
			String email = request.getParameter("email");
			String psw = request.getParameter("psw");
			
			if(email == null || psw == null || email.isEmpty() || psw.isEmpty()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali mancanti o incomplete");
				return;
			}
			
			else if(email==psw) {
				
				response.sendRedirect(path);
			}
		
	}

}
