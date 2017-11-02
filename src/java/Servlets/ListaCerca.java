/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
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
//@WebServlet(name = "ListaCerca", urlPatterns = {"/ListaCerca"})
public class ListaCerca extends HttpServlet {
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
            String ricercaStr = (String) request.getSession(true).getAttribute("ricerca");
            HttpSession s = SecurityLayer.checkSession(request);
            if(s != null){
                System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
                if(s.getAttribute("id") != null){
                    id = (int) s.getAttribute("id");
                }
                else{
                    id=0;
                }  
            }else{
                id = 0; 
            }     
            data.put("id", id);
            if(ricercaStr != ""){
                ArrayList<Progetto> prog = null;
                int numProgetti=0;
                int numSviluppatori=0;
                data.put("stringa",ricercaStr);
                try{//Prova la connessione al Database
                    Databasee.connect();
                    ResultSet co = Databasee.searchProgetti(ricercaStr);
                    prog = new ArrayList<Progetto>();
                    while (co.next()) {
                            String titolo = co.getString("titolo");
                            String descrizione = co.getString("descrizione");
                            int codice= co.getInt("id");
                            Progetto lista = new Progetto(titolo, descrizione,codice);
                            prog.add(lista);            
                            numProgetti +=1;
                    }
                    Databasee.close();
                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(Homee.class.getName()).log(Level.SEVERE, null, ex);
                }
                data.put("progetti", prog);
                data.put("numProgetti",numProgetti);
                System.out.println("ECCO PROVA PROG, COSA C'E' ??");
                System.out.println(prog);
                ArrayList<Sviluppatore> svilup = null;
                //manca  la parte dello sviluppatore!
                try{
                    Databasee.connect();
                    ResultSet co = Databasee.searchSviluppatori(ricercaStr);
                    svilup = new ArrayList<Sviluppatore>();
                    while(co.next()){
                        String nome = co.getString("nome");
                        String cognome = co.getString("cognome");
                        int codice= co.getInt("id");
                        Sviluppatore lista = new Sviluppatore(codice,nome,cognome);
                        svilup.add(lista);
                        numSviluppatori +=1;
                    }
                    Databasee.close();
                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(Homee.class.getName()).log(Level.SEVERE, null, ex);
                }
                data.put("sviluppatori",svilup);
                data.put("numSviluppatori", numSviluppatori);
                System.out.println("CHECK DI DATA!");
                System.out.println(data);
            }else{
                
            }
            FreeMarker.process("listaCerca.html", data, response, getServletContext());
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
            String action = request.getParameter("value");
            if("login".equals(action)){ // SE il metodo post è il login....
                System.out.println("IL TIPO DI POST E' UN LOGIN!!! ");
                String EmailL = request.getParameter("email");
                String PassL = request.getParameter("password");
                
                
                
                //piccolo controllo per entrare nella pagina backend (ovviamente il controllo sarà molto più ampio)
                       if(EmailL.equals("admin@admin.it") && PassL.equals("admin")){
                           id = LoginValidate.validateOfficer(EmailL, PassL);
                           try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        //processRequest(request, response);
                        
                        response.sendRedirect("backend");
                       // FreeMarker.process("backend.html", data, response, getServletContext());
                    }catch(Exception e2){
                        System.out.println("Errore nel creare la sessione");
                        Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, e2);
                    }
                    
                }
                       
                       
                id = LoginValidate.validate(EmailL, PassL);
                if (id == 0) { //data.put("id",id); è inutile perché nel caso in cui non ci si connetta non ci serve registrare l'id e la mail
                // ID == 0 significa che non si è connessi! appare scritta  "non connesso!"
                    data.put("nome","");
                    data.put("id",0);
                    //out.println("<script type=\"text/javascript\">");
                    //out.println("alert('User or password incorrect');");
                    //out.println("</script>");
                    FreeMarker.process("listaCerca.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("listaCerca.html", data, response, getServletContext());
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
                    //processRequest(request, response);
                    //FreeMarker.process("listaCerca.html", data, response, getServletContext());
                    response.sendRedirect("index");
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }else if("search".equals(action)){
                System.out.println("COMINCIA LA RICERCA!");
                String SearchStringa = request.getParameter("ricerca");
                System.out.println("RICERCA IN CORSO::::: >>>" + SearchStringa);           
                HttpSession s = SecurityLayer.checkSession(request);
                if(s != null){//condizione per vedere se la sessione esiste.                    
                    s.setAttribute("ricerca",SearchStringa);   
                }else{
                    HttpSession z = request.getSession(true);
                    z.setAttribute("ricerca",SearchStringa);         
                }   
                data.put("ricerca", SearchStringa);                
                response.sendRedirect("listaCerca");
            }else if("d_progetto".equals(action)){
                //INIZIO >>
                
                System.out.println("HO CLICCATO IL BOTTONE DI UN PROGETTO");
                int num = Integer.parseInt(request.getParameter("dettagli"));
                data.put("idprogetto", num);
                HttpSession s = SecurityLayer.checkSession(request);
                if(s != null){//condizione per vedere se la sessione esiste. 
                    System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
                    if(s.getAttribute("id") != null){ 
                        id = (int) s.getAttribute("id");
                    }else{ 
                        id=0;
                    }              
                    s.setAttribute("idprogetto",num);   
                }else{
                    System.out.println("Ho cliccato, Non esiste sessione, come passo i dati del progetto?");
                    id = 0;
                    HttpSession z = request.getSession(true);
                    z.setAttribute("idprogetto", num);         
                }   
                data.put("id", id); 
                response.sendRedirect("dettagliProgetto");
                
                // FINE <<
            }else if("d_sviluppatore".equals(action)){
                //INIZIO >>
                
                System.out.println("HO CLICCATO IL BOTTONE DI UNO SVILUPPATORE");
                int num = Integer.parseInt(request.getParameter("dettagli"));
                data.put("idsviluppatore", num);
                HttpSession s = SecurityLayer.checkSession(request);
                if(s != null){//condizione per vedere se la sessione esiste. 
                    System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
                    if(s.getAttribute("id") != null){ 
                        id = (int) s.getAttribute("id");
                    }else{ 
                        id=0;
                    }              
                    s.setAttribute("idsviluppatore",num);   
                }else{
                    System.out.println("Ho cliccato, Non esiste sessione, come passo i dati dello sviluppatore?");
                    id = 0;
                    HttpSession z = request.getSession(true);
                    z.setAttribute("idsviluppatore", num);         
                }   
                data.put("id", id); 
                response.sendRedirect("profilo");
                
                // FINE <<
            }
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
