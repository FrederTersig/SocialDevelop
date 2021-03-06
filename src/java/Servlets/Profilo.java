/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Skill;
import System.Sviluppatore; 
import System.Valutazione;
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
//@WebServlet(name = "Profilo", urlPatterns = {"/Profilo"})
public class Profilo extends HttpServlet {
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
            throws ServletException, IOException, SQLException, Exception {
            response.setContentType("text/html;charset=UTF-8");
            HttpSession s = SecurityLayer.checkSession(request);
            int idPro = (int) s.getAttribute("id");
            data.put("idsviluppatore",idPro);
            
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
            //Sviluppatore prova = null;
            try{
                Databasee.connect();
                ResultSet ex = Databasee.getInfoProfilo(idPro); // SBAGLIATO, devo avere l'id del profilo CLICCATO***************************************
                detSvilupp = new ArrayList<Sviluppatore>();
                while(ex.next()){
                    String nome = ex.getString("nome");
                    String cognome = ex.getString("cognome");
                    String email = ex.getString("email");
                    String telefono= (String) ex.getString("telefono");
                    String indirizzo= ex.getString("indirizzo");
                     String immagine= ex.getString("immagine");
                     String curriculum= ex.getString("curriculum");
                     System.out.println(curriculum);
                    Date nascita = ex.getDate("data");
                    Sviluppatore lista = new Sviluppatore(nome,cognome,nascita,email,telefono,indirizzo,immagine,curriculum);
                    detSvilupp.add(lista);
                }
                
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("ARRAY PROFILO??" + detSvilupp);
            data.put("profiloSv", detSvilupp);
            
            ArrayList<Skill> listaSkill = null;
            try{//Prova la connessione al Database
                Databasee.connect();
                ResultSet ex = Databasee.getSvilupSkills(idPro); // DA METTERE ID DELLO SVILUPPATORE CLICCATO!!!!!!!!!!!!!!*******************************************
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
                ResultSet ex = Databasee.getValutazioniProf2(idPro); //****************************************************************
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
            FreeMarker.process("profilo.html", data, response, getServletContext());
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
        } catch (SQLException ex) {
            Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Profilo.class.getName()).log(Level.SEVERE, null, ex);
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
                    FreeMarker.process("profilo.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("profilo.html", data, response, getServletContext());
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
                    //FreeMarker.process("profilo.html", data, response, getServletContext());
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
