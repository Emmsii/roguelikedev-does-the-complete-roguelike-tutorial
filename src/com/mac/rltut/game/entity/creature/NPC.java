package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 12:33 PM.
 */
public class NPC extends Creature{

    protected boolean canAttack;

    protected NPC(){}
    
    public NPC(String name, String description, Sprite sprite, String aiType) {
        super(name, description, sprite, aiType);
        canAttack = false;
    }
    
    public void onTalk(Creature other){
        
    }

    public void setCanAttack(boolean canAttack){
        this.canAttack = canAttack;
    }

    public boolean canAttack(){
        return canAttack;
    }
}
