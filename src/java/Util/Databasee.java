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
        String query = "SELECT titolo, descrizione,datacreazione, id FROM " + table;
        return Databasee.executeQuery(query);
       
     }
     
      public static ResultSet selectProgettoUltimi10() throws SQLException {
        String query = "SELECT datacreazione,titolo, descrizione, id FROM progetto ORDER BY id DESC LIMIT 10";
        return Databasee.executeQuery(query);
       
     }
     
        public static ResultSet selectTask() throws SQLException { //restituisce tutti i task presenti nel db
        String query = "SELECT nome,id FROM task";
        return Databasee.executeQuery(query);
       
     }  
        //Dato un idprogetto, mi restituisce i nomi dei task appartenenti al progetto, il loro stato e il numero di collaboratori.
        public static ResultSet selectTaskProg(int idprogetto) throws SQLException {
            String query = "SELECT taskprogetto.numcollaboratori, taskprogetto.stato, task.nome FROM taskprogetto, task WHERE (idprogetto="+idprogetto+") AND (taskprogetto.idtask = task.id)";
            return Databasee.executeQuery(query);
        }
        
            //Dato un idprogetto, mi restituisce i nomi dei task appartenenti al progetto, il loro stato e il numero di collaboratori.
        public static ResultSet selectTaskProg2(int idprogetto) throws SQLException {
            String query = "SELECT taskprogetto.numcollaboratori, taskprogetto.stato, task.nome, taskprogetto.id FROM taskprogetto, task WHERE (idprogetto="+idprogetto+") AND (taskprogetto.idtask = task.id)";
            return Databasee.executeQuery(query);
        }
        
        //dato un idprogetto, mi restituisce nome e cognome del coordinatore
        public static ResultSet selectProgettoDetail(int idprogetto) throws SQLException{
            String query = "SELECT coordinatore.id, sviluppatore.nome, sviluppatore.cognome, progetto.titolo, progetto.descrizione, progetto.datacreazione FROM progetto, sviluppatore, coordinatore WHERE (progetto.id = "+idprogetto+") AND (progetto.idcoordinatore = coordinatore.id) AND (coordinatore.idsviluppatore = sviluppatore.id)";
            return Databasee.executeQuery(query);
        }
        //Dato un idtask, mi restituisce i dettagli del task, con le skill prerequisito e i collaboratori che ci stanno lavorando e i loro nomi!.
        public static ResultSet selectTaskDetail(int idtask) throws SQLException{
            String query ="SELECT taskprogetto.numcollaboratori, taskprogetto.stato, taskprogetto.descrizione, task.nome, sviluppatore.nome, sviluppatore.cognome FROM taskprogetto, task, collaboratore, sviluppatore WHERE (idtask = "+idtask+") AND (taskprogetto.idtask = task.id) AND (taskprogetto.idtask = collaboratore.idtaskprogetto) AND (collaboratore.idsviluppatore = sviluppatore.id)";
            return Databasee.executeQuery(query);
        }
        
           public static ResultSet contCollaboratori(int idtaskprogetto) throws SQLException{
            String query ="SELECT COUNT(collaboratore.id) AS num FROM collaboratore, taskprogetto WHERE taskprogetto.id=" + idtaskprogetto + " AND collaboratore.idtaskprogetto=taskprogetto.id";
            return Databasee.executeQuery(query);
        }
        
        public static ResultSet getSviluppGener(int id) throws SQLException{
            String query="SELECT sviluppatore.nome, sviluppatore.cognome FROM sviluppatore WHERE sviluppatore.id = "+id;
            return Databasee.executeQuery(query);
        }   
           
           
        //data una stringa, mi restituisce una lista di sviluppatori che hanno quel nome / cognome. (tutto o in parte) ** MANCA L'AVATAR/IMMAGINE fatta in THUUMBNAIL!!
        public static ResultSet searchSviluppatori(String nome) throws SQLException{
            String query="SELECT sviluppatore.nome, sviluppatore.cognome, sviluppatore.id FROM `sviluppatore` WHERE sviluppatore.nome LIKE '"+nome+"%' OR sviluppatore.cognome LIKE '"+nome+"%'";
            return Databasee.executeQuery(query);
        }
        //data una stringa, mi restituisce una lista di progetti che ha quel nome
        public static ResultSet searchProgetti(String nome) throws SQLException{
            String query="SELECT progetto.titolo, progetto.descrizione, progetto.id, progetto.datacreazione FROM `progetto` WHERE progetto.titolo LIKE '"+nome+"%'";
            return Databasee.executeQuery(query);
        }
        
        //dato un idprogetto, ci restituisce TUTTI i commenti.
        public static ResultSet getCommentiProgetto(int idprogetto) throws SQLException{
            String query="SELECT commenti.testo, commenti.visibilità, sviluppatore.nome, sviluppatore.cognome  FROM commenti, progetto, collaboratore, sviluppatore WHERE commenti.idprogetto = progetto.id AND progetto.id = 1 AND commenti.idcollaboratore = collaboratore.id AND collaboratore.idsviluppatore=sviluppatore.id";
            return Databasee.executeQuery(query);
        }
        
        /*Serve query per idVisibilità -> Quando un guest user si connette, se il determinato commento ha visibilità 0 allora non devo mostrarlo. Se invece l'utente è autenticato/loggato, allora
        bisogna vedere se l'id di quell'utente risulta tra gli id degli utenti COLLABORATORI di quel progetto!*/
        
        //dato un idprogetto e un idsviluppatore, ci ritorna due risultati di cui uno è true SE lo sviluppatore fa parte di quel progetto.
        //questo mi serve per vedere se quel determinato sviluppatore è un collaboratore di un progetto E quindi s e può vedere tutti i commenti (anche quelli con visibilità=0).
        //Basta un condizionale per vedere se ritorna un solo false significa che NON fa parte del progetto.
        public static ResultSet checkCollaboratore(int idprogetto, int idsviluppatore) throws SQLException{
            //SELECT DISTINCT IF (1 = taskprogetto.idprogetto AND collaboratore.idtaskprogetto = taskprogetto.id AND collaboratore.idsviluppatore =  11 , 'true', 'false') FROM progetto, taskprogetto, collaboratore
            String query="SELECT DISTINCT IF (" + idprogetto +" = taskprogetto.idprogetto AND collaboratore.idtaskprogetto = taskprogetto.id AND collaboratore.idsviluppatore = "+ idsviluppatore +" , 'true', 'false') FROM progetto, taskprogetto, collaboratore";
            return Databasee.executeQuery(query);
        }
        
        public static ResultSet getCollaboratoreId(int idprogetto,int idsviluppatore) throws SQLException{
            String query="SELECT DISTINCT collaboratore.id FROM  collaboratore, sviluppatore,taskprogetto, progetto WHERE " + idprogetto +" = taskprogetto.idprogetto AND collaboratore.idtaskprogetto = taskprogetto.id AND collaboratore.idsviluppatore = "+ idsviluppatore +" ";
            return Databasee.executeQuery(query);
        }
        
        /*Query che, dato un progetto, ci ridà una lista di SVILUPPATORI che non fanno parte di quel progetto (NO COORDINATORI NO COLLABORATORI)*/
        
        public static ResultSet getElencoPossSvilup(int idprogetto) throws SQLException{
            String query="SELECT sviluppatore.nome, sviluppatore.cognome, sviluppatore.id FROM progetto, taskprogetto, skillscelte, sviluppatore, skillperognitask, skill, livello WHERE progetto.id = "+ idprogetto +" AND taskprogetto.idprogetto = progetto.id AND skillscelte.idtaskprogetto = taskprogetto.id  AND (skillperognitask.id = skillscelte.idskillperognitask AND skill.id = skillperognitask.idskill) AND (livello.idskill = skill.id AND sviluppatore.id = livello.idsviluppatore) AND sviluppatore.id NOT IN( SELECT sviluppatore.id FROM progetto, taskprogetto, collaboratore, sviluppatore WHERE progetto.id="+ idprogetto +" AND taskprogetto.idprogetto = progetto.id AND collaboratore.idtaskprogetto = taskprogetto.id AND sviluppatore.id = collaboratore.idsviluppatore) AND sviluppatore.id NOT IN( SELECT sviluppatore.id FROM progetto, coordinatore, sviluppatore WHERE progetto.id = " + idprogetto + " AND progetto.idcoordinatore = coordinatore.id AND coordinatore.id = sviluppatore.id)";
            return Databasee.executeQuery(query);
        }
        //Query che dato un ID sviluppatore ci dà le informazioni principali TRANNE IMMAGINE PROFILO E CURRICULUM DA METTERE (solo aggiunta di due nomi nella query)!
        public static ResultSet getInfoProfilo(int idsviluppatore) throws SQLException{
            String query="SELECT sviluppatore.nome, sviluppatore.cognome, sviluppatore.data, sviluppatore.email, sviluppatore.telefono, sviluppatore.indirizzo, sviluppatore.immagine FROM sviluppatore WHERE sviluppatore.id = " + idsviluppatore +" ";
            return Databasee.executeQuery(query);
        }
        //query per avere la lista di skill di un utente
        public static ResultSet getSvilupSkills(int idsviluppatore) throws SQLException{
            String query="SELECT skill.nome, livello.preparazione FROM livello, skill WHERE livello.idsviluppatore = " + idsviluppatore +" AND skill.id = livello.idskill";
            return Databasee.executeQuery(query);
        }
        //Query che dato un ID sviluppatore ci dà le informazioni inerenti alle Valutazioni che ha ricevuto, nome del coordinatore che gliel'ha date, titolo del progetto e descrizione della task del progetto.
        public static ResultSet getValutazioniProf(int idsviluppatore) throws SQLException{
            String query="SELECT valutazione.punteggio, sviluppatore.nome, sviluppatore.cognome, progetto.titolo, taskprogetto.descrizione FROM collaboratore,taskprogetto, valutazione, coordinatore, sviluppatore, progetto WHERE collaboratore.idsviluppatore = " + idsviluppatore +" AND valutazione.idcollaboratore = collaboratore.id AND collaboratore.idtaskprogetto = taskprogetto.id AND coordinatore.id = valutazione.idcoordinatore AND (sviluppatore.id = coordinatore.idsviluppatore AND progetto.idcoordinatore = coordinatore.id)";
            return Databasee.executeQuery(query);
            
        }
        
        //Query che dato un idSviluppatore OPPURE un idcoordinatore mi ridà la lista delle richieste/inviti/domande/ecc che hanno.
        // SE sviluppatore = TRUE allora trattiamo l'id immesso come se fosse dello sviluppatore, altrimenti lo trattiamo come coordinatore.
        public static ResultSet getRichieste(int id, boolean sviluppatore) throws SQLException{
            String query = "";
            if(sviluppatore){
                query="SELECT richieste.idsviluppatore,richieste.idcoordinatore, progetto.titolo, task.nome, skill.nome, richieste.datacreazione, richieste.stato, richieste.tipo, richieste.idtaskprogetto FROM progetto, taskprogetto, task, richieste, skillscelte, skillperognitask, skill WHERE richieste.idsviluppatore = " + id + "  AND taskprogetto.id = richieste.idtaskprogetto AND progetto.id = taskprogetto.idprogetto AND task.id = taskprogetto.idtask AND taskprogetto.id = skillscelte.idtaskprogetto AND skillscelte.idskillperognitask = skillperognitask.id AND skillperognitask.idskill = skill.id";
            }else{
                query="SELECT richieste.idsviluppatore,richieste.idcoordinatore, progetto.titolo, task.nome, skill.nome, richieste.datacreazione, richieste.stato, richieste.tipo, richieste.idtaskprogetto FROM progetto, taskprogetto, task, richieste, skillscelte, skillperognitask, skill WHERE richieste.idcoordinatore = " + id + "  AND taskprogetto.id = richieste.idtaskprogetto AND progetto.id = taskprogetto.idprogetto AND task.id = taskprogetto.idtask AND taskprogetto.id = skillscelte.idtaskprogetto AND skillscelte.idskillperognitask = skillperognitask.id AND skillperognitask.idskill = skill.id";
            }
            return Databasee.executeQuery(query);
        }
        // Dopo questa query manca: Query CheckCoordinatore(mi serve anche per il profilo!!)
        // Query con cui prendo i dettagli dello sviluppatore (solo nome/cognome)
        // Query con cui prendo i dettagli di un coordinatore (solo nome/cognome)
        public static ResultSet getIdentitaSvil(int id) throws SQLException{
            String query="SELECT sviluppatore.nome, sviluppatore.cognome FROM sviluppatore WHERE sviluppatore.id =" + id + " ";
            return Databasee.executeQuery(query);
        }
        
        //Dando l'id sviluppatore questa funzione checka SE lo sviluppatore è un coordinatore. SE lo è restituisce l'id del progetto, altrimenti restituisce NULL.
        public static ResultSet checkCoordinatore(int id) throws SQLException{
            String query="SELECT DISTINCT IF ( coordinatore.idsviluppatore = " + id + " AND progetto.idcoordinatore = coordinatore.id, progetto.id  , -1) FROM coordinatore, progetto";
            return Databasee.executeQuery(query);
        }
        
        //dato id svilup, mi dà id coord
        public static ResultSet getCoordId(int id) throws SQLException{
            String query="SELECT DISTINCT IF ( coordinatore.idsviluppatore = " + id + ", coordinatore.id  , -1) FROM coordinatore";
            return Databasee.executeQuery(query);
        }
        //mi restituisce il nome del task del taskprogetto specificato:
        public static ResultSet getRichiestaTask(int idtaskprogetto) throws SQLException{
            String query="SELECT task.nome FROM taskprogetto, task WHERE taskprogetto.id = " + idtaskprogetto + " AND task.id = taskprogetto.idtask";
            return Databasee.executeQuery(query);
        }
        
        //mi restituisce il nome del progetto che ha quel determinato id:
        public static ResultSet getNomeProgetto(int idprogetto) throws SQLException{
            String query="SELECT progetto.titolo FROM progetto WHERE progetto.id = " + idprogetto ;
            return Databasee.executeQuery(query);
        }
     
        /* Query che mi dà la lista di possibili task a cui uno sviluppatore può iscriversi.
        ** Di quali elementi devo tener conto? : 1) TASKPROGETTO.STATO (SE 0 significa che deve essere ancora completato, altrimenti è completo e quindi chiuso!)
        ** 2) SE RIESCO IN TEMPO: tenere conto del numero di collaboratori!!!!!
        ** La query deve, dato un IDSVILUPPATORE, darci una lista di task( + progetto a cui appartiene) a cui lo sviluppatore può "domandare" l'adesione.
        ** OVVIAMENTE se il determinato taskProgetto ha stato = 1, non viene visuualizzato perché significa che è stato COMPLETATO!!!!
        */
        public static ResultSet getListaJob(int idsvil) throws SQLException{
            String query="SELECT progetto.titolo, task.nome, skill.nome, progetto.idcoordinatore, taskprogetto.id FROM livello, skill, skillperognitask, task, skillscelte, taskprogetto, progetto WHERE livello.idsviluppatore = " + idsvil + " AND skill.id = livello.idskill AND skillperognitask.idskill = skill.id AND (task.id = skillperognitask.idtask) AND skillscelte.idskillperognitask = skillperognitask.id AND taskprogetto.id = skillscelte.idtaskprogetto AND progetto.id = taskprogetto.idprogetto AND taskprogetto.stato = 0";
            return Databasee.executeQuery(query);
        }
        
        public static Boolean insertRichiesta(int idSvil, int idCoo, int idTP, String stato, boolean tipo ) throws SQLException{
            String query="INSERT INTO `richieste` (`id`, `idsviluppatore`, `idcoordinatore`, `idtaskprogetto`, `datacreazione`, `stato`, `tipo`) VALUES (NULL, "+idSvil+","+idCoo +", "+idTP+", " + "2000-1-1" +","+ stato+", "+tipo+")";
            return Databasee.updateQuery(query);
        }
        public static Boolean deleteRichiesta(int idrichiesta) throws SQLException{
            String query="DELETE FROM `richieste` WHERE richieste.id = "+idrichiesta;
            return Databasee.updateQuery(query);
        }
        
        
        public static ResultSet selectSvilup() throws SQLException { //restituisce skill nome, livello e nome sviluppatore.
        String query = "SELECT skill.nome, livello.preparazione, sviluppatore.nome, sviluppatore.cognome FROM progetto, taskprogetto, skillscelte, skillperognitask, skill, livello, sviluppatore";
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

