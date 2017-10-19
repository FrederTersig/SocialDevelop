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
public class Task {
    private int id;
    private String nome;
    private int idAdmin;
    
    public Task(int id, String nome, int idAdmin){
        this.id=id;
        this.nome=nome;
        this.idAdmin=idAdmin;
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
