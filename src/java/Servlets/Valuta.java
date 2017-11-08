/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Progetto;
import System.Sviluppatore;
import System.TaskProgetto;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
@WebServlet(name = "Valuta", urlPatterns = {"/Valuta"})
public class Valuta extends HttpServlet {
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
            int cont3=0;
            while(prog.next()){
                int cont=0;
            int cont2=0;
                ResultSet taskprog=Databasee.selectRecord("taskprogetto","taskprogetto.idprogetto=" + prog.getInt("progetto.id"));
                ResultSet n=Databasee.contTaskProgetto(prog.getInt("progetto.id"));
                while(n.next()){
                    cont=n.getInt("num");
                    System.out.println(cont);
                }
                while(taskprog.next()){
                    ResultSet val=Databasee.selectRecord("taskprogetto,collaboratore,valutazione","collaboratore.idtaskprogetto=" + taskprog.getInt("id") + " AND collaboratore.id=valutazione.idcollaboratore");
                    if(val.next()){
                        System.out.println("dimmi che entri/////");
                        cont2++;
                        System.out.println(cont2);
                    }
                }
                System.out.println(cont);
                System.out.println(cont2);
                
                if(cont!=cont2){
                    
                String nomp=prog.getString("titolo");
                int idp=prog.getInt("progetto.id");
                          String datascad=prog.getString("datascad");
                            System.out.println(datascad);
                            int scad=1;
                            if(datascad!=null){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                 java.util.Date daConfrontare = sdf.parse(datascad);

                 // Catturo la data di oggi
                 java.util.Date oggi = java.util.GregorianCalendar.getInstance().getTime( );

                 // Faccio confronto
                 if(oggi.compareTo(daConfrontare)<0) {
                 System.out.println("in corso");
                 scad=1;
                 } else if(oggi.compareTo(daConfrontare)>0) {
                     scad=0;
                 System.out.println("finito");
                 cont3++;
                    System.out.println(cont3 + "+++++++++++" );
                 } else if(oggi.compareTo(daConfrontare) == 0) {
                     scad=2;
                 System.out.println("ultimo giorno");
                 }}
               
                Progetto pr=new Progetto(idp,nomp,scad);
                pro.add(pr);
            }
            }
            if(cont3==0){
                data.put("progetti", null);
            }else{
            data.put("progetti", pro);
            }
            Databasee.close();
             FreeMarker.process("valutazione.html", data, response, getServletContext());
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
         Logger.getLogger(Valuta.class.getName()).log(Level.SEVERE, null, ex);
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
             if("valuta".equals(action)){
                 int idp=Integer.parseInt(request.getParameter("progetti"));
                  HttpSession s = SecurityLayer.checkSession(request);
                 s.setAttribute("idproge",idp);
                 ResultSet prog=Databasee.selectRecord("sviluppatore,coordinatore,progetto", "sviluppatore.id=" + id + " AND sviluppatore.id=coordinatore.idsviluppatore AND coordinatore.id=progetto.idcoordinatore");
            ArrayList<Progetto> pro=new ArrayList<Progetto>(); 
             ArrayList<TaskProgetto> lit=new ArrayList<TaskProgetto>();
            while(prog.next()){
                int cont=0;
            int cont2=0;
                ResultSet taskprog=Databasee.selectRecord("taskprogetto,task","taskprogetto.idprogetto=" + prog.getInt("progetto.id") + " AND taskprogetto.idtask=task.id");
                ResultSet n=Databasee.contTaskProgetto(prog.getInt("progetto.id"));
                while(n.next()){
                    cont=n.getInt("num");
                    System.out.println(cont);
                }
                while(taskprog.next()){
                    ResultSet val=Databasee.selectRecord("taskprogetto,collaboratore,valutazione","collaboratore.idtaskprogetto=" + taskprog.getInt("id") + " AND collaboratore.id=valutazione.idcollaboratore");
                    if(val.next()){
                        System.out.println("dimmi che entri/////");
                        cont2++;
                        System.out.println(cont2);
                    } else {
                        int idt=taskprog.getInt("taskprogetto.id");
                     String nome=taskprog.getString("task.nome");
                     TaskProgetto l=new TaskProgetto(idt,nome);
                     lit.add(l);
                    }
                }}
                 
                 
                 
                 /*ResultSet listtask=Databasee.selectRecord("taskprogetto,task", "taskprogetto.idprogetto=" + idp + " AND taskprogetto.idtask=task.id");
                 ArrayList<TaskProgetto> lit=new ArrayList<TaskProgetto>();
                 while(listtask.next()){
                     int idt=listtask.getInt("taskprogetto.id");
                     String nome=listtask.getString("task.nome");
                     TaskProgetto l=new TaskProgetto(idt,nome);
                     lit.add(l);
                 }*/
                 data.put("listatask",lit);
            Databasee.close();
            FreeMarker.process("taskval.html", data, response, getServletContext());
             }
         
     } catch (Exception ex) {
         Logger.getLogger(Valuta.class.getName()).log(Level.SEVERE, null, ex);
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
