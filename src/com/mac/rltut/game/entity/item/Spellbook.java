package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.spells.Spell;

import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:14 AM.
 */
public class Spellbook extends Item{

    private List<Spell> writtenSpells;
    
    protected Spellbook(){}
    
    public Spellbook(String name, String description, Sprite sprite, List<Spell> spells) {
        super(name, description, sprite);
        this.writtenSpells = spells;
    }

    public void addWrittenSpell(String name, Effect effect, int manaCost){
        writtenSpells.add(new Spell(name, effect, manaCost));
    }
    
    public List<Spell> writtenSpells(){
        return writtenSpells;
    }
    
}
