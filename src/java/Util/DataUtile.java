/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import System.Progetto;
import static Util.DataUtile.crypt;
import Util.Databasee;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Objects.isNull;
import javax.naming.NamingException;
import java.io.*;
/**
 *
 * @author user1
 */
public class DataUtile {
    
    public static void listaProgetti() throws Exception {
        ArrayList<Progetto> lista = new ArrayList<Progetto>();
        
        Databasee.connect();
        
        try{
            ResultSet record = Databasee.selectProgetto("progetto");
            
            while(record.next()){
                String titolo= record.getString("titolo");
                String descrizione= record.getString("descrizione");
             
                System.out.println("TITOLO: " + titolo);
                System.out.println("DESCRIZIONE: " + descrizione);
            
            }
            Databasee.close();
        }  catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
       
    }
    
    public static int checkUser(String email, String pass) throws Exception {
        int w = 0;
        try {

            Databasee.connect();
            if (!isNull(pass)) {
                pass = crypt(pass);
            }

            System.out.println(email);
            System.out.println(w);
            System.out.println(pass);

            String condition = "email = '" + email + "' AND password = '" + pass + "'"; // da cambiare!!

            System.out.println(condition);
            ResultSet r = Databasee.selectRecord("sviluppatore", condition);
            System.out.println("*************Risultato della chiamata al DATABASE******");
            System.out.println(r);
            
            while (r.next()) {
                w = r.getInt("id");
            }
            
            //Databasee.close(); //??
        } catch (NamingException e) {
        } catch (SQLException e) {
        }
        System.out.println("FINE CHECKUSER, risultato >  " + w );
        return w;
    }
    
    
       public static int checkOfficer(String email, String pass) throws Exception {
        int w = 0;
        try {

            Databasee.connect();
            if (!isNull(pass)) {
                
            }

            System.out.println(email);
            System.out.println(w);
            System.out.println(pass);

            String condition = "user = '" + email + "' AND pass = '" + pass + "'"; // da cambiare!!

            System.out.println(condition);
            ResultSet r = Databasee.selectRecord("admin", condition);
            System.out.println("*************Risultato della chiamata al DATABASE******");
            System.out.println(r);
            
            while (r.next()) {
                w = r.getInt("id");
            }
            
            //Databasee.close(); //??
        } catch (NamingException e) {
        } catch (SQLException e) {
        }
        System.out.println("FINE CHECKUSER, risultato >  " + w );
        return w;
    }
    
    public static String crypt(String string) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = string.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

    }
    
        public static String getEmail(String email) throws Exception {

        String nome = null;

        try {

            Databasee.connect();

            String condition = "email = '" + email + "'";

            ResultSet r = Databasee.selectRecord("sviluppatore", condition);

            while (r.next()) {
                nome = r.getString("nome");

            }

            System.out.println(nome);
        } catch (NamingException e) {
        } catch (SQLException e) {
        }
        return nome;
    }

    
   /* public static void main(String[] args) throws Exception{
     listaProgetti();}*/
    
}
