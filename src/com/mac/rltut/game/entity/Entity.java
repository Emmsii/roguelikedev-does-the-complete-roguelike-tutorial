package com.mac.rltut.game.entity;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.map.Map;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:15 AM.
 */
public abstract class Entity {
    
    public int x, y, z;
    protected Sprite sprite;
    protected Map map;
    
    public Entity(Sprite sprite){
        this.sprite = sprite;
    }
    
    public void init(Map map){
        this.map = map;
    }
    
    public abstract void update();
    
    public Sprite sprite(){
        return sprite;
    }
}
