package com.mac.rltut.game.effects;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.game.entity.creature.Creature;
import jdk.nashorn.internal.ir.Terminal;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 09:15 AM.
 */
public class Effect {

    protected String name;
    protected String description;
    protected int duration;
    protected float chance;
    protected boolean canUseWithItem;

    public Effect(String name, String description, int duration, float chance, boolean canUseWithItem) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.chance = chance;
        this.canUseWithItem = canUseWithItem;
    }

    public Effect(Effect other) {
        this.name = other.name;
        this.description = other.description;
        this.duration = other.duration;
        this.chance = other.chance;
        this.canUseWithItem = other.canUseWithItem;
    }

    public void update(Creature creature) {
        duration--;
    }

    public void onUseSelf(Creature creature) {

    }

    public void onUseOther(Creature other) {

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

    public String description(){
        return description;
    }

    public float chance() {
        return chance;
    }

    public String chancePercent(){
        return (Math.round(chance * 100)) + "%";
    }
    
    public boolean canUseWithItem() {
        return canUseWithItem;
    }

}
