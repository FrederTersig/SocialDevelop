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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class PanRichieste extends HttpServlet {
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
            ArrayList<Integer> listaCoordProgId = null; //Lista che mi dà tutti i progetti di cui lo sviluppatore è coordinatore.
            boolean isCoordinator=false;
           
            try{
                Databasee.connect();
                
                ResultSet list = Databasee.checkCoordinatore(id);// GIUSTA!!!
                listaCoordProgId = new ArrayList<Integer>();
                while(list.next()){
                    int idProg = list.getInt(1);
                    if(idProg > -1){
                        listaCoordProgId.add(idProg);
                        if(!isCoordinator){
                            isCoordinator=true;
                        }
                    }   
                }
                
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
            }
            data.put("listaProgCoord",listaCoordProgId);
            data.put("isCoordinator", isCoordinator);
            System.out.println("Coordinatore? " + isCoordinator + " | ListaProgetti? "+ listaCoordProgId );
            
            ArrayList<Richieste> offerte=null;
            try{
                Databasee.connect();               
                ResultSet req = Databasee.getListaJob(id); // GIUSTA!!!
                offerte = new ArrayList<Richieste>();
                while(req.next()){
                    int idTaskProgetto=req.getInt("taskprogetto.id");
                    int numCollaboratori=req.getInt("taskprogetto.numcollaboratori");
                    //FACCIO IL CHECK DEL NUMCOLLABORATORI: SE E' PIENO BISOGNA DIRLO!
                    Integer n=null;
                    ResultSet part=Databasee.contCollaboratori(idTaskProgetto);
                    while(part.next()){
                        int collat=part.getInt("num");
                        n=new Integer(collat);
                    }
                    int taskEccesso=0;
                    if(n >= numCollaboratori){
                        taskEccesso=1;
                    }

                    int idCoordinatore=req.getInt("progetto.idcoordinatore");
                    
                    String reqTitolo=req.getString("titolo");
                    String reqTask=req.getString("task.nome");
                    int inviataOff=0;
                    String reqStato="Disponibile";
                    //check per vedere se è già stato inviato questa richiesta
                    ResultSet check = Databasee.isInviteDone(id, idCoordinatore, idTaskProgetto);
                    reqStato="Attesa";
                    if(check.absolute(2)){
                            inviataOff=1;
                            reqStato="Attesa";
                    }              
                    boolean reqTipo=true; //equivale a 1
                    Richieste r = new Richieste(id,idCoordinatore,reqTitolo, reqTask,reqStato,reqTipo,idTaskProgetto,inviataOff,taskEccesso);
                    offerte.add(r);
                    System.out.println("???" + r + inviataOff);
                }
                Databasee.close();
            }catch(NamingException e) {
                System.out.println(e);
            }catch (SQLException e) {
                System.out.println(e);
            }catch (Exception ex) {
                    Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println(offerte);

            data.put("offerte", offerte);
            //Fine Nuovo Pannello
            
            ArrayList<Richieste> proposte=null;
            try{
                Databasee.connect();
       
                ResultSet req = Databasee.getRichieste(id, true); // ID sviluppatore + TRUE SE  E' SVILUPPATORE! iN QUESTO CASO SI!!! GIUSTISSIMA!!!!
                proposte = new ArrayList<Richieste>();
                while(req.next()){ // proposte sono tipo 0!
                    boolean reqTipo=req.getBoolean("tipo");   
                    if(!reqTipo){ // mi deve mostrare solo proposte mandate dai coordinatori allo sviluppatore
                        int reqIdCoord=req.getInt("idcoordinatore");      
                        int reqIdSvil=req.getInt("idsviluppatore");                
                        int reqIdTaskPro = req.getInt("idtaskprogetto");
                        String reqTitolo=req.getString("progetto.titolo");                
                        String reqTask=req.getString("task.nome");                 
                        String reqStato=req.getString("stato");         
                        
                        int numCollaboratori=req.getInt("taskprogetto.numcollaboratori");
                        //FACCIO IL CHECK DEL NUMCOLLABORATORI: SE E' PIENO BISOGNA DIRLO!
                        //INIZIO
                        Integer n=null;
                        ResultSet part=Databasee.contCollaboratori(reqIdTaskPro);
                        while(part.next()){
                            int collat=part.getInt("num");
                            n=new Integer(collat);
                        }
                        int taskEccesso=0;
                        if(n >= numCollaboratori){
                            taskEccesso=1;
                        }
                        //FINE
                        
                        Richieste r = new Richieste(reqIdSvil,reqIdCoord, reqTitolo, reqTask, reqStato, reqTipo, reqIdTaskPro,taskEccesso);
                        proposte.add(r);
                    }
                }
                Databasee.close();
            }catch(NamingException e) {
            }catch (SQLException e) {
            }catch (Exception ex) {
                    Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
            }
            data.put("proposte",proposte);
            System.out.println("data>>>>>>>>>>>>" + data);
            
            FreeMarker.process("panRichieste.html", data, response, getServletContext());
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
            }else if("insertOfferta".equals(action)){
                System.out.println("invia offerta!");
                Map<String, Object> map = new HashMap<String, Object>();
                ArrayList<Richieste> offerte=null;
                try{ //INSERT PROVA
                    Databasee.connect();
                    System.out.println("Cominciamo");
                    //Altra prova
                    Calendar c = Calendar.getInstance();

                    System.out.println(c.getTime());	/* Rappresentazione come stringa in base al tuo Locale */
                    System.out.println(c.get(Calendar.YEAR)); /* Ottieni l'anno */
                    System.out.println(c.get(Calendar.MONTH)); /* Ottieni il mese */
                    System.out.println(c.get(Calendar.DAY_OF_MONTH)); /* Ottieni il giorno */
                    int year=c.get(Calendar.YEAR);
                    int month= c.get(Calendar.MONTH)+1;
                    int day=c.get(Calendar.DAY_OF_MONTH);
                    String today=year + "/" + month + "/" + day;
                    
                    int idCoord = Integer.parseInt(request.getParameter("coordinatore"));
                    int idTP = Integer.parseInt(request.getParameter("taskprog"));
                    
                    map.put("idsviluppatore", id);
                    map.put("idcoordinatore",idCoord);
                    map.put("idtaskprogetto",idTP);
                    map.put("stato","Attesa");
                    map.put("tipo",1);
                    map.put("datacreazione",today);
                    Databasee.insertRecord("richieste", map);
                        
                    
                    
                    Databasee.close();
                }catch(NamingException e) {
                    System.out.println(e );
                }catch (SQLException e) {
                    System.out.println(e );
                }catch (Exception ex) {
                        Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                response.sendRedirect("panRichieste");
            }else if("deleteOfferta".equals(action)){
                System.out.println("Cancelliamo");
                int idCoord = Integer.parseInt(request.getParameter("coordinatore"));
                int idTP = Integer.parseInt(request.getParameter("taskprog"));
                try{ //INSERT PROVA
                    Databasee.connect();
                    Databasee.deleteRichiesta(id, idCoord, idTP);
               
                    Databasee.close();
                }catch(NamingException e) {
                    System.out.println(e );
                }catch (SQLException e) {
                    System.out.println(e );
                }catch (Exception ex) {
                        Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                response.sendRedirect("panRichieste");
            }else if("Accetta".equals(action)){
                System.out.println("ACCETTA");
                Map<String, Object> map = new HashMap<String, Object>();
                int idCoord = Integer.parseInt(request.getParameter("coordinatore"));
                int idTP = Integer.parseInt(request.getParameter("taskprog"));
                map.put("idsviluppatore", id);
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
                try{ 
                    Databasee.connect();
                    Databasee.deleteRichiesta(id, idCoord, idTP);
               
                    Databasee.close();
                }catch(NamingException e) {
                    System.out.println(e );
                }catch (SQLException e) {
                    System.out.println(e );
                }catch (Exception ex) {
                        Logger.getLogger(Richieste.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect("panRichieste");
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
