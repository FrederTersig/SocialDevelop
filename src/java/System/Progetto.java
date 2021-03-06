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
    private String datacreazione;
    private String nomeCoordinatore;
    private String cognomeCoordinatore;
     private String datascad;
    private int idImmagine;
    private int scad;
    
    public Progetto(int id, int idCoordinatore, String titolo, String descrizione, String dataCreazione, int idImmagine){
        this.id=id;
        this.idCoordinatore=idCoordinatore;
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.datacreazione=dataCreazione;
        this.idImmagine=idImmagine;
    }
    
    public Progetto(String titolo, String descrizione){
        this.titolo=titolo;
        this.descrizione=descrizione;
    }
    
      public Progetto(String titolo, String descrizione, String datascad){
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.datascad=datascad;
    }
    
    
      public Progetto(int id, String titolo){
        this.titolo=titolo;
        this.id=id;
    }
      
      public Progetto(int id, String titolo, int scad){
        this.titolo=titolo;
        this.id=id;
        this.scad=scad;
    }
    
    public Progetto(String titolo){
        this.titolo=titolo;
        
    }
    
    public Progetto(String titolo, String descrizione, int id){
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.id = id;
    }
    
       public Progetto(String titolo, String descrizione, int id, String datacreazione){
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.id = id;
        this.datacreazione=datacreazione;
        this.scad=scad;
    }
       
       
          public Progetto(String titolo, String descrizione, int id, String datacreazione, int scad){
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.id = id;
        this.datacreazione=datacreazione;
        this.scad=scad;
    }
    //progetto per il dettaglioProgetto - nome,cognome,titolo,descrizione
    public Progetto(String nome, String cognome, String titolo, String descrizione){
        this.nomeCoordinatore=nome;
        this.cognomeCoordinatore=cognome;
        this.titolo=titolo;
        this.descrizione=descrizione;
    }
    
    public String getNomeCoord(){
        return nomeCoordinatore;
    }
    
      public String getDataScad(){
        return datascad;
    }
    
    public String getCognomeCoord(){
        return cognomeCoordinatore;
    }
    public void setNomeCoord(String nome){
        this.nomeCoordinatore=nome;
    }
    public void setCognomeCoord(String cognome){
        this.cognomeCoordinatore=cognome;
    }
    
    public int getId(){
        return id;
    }
    
    public int getScad(){
        return scad;
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
    
    public String getDescrizione(){
        return descrizione;
    }
    
      public void setDataCreazione(String dataCreazione){
        this.datacreazione=dataCreazione;
    }
    
    public String getDataCreazione(){
        return datacreazione;
    }
    
     public void setIdImmagine(int idImmagine){
        this.idImmagine=idImmagine;
    }
    
    public int getIdImmagine(){
        return idImmagine;
    }
    

}
