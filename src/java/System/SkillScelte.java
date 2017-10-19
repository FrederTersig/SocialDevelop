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
public class SkillScelte {
    private int id;
    private int idSkillPerOgniTask;
    private int idTaskProgetto;
    
    public SkillScelte(int id, int idSkillPerOgniTask, int idTaskProgetto){
        this.id=id;
        this.idSkillPerOgniTask=idSkillPerOgniTask;
        this.idTaskProgetto=idTaskProgetto;
    }
    
   
    
    public int getId(){
        return id;
    }
    
      public void setIdSkillPerOgniTask(int idSkillPerOgniTask){
        this.idSkillPerOgniTask=idSkillPerOgniTask;
    }
    
    public int getIdSkillPerOgniTask(){
        return idSkillPerOgniTask;
    }
    
          public void setIdTaskProgetto(int idTaskProgetto){
        this.idTaskProgetto=idTaskProgetto;
    }
    
    public int getIdTaskProgetto(){
        return idTaskProgetto;
    }
    
    
}
