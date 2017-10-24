/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Progetto;
import System.Sviluppatore;
import System.TaskProgetto;

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
//@WebServlet(name = "ListaProgetti", urlPatterns = {"/ListaProgetti"})
public class ListaProgetti extends HttpServlet {
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
            System.out.println("processRequest di listaProcessi!");
            
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
            ArrayList<Progetto> prog = null;
            try{//Prova la connessione al Database
                Databasee.connect();
                ResultSet co = Databasee.selectProgetto("progetto");
                prog = new ArrayList<Progetto>();
                while (co.next()) {
                        String titolo = co.getString("titolo");
                        String descrizione = co.getString("descrizione");
                        int idProg = co.getInt("id");
                        Progetto lista = new Progetto(titolo, descrizione,idProg);
                        prog.add(lista);            
                }
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Progetto.class.getName()).log(Level.SEVERE, null, ex);
            }
            data.put("progetti", prog);
            FreeMarker.process("listaProgetti.html", data, response, getServletContext());
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
                    FreeMarker.process("listaProgetti.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("listaProgetti.html", data, response, getServletContext());
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
                    FreeMarker.process("listaProgetti.html", data, response, getServletContext());
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }else if("d_progetto".equals(action)){
                System.out.println("HO CLICCATO IL BOTTONE DI UN PROGETTO");
                int num = Integer.parseInt(request.getParameter("dettagli"));
                System.out.println("ID PROGETTI, DEVE COINCIDERE CON ID PROGETTO TASK --> " + num);
                /*Bisogna connettersi al DB, prendere tutte le informazioni di Progetto con quel determinato ID, e fare un hashMap*/
                ArrayList<TaskProgetto> taskProg = null;
                // DA MODIFICARE
                try{//Prova la connessione al Database
                    
                    Databasee.connect();
                    ResultSet co = Databasee.selectRecord("taskprogetto","idprogetto=" + num);
                    taskProg = new ArrayList<TaskProgetto>();
                    while (co.next()) {
                            //String titolo = co.getString("titolo");
                            //String descrizione = co.getString("descrizione");
                            //int idProg = co.getInt("id");
                            //Progetto lista = new Progetto(titolo, descrizione,idProg);
                            //prog.add(lista);   

                            int numeroColl = co.getInt("numcollaboratori");
                            boolean stato = co.getBoolean("stato");
                            int id = co.getInt("id");
                            String descrizione_task = co.getString("descrizione");
                            int id_progetto_task = co.getInt("idProgetto");
                            int id_task = co.getInt("idTask");
                            //System.out.println("Task presenti per quel progetto!!!");
                           // System.out.println("id progetto task:> " + id_progetto_task + " | id task:> " + id_task + " |id dell'insieme:> " + id_insieme);
                            //System.out.println("Descrizione del task! -> " + descrizione_task);
                            //public TaskProgetto(int id, String descrizione, int numCollaboratori, boolean stato, int idProgetto, int idTask){
                            TaskProgetto lista = new TaskProgetto(id,descrizione_task,numeroColl,stato,id_progetto_task,id_task);
                            taskProg.add(lista);
                    }
                    Databasee.close();
                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(Progetto.class.getName()).log(Level.SEVERE, null, ex);
                }
                data.put("taskprogetto", taskProg);
                System.out.println("appena prima del freemarker che ti porta a dettagliProgetto!");
                processRequest(request, response);
                FreeMarker.process("dettagliprogetto.html", data, response, getServletContext()); //perché dettagliprogetto in minuscolo?

                
            }
    }
 //selectRecord(String table, String condition)
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
