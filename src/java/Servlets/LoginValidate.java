package Servlets;


//TANTI IMPORT DA CANCELLARE MA CI SI PENSA DOPO!!

import System.Sviluppatore;

import Util.DataUtile;
import static Util.DataUtile.crypt;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LOGIN DIVENTA NON PIU' UNA SERVLET A PARTE MA UN FILE JAVA CHE HA UN METODO!!!!!!!!!!!!
 * 
 */
public class LoginValidate {
    /*  //fatti prima
        String EmailL = request.getParameter("email");
        String PassL = request.getParameter("password");  

    */
        public static int validate(String name, String pswd){
            int id=0;
            if (!(null == name) && !(null == pswd)) {
                try { // Checka l'esistenza della coppia email/password nel database per l'autenticazione
                    id = DataUtile.checkUser(name, pswd);
                    System.out.println("ID---> " + id );
                } catch (Exception e1) {
                    System.out.println("Errore nel DataUtile.checkUser!");
                    e1.printStackTrace();
                }
            }
            return id;
        }
        
}



