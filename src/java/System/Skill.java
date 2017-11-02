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
public class Skill {
    private int id;
    private String nome;
    private int competenza;
    private int idAdmin;
    
    public Skill(int id, String nome, int idAdmin){
        this.id=id;
        this.nome=nome;
        this.idAdmin=idAdmin;
    }
    //SKILL con il proprio livello di preparazione
    public Skill(String nome, int competenza){
        this.nome=nome;
        this.competenza = competenza;
    }
    

    public int getCompetenza(){
        return this.competenza;
    }
    
    public Skill(String nome){
       
        this.nome=nome;
     
    }
        
         public Skill(int id, String nome){
        this.id=id;
        this.nome=nome;
        
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
    
    public void setIdAdmin(int idAdmin){
        this.idAdmin=idAdmin;
    }
    
    public int getIdAdmin(){
        return idAdmin;
    }
}
