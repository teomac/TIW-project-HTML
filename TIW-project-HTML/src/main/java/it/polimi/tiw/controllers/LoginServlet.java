package it.polimi.tiw.controllers;

import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;

import java.io.PrintWriter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;



@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet{
	private final static long serialVersionUID = 1L;
	
	private Connection connection = null;


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			String path = null;
		
		
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if(username==null || password == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Credenziali mancanti o incomplete");
				return;
			}
			
			else{
				
				response.sendRedirect(path);
			}

		
	}

}
