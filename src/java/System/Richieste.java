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
