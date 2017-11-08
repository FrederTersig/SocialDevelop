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
                }else{
                    id=0;
                }
                System.out.println("ID ?? > " + id );
                data.put("id", id);    
            }else{
                id = 0;
                data.put("id", id);
            }
            //Inizio Nuovo pannello
            ArrayList<Richieste> disponibili=null; // viene creata una richiesta dal coordinatore -> 0
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

                //INIZIO disponibili
                ResultSet disp= Databasee.getListaSvil(idCoordinatore,id);
                disponibili = new ArrayList<Richieste>();
                while(disp.next()){
                    int idTaskProgetto=disp.getInt("taskprogetto.id");
                    int numCollaboratori=disp.getInt("taskprogetto.numcollaboratori");
                    //FACCIO IL CHECK DEL NUMCOLLABORATORI: SE E' PIENO BISOGNA DIRLO!
                    Integer n=null;
                    ResultSet part=Databasee.contCollaboratori(idTaskProgetto);
                    while(part.next()){
                        int collat=part.getInt("num");
                        n=new Integer(collat);
                    }
                    
                    if(n < numCollaboratori){

                        int inviataOff=0;
                        int idSviluppatore=disp.getInt("sviluppatore.id");
                        String nome=disp.getString("sviluppatore.nome");
                        System.out.println("SVILUPPATORE/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
                        System.out.println(idSviluppatore);
                        String cognome=disp.getString("sviluppatore.cognome");
                        String titolo=disp.getString("progetto.titolo");
                        String taskNome=disp.getString("task.nome");
                        //check per vedere se è già stato inviato questa richiesta
                        ResultSet check = Databasee.isInviteDone(idSviluppatore, idCoordinatore, idTaskProgetto);
                        if(check.absolute(2)){
                                inviataOff=1;
                        }              

                        Richieste r = new Richieste(idSviluppatore,nome,cognome,titolo,idTaskProgetto, taskNome,inviataOff);
                        disponibili.add(r);
                    }     
                }
                
                //INIZIO INVITI
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
                            String reqDataCreazione="abc"; //SBAGLIATO VISTO CHE IL TIPO E' DATE!!!!                  
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

                            Richieste r = new Richieste(nomeS,cognS,reqIdSvil,reqIdCoord, reqTitolo, reqTask, reqDataCreazione, reqStato, reqTipo, reqIdTaskPro, taskEccesso);
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
                            String reqDataCreazione="abc"; //SBAGLIATO VISTO CHE IL TIPO E' DATE!!!!                  
                            String reqStato=req2.getString("stato");
                            
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

                            Richieste r = new Richieste(nomeS,cognS,reqIdSvil,reqIdCoord, reqTitolo, reqTask, reqDataCreazione, reqStato, reqTipo, reqIdTaskPro, taskEccesso);
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
            data.put("disponibili",disponibili);
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
                //ACCETTI RICHIESTA, INSERT AL COLLABORATORE E CANCELLI RICHIESTA!!!!
                Map<String, Object> map = new HashMap<String, Object>();
                int idSvil = Integer.parseInt(request.getParameter("sviluppatore"));
                int idTP = Integer.parseInt(request.getParameter("taskprog"));
                int idCoord = Integer.parseInt(request.getParameter("coordinatore"));
                
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
                
                try{ 
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
            }else if("insertOfferta".equals(action)){
                System.out.println("invia offerta!");
                Map<String, Object> map = new HashMap<String, Object>();
                
                int idCoordinatore=0;
                try{ //INSERT PROVA
                    Databasee.connect();
                    System.out.println("prendo coordinatore id");
                    ResultSet cord = Databasee.getCoordId(id);
                    System.out.println("idCoordinatore preso?");
                    while(cord.next()){
                        System.out.println("Quanti ID Coordinatore?");
                        int i = cord.getInt(1);
                        if(i > -1){
                            idCoordinatore = i;
                        }
                    }
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
                    
                    int idSvil = Integer.parseInt(request.getParameter("sviluppatore"));
                    int idTP = Integer.parseInt(request.getParameter("taskprog"));
                    
                    map.put("idsviluppatore",idSvil);
                    map.put("idcoordinatore",idCoordinatore);
                    map.put("idtaskprogetto",idTP);
                    map.put("stato","Attesa");
                    map.put("tipo",0);
                    map.put("datacreazione",today);
                    System.out.println("--------------------------------------------------------------------");
                    System.out.println("eccolo >" + map);
                    Databasee.insertRecord("richieste", map);
 
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
