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
public class Commenti {
    private int id;
    private String testo;
    private boolean visibilita;
    private String tipo;
    private int idCollaboratore;
    private int idProgetto;
    private String nomeSvil;
    private String cognomeSvil;
    
    public Commenti(int id, String testo, boolean visibilita, String tipo, int idCollaboratore, int idProgetto){
        this.id=id;
        this.testo=testo;
        this.visibilita=visibilita;
        this.tipo=tipo;
        this.idCollaboratore=idCollaboratore;
        this.idProgetto=idProgetto;
       
     }
    public Commenti(String testo, String nome, String cognome, boolean visib){ // MANCA IMMAGINE!!!!!!
        this.testo=testo;
        this.nomeSvil=nome;
        this.cognomeSvil=cognome;
        this.visibilita=visib;
    }
    
    
    public int getId(){
        return id;
    }
    
      public void setTesto(String testo){
        this.testo=testo;
    }
    
    public String getTesto(){
        return testo;
    }
    
      public void setVisibilita(boolean visibilita){
        this.visibilita=visibilita;
    }
    
    public boolean getVisibilita(){
        return visibilita;
    }
    
      public void setTipo(String tipo){
        this.tipo=tipo;
    }
    
    public String getTipo(){
        return tipo;
    }
    
      public void setIdCollaboratore(int idCollaboratore){
        this.idCollaboratore=idCollaboratore;
    }
    
    public int getIdCollaboratore(){
        return idCollaboratore;
    }
    
      public void setIdProgetto(int idProgetto){
        this.idProgetto=idProgetto;
    }
    
    public int getIdProgetto(){
        return idProgetto;
    }
}
