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
    
    public Richieste(boolean tipo){
      
        this.tipo=tipo;
    }
    
    // Proposte
    public Richieste(int idSviluppatore, int idCoordinatore, String progTitolo, String taskNome, String skillNome, String dataCreazione, String stato, boolean tipo, int idtaskprogetto){
        this.idSviluppatore=idSviluppatore;
        this.idCoordinatore=idCoordinatore;
        this.idTaskProgetto=idtaskprogetto;
        this.taskNome = taskNome;
        this.progTitolo = progTitolo;
        this.skillNome = skillNome;
        this.dataCreazione = dataCreazione;
        this.stato=stato;
        this.tipo=tipo;
        
        
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
    
      public void setIdSviluppatore(int idSviluppatore){
        this.idSviluppatore=idSviluppatore;
    }
    
    public int getIdSvilupaptore(){
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
