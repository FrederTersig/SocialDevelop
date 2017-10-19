/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package System;

/**
 *
 * @author Claudio Morè
 */
public class Admin {
    
    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String tel;
    
    public Admin(int id, String nome, String cognome, String email, String tel){
        this.id=id;
        this.nome=nome;
        this.cognome=cognome;
        this.email=email;
        this.tel=tel;
    
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
    
      public void setEmail(String email){
        this.email=email;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setTel(String tel){
        this.tel=tel;
    }
    
    public String getTel(){
        return tel;
    }
    
    
    
}
