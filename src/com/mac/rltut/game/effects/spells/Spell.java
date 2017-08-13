package com.mac.rltut.game.effects.spells;

import com.mac.rltut.game.effects.Effect;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 13/08/2017 at 02:50 PM.
 */
public class Spell {
    
    private String name;
    private Effect effectOther;
    private Effect effectSelf;
    private int manaCost;
    
    protected Spell(){}
    
    public Spell(String name, Effect effectOther, Effect effectSelf, int manaCost){
        this.name = name;
        this.effectOther = effectOther;
        this.effectSelf = effectSelf;
        this.manaCost = manaCost;
    }
    
    public String name(){
        return name;
    }
    
    public Effect effectOther(){
        return effectOther.newInstance();
    }

    public Effect effectSelf(){
        if(effectSelf == null) return null;
        return new Effect(effectSelf);
    }
    
    public int manaCost(){
        return manaCost;
    }
    
}
