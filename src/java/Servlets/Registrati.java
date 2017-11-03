/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import System.Admin;
import System.Skill;
import Util.DataUtile;
import System.Sviluppatore;
import static Util.DataUtile.crypt;
import Util.Database;
import Util.Databasee;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;*/

/**
 *
 * @author user1
 */

public class Registrati extends HttpServlet {
    Map<String, Object> data = new HashMap<String, Object>();
        /*private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIRECTORY = "upload";
    private static final int THRESHOLD_SIZE = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    int titurl = 0;

    public static String toString(int a, int b, int c) {
        return a + "-" + b + "-" + c;
    }*/
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
            // Controllo ID da sessione 
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
            //Fine controllo ID da sessione
            try{
                          Databasee.connect();
              
         //int idtask=(int) s.getAttribute("idtask");
         //System.out.println(idtask);
         //s.setAttribute("idtask",idtask);
         ResultSet ts= Databasee.selectRecord2("skill");
         
         ArrayList<Skill> Skill = new ArrayList<Skill>();
       
         while(ts.next()){
        
             String nomeskill = ts.getString("nome");
             int idskill = ts.getInt("id");
             Skill lista2 = new Skill(idskill,nomeskill);
            
             Skill.add(lista2);
         }
         
         data.put("nomeskill", Skill);
         Databasee.close();
                FreeMarker.process("registrati.html", data, response, getServletContext());
            }catch(Exception e){}
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
            System.out.println("STO FACENDO UNA RICHIESTA DI POST!");
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
                    FreeMarker.process("registrati.html", data, response, getServletContext());
                } else {
                    try{ 
                        HttpSession s = SecurityLayer.createSession(request, EmailL, id);
                        System.out.println("Sessione Creata, Connesso!");
                        data.put("nome",EmailL);
                        data.put("id",id);
                        //RequestDispatcher rd = request.getRequestDispatcher("index"); //<- dispatch di una richiesta ad un'altra servlet.
                        s.setAttribute("id", id);
                        //processRequest(request, response);
                        //FreeMarker.process("registrati.html", data, response, getServletContext());
                        response.sendRedirect("index");
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
                    //FreeMarker.process("registrati.html", data, response, getServletContext());
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
            }else if("registra".equals(action)){
                System.out.println("POST::: Sto Registrando un nuovo account!");
                
                String nome = request.getParameter("Nome");
                String cognome = request.getParameter("Cognome");
                String email = request.getParameter("Email");
                String pass = request.getParameter("Pass");
                String dataNascita = request.getParameter("DataNascita");
                //String username = request.getParameter("Username");
                String tel = request.getParameter("Telefono");
                String indirizzo = request.getParameter("Indirizzo");
                String[] skill = request.getParameterValues("skill");
                for(int i=0; i<skill.length; i++){
                System.out.println(skill[i]);}
                
                String[] punteggio =request.getParameterValues("punteggio");
                for(int i=0; i<punteggio.length; i++){
                    if(punteggio[i]!=""){
                System.out.println(punteggio[i]);}
                }
                // REGISTRAZIONE 
                try {
                    System.out.println("POST::: Mi connetto al database");
                    //Databasee.connect();
                    Map<String, Object> map = new HashMap<String, Object>();
                    System.out.println("COMINCIO A MAPPARE I VALORI---------------------------");
                    map.put("nome", nome);
                    map.put("cognome", cognome);
                    map.put("email", email);
                    map.put("password", crypt(pass));           
                    map.put("data", dataNascita);
                    map.put("telefono", tel);
                    map.put("indirizzo", indirizzo);
                    System.out.println("PRENDO TUTTI I VALORI???");
                    System.out.println(map);
                    System.out.println("FINE VALORI!!!");
                    //NO ID perché così l'utente potrà connettersi con le nuove credenziali (?)
                    int check = DataUtile.checkUser(email, pass);
                    System.out.println("CHECK: " + check);
                    if (check == 0) {//INSERISCO I DATI DI MAP NEL DATABASE
                        System.out.println("DENTRO CONDIZIONE CHECK=0");
                        Databasee.insertRecord("sviluppatore", map);
                        //INSERISCO NEL DB LE SKILL CHE LO SVILUPPATORE HA DETTO DI AVERE
                        String emailn="'" + email + "'";
                        ResultSet sv = Databasee.selectRecord("sviluppatore", "email= " + emailn);
                        map.clear();
                        //QUA E' UN MACELLO MA FUNZIONA. INSERISCE NELL'ENTITA' LIVELLO LE SKILL SCELTE DALL'UTENTE E IL SUO LIVELLO DI PREPARAZIONE
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
                        while(sv.next()){
                            int idsvi = sv.getInt("id");
                            map.put("idsviluppatore", idsvi);
                            for(int i=0; i<skill.length; i++){
                                map.put("idskill", skill[i]);
                                map.put("preparazione", prep[i]);
                                Databasee.insertRecord("livello", map);
                            }
                                           
                        }
                    }else{
                        response.sendRedirect("index");
                    }
                 
                    
                   //****************UPLOAD IMMAGINE*********************
                  /* System.out.println("+++++++inizio upload!!+++++");
                  
        if (!ServletFileUpload.isMultipartContent(request)) {

            PrintWriter writer = response.getWriter();
            writer.println("Request does not contain upload data");
            writer.flush();
            return;
        }

        // Configurazione impostazioni di upload
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);
        System.out.println("UPLOAD: " + upload);

        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        //Costruisce il path della cartella dove fare l'upload del file
        String uploadPath = "C:\\Users\\user1\\Desktop\\SocialDevelopGitHub\\web\\template" + UPLOAD_DIRECTORY;
        System.out.println("UPLOAD PATH: " + uploadPath);

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        System.out.println("UPLOAD DIR: " + uploadDir);

        if (!uploadDir.exists()) {
            System.out.println("DENTRO IF-EXIST");

            uploadDir.mkdir();
        }

        try {
            // parses the request's content to extract file data
            List formItems = upload.parseRequest(request);
            Iterator iter = formItems.iterator();
            System.out.println("FORM ITEMS: " + formItems);
            System.out.println("ITER: " + iter);

            // iterates over form's fields
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                System.out.println("ITEM: " + item);

                // processes only fields that are not form fields
                if (!item.isFormField()) {
                    String fileName2 = new File(item.getName()).getName();
                    System.out.println("FILENAME2: " + fileName2);

                    String fileName = item.getName();
                    System.out.println("FILENAME: " + fileName);

                    String filePath = uploadPath + File.separator + fileName;
                    System.out.println("FILEPATH: " + filePath);

                    File storeFile = new File(filePath);
                    System.out.println("STOREFILE" + storeFile);

                    map.put("immagine", "template/upload/" + fileName);
                    // saves the file on disk
                    item.write(storeFile);

                    System.out.println("TITOLO POST: " + titurl);

                    Database.connect();
                    Database.insertRecord("immagini", map);
                    Database.close();

                    map.clear();
                }
            }
            request.setAttribute("message", "Upload has been done successfully!");
        } catch (Exception ex) {
            request.setAttribute("message", "There was an error: " + ex.getMessage());
        }*/
                response.sendRedirect("index");

                } catch (NamingException ex) {
                    Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Sviluppatore.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                System.out.println("ERRORE!!! CHE TIPO DI POST HO FATTO?");
            }
}
}

