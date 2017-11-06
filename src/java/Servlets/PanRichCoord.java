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
            ArrayList<Richieste> inviti=null;
            ArrayList<Richieste> domande=null;
            int idCoordinatore = 0;
            try{
                Databasee.connect();
                ResultSet cord = Databasee.getCoordId(id);
                
                while(cord.next()){
                    idCoordinatore = cord.getInt("id");
                }
                
                //if(idCoordinatore > -1){
                
                    ResultSet req = Databasee.getRichieste(idCoordinatore, false); // ID sviluppatore + TRUE SE  E' SVILUPPATORE! iN QUESTO CASO SI!!! GIUSTISSIMA!!!!
                    inviti = new ArrayList<Richieste>();
                    while(req.next()){
                        int reqIdCoord=req.getInt("idcoordinatore");      
                        int reqIdSvil=req.getInt("idsviluppatore");                
                        int reqIdTaskPro = req.getInt("idtaskprogetto");
                        String reqTitolo=req.getString("progetto.titolo");                
                        String reqTask=req.getString("task.nome");
                        String reqSkill=req.getString("skill.nome");
                        String reqDataCreazione="abc"; //SBAGLIATO VISTO CHE IL TIPO E' DATE!!!!                  
                        String reqStato=req.getString("stato");
                        boolean reqTipo=req.getBoolean("tipo");             
                        Richieste r = new Richieste(reqIdSvil,reqIdCoord, reqTitolo, reqTask, reqSkill, reqDataCreazione, reqStato, reqTipo, reqIdTaskPro);
                        inviti.add(r);
                    }
                    
                    domande = new ArrayList<Richieste>();
                    
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
