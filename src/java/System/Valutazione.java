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
public class Valutazione {
    private int id;
    private int punteggio;
    private int idCoordinatore;
    private int idCollaboratore;
    
    public Valutazione(int id, int punteggio, int idCoordinatore, int idCollaboratore){
        this.id=id;
        this.punteggio=punteggio;
        this.idCoordinatore=idCoordinatore;
        this.idCollaboratore=idCollaboratore;
    }
    
        

    public int getId(){
        return id;
    }
        
    public void setPunteggio(int punteggio){
        this.punteggio=punteggio;
    }
    
    public int getPunteggio(){
        return punteggio;
    }
    
      public void setIdCoordinatore(int idCoordinatore){
        this.idCoordinatore=idCoordinatore;
    }
    
    public int getIdCoordinatore(){
        return idCoordinatore;
    }
    
      public void setIdCollaboratore(int idCollaboratore){
        this.idCollaboratore=idCollaboratore;
    }
    
    public int getIdCollaboratore(){
        return idCollaboratore;
    }
    
    
}
