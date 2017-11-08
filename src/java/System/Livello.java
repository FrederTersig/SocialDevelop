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
public class Livello {
    private int id;
    private int preparazione;
    private int idSviluppatore;
    private int idSkill;
    
    public Livello(int id, int preparazione, int idSviluppatore, int idSkill){
        this.id=id;
        this.preparazione=preparazione;
        this.idSviluppatore=idSviluppatore;
        this.idSkill=idSkill;
    }
    
      public Livello(int preparazione){
        
        this.preparazione=preparazione;
       
    }
    

    
    public int getId(){
        return id;
    }
    
      public void setPreparazione(int preparazione){
        this.preparazione=preparazione;
    }
    
    public int getPreparazione(){
        return preparazione;
    }
    
      public void setIdSviluppatore(int idSvilupaptore){
        this.idSviluppatore=idSviluppatore;
    }
    
    public int getIdSviluppatore(){
        return idSviluppatore;
    }
    
      public void setIdSkill(int idSkill){
        this.idSkill=idSkill;
    }
    
    public int getIdSkill(){
        return idSkill;
    }
}
