/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Util.Databasee;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
@WebServlet(name = "ModificaProgetti3", urlPatterns = {"/ModificaProgetti3"})
public class ModificaProgetti3 extends HttpServlet {
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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ModificaProgetti3</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ModificaProgetti3 at " + request.getContextPath() + "</h1>");
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
        Map<String, Object> map = new HashMap<String, Object>();
         HttpSession s = SecurityLayer.checkSession(request);
          int idpro=(int) s.getAttribute("idprogetto");
        String descrizione=request.getParameter("descrizione");
        String titolo=request.getParameter("titolo");
        String datas=request.getParameter("ds");
        
        if(descrizione!=""){
            try {
                map.put("descrizione", descrizione);
                Databasee.connect();
                Databasee.updateRecord("progetto",map,"progetto.id=" + idpro);
                map.clear();
            } catch (Exception ex) {
                Logger.getLogger(ModificaProgetti3.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        if(titolo!=""){
            try {
                map.put("titolo", titolo);
                Databasee.connect();
                Databasee.updateRecord("progetto",map,"progetto.id=" + idpro);
                map.clear();
            } catch (Exception ex) {
                Logger.getLogger(ModificaProgetti3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(datas!=""){
            try {
                map.put("datascad", datas);
                Databasee.connect();
                Databasee.updateRecord("progetto",map,"progetto.id=" + idpro);
                map.clear();
            } catch (Exception ex) {
                Logger.getLogger(ModificaProgetti3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        response.sendRedirect("profilo");
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
