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
public class Coordinatore {
    private int id;
    private int idSviluppatore;
    
    public Coordinatore(int id, int idSviluppatore){
        this.id=id;
        this.idSviluppatore=idSviluppatore;
    }
    
      public Coordinatore(int id){
        this.id=id;
      
    }
 
    
    public int getId(){
        return id;
    }
    
      public void setIdSviluppatore(int idSviluppatore){
        this.idSviluppatore=idSviluppatore;
    }
    
    public int getIdSviluppatore(){
        return idSviluppatore;
    }
}
