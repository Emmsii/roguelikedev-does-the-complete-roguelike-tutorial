package com.mac.rltut.game.effects.spells;

import com.mac.rltut.game.effects.Effect;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 13/08/2017 at 02:50 PM.
 */
public class Spell {
    
    private String name;
    private Effect effect;
    private int manaCost;
    
    protected Spell(){}
    
    public Spell(String name, Effect effect, int manaCost){
        this.name = name;
        this.effect = effect;
        this.manaCost = manaCost;
    }
    
    public String name(){
        return name;
    }
    
    public Effect effect(){
        return effect;
    }
    
    public int manaCost(){
        return manaCost;
    }
    
}
