/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import System.Task;
import System.Skill;
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
@WebServlet(name = "AddSkill", urlPatterns = {"/AddSkill"})
public class AddSkill extends HttpServlet {
Map<String, Object> data = new HashMap<String, Object>();
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
         int id=(int) s.getAttribute("id");
         System.out.println(id);
        try{
          Databasee.connect();
              
         //int idtask=(int) s.getAttribute("idtask");
         //System.out.println(idtask);
         //s.setAttribute("idtask",idtask);
         ResultSet ts= Databasee.selectRecord2("skill");
         
         ArrayList<Skill> Skill = new ArrayList<Skill>();
       
         while(ts.next()){
        
             String nomeskill = ts.getString("nome");
             
             Skill lista2 = new Skill(nomeskill);
            
             Skill.add(lista2);
         }
         
         data.put("nomeskill", Skill);
         Databasee.close();
         FreeMarker.process("addskill.html", data, response, getServletContext());
          }
     catch (SQLException ex) {
            Logger.getLogger(Backend.class.getName()).log(Level.SEVERE, null, ex);
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
    try {
        processRequest(request, response);
    } catch (Exception ex) {
        Logger.getLogger(Backend.class.getName()).log(Level.SEVERE, null, ex);
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
        HttpSession s = SecurityLayer.checkSession(request);
        String action = request.getParameter("value");
         int id=(int) s.getAttribute("id");
         System.out.println(id);
         
    try {
        Databasee.connect();
        
         if("logout".equals(action)){
                      System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    //processRequest(request, response);
                    response.sendRedirect("index");
                    //FreeMarker.process("index.html", data, response, getServletContext());
                }catch(Exception e3){
                    e3.printStackTrace();
                }
         }
         
         /*String azione = request.getParameter("valore");
          if("taskscelto".equals(action)){
         String idtask=request.getParameter("idtask");
         ResultSet ts= Databasee.selectTaskSkill("idtask");
         ArrayList<Task> Task = new ArrayList<Task>();
         ArrayList<Skill> Skill = new ArrayList<Skill>();
         while(ts.next()){
             String nometask = ts.getString("nometask");
             String nomeskill = ts.getString("nome");
             Task lista = new Task(nometask);
             Skill lista2 = new Skill(nomeskill);
             Task.add(lista);
             Skill.add(lista2);
         }
         data.put("nometask", Task);
         data.put("nomeskill", Skill);
         FreeMarker.process("addskill.html", data, response, getServletContext());
          }*/
         
        String azione = request.getParameter("valore"); 
       if("addskill".equals(azione)){
        String nome = request.getParameter("nome");
                Map<String, Object> map = new HashMap<String, Object>();
                 map.put("nome", nome);
                 map.put("idadmin", id);
                 Databasee.insertRecord("skill", map);
                 Databasee.close();
        processRequest(request, response);
       }}
     catch (Exception ex) {
        Logger.getLogger(Backend.class.getName()).log(Level.SEVERE, null, ex);
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