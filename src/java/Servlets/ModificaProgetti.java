/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Progetto;
import System.TaskProgetto;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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
@WebServlet(name = "ModificaProgetti", urlPatterns = {"/ModificaProgetti"})
public class ModificaProgetti extends HttpServlet {
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
            throws ServletException, IOException, Exception {
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
            Databasee.connect();
            ResultSet prog=Databasee.selectRecord("sviluppatore,coordinatore,progetto", "sviluppatore.id=" + id + " AND sviluppatore.id=coordinatore.idsviluppatore AND coordinatore.id=progetto.idcoordinatore");
            ArrayList<Progetto> pro=new ArrayList<Progetto>(); 
            while(prog.next()){
                String nomp=prog.getString("titolo");
                int idp=prog.getInt("progetto.id");
                Progetto pr=new Progetto(idp,nomp);
                pro.add(pr);
            }
            data.put("progetti", pro);
            Databasee.close();
             FreeMarker.process("modificaprogetti.html", data, response, getServletContext());
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
         Logger.getLogger(ModificaProgetti.class.getName()).log(Level.SEVERE, null, ex);
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
     try {
         Databasee.connect();
         HttpSession s = SecurityLayer.checkSession(request);
         int idpr=Integer.parseInt(request.getParameter("progetti"));
         s.setAttribute("idprogetto", idpr);
         System.out.println(idpr);
         ResultSet task=Databasee.selectRecord("progetto,taskprogetto,task", "progetto.id=" + idpr + " AND progetto.id=taskprogetto.idprogetto AND taskprogetto.idtask=task.id");
         ArrayList<TaskProgetto> tas=new ArrayList<TaskProgetto>();
         while(task.next()){
             String nomet=task.getString("task.nome");
             int idtp=task.getInt("taskprogetto.id");
             TaskProgetto t=new TaskProgetto(idtp,nomet);
             tas.add(t);
            
         }
         data.put("taskp",tas);
         Databasee.close();
        FreeMarker.process("modificaprogetti2.html", data, response, getServletContext());
     } catch (Exception ex) {
         Logger.getLogger(ModificaProgetti.class.getName()).log(Level.SEVERE, null, ex);
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
