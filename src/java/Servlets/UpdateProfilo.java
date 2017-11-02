/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

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
import static javax.swing.UIManager.getInt;

/**
 *
 * @author user1
 */
@WebServlet(name = "UpdateProfilo", urlPatterns = {"/UpdateProfilo"})
public class UpdateProfilo extends HttpServlet {
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
            throws ServletException, IOException, SQLException {
     try {
         response.setContentType("text/html;charset=UTF-8");
         HttpSession s = SecurityLayer.checkSession(request);
         if(s != null){//condizione per vedere se la sessione esiste.
             System.out.println("S DIVERSA DA NULL! ADESSO ID VIENE CAMBIATO!! GUARDA!");
             if(s.getAttribute("id") != null) id = (int) s.getAttribute("id");
             else id=0;
             System.out.println("ID ?? > " + id );
             data.put("id", id);
         }else{
             id = 0;
             data.put("id", id);
         }
         Databasee.connect();
         ResultSet sv=Databasee.selectRecord("sviluppatore", "sviluppatore.id=" + id);
         ArrayList<Sviluppatore> asd=new ArrayList<Sviluppatore>();
         while(sv.next()){
             String indirizzo=sv.getString("indirizzo");
             String telefono=sv.getString("telefono");
             Sviluppatore up=new Sviluppatore(indirizzo,telefono);
             asd.add(up);
         }
         
         data.put("svi", asd);
         
         ResultSet skil=Databasee.selectRecord("sviluppatore,livello,skill", "sviluppatore.id=" + id + " AND sviluppatore.id=livello.idsviluppatore AND livello.idskill=skill.id");
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
         ResultSet skillm=Databasee.selectRecord("skill",b);
         ArrayList<Skill> ski=new ArrayList<Skill>();
         
         while(skillm.next()){
             int id=skillm.getInt("id");
             String nome=skillm.getString("nome");
             Skill c=new Skill(id, nome);
             ski.add(c);
         }
         data.put("skill",ski);
         
         ResultSet cur=Databasee.selectRecord("sviluppatore","sviluppatore.id=" + s.getAttribute("id"));
         ArrayList<Sviluppatore> curri=new ArrayList<Sviluppatore>();
         while(cur.next()){
             String pathc=cur.getString("curriculum");
             String imm=cur.getString("immagine");
             System.out.println(imm);
             Sviluppatore curric=new Sviluppatore(imm);
             curri.add(curric);
         }
         data.put("curricu",curri);
         Databasee.close();
         FreeMarker.process("updateprofilo.html", data, response, getServletContext());
     } catch (Exception ex) {
         Logger.getLogger(UpdateProfilo.class.getName()).log(Level.SEVERE, null, ex);
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
     } catch (SQLException ex) {
         Logger.getLogger(UpdateProfilo.class.getName()).log(Level.SEVERE, null, ex);
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
     try {
         Databasee.connect();
         String indirizzo=request.getParameter("indirizzo");
         String telefono=request.getParameter("telefono");
         String[] skill = request.getParameterValues("skill");
         String[] punteggio =request.getParameterValues("punteggio");
         
         if(indirizzo!=""){
             Map<String, Object> map = new HashMap<String, Object>();
             map.put("indirizzo", indirizzo);
             Databasee.updateRecord("sviluppatore", map, "sviluppatore.id=" + s.getAttribute("id"));
         }
         if(telefono!=""){
             Map<String, Object> map2 = new HashMap<String, Object>();
             map2.put("telefono", telefono);
             Databasee.updateRecord("sviluppatore", map2, "sviluppatore.id=" + s.getAttribute("id"));
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
                            map3.put("idsviluppatore", s.getAttribute("id"));
                            for(int i=0; i<skill.length; i++){
                                map3.put("idskill", skill[i]);
                                map3.put("preparazione", prep[i]);
                                Databasee.insertRecord("livello", map3);
                            }
         }           
                        
         
         Databasee.close();
         
         
         
         processRequest(request, response);
     } catch (SQLException ex) {
         Logger.getLogger(UpdateProfilo.class.getName()).log(Level.SEVERE, null, ex);
     } catch (Exception ex) {
         Logger.getLogger(UpdateProfilo.class.getName()).log(Level.SEVERE, null, ex);
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
