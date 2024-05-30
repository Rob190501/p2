package it.unisa.control;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
			
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	UserDao usDao = new UserDao();
		
		try
		{	    

		     UserBean user = new UserBean();
		     user.setUsername(request.getParameter("un"));
		     String rawPassword = request.getParameter("pw");
		        
		     // Crittografare la password inviata dall'utente per confrontarla con quella memorizzata
		     String hashedPassword = hashPassword(rawPassword);
		     user.setPassword(hashedPassword);
		        
		     user = usDao.doRetrieve(request.getParameter("un"), hashedPassword);		    
		    
		     String checkout = request.getParameter("checkout");
		     
		     if (user.isValid())
		     {
			        
		          HttpSession session = request.getSession(true);	    
		          session.setAttribute("currentSessionUser",user); 
		          if(checkout!=null)
		        	  response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
		          
		          else
		        	  response.sendRedirect(request.getContextPath() + "/Home.jsp");
		     }
			        
		     else 
		          response.sendRedirect(request.getContextPath() +"/Login.jsp?action=error"); //error page 
		} 
				
				
		catch(SQLException e) {
			System.out.println("Error:" + e.getMessage());
		}
		  }
	
	private String bytesToHex(byte[] bytes) {
	    StringBuilder hexString = new StringBuilder(2 * bytes.length);
	    for (byte b : bytes) {
	        String hex = Integer.toHexString(0xff & b);
	        if (hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}

	private String hashPassword(String password) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hashedBytes = digest.digest(password.getBytes());
	        
	        // Converti l'hash in una stringa esadecimale
	        return bytesToHex(hashedBytes).toLowerCase();
	    } catch (NoSuchAlgorithmException e) {
	        // Gestire l'eccezione in modo appropriato
	        e.printStackTrace();
	        return null;
	    }
	}

}
