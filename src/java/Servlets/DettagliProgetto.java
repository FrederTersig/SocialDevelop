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
import System.Task;

import Util.DataUtile;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
/**/
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
/*Libreria Servlet*/
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Federico Tersigni
 */
//@WebServlet(name = "DettagliProgetto", urlPatterns = {"/DettagliProgetto"})
public class DettagliProgetto extends HttpServlet {
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
    
    /*
    
                // DA MODIFICARE
                

                //PROVA
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            System.out.println("Richiedo di entrare in dettagliProgetto!");
            int num = (int) request.getSession(true).getAttribute("idprogetto");
            data.put("idprogetto",num);
            HttpSession s = SecurityLayer.checkSession(request);

            if(s != null){//condizione per vedere se la sessione esiste. 
                System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
                if(s.getAttribute("id") != null){
                    id = (int) s.getAttribute("id");
                }else{ 
                    id=0;
                }
                System.out.println("ID ?? > " + id );
                data.put("id", id);    
            }else{
                System.out.println("Non esiste la sessione mentre si è in dettagliProgetto");
                id = 0;
                data.put("id", id);
            }  
            ArrayList<TaskProgetto> taskProg=null;
                try{
                    Databasee.connect();
                    ResultSet co = Databasee.selectTaskProg(num);
                    taskProg = new ArrayList<TaskProgetto>();
                    while (co.next()){
                        int numeroColl = co.getInt("numcollaboratori");
                        boolean stato = co.getBoolean("stato");
                        String nome = co.getString("nome");
                        TaskProgetto lista = new TaskProgetto(numeroColl, stato, nome);
                        taskProg.add(lista);
                    }

                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(TaskProgetto.class.getName()).log(Level.SEVERE, null, ex);
                }
                data.put("taskprogetto", taskProg);
            /* 
            ArrayList<TaskProgetto> taskProg = null;
            ArrayList<Task> task = null;
            List<Integer> listaIdTask = new ArrayList<Integer>();

                try{//Query per avere gli id dei task presenti al progetto              
                    Databasee.connect();
                    ResultSet co = Databasee.selectRecord("taskprogetto","idprogetto=" + num);
                    taskProg = new ArrayList<TaskProgetto>();
                    while (co.next()) {
                            int numeroColl = co.getInt("numcollaboratori");
                            boolean stato = co.getBoolean("stato");
                            int id = co.getInt("id");
                            String descrizione_task = co.getString("descrizione");
                            int id_progetto_task = co.getInt("idProgetto");
                            int id_task = co.getInt("idTask");
                            TaskProgetto lista = new TaskProgetto(id,descrizione_task,numeroColl,stato,id_progetto_task,id_task);
                            taskProg.add(lista);
                            listaIdTask.add(id_task);
                    }
                    System.out.println("PROVA>>>" + taskProg.get(0).getId());
                    Databasee.close();
                }catch(NamingException e) {
                }catch (SQLException e) {
                }catch (Exception ex) {
                        Logger.getLogger(TaskProgetto.class.getName()).log(Level.SEVERE, null, ex);
                }
                data.put("taskprogetto", taskProg); 
                
                try{
                    Databasee.connect();
                    task = new ArrayList<Task>();
                    System.out.println("Dettagli dei task");
                    //da cambiare
                    for(Integer i:listaIdTask){
                        System.out.println("ECCOLO!!");
                        ResultSet co = Databasee.selectRecord("task","id="+i);
                        //Prendo i risultati che ho ricevuto nella chiamata:
                        String nome = co.getString("nome");
                        Task lista = new Task(i, nome); // i è l'id del task
                        task.add(lista);
                        System.out.println("!!!! >  " + lista);
                        System.out.println("!!!! >  " + task);
                    }
                    Databasee.close();
                }catch (NamingException e){
                }catch (SQLException e){
                }catch (Exception ex){
                        Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
                }
                data.put("task",task);
                System.out.println("visualizzazione DATA");
                System.out.println(data);
            */

            FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
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
        System.out.println("get di dettagliProgetto");
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
            String action = request.getParameter("value");
            System.out.println("post di dettaglioProgetto");
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
                    FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        processRequest(request, response);
                        FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
                    }catch(Exception e2){
                        System.out.println("Errore nel creare la sessione");
                        Logger.getLogger(Progetto.class.getName()).log(Level.SEVERE, null, e2);
                    }
                }   
            }else if("logout".equals(action)){ // Inizio del logout
                System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    //processRequest(request, response);
                    //FreeMarker.process("dettagliProgetto.html", data, response, getServletContext());
                    response.sendRedirect("index");
                }catch(Exception e3){
                    e3.printStackTrace();
                }
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
