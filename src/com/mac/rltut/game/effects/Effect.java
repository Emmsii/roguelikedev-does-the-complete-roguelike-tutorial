package com.mac.rltut.game.effects;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 09:15 AM.
 */
public class Effect implements Cloneable{
    
    protected String name;
    protected String adjective;
    protected String description;
    protected int duration;
    protected float chance;

    protected Effect(){}
    
    public Effect(String name, String adjective, String description, int duration, float chance) {
        this.name = name;
        this.adjective = adjective;
        this.description = description;
        this.duration = duration;
        this.chance = chance;
    }

    public Effect(Effect other) {
        this.name = other.name;
        this.adjective = other.adjective;
        this.description = other.description;
        this.duration = other.duration;
        this.chance = other.chance;
    }

    public void update(Creature creature) {
        duration--;
    }

    public void start(Creature creature) {

    }

    public void stop(Creature creature) {

    }

    public boolean isDone() {
        return duration < 1;
    }

    public String name() {
        return name;
    }

    public String adjective(){
        return adjective;
    }
    
    public String description(){
        return description;
    }

    public float chance() {
        return chance;
    }

    public String chancePercent(){
        return (Math.round(chance * 100)) + "%";
    }

    public Effect newInstance(){
        try {
            return (Effect) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
