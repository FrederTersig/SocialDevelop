/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Skill;
import System.Sviluppatore;
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
 int id=0;
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
