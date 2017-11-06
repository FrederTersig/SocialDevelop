/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Skill;
import System.Sviluppatore;
import System.Valutazione;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
@WebServlet(name = "ProfiloCercato", urlPatterns = {"/ProfiloCercato"})
public class ProfiloCercato extends HttpServlet {
 Map<String, Object> data = new HashMap<String, Object>();
    public int id=0;
    int num=0;
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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
  HttpSession s = SecurityLayer.checkSession(request);
  
  
   if(s != null){//condizione per vedere se la sessione esiste. 
                System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
                
                if(s.getAttribute("id") != null){
                    id = (int) s.getAttribute("id");
                    
                }
                
                else{
                    id=0;
                }
                System.out.println("ID ?? > " + id );
                data.put("id", id);    
            }else{
                id = 0;
                data.put("id", id);
             
                
            }     
  
  
  ArrayList<Sviluppatore> detSvilupp = null;
            try{
                Databasee.connect();
                ResultSet ex = Databasee.getInfoProfilo(num); 
                detSvilupp = new ArrayList<Sviluppatore>();
                while(ex.next()){
                    String nome = ex.getString("nome");
                    String cognome = ex.getString("cognome");
                    String email = ex.getString("email");
                    String telefono= (String) ex.getString("telefono");
                    String indirizzo= ex.getString("indirizzo");
                    String immagine= ex.getString("immagine");
                    Date nascita = ex.getDate("data");
                    Sviluppatore lista = new Sviluppatore(nome,cognome,nascita,email,telefono,indirizzo,immagine);
                    detSvilupp.add(lista);
                }
                
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
            }
    data.put("profiloSv", detSvilupp);
    
                ArrayList<Skill> listaSkill = null;
            try{//Prova la connessione al Database
                Databasee.connect();
                ResultSet ex = Databasee.getSvilupSkills(num); 
                listaSkill = new ArrayList<Skill>();
                while (ex.next()) {
                        String nome = ex.getString("nome");
                        int competenza = ex.getInt("preparazione");
                        
                        Skill lista = new Skill(nome, competenza);
                        listaSkill.add(lista);            
                }
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            data.put("skill", listaSkill);
            ArrayList<Valutazione> detValutazione = null;
            try{
                Databasee.connect();
                ResultSet ex = Databasee.getValutazioniProf(num); 
                detValutazione = new ArrayList<Valutazione>();
                while(ex.next()){
                   int punteggio = ex.getInt("punteggio");
                   String nome = ex.getString("nome"); 
                   String cognome = ex.getString("cognome");
                   String titolo = ex.getString("titolo");
                   String descrizione = ex.getString("descrizione");
                   
                   Valutazione lista = new Valutazione(punteggio,nome,cognome,titolo,descrizione);
                   detValutazione.add(lista);
                }
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Homee.class.getName()).log(Level.SEVERE, null, ex);
            }
            data.put("valutazioni", detValutazione);
            Databasee.connect();
            ResultSet coo=Databasee.selectRecord("coordinatore,sviluppatore","sviluppatore.id=" + id + " AND sviluppatore.id=coordinatore.idsviluppatore");
           
            if(coo.next()!=false){
                data.put("coord",1);
            } else{
                data.put("coord",0);
            }
            Databasee.close();
            //DEVI AGGIUNGERE TUTTO SU DATA
            FreeMarker.process("profilocercato.html", data, response, getServletContext());
  
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
     try {
         processRequest(request, response);
     } catch (Exception ex) {
         Logger.getLogger(ProfiloCercato.class.getName()).log(Level.SEVERE, null, ex);
     }
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
     try {
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
            }
                         
         if("d_sviluppatore".equals(action)){
         System.out.println("HO CLICCATO IL BOTTONE DI UNO SVILUPPATORE");
                num = Integer.parseInt(request.getParameter("dettagli"));
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
                    System.out.println(z.getAttribute("idsviluppatore") + "//////(//////");
                }   
                data.put("id", id);
         processRequest(request, response);}
     } catch (Exception ex) {
         Logger.getLogger(ProfiloCercato.class.getName()).log(Level.SEVERE, null, ex);
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
