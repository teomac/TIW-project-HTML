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


import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;

@WebServlet("/registrationServlet")
public class RegistrationServlet {
	private final static long serialVersionUID = 1L;
	private Connection con = null;

}
