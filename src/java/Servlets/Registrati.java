/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Util.DataUtile;
import System.Sviluppatore;
import static Util.DataUtile.crypt;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author user1
 */

public class Registrati extends HttpServlet {
    Map<String, Object> data = new HashMap<String, Object>();
    public int id=0;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            // Controllo ID da sessione 
            HttpSession s = SecurityLayer.checkSession(request);
            if(s != null){//condizione per vedere se la sessione esiste. 
                System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
                if(s.getAttribute("id") != null) id = (int) s.getAttribute("id");
                else id=0;
                System.out.println("ID ?? > " + id );
                data.put("id", id);    
            }else{
                id = 0;
                data.put("id", id);
            }  
            //Fine controllo ID da sessione
            try{
                FreeMarker.process("registrati.html", data, response, getServletContext());
            }catch(Exception e){}
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            System.out.println("STO FACENDO UNA RICHIESTA DI POST!");
            String action = request.getParameter("value");
            if("login".equals(action)){ // SE il metodo post è il login....
                System.out.println("IL TIPO DI POST E' UN LOGIN!!! ");
                String EmailL = request.getParameter("email");
                String PassL = request.getParameter("password");
                id = LoginValidate.validate(EmailL, PassL);
                if (id == 0) { //data.put("id",id); è inutile perché nel caso in cui non ci si connetta non ci serve registrare l'id e la mail
                // ID == 0 significa che non si è connessi! appare scritta  "non connesso!"
                    data.put("nome","");
                    data.put("id",0);
                    //out.println("<script type=\"text/javascript\">");
                    //out.println("alert('User or password incorrect');");
                    //out.println("</script>");
                    FreeMarker.process("registrati.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("registrati.html", data, response, getServletContext());
                    }catch(Exception e2){
                        System.out.println("Errore nel creare la sessione");
                        Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, e2);
                    }
                }   
            }else if("logout".equals(action)){ // Inizio del logout
               System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    processRequest(request, response);
                    FreeMarker.process("registrati.html", data, response, getServletContext());
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }else if("registra".equals(action)){
                System.out.println("POST::: Sto Registrando un nuovo account!");
                
                String nome = request.getParameter("Nome");
                String cognome = request.getParameter("Cognome");
                String email = request.getParameter("Email");
                String pass = request.getParameter("Pass");
                String dataNascita = request.getParameter("DataNascita");
                //String username = request.getParameter("Username");
                String tel = request.getParameter("Telefono");
                String indirizzo = request.getParameter("Indirizzo");
                
                
                // REGISTRAZIONE 
                try {
                    System.out.println("POST::: Mi connetto al database");
                    //Databasee.connect();
                    Map<String, Object> map = new HashMap<String, Object>();
                    System.out.println("COMINCIO A MAPPARE I VALORI---------------------------");
                    map.put("nome", nome);
                    map.put("cognome", cognome);
                    map.put("email", email);
                    map.put("password", crypt(pass));           
                    map.put("data", dataNascita);
                    map.put("telefono", tel);
                    map.put("indirizzo", indirizzo);
                    System.out.println("PRENDO TUTTI I VALORI???");
                    System.out.println(map);
                    System.out.println("FINE VALORI!!!");
                    //NO ID perché così l'utente potrà connettersi con le nuove credenziali (?)
                    int check = DataUtile.checkUser(email, pass);
                    System.out.println("CHECK: " + check);
                    if (check == 0) {//INSERISCO I DATI DI MAP NEL DATABASE
                        System.out.println("DENTRO CONDIZIONE CHECK=0");
                        Databasee.insertRecord("sviluppatore", map);
                        System.out.println("SORPASSO DATABASEE.INSERTRECORD");
                        response.sendRedirect("index");
                    }else{
                        response.sendRedirect("index");
                    }
                    try {
                        Databasee.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (NamingException ex) {
                    Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                System.out.println("ERRORE!!! CHE TIPO DI POST HO FATTO?");
            }
    }
}
