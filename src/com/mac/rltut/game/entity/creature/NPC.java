package com.mac.rltut.game.entity.creature;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 08/08/2017 at 12:33 PM.
 */
public class NPC extends Creature{

    protected NPC(){}
    
    public NPC(String name, String description, Sprite sprite, String aiType) {
        super(name, description, sprite, aiType);
    }
    
    public void onTalk(Creature other){
        
    }
}
