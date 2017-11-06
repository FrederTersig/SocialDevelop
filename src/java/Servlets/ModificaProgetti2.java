/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Skill;
import System.Task;
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
@WebServlet(name = "ModificaProgetti2", urlPatterns = {"/ModificaProgetti2"})
public class ModificaProgetti2 extends HttpServlet {
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
            FreeMarker.process("modificaprogetti2.html", data, response, getServletContext());
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
        
         String t=request.getParameter("t");
         Databasee.connect();
         HttpSession s = SecurityLayer.checkSession(request);
          int idpro=(int) s.getAttribute("idprogetto");
         if("mt".equals(t)){
             int idt=Integer.parseInt(request.getParameter("task"));
             s.setAttribute("idtaskprogetto",idt);
             ResultSet tp=Databasee.selectRecord("taskprogetto","taskprogetto.id=" + idt);
             ArrayList<TaskProgetto> tapr=new ArrayList<TaskProgetto>();
             while(tp.next()){
                 String descrizione=tp.getString("descrizione");
                 int nc=tp.getInt("numcollaboratori");
                 TaskProgetto tas=new TaskProgetto(nc,descrizione);
                 tapr.add(tas);
             }
             data.put("desc", tapr);
             
            int idtask=Integer.parseInt(request.getParameter("task"));
                      ResultSet skil=Databasee.selectRecord("skillscelte,skill,skillperognitask", "skillscelte.idtaskprogetto=" + idtask + " AND skillscelte.idskillperognitask=skillperognitask.id AND skillperognitask.idskill=skill.id");
         int numRows=0;
         while(skil.next()){
             numRows++;
             System.out.println(numRows);
         }
         numRows--;
         String a="";
         String b="";
         int i=0;
         skil.absolute(0);
         
         while(skil.next()){
             int idsk=skil.getInt("skill.id");
             if(i<numRows){
                 a="skill.id!=" + idsk + " AND ";
                 i++;
             }
             else{
                 a="skill.id!=" + idsk;
             }
             b=b+a;
         }
         System.out.println(b);
          ArrayList<Skill> ski=new ArrayList<Skill>();
         if(b!=""){
             ResultSet idtak2=Databasee.selectRecord("taskprogetto,task", "taskprogetto.id=" + idtask + " AND taskprogetto.idtask=task.id");
             int idtask2=0;
             while(idtak2.next()){
                 idtask2=idtak2.getInt("task.id");
             }
         ResultSet skillm=Databasee.selectRecord("skill,skillperognitask,task",b + " AND task.id=" + idtask2 + " AND skillperognitask.idskill=skill.id AND skillperognitask.idtask=task.id");
         
          while(skillm.next()){
             int id=skillm.getInt("skill.id");
             String nome=skillm.getString("skill.nome");
             Skill c=new Skill(id, nome);
             ski.add(c);
         }
         } else {
             ResultSet skillm=Databasee.selectRecord2("skill");
              while(skillm.next()){
             int id=skillm.getInt("id");
             String nome=skillm.getString("nome");
             Skill c=new Skill(id, nome);
             ski.add(c);}
         }
        
         
        
         data.put("skill",ski);
             FreeMarker.process("updatetaskprog.html", data, response, getServletContext());
         } else {
          ResultSet task=Databasee.selectRecord("progetto,taskprogetto,task", "progetto.id=" + idpro + " AND progetto.id=taskprogetto.idprogetto AND taskprogetto.idtask=task.id");
         int numRows=0;
         while(task.next()){
             numRows++;
             System.out.println(numRows);
         }
         numRows--;
         String a="";
         String b="";
         int i=0;
         task.absolute(0);
         
             while(task.next()){
             int idta=task.getInt("task.id");
             if(i<numRows){
                 a="task.id!=" + idta + " AND ";
                 i++;
             }
             else{
                 a="task.id!=" + idta;
             }
             b=b+a;
         }
             
                      System.out.println(b);
          ArrayList<Task> ta=new ArrayList<Task>();
         if(b!=""){
         ResultSet taskm=Databasee.selectRecord("task",b);
         
          while(taskm.next()){
             int id=taskm.getInt("id");
             String nome=taskm.getString("nome");
             Task c=new Task(id, nome);
             ta.add(c);
         }
         } else {
             ResultSet taskm=Databasee.selectRecord2("task");
              while(taskm.next()){
             int id=taskm.getInt("id");
             String nome=taskm.getString("nome");
             Task c=new Task(id, nome);
             ta.add(c);}
         }
        
         
        
         data.put("task",ta);
             
         }
        ResultSet idcoor=Databasee.selectRecord("sviluppatore,coordinatore","coordinatore.idsviluppatore=" + s.getAttribute("id"));
        while (idcoor.next()){
            s.setAttribute("idcoor", idcoor.getInt("coordinatore.id"));
        }
         FreeMarker.process("taskskillprogetto.html", data, response, getServletContext());
     } catch (Exception ex) {
         Logger.getLogger(ModificaProgetti2.class.getName()).log(Level.SEVERE, null, ex);
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
