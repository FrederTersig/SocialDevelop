/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Progetto;
import System.Sviluppatore;
import Util.DataUtile;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
/**/
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
/*Libreria Servlet*/
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Federico Tersigni
 */
//@WebServlet(name = "CreaProgetto", urlPatterns = {"/CreaProgetto"})
public class CreaProgetto extends HttpServlet {
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
            
            FreeMarker.process("creaprogetto.html", data, response, getServletContext());
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
        Map<String, Object> map = new HashMap<String, Object>();
         HttpSession a = SecurityLayer.checkSession(request);
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
                    FreeMarker.process("creaprogetto.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("creaprogetto.html", data, response, getServletContext());
                    }catch(Exception e2){
                        System.out.println("Errore nel creare la sessione");
                        Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, e2);
                    }
                }   
            } if("logout".equals(action)){// if("logout".equals(action)){ // Inizio del logout
                System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    processRequest(request, response);
                    FreeMarker.process("creaprogetto.html", data, response, getServletContext());
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }
            
             String nome = request.getParameter("nome");
                String descrizione = request.getParameter("descrizione");
Calendar c = Calendar.getInstance();

System.out.println(c.getTime());	/* Rappresentazione come stringa in base al tuo Locale */
System.out.println(c.get(Calendar.YEAR)); /* Ottieni l'anno */
System.out.println(c.get(Calendar.MONTH)); /* Ottieni il mese */
System.out.println(c.get(Calendar.DAY_OF_MONTH)); /* Ottieni il giorno */
int year=c.get(Calendar.YEAR);
int month= c.get(Calendar.MONTH)+1;
int day=c.get(Calendar.DAY_OF_MONTH);
String today=year + "/" + month + "/" + day;
System.out.println(today);
System.out.println(a.getAttribute("id"));
map.put("idsviluppatore", a.getAttribute("id"));
int idco=0;
        try {
            Databasee.connect();
            ResultSet coor=Databasee.selectRecord("sviluppatore,coordinatore", a.getAttribute("id") + "=coordinatore.idsviluppatore");
            if(!coor.next()){
            Databasee.insertRecord("coordinatore", map);}
          
            map.clear();
            ResultSet coo=Databasee.selectRecord2("sviluppatore,coordinatore", a.getAttribute("id") + "=coordinatore.idsviluppatore");
            while(coo.next()){
                idco = coo.getInt("id");
            }
            map.put("idcoordinatore", idco);
            map.put("titolo", nome);
            map.put("descrizione", descrizione);
            map.put("datacreazione", today);
            Databasee.insertRecord("progetto", map);
            Databasee.close();
        } catch (SQLException ex) {
            Logger.getLogger(CreaProgetto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CreaProgetto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.sendRedirect("listaProgetti");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
