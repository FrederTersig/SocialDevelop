/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

/**
 *
 * @author user1
 */
public class Sviluppatore {
    private int id;
    private String nome;
    private String cognome;
    private String dataDiNascita;
    private String email;
    private int telefono;
    private String curriculum;
    private String indirizzo;
    private String password;
    private int idImmagine;
    
    public Sviluppatore(int id, String nome, String cognome, String dataDiNascita, String email, int telefono, String curriculum, String indirizzo, String password, int idImmagine){
        this.id=id;
        this.nome=nome;
        this.cognome=cognome;
        this.dataDiNascita=dataDiNascita;
        this.email=email;
        this.telefono=telefono;
        this.curriculum=curriculum;
        this.indirizzo=indirizzo;
        this.password=password;
        this.idImmagine=idImmagine;
    }
    
    public Sviluppatore(String nome, String cognome){
        this.nome = nome;
        this.cognome = cognome;
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
        
    public void setDataDiNascita(String dataDiNascita){
        this.dataDiNascita=dataDiNascita;
    }
    
    public String getDataDiNascita(){
        return dataDiNascita;
    }
    
        
    public void setEmail(String email){
        this.email=email;
    }
    
    public String getEmail(){
        return email;
    }
    
        
    public void setTelefono(int telefono){
        this.telefono=telefono;
    }
    
    public int getTelefono(){
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
    
        public void setIdImmagine(int idImmagine){
        this.idImmagine=idImmagine;
    }
    
    public int getIdImmagine(){
        return idImmagine;
    }
    
   
}
