package Util;

//import System.Libro;
//import System.Utente;
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

/**
 *
 * @author Stefano Cortellessa
 */
public class DataUtil {

    /**
     * Converto una data da String a Date
     *
     * @param date data da convertire
     * @param format formato di date
     * @return data nell formato yyyy-MM-dd
     * @throws java.text.ParseException
     */
    public static Date stringToDate(String date, String format) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(format);
        java.util.Date myDate;

        myDate = formatter.parse(date);
        Date sqlDate = new Date(myDate.getTime());
        return sqlDate;

    }

    /**
     * Converte una data da Date a String
     *
     * @param date data da convertire
     * @return stringa nel formato yyyy-MM-dd
     */
    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

   /*
    * data l'email restituisce l'id del gruppo a cui appartiene
    * 1 - Attivo
    * 2 - Passivo
    */
    public static int getGroup(String email) throws Exception {
        int id = 0;
        Database.connect();
        String condition = "email='" + email + "'";
        ResultSet rs = Database.selectRecord("utenti", condition);
        while (rs.next()) {
            id = rs.getInt("gruppo");
        }
        System.out.println("GROUP :" + id);
        return id;
    }

    public static ArrayList<Libro> search(String input) throws Exception {

        ArrayList<Libro> result = new ArrayList<Libro>();

        Database.connect();
        //System.out.println(input);

        try {
            String condition = " isbn LIKE '%" + input + "%' OR titolo LIKE '%" + input + "%' OR autore LIKE '%" + input
                    + "%' OR editore LIKE '%" + input + "%' OR lingua LIKE '%" + input + "%'OR inserita LIKE '%"
                    + input + "%'";
            //System.out.println(condition);

            ResultSet record = Database.selectJoinW("pubblicazioni AS p", "ristampe AS r", "p.id = r.id_pubblicazioni", condition);
            while (record.next()) {

                String titolo = record.getString("titolo");
                String autore = record.getString("autore");
                String editore = record.getString("editore");
                String path = record.getString("path");
                int id = record.getInt("identificatore");
                String descrizione = record.getString("descrizione");
                int edizione = record.getInt("edizione");
                //System.out.println("ID: " + id);
                System.out.println("TITOLO: " + titolo);
                System.out.println("DESCRIZIONE: " + descrizione);
                System.out.println("EDITORE: " + editore);
                System.out.println("EDIZIONE: " + edizione);
                System.out.println("PAAAATH: " + path);
                //System.out.println(tempLib);

                Libro k = new Libro(titolo, descrizione, id, editore, edizione, path);
                result.add(k);
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return result;
    }

    public static ArrayList<Libro> searchPub(String input) throws Exception {

        ArrayList<Libro> result = new ArrayList<Libro>();

        Database.connect();
        //System.out.println(input);

        try {
            String condition = " titolo LIKE '%" + input + "%' OR autore LIKE '%" + input + "%'";
            //System.out.println(condition);
            String titlee = null;
            ResultSet record = Database.selectRecord("pubblicazioni ", condition);
            while (record.next()) {

                String titolo = record.getString("titolo");
                String autore = record.getString("autore");
                int id = record.getInt("id");
                String descrizione = record.getString("descrizione");
                titlee = titolo;
                System.out.println("TITOLO: " + titolo);
                System.out.println("DESCRIZIONE: " + descrizione);

                Libro k = new Libro(titolo, autore, id);
                result.add(k);
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return result;
    }

    public static ArrayList<Utente> searchBO(String input) throws Exception {

        ArrayList<Utente> result = new ArrayList<Utente>();
        //Libro libro = new Libro();
        Database.connect();

        try {
            String condition = " nome LIKE '%" + input + "%' OR cognome LIKE '%" + input + "%'";

            ResultSet record = Database.selectRecord("utenti", condition);
            while (record.next()) {

                String email = record.getString("email");
                String nome = record.getString("nome");
                String cognome = record.getString("cognome");

                Utente tempLib = new Utente(nome, cognome, email);
                System.out.println(tempLib);
                result.add(tempLib);
            }

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return result;

    }

    public static String getEmail(String email) throws Exception {

        String nome = null;

        try {

            Database.connect();

            String condition = "email = '" + email + "'";

            ResultSet r = Database.selectRecord("utenti", condition);

            while (r.next()) {
                nome = r.getString("nome");

            }

            System.out.println(nome);
        } catch (NamingException e) {
        } catch (SQLException e) {
        }
        return nome;
    }

    public static int checkUser(String email, String pass) throws Exception {
        int w = 0;
        try {

            Database.connect();
            if (!isNull(pass)) {
                pass = crypt(pass);
            }

            System.out.println(email);
            System.out.println(w);
            System.out.println(pass);

            String condition = "email = '" + email + "' AND password = '" + pass + "'";

            System.out.println(condition);
            ResultSet r = Database.selectRecord("utenti", condition);
            while (r.next()) {
                w = r.getInt("id");
            }

        } catch (NamingException e) {
        } catch (SQLException e) {
        }
        return w;
    }

    public static Utente getUser(String email) throws Exception {
        Utente utente = new Utente();

        try {

            Database.connect();
            ResultSet r = Database.selectRecord("utenti", "email =" + email);

            while (r.next()) {

                String uemail = r.getString("email");
                String nome = r.getString("nome");
                String cognome = r.getString("cognome");
                int gruppi = r.getInt("gruppo");

                utente = new Utente(email, nome, cognome, gruppi);
            }

            /*     Iterator<Pub> it = lista.iterator();
                while( it.hasNext()){
                    Pub temp= it.next();
                    pubblicazioni.put(temp.getNome(), temp.getDescrizione());
                } */
            try {
                Database.close();
            } catch (SQLException ex) {
            }

        } catch (NamingException ex) {
            System.out.println("ex1");
        } catch (SQLException ex) {
            System.out.println("ex2");
        }

        return utente;
    }

    /**
     * Controllo su String. Contiene solo caratteri alfanumerici?
     *
     * @param toCheck stringa sul quale effettuare il controllo
     * @param space se true accetta anche gli spazi.
     * @return true se la stringa Ã¨ alfanumerica, false altrimenti.
     */
    public static boolean isAlphanumeric(String toCheck, boolean space) {
        if (toCheck.equals("")) {
            return true;
        }

        if (space) {
            return toCheck.matches("[a-zA-Z' ]+");
        } else {
            return toCheck.matches("[a-zA-Z']+");
        }

    }

    /**
     * Eliminazione degli spazi esterni e dei doppi spazi interni
     *
     * @param toTrim stringa da elaborare
     * @return stringa "pulita"
     */
    public static String spaceTrim(String toTrim) {
        return toTrim.trim().replaceAll("\\s+", " ");
    }

    /**
     * Cripta una stringa
     *
     * @param string stringa da criptare
     * @return stringa criptata
     */
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

    /**
     * Verifica se una stringa criptata Ã¨ stata generata da un'altra stringa
     *
     * @param string_crypted stringa criptata
     * @param to_check stringa da verificare
     * @return true se la password Ã¨ stata verificata, false altrimenti
     */
    public static boolean decrypt(String string_crypted, String to_check) {
        if (to_check == null || string_crypted == null) {
            return false;
        }
        return string_crypted.equals(crypt(to_check));
    }
}
