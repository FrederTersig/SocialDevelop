package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import System.Progetto;
import System.Richieste;
import System.Sviluppatore;
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
/* libreria servlet*/
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Federico Tersigni
 */
//@WebServlet(urlPatterns = {"/Message"})
public class PanRichCoord extends HttpServlet {
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
            throws ServletException, IOException, SQLException, Exception {
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
            //Inizio Nuovo pannello
            ArrayList<Richieste> inviti=null; // tipo richiesta = 0 > inviata dal coordinatore
            ArrayList<Richieste> domande=null; // tipo richiesta = 1 > inviata dallo sviluppatore
            int idCoordinatore = 0;
            try{
                Databasee.connect();
                ResultSet cord = Databasee.getCoordId(id);
                System.out.println("idCoordinatore preso?");
                while(cord.next()){
                    System.out.println("Quanti ID Coordinatore?");
                    int i = cord.getInt(1);
                    if(i > -1){
                        idCoordinatore = i;
                    }
                }
                System.out.println("id coordinatore FINALE >>>>>>>>>" + idCoordinatore);
                //if(idCoordinatore > -1){
                
                    ResultSet req = Databasee.getRichieste(idCoordinatore, false); // ID sviluppatore + TRUE SE  E' SVILUPPATORE! iN QUESTO CASO SI!!! GIUSTISSIMA!!!!
                    inviti = new ArrayList<Richieste>();
                    while(req.next()){
                        boolean reqTipo=req.getBoolean("tipo"); 
                        if(!reqTipo){//inviate da coordinatori
                            int reqIdCoord=req.getInt("idcoordinatore");      
                            int reqIdSvil=req.getInt("idsviluppatore"); 
                            // MI SERVE NOME/COGNOME SVILUPPATORE!
                            
                            ResultSet aSvil = Databasee.getSviluppGener(reqIdSvil);
                            String nomeS="";
                            String cognS="";
                            while(aSvil.next()){
                                nomeS = aSvil.getString(1);
                                cognS = aSvil.getString(2);
                            }
                            int reqIdTaskPro = req.getInt("idtaskprogetto");
                            String reqTitolo=req.getString("progetto.titolo");                
                            String reqTask=req.getString("task.nome");
                            String reqSkill=req.getString("skill.nome");
                            String reqDataCreazione="abc"; //SBAGLIATO VISTO CHE IL TIPO E' DATE!!!!                  
                            String reqStato=req.getString("stato");

                            Richieste r = new Richieste(nomeS,cognS,reqIdSvil,reqIdCoord, reqTitolo, reqTask, reqSkill, reqDataCreazione, reqStato, reqTipo, reqIdTaskPro);
                            inviti.add(r);
                        }
                    }
                    
                    ResultSet req2 = Databasee.getRichieste(idCoordinatore,false);
                    domande = new ArrayList<Richieste>();
                    while(req2.next()){
                        boolean reqTipo=req2.getBoolean("tipo"); 
                        if(reqTipo){ //inviate da utenti/sviluppatori
                            int reqIdCoord=req2.getInt("idcoordinatore");      
                            int reqIdSvil=req2.getInt("idsviluppatore");
                            
                            ResultSet aSvil = Databasee.getSviluppGener(reqIdSvil);
                            String nomeS="";
                            String cognS="";
                            while(aSvil.next()){
                                nomeS = aSvil.getString(1);
                                cognS = aSvil.getString(2);
                            }
                            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" + nomeS + cognS);
                            int reqIdTaskPro = req2.getInt("idtaskprogetto");
                            String reqTitolo=req2.getString("progetto.titolo");                
                            String reqTask=req2.getString("task.nome");
                            String reqSkill=req2.getString("skill.nome");
                            String reqDataCreazione="abc"; //SBAGLIATO VISTO CHE IL TIPO E' DATE!!!!                  
                            String reqStato=req2.getString("stato");

                            Richieste r = new Richieste(nomeS,cognS,reqIdSvil,reqIdCoord, reqTitolo, reqTask, reqSkill, reqDataCreazione, reqStato, reqTipo, reqIdTaskPro);
                            domande.add(r);
                        }
                    }
                    
                //}
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
            }
            data.put("inviti",inviti);
            data.put("domande",domande);
            data.put("idCoordina", idCoordinatore);
            System.out.println("data>>>>>>>>>>>>" + data);
            
            
            FreeMarker.process("panRichCoord.html", data, response, getServletContext());
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
            Logger.getLogger(PanRichieste.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PanRichieste.class.getName()).log(Level.SEVERE, null, ex);
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
            String action = request.getParameter("value");
            if("logout".equals(action)){ // Inizio del logout
                System.out.println("CLICCATO LOGOUT!");
                try{
                    SecurityLayer.disposeSession(request); //chiude la sessione
                    id=0; //azzera l'id per il template
                    data.put("id",id);
                    //processRequest(request, response);
                    //FreeMarker.process("panRichieste.html", data, response, getServletContext());
                    response.sendRedirect("index");
                }catch(Exception e3){
                    e3.printStackTrace();
                }
            }else if("search".equals(action)){
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
            }else if("deleteOfferta".equals(action)){
                System.out.println("Cancelliamo--------------------------------------------------------------------------------------");
                int idSvil = Integer.parseInt(request.getParameter("sviluppatore"));
                int idCoord = Integer.parseInt(request.getParameter("coordinatore"));
                int idTP = Integer.parseInt(request.getParameter("taskprog"));
                try{ //INSERT PROVA
                    Databasee.connect();
                    Databasee.deleteRichiesta(idSvil, idCoord, idTP);
               
                    Databasee.close();
                }catch(NamingException e) {
                    System.out.println(e );
                }catch (SQLException e) {
                    System.out.println(e );
                }catch (Exception ex) {
                        Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                response.sendRedirect("panRichCoord");
            }else if("Accetta".equals(action)){
                System.out.println("ACCETTA");
                Map<String, Object> map = new HashMap<String, Object>();
                int idSvil = Integer.parseInt(request.getParameter("sviluppatore"));
                int idTP = Integer.parseInt(request.getParameter("taskprog"));
                
                
                map.put("idsviluppatore", idSvil);
                map.put("idtaskprogetto",idTP);
                    
                try{
                    Databasee.connect();
                    
                    Databasee.insertRecord("collaboratore", map);
                    Databasee.close();
                }catch(NamingException e) {
                    System.out.println(e );
                }catch (SQLException e) {
                    System.out.println(e );
                }catch (Exception ex) {
                        Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect("panRichCoord");
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
