/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
@WebServlet(name = "UpdateTaskProgetto", urlPatterns = {"/UpdateTaskProgetto"})
public class UpdateTaskProgetto extends HttpServlet {
 Map<String, Object> data = new HashMap<String, Object>();
 int id =0;
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
       FreeMarker.process("updatetaskprog.html", data, response, getServletContext());
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
     try {
         HttpSession s = SecurityLayer.checkSession(request);
         
         String action = request.getParameter("value");
         
         if("logout".equals(action)){ // Inizio del logout
             System.out.println("CLICCATO LOGOUT!");
             try{
                 SecurityLayer.disposeSession(request); //chiude la sessione
                 id=0; //azzera l'id per il template
                 data.put("id",id);
                 response.sendRedirect("index");
             }catch(Exception e3){
                 e3.printStackTrace();
             }
         }else if("search".equals(action)){
             System.out.println("COMINCIA LA RICERCA!");
             String SearchStringa = request.getParameter("ricerca");
             System.out.println("RICERCA IN CORSO::::: >>>" + SearchStringa);
             
             if(s != null){//condizione per vedere se la sessione esiste.
                 s.setAttribute("ricerca",SearchStringa);
             }else{
                 HttpSession z = request.getSession(true);
                 z.setAttribute("ricerca",SearchStringa);
             }
             data.put("ricerca", SearchStringa);
             response.sendRedirect("listaCerca");
         }
         
         Databasee.connect();
         String descrizione=request.getParameter("descrizione");
         String nc=request.getParameter("nc");
         String[] skill = request.getParameterValues("skill");
         String[] punteggio =request.getParameterValues("punteggio");
         
         if(descrizione!=""){
             Map<String, Object> map = new HashMap<String, Object>();
             map.put("descrizione", descrizione);
             Databasee.updateRecord("taskprogetto", map, "taskprogetto.id=" + s.getAttribute("idtaskprogetto"));
         }
         if(nc!=""){
             Map<String, Object> map2 = new HashMap<String, Object>();
             map2.put("numCollaboratori", nc);
             Databasee.updateRecord("taskprogetto", map2, "taskprogetto.id=" + s.getAttribute("idtaskprogetto"));
         }
         
         
         if(skill!=null){
             
             
             for(int i=0; i<skill.length; i++){
                 System.out.println(skill[i]);}
             
             
             for(int i=0; i<punteggio.length; i++){
                 if(punteggio[i]!=""){
                     System.out.println(punteggio[i]);}
             }
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
             
             Map<String, Object> map3 = new HashMap<String, Object>();
             ResultSet idtask=Databasee.selectRecord("taskprogetto,task", "taskprogetto.id=" +s.getAttribute("idtaskprogetto")+ " AND taskprogetto.idtask=task.id");
            int idta=0;
             while(idtask.next()){
              idta=idtask.getInt("task.id");
            }
             for(int i=0; i<skill.length; i++){
              
                 ResultSet idskillperognitask=Databasee.selectRecord("skillperognitask", "skillperognitask.idskill=" + skill[i] + " AND skillperognitask.idtask=" + idta);
               while(idskillperognitask.next()){
                   System.out.println("ENTRIIIIIIIIIIIIIIII");
                   int idspot=idskillperognitask.getInt("id");
                   map3.put("idskillperognitask", idspot);
                   map3.put("livellomin", prep[i]);
                   map3.put("idtaskprogetto", s.getAttribute("idtaskprogetto"));
                   Databasee.insertRecord("skillscelte", map3);
                   
               }
               } 
         }
         
         
         Databasee.close();
         response.sendRedirect("profilo");
     } catch (Exception ex) {
         Logger.getLogger(UpdateTaskProgetto.class.getName()).log(Level.SEVERE, null, ex);
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
