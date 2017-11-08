/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user1
 */
public class Richieste {
    private int id;
    private int idSviluppatore;
    private int idCoordinatore;
    private int idTaskProgetto;
    private String dataCreazione;
    private String stato;
    private boolean tipo;
    private int inviata,eccesso;
    //Aggiunte per richieste "veloci"
    private String nomeSvil,cognSvil,nomeCoord,cognCoord,taskNome,progTitolo,skillNome; 
    //NOTA*** Manca data inizio/fine
    
    public Richieste(int id, int idSviluppatore, int idCoordinatore, int idTaskProgetto, String dataCreazione, String stato, boolean tipo){
        this.id=id;
        this.idSviluppatore=idSviluppatore;
        this.idCoordinatore=idCoordinatore;
        this.idTaskProgetto=idTaskProgetto;
        this.dataCreazione=dataCreazione;
        this.stato=stato;
        this.tipo=tipo;
    }
    //Richieste r = new Richieste(idSviluppatore,nome,cognome,titolo,idTaskProgetto,numCollaboratori, taskNome);
    public Richieste(int idSviluppatore, String nomeSvil, String cognSvil,String progTitolo, int idTaskProgetto, String taskNome, int inviata){
        this.idSviluppatore=idSviluppatore;
        this.nomeSvil=nomeSvil;
        this.cognSvil=cognSvil;
        this.progTitolo=progTitolo;
        this.idTaskProgetto=idTaskProgetto;
        this.taskNome=taskNome;
        this.inviata=inviata;
    }
    
    public Richieste(boolean tipo){
      
        this.tipo=tipo;
    }
   
    
    // Proposte SVILUPPATORE
    public Richieste(int idSviluppatore, int idCoordinatore, String progTitolo, String taskNome, String dataCreazione, String stato, boolean tipo, int idtaskprogetto, int eccesso){
        this.idSviluppatore=idSviluppatore;
        this.idCoordinatore=idCoordinatore;
        this.idTaskProgetto=idtaskprogetto;
        this.taskNome = taskNome;
        this.progTitolo = progTitolo;
        this.dataCreazione = dataCreazione;
        this.stato=stato;
        this.tipo=tipo;
        this.eccesso=eccesso;
    }
    //Offerte Sviluppatore > listaJob
    public Richieste(int idSviluppatore, int idCoordinatore, String progTitolo, String taskNome, String dataCreazione, String stato, boolean tipo, int idtaskprogetto, int inviata,int eccesso){
        this.idSviluppatore=idSviluppatore;
        this.idCoordinatore=idCoordinatore;
        this.idTaskProgetto=idtaskprogetto;
        this.taskNome = taskNome;
        this.progTitolo = progTitolo;
  
        this.dataCreazione = dataCreazione;
        this.stato=stato;
        this.tipo=tipo;
        this.inviata=inviata;
        this.eccesso=eccesso;
    }
    public Richieste(String nomeSvil, String cognSvil, int idSviluppatore, int idCoordinatore, String progTitolo, String taskNome, String dataCreazione, String stato, boolean tipo, int idtaskprogetto, int eccesso){
        this.idSviluppatore=idSviluppatore;
        this.idCoordinatore=idCoordinatore;
        this.idTaskProgetto=idtaskprogetto;
        this.taskNome = taskNome;
        this.progTitolo = progTitolo;
        this.dataCreazione = dataCreazione;
        this.stato=stato;
        this.tipo=tipo;
        this.nomeSvil = nomeSvil;
        this.cognSvil = cognSvil;
        this.eccesso = eccesso;
        
    }
    // Per le richieste generiche
    public Richieste(String nomeSvil,String cognSvil,String nomeCoord,String cognCoord,String taskNome,String progTitolo,String skillNome){
        this.nomeSvil = nomeSvil;
        this.cognSvil = cognSvil;
        this.nomeCoord = nomeCoord;
        this.cognCoord = cognCoord;
        this.taskNome = taskNome;
        this.progTitolo = progTitolo;
        this.skillNome = skillNome;
    }
    // Per visualizzare le offeret
    public Richieste(String progTitolo, String taskNome, String skillNome){
        this.taskNome = taskNome;
        this.progTitolo = progTitolo;
        this.skillNome = skillNome;
    }
    public int getEccesso(){
        return eccesso;
    }
    public String getNomeSvil(){
        return nomeSvil;
    }
    public String getCognSvil(){
        return cognSvil;
    }
    public String getNomeCoord(){
        return nomeCoord;
    }
    public String getCognCoord(){
        return cognCoord;
    }
    public String getTaskNome(){
        return taskNome;
    }
    public String getProgTitolo(){
        return progTitolo;
    }
    public String getSkillNome(){
        return skillNome;
    }
    public int getId(){
        return id;
    }
    public int getInviata(){
        return inviata;
    }
    
      public void setIdSviluppatore(int idSviluppatore){
        this.idSviluppatore=idSviluppatore;
    }
    
    public int getIdSviluppatore(){
        return idSviluppatore;
    }
    
      public void setIdCoordinatore(int idCoordinatore){
        this.idCoordinatore=idCoordinatore;
    }
    
    public int getIdCoordinatore(){
        return idCoordinatore;
    }
    
     public void setIdTaskProgetto(int idTaskProgetto){
        this.idTaskProgetto=idTaskProgetto;
    }
    
    public int getIdTaskProgetto(){
        return idTaskProgetto;
    }
    
      public void setDataCreazione(String dataCreazione){
        this.dataCreazione=dataCreazione;
    }
    
    public String getDataCreazione(){
        return dataCreazione;
    }
    
      public void setStato(String stato){
        this.stato=stato;
    }
    
    public String getStato(){
        return stato;
    }
    
      public void setTipo(boolean tipo){
        this.tipo=tipo;
    }
    
    public boolean getTipo(){
        return tipo;
    }
}
