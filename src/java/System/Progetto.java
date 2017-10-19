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
public class Progetto {
    private int id;
    private int idCoordinatore;
    private String titolo;
    private String descrizione;
    private String dataCreazione;
    private int idImmagine;
    
    public Progetto(int id, int idCoordinatore, String titolo, String descrizione, String dataCreazione, int idImmagine){
        this.id=id;
        this.idCoordinatore=idCoordinatore;
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.dataCreazione=dataCreazione;
        this.idImmagine=idImmagine;
    }
    
      public Progetto(String titolo, String descrizione){
        
        
        this.titolo=titolo;
        this.descrizione=descrizione;
        
    }
  
    
    public int getId(){
        return id;
    }
    
      public void setIdCoordinatore(int idCoordinatore){
        this.idCoordinatore=idCoordinatore;
    }
    
    public int getIdCoordinatore(){
        return idCoordinatore;
    }
    
      public void setTitolo(String titolo){
        this.titolo=titolo;
    }
    
    public String getTitolo(){
        return titolo;
    }
    
      public void setDescrizione(String descrizione){
        this.descrizione=descrizione;
    }
    
    public String getdescrizione(){
        return descrizione;
    }
    
      public void setDataCreazione(String dataCreazione){
        this.dataCreazione=dataCreazione;
    }
    
    public String getDataCreazione(){
        return dataCreazione;
    }
    
     public void setIdImmagine(int idImmagine){
        this.idImmagine=idImmagine;
    }
    
    public int getIdImmagine(){
        return idImmagine;
    }
    

}
