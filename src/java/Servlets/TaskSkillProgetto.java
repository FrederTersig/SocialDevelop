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
@WebServlet(name = "TaskSkillProgetto", urlPatterns = {"/TaskSkillProgetto"})
public class TaskSkillProgetto extends HttpServlet {
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
         
              FreeMarker.process("taskskillprogetto.html", data, response, getServletContext());
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
            
             String idtask = request.getParameter("idt");
             a.setAttribute("idtask", idtask);
                String descrizione = request.getParameter("descrizione");
                int membri =Integer.parseInt(request.getParameter("membri"));
                int idcor=(int) a.getAttribute("idcoor");
        try {
            Databasee.connect();
            ResultSet idprog=Databasee.selectMaxRecord("progetto", idcor + "=progetto.idcoordinatore");
            while(idprog.next()){
                int idprogetto= idprog.getInt("id");
                a.setAttribute("idprogetto", idprogetto);
                map.put("idprogetto", idprogetto);
            }
            
            map.put("descrizione", descrizione);
            map.put("idtask", idtask);
            map.put("numcollaboratori", membri);
            map.put("stato", 0);
            Databasee.insertRecord("taskprogetto", map);
            ResultSet utask=Databasee.selectMaxRecord("taskprogetto", "taskprogetto.idprogetto="+ a.getAttribute("idprogetto"));
            while (utask.next()){
                int idt=utask.getInt("id");
                a.setAttribute("idtaskprogetto",idt);
            }
            ResultSet tasksel=Databasee.selectRecord("task", "task.id=" + idtask);
            ArrayList<Task> Task = new ArrayList<Task>();
            while(tasksel.next()){
                String nomtask=tasksel.getString("nome");
                Task lista3 = new Task(nomtask);
                Task.add(lista3);
                data.put("nomtask", Task);
            }
            ResultSet skilltask=Databasee.selectRecordst("skillperognitask, skill", "idtask=" + idtask + " AND skill.id=skillperognitask.idskill");
            ArrayList<Skill> Skill = new ArrayList<Skill>();
            while(skilltask.next()){
                String nomskill=skilltask.getString("nome");
                int idskills=skilltask.getInt("id");
                 Skill lista2 = new Skill(idskills,nomskill);
            
             Skill.add(lista2);
            }
            data.put("nomes", Skill);
            Databasee.close();
        } catch (SQLException ex) {
            Logger.getLogger(TaskSkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TaskSkillProgetto.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        FreeMarker.process("skillprogetto.html", data, response, getServletContext());
        //processRequest(request, response);
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
