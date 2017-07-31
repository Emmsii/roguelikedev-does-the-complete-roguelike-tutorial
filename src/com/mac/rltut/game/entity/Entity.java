package com.mac.rltut.game.entity;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.world.World;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:15 AM.
 */
public abstract class Entity implements Cloneable{
    
    public int id;
    public int x, y, z;
    protected String name;
    protected String description;
    protected Sprite sprite;
    protected World world;

    public Entity() {}
    
    public Entity(String name, String description, Sprite sprite){
        this.name = name;
        this.description = description;
        this.sprite = sprite;
    }
    
    public void init(int id, World world){
        this.id = id;
        this.world = world;
    }
    
    public abstract void update();
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String name(){
        return name;
    }
    
    public String description(){
        return description;
    }
    
    public Sprite sprite(){
        return sprite;
    }
    
    public World world(){
        return world;
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
