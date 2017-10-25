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
public class TaskProgetto {
    private int id;
    private String descrizione;
    private int numCollaboratori;
    private boolean stato;
    private int idProgetto;
    private int idTask;
    
    public TaskProgetto(int id, String descrizione, int numCollaboratori, boolean stato, int idProgetto, int idTask){
        this.id=id;
        this.descrizione=descrizione;
        this.numCollaboratori=numCollaboratori;
        this.stato=stato;
        this.idProgetto=idProgetto;
        this.idTask=idTask;
    }
    
        
  
    
    public int getId(){
        return id;
    }
        
    public void setDescrizione(String descrizione){
        this.descrizione=descrizione;
    }
    
    public String getDescrizione(){
        return descrizione;
    }
        
    public void setNumCollaboratori(int numCollaboratori){
        this.numCollaboratori=numCollaboratori;
    }
    
    public int getnumCollaboratori(){
        return numCollaboratori;
    }
        
    public void setStato(boolean stato){
        this.stato=stato;
    }
    
    public boolean getStato(){
        return stato;
    }
    
        
    public void setIdProgetto(int idProgetto){
        this.idProgetto=idProgetto;
    }
    
    public int getIdProgetto(){
        return idProgetto;
    }
        
    public void setIdTask(int idTask){
        this.idTask=idTask;
    }
    
    public int getIdTask(){
        return idTask;
    }
    
    
    
   
            
          
           
}
