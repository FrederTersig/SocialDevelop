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
public class Immagini {
    
    private int id;
    private String immagine;
    
    public Immagini(int id, String immagine){
        this.id = id;
        this.immagine = immagine;
    }
    
    public int getId(){
        return id;
    }
    
   
    
    public String getImmagine(){
        return immagine;
    }
    public void setImmagine(String immagine){
        this.immagine = immagine;
    }
}
