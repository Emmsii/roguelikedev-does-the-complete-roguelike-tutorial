package com.mac.rltut.game.entity;

import com.mac.rltut.engine.graphics.Sprite;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:15 AM.
 */
public abstract class Entity {
    
    public int x, y;
    protected Sprite sprite;
    
    public Entity(Sprite sprite){
        this.sprite = sprite;
    }
    
    public void init(){
        
    }
    
    public abstract void update();
    
    public Sprite sprite(){
        return sprite;
    }
}
