package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.game.entity.creature.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 05:08 PM.
 */
public class PackAI {
    
    private List<PackMemberAI> creatures;
  
    public PackAI(){
        this.creatures = new ArrayList<PackMemberAI>();
    }
    
    public void update(){
        
    }

    public void addCreature(PackMemberAI creature){
        creatures.add(creature);
    }
}
