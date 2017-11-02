/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Util.Databasee;
import System.Task;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
@WebServlet(name = "Invita", urlPatterns = {"/Invita"})
public class Invita extends HttpServlet {
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Invita</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Invita at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        HttpSession a = SecurityLayer.checkSession(request);
        String azione = request.getParameter("ancora");
               System.out.println(azione);
               if(azione.equals("inv")){
                  try {
                      Databasee.connect();
                      String[] idsv =request.getParameterValues("idsv");
                      int[] ids=new int[idsv.length];
                      for(int i=0; i<idsv.length; i++){
                          ids[i]=Integer.parseInt(idsv[i]);
                          System.out.println(ids[i]);
                      }
                      int idco=(int) a.getAttribute("idcoor");
                      
                      int idta=(int) a.getAttribute("idtaskprogetto");
                      Map<String, Object> map = new HashMap<String, Object>();
                      map.put("idcoordinatore", idco);
                      map.put("idtaskprogetto", idta);
                      
                      Calendar c = Calendar.getInstance();
                      
                      int year=c.get(Calendar.YEAR);
                      int month= c.get(Calendar.MONTH)+1;
                      int day=c.get(Calendar.DAY_OF_MONTH);
                      String today=year + "/" + month + "/" + day;
                      map.put("datacreazione", today);
                      map.put("tipo",0);
                      
                      for(int i=0; i<ids.length; i++){
                          ids[i]=Integer.parseInt(idsv[i]);
                          map.put("idsviluppatore", ids[i]);
                          try {
                              Databasee.insertRecord("richieste", map);
                             
                          } catch (SQLException ex) {
                              Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
                          }
                          System.out.println(ids[i]);
                      }
                       Databasee.close();
                      ArrayList<Task> compiti = null;
                      
                      
                      try {
                          Databasee.connect();
                      } catch (Exception ex) {
                          Logger.getLogger(SkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      ResultSet task= Databasee.selectTask();
                      
                      compiti = new ArrayList<Task>();
                      try {
                          while (task.next()) {
                              String tas = task.getString("nome");
                              int idt= task.getInt("id");
                              Task lista = new Task(idt,tas);
                              compiti.add(lista);
                          }
                      } catch (SQLException ex) {
                          Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      
                      Databasee.close();
                      data.put("task", compiti);
                      FreeMarker.process("taskskillprogetto.html", data, response, getServletContext());
                      
                  } catch (SQLException ex) {
                      Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (Exception ex) {
                Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
            }
               
               } 
               if(azione.equals("fine")){
                   response.sendRedirect("index");
               }
               
               
                  if(azione.equals("ind")){
                      try {
                          ArrayList<Task> compiti = null;
                          
                          
                          try {
                              Databasee.connect();
                          } catch (Exception ex) {
                              Logger.getLogger(SkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
                          }
                          ResultSet task= Databasee.selectTask();
                          
                          compiti = new ArrayList<Task>();
                          try {
                              while (task.next()) {
                                  String tas = task.getString("nome");
                                  int idt= task.getInt("id");
                                  Task lista = new Task(idt,tas);
                                  compiti.add(lista);
                              }
                          } catch (SQLException ex) {
                              Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
                          }
                          
                          Databasee.close();
                          data.put("task", compiti);
                          FreeMarker.process("taskskillprogetto.html", data, response, getServletContext());
                          
                          
                      } catch (SQLException ex) {
                          Logger.getLogger(Invita.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      
                
               }
               
        //Databasee.insertRecord("richieste", map);
        processRequest(request, response);
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
