/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Sviluppatore;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.equals;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "TaskVal", urlPatterns = {"/TaskVal"})
public class TaskVal extends HttpServlet {
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
       FreeMarker.process("taskval.html", data, response, getServletContext());
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
     try {
         Databasee.connect();
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
                 //processRequest(request, response);
                 //FreeMarker.process("listaProgetti.html", data, response, getServletContext());
                 response.sendRedirect("index");
             }catch(Exception e3){
                 e3.printStackTrace();
             }}
         if("search".equals(action)){
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
          if("coll".equals(action)){
              int idtp=Integer.parseInt(request.getParameter("task"));
              int idv=0;
              ResultSet colla=Databasee.selectRecord("collaboratore,sviluppatore,valutazione","collaboratore.idtaskprogetto=" + idtp + " AND collaboratore.idsviluppatore=sviluppatore.id AND collaboratore.id=valutazione.idcollaboratore");
              ArrayList<Sviluppatore> coll=new ArrayList<Sviluppatore>();
              if(!colla.next()){// è vuoto i
                  ResultSet collab=Databasee.selectRecord("collaboratore,sviluppatore","collaboratore.idtaskprogetto=" + idtp + " AND collaboratore.idsviluppatore=sviluppatore.id");
              while(collab.next()){
                  int idc=collab.getInt("collaboratore.id");
                  String nome=collab.getString("sviluppatore.nome");
                  String cognome=collab.getString("sviluppatore.cognome");
                  //idv=collab.getInt("valutazione.id");
             
                      
                 
                  Sviluppatore c=new Sviluppatore(idc,nome,cognome);
                  coll.add(c);
              
              }
              data.put("coll",coll);
              FreeMarker.process("listcoll.html", data, response, getServletContext());
              }
          }
         
     } catch (Exception ex) {
         Logger.getLogger(TaskVal.class.getName()).log(Level.SEVERE, null, ex);
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
