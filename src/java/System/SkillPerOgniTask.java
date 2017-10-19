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
public class SkillPerOgniTask {
    private int id;
    private int idSkill;
    private int idTask;
    private int idAdmin;
    
    public SkillPerOgniTask(int id, int idSkill, int idTask, int idAdmin){
        this.id=id;
        this.idSkill=idSkill;
        this.idTask=idTask;
        this.idAdmin=idAdmin;
    }
    

    
    public int getId(){
        return id;
    }
    
      public void setIdSkill(int idSkill){
        this.idSkill=idSkill;
    }
    
    public int getIdSkill(){
        return idSkill;
    }
    
      public void setIdTask(int idTask){
        this.idTask=idTask;
    }
    
    public int getIdTask(){
        return idTask;
    }
    
      public void setIdAdmin(int idAdmin){
        this.idAdmin=idAdmin;
    }
    
    public int getIdAdmin(){
        return idAdmin;
    }
}
