/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Task;
import System.Skill;
import System.Sviluppatore;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
@WebServlet(name = "SkillProgetto", urlPatterns = {"/SkillProgetto"})
public class SkillProgetto extends HttpServlet {
      Map<String, Object> data = new HashMap<String, Object>();
    public int id=0;
public int idtp=0;
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
       FreeMarker.process("skillprogetto.html", data, response, getServletContext());
        }
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
                    //processRequest(request, response);
                    FreeMarker.process("index.html", data, response, getServletContext());
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }
            
          try {
              Databasee.connect();
          } catch (Exception ex) {
              Logger.getLogger(SkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
          }
             String[] skill = request.getParameterValues("skill");
              String[] punteggio =request.getParameterValues("livmin");
             int cont2=0;
                       for(int i=0; i<punteggio.length; i++){
                           if(punteggio[i]!=""){
                           cont2++;}
                       }
                       String[] prep= new String[cont2];
                       
                       int cont3=0;
                               for(int j=0; j<punteggio.length; j++){           
                    if(punteggio[j]!=""){
                        prep[cont3]=punteggio[j];
                        cont3++;}
                            }
          try {
              ResultSet idtaskprogetto=Databasee.selectMaxRecord("taskprogetto", "taskprogetto.idprogetto=" + a.getAttribute("idprogetto") );
              while (idtaskprogetto.next()){
                  idtp=idtaskprogetto.getInt("id");
                  map.put("idtaskprogetto", idtp);
          } 
              
               for(int i=0; i<skill.length; i++){
              ResultSet idskillperognitask=Databasee.selectRecord("skillperognitask", "skillperognitask.idskill=" + skill[i] + " AND skillperognitask.idtask=" + a.getAttribute("idtask"));
               while(idskillperognitask.next()){
                   System.out.println("ENTRIIIIIIIIIIIIIIII");
                   int idspot=idskillperognitask.getInt("id");
                   map.put("idskillperognitask", idspot);
                   map.put("livellomin", prep[i]);
                   Databasee.insertRecord("skillscelte", map);
                   
               }
               } 
               Databasee.close();
               String azione = request.getParameter("fine");
               System.out.println(azione);
               if(azione.equals("indietro")){
ArrayList<Task> compiti = null;
    
       
                  try {
                      Databasee.connect();
                  } catch (Exception ex) {
                      Logger.getLogger(SkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
                  }
             ResultSet task= Databasee.selectTask();
            compiti = new ArrayList<Task>();
            while (task.next()) {
                        String tas = task.getString("nome");
                        int idt= task.getInt("id");
                        Task lista = new Task(idt,tas);
                        compiti.add(lista);            
                }
            
            Databasee.close();
             data.put("task", compiti);
        FreeMarker.process("taskskillprogetto.html", data, response, getServletContext());
               
               } 
               if(azione.equals("fine")){
                   response.sendRedirect("index");
               }
               
               
               if(azione.equals("invita")){
                   int idprog=(int) a.getAttribute("idprogetto");
        System.out.println(idprog);
         System.out.println(idtp); 
         ResultSet svilup=Databasee.selectSvilup(" progetto.id=" + idprog + " AND progetto.id=taskprogetto.idprogetto AND taskprogetto.id=" + idtp + " AND taskprogetto.id=skillscelte.idtaskprogetto AND skillscelte.id=" + skill[i] + " AND skillscelte.idskillperognitask=skillperognitask.id AND skillperognitask.idskill=skill.id AND skill.id=livello.idskill AND livello.preparazione>=skillscelte.livellomin AND sviluppatore.id=livello.idsviluppatore");
         ArrayList<Sviluppatore> svi= new ArrayList<Sviluppatore>(); 
         while(svilup.next()){
             String nomesvi=svilup.getString("nome");
             String cognomesvi=svilup.getString("cognome");
             Sviluppatore c=new Sviluppatore("nomesvi","cognomesvi");
             svi.add(c);
           
         }
         data.put("nomianti",svi);
         FreeMarker.process("invita.html", data, response, getServletContext());
               }
            } catch (SQLException ex) {
              Logger.getLogger(SkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
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
