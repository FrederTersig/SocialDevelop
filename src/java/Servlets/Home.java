/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

/*import System.Libro;
import System.Utente;*/
import Util.DataUtil;
import Util.Database;
import Util.FreeMarker;
import Util.SecurityLayer;
import java.io.IOException;
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
 * @author MARCO
 */
public class Home extends HttpServlet {
    
        Map<String, Object> data = new HashMap<String, Object>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception 
    {
        HttpSession s = SecurityLayer.checkSession(request);

        if (s != null) {
            
            int group = DataUtil.getGroup((String) s.getAttribute("username"));
            data.put("group", group);

            String nome = DataUtil.getEmail((String) s.getAttribute("username"));
            data.put("nome", nome);
            String user = ((String) s.getAttribute("username"));
            data.put("user", user);
        
        //Le più consigliate
        List<Libro> cons = null;
        
        try{
            Database.connect();
            ResultSet co = Database.selectJoinL("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni",
                        "`r`.`Consigliato` DESC", 0, 5);
            
            cons = new ArrayList<Libro>();
            
            while (co.next()) {
                
                int id = co.getInt("identificatore");
                String titolo = co.getString("titolo");
                String editore = co.getString("editore");
                String descrizione = co.getString("descrizione");
                int edizione = co.getInt("edizione");
                String path = co.getString("path");
                
                Libro tcc = new Libro(titolo, descrizione, id, editore, edizione, path);
                cons.add(tcc);            
            }
            Database.close();
        }
            catch(NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        data.put("consigliati", cons);
        
        
        //Utenti più attivi
        List<Utente> utent = null;
            try {
                Database.connect();
                ResultSet u = Database.selectRecordOr("utenti", "`utenti`.`modifiche` DESC LIMIT 0,5");
                System.out.println("U: " + u);
             
                utent = new ArrayList<Utente>();
                while (u.next()) {
                    int id = u.getInt("id");
                    String nomeu = u.getString("nome");
                    String cognomeu = u.getString("cognome");
                    int gruppo = u.getInt("gruppo");
                    int modifiche = u.getInt("modifiche");
                    String email = u.getString("email");

                   
                    Utente ute = new Utente(email, nomeu, cognomeu, gruppo, modifiche);
                    System.out.println("Uten: " + ute);

                    utent.add(ute);

                }
                Database.close();

            } catch (NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Ut: " + utent);
            
            data.put("utenti", utent);
            
            
            
           List<Libro> lib = null;

            try {
                Database.connect();
                ResultSet rs = Database.selectJoinL("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni",
                        "`r`.`identificatore` ASC", 0, 5);

                lib = new ArrayList<Libro>();

                while (rs.next()) {
                    //System.out.println("DENTRO ");
                    int id = rs.getInt("identificatore");
                    String titolo = rs.getString("titolo");
                    String editore = rs.getString("editore");
                    String descrizione = rs.getString("descrizione");
                    int edizione = rs.getInt("edizione");
                    //System.out.println("ID: " + id);
                    //System.out.println("TITOLO: " + titolo);
                    //System.out.println("DESCRIZIONE: " + descrizione);
                    String path = rs.getString("path");
                    Libro tt = new Libro(titolo, descrizione, id, editore, edizione, path);
                    lib.add(tt);
                }
                Database.close();

            } catch (NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }

            data.put("libri", lib);
            
            
            
            List<Libro> libro = null;

            try {
                Database.connect();
                ResultSet rst = Database.selectJoinL("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni",
                        "`r`.`Data` DESC", 0, 5);

                libro = new ArrayList<Libro>();

                while (rst.next()) {
                    int id = rst.getInt("identificatore");
                    String titolo = rst.getString("titolo");
                    String editore = rst.getString("editore");
                    String descrizione = rst.getString("descrizione");
                    int edizione = rst.getInt("edizione");
                    //System.out.println("ID: " + id);
                    //System.out.println("TITOLO: " + titolo);
                    //System.out.println("DESCRIZIONE: " + descrizione);
                    String path = rst.getString("path");
                    Libro tt = new Libro(titolo, descrizione, id, editore, edizione, path);
                    libro.add(tt);
                }
                Database.close();

            } catch (NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }

            data.put("libridata", libro);
        
        FreeMarker.process("index.html", data, response, getServletContext());
        }
        else{
            //Le più consigliate
            List<Libro> cons = null;
        
            try{
            Database.connect();
            ResultSet co = Database.selectJoinL("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni",
                        "`r`.`Consigliato` DESC", 0, 5);
            
            cons = new ArrayList<Libro>();
            
            while (co.next()) {
                
                int id = co.getInt("identificatore");
                String titolo = co.getString("titolo");
                String editore = co.getString("editore");
                String descrizione = co.getString("descrizione");
                int edizione = co.getInt("edizione");
                String path = co.getString("path");
                
                Libro tcc = new Libro(titolo, descrizione, id, editore, edizione, path);
                cons.add(tcc);            
            }
            Database.close();
        }
            catch(NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        data.put("consigliati", cons);
        
        
        //Utenti più attivi
        List<Utente> utent = null;
            try {
                Database.connect();
                ResultSet u = Database.selectRecordOr("utenti", "`utenti`.`modifiche` DESC LIMIT 0,5");
                System.out.println("U: " + u);
             
                utent = new ArrayList<Utente>();
                while (u.next()) {
                    int id = u.getInt("id");
                    String nomeu = u.getString("nome");
                    String cognomeu = u.getString("cognome");
                    int gruppo = u.getInt("gruppo");
                    int modifiche = u.getInt("modifiche");
                    String email = u.getString("email");

                   
                    Utente ute = new Utente(email, nomeu, cognomeu, gruppo, modifiche);
                    System.out.println("Uten: " + ute);

                    utent.add(ute);

                }
                Database.close();

            } catch (NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Ut: " + utent);
            
            data.put("utenti", utent);
            
            
            
           List<Libro> lib = null;

            try {
                Database.connect();
                ResultSet rs = Database.selectJoinL("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni",
                        "`r`.`identificatore` ASC", 0, 5);

                lib = new ArrayList<Libro>();

                while (rs.next()) {
                    //System.out.println("DENTRO ");
                    int id = rs.getInt("identificatore");
                    String titolo = rs.getString("titolo");
                    String editore = rs.getString("editore");
                    String descrizione = rs.getString("descrizione");
                    int edizione = rs.getInt("edizione");
                    //System.out.println("ID: " + id);
                    //System.out.println("TITOLO: " + titolo);
                    //System.out.println("DESCRIZIONE: " + descrizione);
                    String path = rs.getString("path");
                    Libro tt = new Libro(titolo, descrizione, id, editore, edizione, path);
                    lib.add(tt);
                }
                Database.close();

            } catch (NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }

            data.put("libri", lib);
            
            
            
            List<Libro> libro = null;

            try {
                Database.connect();
                ResultSet rst = Database.selectJoinL("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni",
                        "`r`.`Data` DESC", 0, 5);

                libro = new ArrayList<Libro>();

                while (rst.next()) {
                    int id = rst.getInt("identificatore");
                    String titolo = rst.getString("titolo");
                    String editore = rst.getString("editore");
                    String descrizione = rst.getString("descrizione");
                    int edizione = rst.getInt("edizione");
                    //System.out.println("ID: " + id);
                    //System.out.println("TITOLO: " + titolo);
                    //System.out.println("DESCRIZIONE: " + descrizione);
                    String path = rst.getString("path");
                    Libro tt = new Libro(titolo, descrizione, id, editore, edizione, path);
                    libro.add(tt);
                }
                Database.close();

            } catch (NamingException e) {
            } catch (SQLException e) {
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }

            data.put("libridata", libro);            
            
            
            
            FreeMarker.process("index.html", data, response, getServletContext());
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
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            processRequest(request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
