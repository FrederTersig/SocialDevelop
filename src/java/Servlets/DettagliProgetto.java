/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Commenti;
import System.Coordinatore;
import System.Progetto;
import System.Sviluppatore;
import System.TaskProgetto;
import System.Task;

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
//@WebServlet(name = "DettagliProgetto", urlPatterns = {"/DettagliProgetto"})
public class DettagliProgetto extends HttpServlet {
    Map<String, Object> data = new HashMap<String, Object>();
    public int id=0;
    public int codiceCollaboratore=0;
    int num=0; // Variabile dove si salva l'id progetto
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
            System.out.println("Richiedo di entrare in dettagliProgetto!");
            // request.getSession andrà bene per utenti loggati e non?
            num = (int) request.getSession(true).getAttribute("idprogetto");
            data.put("idprogetto",num);
            HttpSession s = SecurityLayer.checkSession(request);
            Boolean collabora = false;
            //COMMENTI = testo, nome, cognome, visibilità(boolean)
            
            if(s != null){//condizione per vedere se la sessione esiste. 
                System.out.println("S DIVERSA DA NULL DENTRO DETTAGLIPROGETTO!");
                if(s.getAttribute("id") != null){
                    id = (int) s.getAttribute("id");
                }else{ 
                    id=0;
                }
                System.out.println("ID ?? > " + id );
                data.put("id", id);    
                
                //SE esiste lo userid nella sessione, vedi se fa parte del progetto. 
                //Registra in data la variabile booleana "Collabora" che sarà false se non è connesso o se non è tra i collaboratori del progetto
                //True altrimenti
                System.out.println("Check collaboratore per progetto con id -> " + num);
                
                if(s.getAttribute("userid") != null){
                    int svil = (int) s.getAttribute("userid");
                    try{
                        Databasee.connect();
                        System.out.println("APPENA PRIMA DI RESULTSET PER CHECKCOLLABORATORE!");
                        ResultSet ko = Databasee.checkCollaboratore(num, svil);
                        System.out.println("Risultato :::: ");
                        collabora = ko.absolute(2);
                        if(collabora){
                            //SE lo sviluppatore è un collaboratore, registrami il suo id
                            ResultSet cid = Databasee.getCollaboratoreId(num, svil);
                            while(cid.next()){
                                codiceCollaboratore = cid.getInt(1);
                                System.out.println("HO L'ID DEL COLLABORATORE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + codiceCollaboratore);
                            }
                        }
                        System.out.println("UGUALI? >> "+ ko.absolute(2) + " " + collabora );
                        //int prova = ko.getInt("visibilità");
                        Databasee.close();
                    }catch(NamingException e) {
                    }catch (SQLException e) {
                    }catch (Exception ex) {
                            Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    System.out.println("Non esiste userid");
                }
            }else{
                System.out.println("Non esiste la sessione mentre si è in dettagliProgetto");
                id = 0;
                data.put("id", id);
            }  
            data.put("collabora", collabora);
            //ArrayList<Progetto> prog=null;
            System.out.println("COMINCIO LA CONNESSIONE");

            System.out.println(data);
            System.out.println("---------");
            // Parte dove si recuperano le informazioni del progetto dal database
            ArrayList<Progetto> progettoDet =null;
            ArrayList<Coordinatore> cor=null;
            
                try{
                    progettoDet = new ArrayList<Progetto>();
                    Databasee.connect();
                    ResultSet mo = Databasee.selectProgettoDetail(num);
                     cor=new ArrayList<Coordinatore>();
                    while(mo.next()){
                        String nome = mo.getString("nome");
                        String cognome = mo.getString("cognome");
                        String descrizione = mo.getString("descrizione");
                        String titolo = mo.getString("titolo");
                        int idcoor=mo.getInt("coordinatore.id");
                       Coordinatore idcoo=new Coordinatore(idcoor);
                        Progetto nuova = new Progetto(nome,cognome,titolo,descrizione);
                        cor.add(idcoo);
                        progettoDet.add(nuova);
                    }
                   
                    Databasee.close();
                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(Progetto.class.getName()).log(Level.SEVERE, null, ex);
                }
            data.put("progettodettaglio", progettoDet);
            data.put("idcoordinatore", cor);
            
            //Chiamata per prendere informazioni dei task presenti nel progetto
            ArrayList<TaskProgetto> taskProg=null;
                try{
                    Databasee.connect();
                    ResultSet co = Databasee.selectTaskProg2(num);
                    taskProg = new ArrayList<TaskProgetto>();
                    while (co.next()){
                        int numeroColl = co.getInt("numcollaboratori");
                        boolean stato = co.getBoolean("stato");
                        String nome = co.getString("nome");
                        int idtp=co.getInt("taskprogetto.id");
                        TaskProgetto lista = new TaskProgetto(idtp,numeroColl, stato, nome);
                        taskProg.add(lista);
                    }
                    Databasee.close();

                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(TaskProgetto.class.getName()).log(Level.SEVERE, null, ex);
                }
            data.put("taskprogetto", taskProg);
            
            ArrayList<Commenti> listaCommenti=null;
                try{
                    Databasee.connect();
                    ResultSet co = Databasee.getCommentiProgetto(num);
                    listaCommenti = new ArrayList<Commenti>();
                    while(co.next()) {
                        String testo = co.getString("testo");
                        Boolean visibil = co.getBoolean("visibilità");
                        String nome = co.getString("nome");
                        String cognome = co.getString("cognome");
                        Commenti lista = new Commenti(testo,nome,cognome,visibil);
                        listaCommenti.add(lista);
                    }
                    Databasee.close();
                    
                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(Commenti.class.getName()).log(Level.SEVERE, null, ex);
                }
            data.put("commenti", listaCommenti);
            System.out.println("Check SESSIONE: COSA ABBIAMO??");
            System.out.println(data);
            FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
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
        System.out.println("get di dettagliProgetto");
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
            System.out.println("post di dettaglioProgetto");
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
                    FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
                    }catch(Exception e2){
                        System.out.println("Errore nel creare la sessione");
                        Logger.getLogger(Progetto.class.getName()).log(Level.SEVERE, null, e2);
                    }
                }   
            }else if("logout".equals(action)){ // Inizio del logout
                System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    //processRequest(request, response);
                    //FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
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
            }else if("commenta".equals(action)) {
                
                //String action = request.getParameter("value");
                
                System.out.println("PROVO A MANDARE UN COMMENTA!!!");
                String vis=request.getParameter("check"); // giusto
                String comment=request.getParameter("comment"); // giusto
   
                Map<String,Object> commenti=new HashMap<String,Object>();
                if("no".equals(vis)){
                    try {
                       commenti.put("idcollaboratore", codiceCollaboratore);
                       commenti.put("idprogetto", num);
                       commenti.put("testo", comment);
                       commenti.put("visibilità", 1);
                       Databasee.connect();
                       Databasee.insertRecord("commenti", commenti);
                       Databasee.close();
                       response.sendRedirect("dettagliProgetto");
                    } catch (SQLException ex) {
                       Logger.getLogger(DettagliProgetto.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                       Logger.getLogger(DettagliProgetto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                }else{
                    try {
                       commenti.put("idcollaboratore", codiceCollaboratore);
                       commenti.put("idprogetto", num);
                       commenti.put("testo", comment);
                       commenti.put("visibilità", 0);
                       Databasee.connect();
                       Databasee.insertRecord("commenti", commenti);
                       Databasee.close();
                       response.sendRedirect("dettagliProgetto");
                    } catch (SQLException ex) {
                       Logger.getLogger(DettagliProgetto.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                       Logger.getLogger(DettagliProgetto.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
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
