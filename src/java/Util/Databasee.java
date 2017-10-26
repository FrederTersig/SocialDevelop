/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import System.Sviluppatore;
import static Util.Database.DRIVER;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.lang.String;
/**
 *
 * @author user1
 */
public class Databasee {
    
    protected static String DRIVER = "com.mysql.jdbc.Driver";
    protected static String url = "jdbc:mysql://localhost/socialp";
    protected static String user = "root";
    protected static String psw = "";

    private static Connection db;
    
     public static void connect() throws Exception {
        try {
            Class.forName(DRIVER);
            db = DriverManager.getConnection(url, user, psw);
            System.out.println("CONNESSO (Databasee.connect())");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
      
     
     public static void close() throws SQLException {
        Databasee.db.close();
        System.out.println("DISCONNESSO (Databasee.close())");
    }
     
      static ResultSet executeQuery(String query) throws SQLException {

        Statement s1 = Databasee.db.createStatement();

        System.out.println(query);
        ResultSet records = s1.executeQuery(query);
        System.out.println("executeQuery");
        return records;
    }
      
       private static boolean updateQuery(String query) throws SQLException {

        Statement s1;
        s1 = Databasee.db.createStatement();
        s1.executeUpdate(query);
        System.out.println("executeUpdate");
        s1.close();

        return true;

    }
     
     public static ResultSet selectProgetto(String table) throws SQLException {
        String query = "SELECT titolo, descrizione, id FROM " + table;
        return Databasee.executeQuery(query);
       
     }
     
      public static ResultSet selectProgettoUltimi5() throws SQLException {
        String query = "SELECT titolo, descrizione, id FROM progetto ORDER BY id DESC LIMIT 4";
        return Databasee.executeQuery(query);
       
     }
     
        public static ResultSet selectTask() throws SQLException { //restituisce tutti i task presenti nel db
        String query = "SELECT nome,id FROM task";
        return Databasee.executeQuery(query);
       
     }  
        public static ResultSet selectTaskProg(int idprogetto) throws SQLException {
            String query = "SELECT taskprogetto.numcollaboratori, taskprogetto.stato, task.nome FROM taskprogetto, task WHERE (idprogetto="+idprogetto+") AND (taskprogetto.idtask = task.id)";
            return Databasee.executeQuery(query);
         
        }
        
             public static ResultSet selectSvilup() throws SQLException { //restituisce tutti i task presenti nel db
        String query = "SELECT skill.nome, livello.preparazione, sviluppatore.nome FROM progetto, taskprogetto, skillscelte, skillperognitask, skill, livello, sviluppatore";
        return Databasee.executeQuery(query);
       
     }
        
            public static ResultSet selectTaskSkill(int id) throws SQLException { //restituisce tutti i task presenti nel db
        String query = "SELECT DISTINCT task.nome AS nometask, task.id, skill.nome FROM task, skill, skillperognitask WHERE task.id=" + id + " AND task.id=skillperognitask.idtask AND skill.id=skillperognitask.idskill";
        return Databasee.executeQuery(query);
       
     }
            
            
        public static ResultSet skillNonAssociate() throws SQLException { //restituisce tutte le skill ancora non associate ad un task
        String query = "SELECT skill.nome, skill.id FROM skill LEFT JOIN skillperognitask ON skill.id =skillperognitask.idskill WHERE skillperognitask.idskill IS NULL";
        return Databasee.executeQuery(query);
       
     }
        
        
            public static boolean updateRecord(String table, Map<String, Object> data, String condition) throws SQLException {
        // Generazione query
        String query = "UPDATE " + table + " SET ";

        Object value;
        String attr;

        for (Map.Entry<String, Object> e : data.entrySet()) {
            attr = e.getKey();
            value = e.getValue();
            if (value instanceof String) {
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            } else {
                query = query + attr + " = " + value + ", ";
            }

        }
        query = query.substring(0, query.length() - 2) + " WHERE " + condition;
        System.out.println(query);
        // Esecuzione query
        return Databasee.updateQuery(query);
    }
     
      public static ResultSet selectRecord(String table, String condition) throws SQLException {
        // Generazione query
        String query = "SELECT * FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Databasee.executeQuery(query);
    }
      
          public static ResultSet selectRecordst(String table, String condition) throws SQLException {
        // Generazione query
        String query = "SELECT skill.id, skill.nome FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Databasee.executeQuery(query);
    }
      
        public static ResultSet selectMaxRecord(String table, String condition) throws SQLException {
        // Generazione query
        String query = "SELECT MAX(id) AS id FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Databasee.executeQuery(query);
    }
      
          public static ResultSet selectRecord2(String table, String condition) throws SQLException {
        // Generazione query
        String query = "SELECT coordinatore.id FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Databasee.executeQuery(query);
    }
      
        public static ResultSet selectRecord2(String table) throws SQLException {
        // Generazione query
        String query = "SELECT * FROM " + table;
        // Esecuzione query
        return Databasee.executeQuery(query);
    }
      
      
          public static boolean insertRecord(String table, Map<String, Object> data) throws SQLException {
        // Generazione query
        String query = "INSERT INTO " + table + " SET ";
        Object value;
        String attr;

        for (Map.Entry<String, Object> e : data.entrySet()) {
            attr = e.getKey();
            value = e.getValue();
            if (value instanceof Integer) {
                query = query + attr + " = " + value + ", ";
            } else {
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }
        }
        query = query.substring(0, query.length() - 2);
        System.out.println(query);
        // Esecuzione query

        return Databasee.updateQuery(query);
    }
          
         /* public static void inserimento(String table, Map<String, Object> data) throws SQLException{
              Iterator it=data.entrySet().iterator();
              String key;
              Object value;
              while(it.hasNext()){
                  Map.Entry entry=(Map.Entry)it.next();
                  key=(String) entry.getKey();
                  value=entry.getValue();
              }
              String query="INSERT INTO " + table + " VALUES ";
          }*/
          
  
     
     
  }

