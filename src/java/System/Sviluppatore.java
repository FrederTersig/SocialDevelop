/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

import java.sql.Date;

/**
 *
 * @author user1
 */
public class Sviluppatore {
    private int id;
    private String nome;
    private String cognome;
    private Date dataDiNascita;
    private String email;
    private String telefono;
    private String curriculum;
    private String indirizzo;
    private String password;
    private String immagine;



    public Sviluppatore(int id, String nome, String cognome, Date dataDiNascita, String email, String telefono, String curriculum, String indirizzo, String password, String immagine){
        this.id=id;
        this.nome=nome;
        this.cognome=cognome;
        this.dataDiNascita=dataDiNascita;
        this.email=email;
        this.telefono=telefono;
        this.curriculum=curriculum;
        this.indirizzo=indirizzo;
        this.password=password;
        this.immagine=immagine;
    }
    
    public Sviluppatore(String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
    }
       
    public Sviluppatore(int id, String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
        this.id=id;
    }
    
      public Sviluppatore(String curriculum){
        this.curriculum = curriculum;
     
    }
    
    
   //Sviluppatore che ci viene dato dalla pagina "profilo" NON PERSONALE

    public Sviluppatore(String nome, String cognome, Date data, String email, String telefono, String indirizzo, String immagine){

        this.nome = nome;
        this.cognome = cognome;
        this.email = email; 
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.dataDiNascita = data;
        this.immagine=immagine;
    }
    
    
    
    public Sviluppatore() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
 
    
    public int getId(){
        return id;
    }
    
          public void setNome(String nome){
        this.nome=nome;
    }
    
    public String getNome(){
        return nome;
    }
    
    public void setCognome(String cognome){
        this.cognome=cognome;
    }
    
    public String getCognome(){
        return cognome;
    }
        
    public void setDataDiNascita(Date dataDiNascita){
        this.dataDiNascita=dataDiNascita;
    }
    
    public Date getDataDiNascita(){
        return dataDiNascita;
    }
    
        
    public void setEmail(String email){
        this.email=email;
    }
    
    public String getEmail(){
        return email;
    }
    
        
    public void setTelefono(String telefono){
        this.telefono=telefono;
    }
    
    public String getTelefono(){
        return telefono;
    }
    
        
    public void setCurriculm(String curriculum){
        this.curriculum=curriculum;
    }
    
    public String getCurrciulum(){
        return curriculum;
    }
    
        
    public void setIndirizzo(String indirizzo){
        this.indirizzo=indirizzo;
    }
    
    public String getIndirizzo(){
        return indirizzo;
    }
        
    public void setPassword(String password){
        this.password=password;
    }
    
    public String getPassword(){
        return password;
    }
    
        public void setImmagine(String immagine){
        this.immagine=immagine;
    }
    
    public String getImmagine(){
        return immagine;
    }
    
   
}
