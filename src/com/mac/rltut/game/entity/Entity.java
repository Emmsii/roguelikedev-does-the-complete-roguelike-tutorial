package com.mac.rltut.game.entity;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.world.World;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:15 AM.
 */
public abstract class Entity implements Cloneable{
    
    public int x, y, z;
    protected String name;
    protected Sprite sprite;
    protected World world;
    
    public Entity(String name, Sprite sprite){
        this.name = name;
        this.sprite = sprite;
    }
    
    public void init(World world){
        this.world = world;
    }
    
    public abstract void update();
    
    public String name(){
        return name;
    }
    
    public Sprite sprite(){
        return sprite;
    }
    
    public Entity newInstance(){
        try {
            return (Entity) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
