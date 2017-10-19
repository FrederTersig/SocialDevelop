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
public class Collaboratore {
    private int id;
    private int idTaskProgetto;
    private int idSviluppatore;
    
    public Collaboratore(int id, int idTaskProgetto, int idSviluppatore){
        this.id=id;
        this.idTaskProgetto=idTaskProgetto;
        this.idSviluppatore=idSviluppatore;
    }
    
    public void setIdTaskProgetto(int idTaskProgetto){
        this.idTaskProgetto=idTaskProgetto;
    }
    
    public int getIdTaskProgetto(){
        return idTaskProgetto;
    }
    
    public void setIdSviluppatore(int idSViluppatore){
        this.idSviluppatore=idSviluppatore;
    }
    
    public int getIdSviluppatore(){
        return idSviluppatore;
    }
    
      
    
    public int getId(){
        return id;
    }
}
