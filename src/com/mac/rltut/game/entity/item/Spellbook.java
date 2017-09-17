package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:14 AM.
 */
public class Spellbook extends Item{

    private List<Spell> writtenSpells;
    
    protected Spellbook(){}
    
    public Spellbook(String name, String description, Sprite sprite) {
        super(name, description, sprite);
        this.writtenSpells = new ArrayList<Spell>();
    }

    public void addWrittenSpell(String name, Effect effectSelf, Effect effectOther, int manaCost){
        writtenSpells.add(new Spell(name, effectSelf, effectOther, manaCost));
    }
    
    public List<Spell> writtenSpells(){
        return writtenSpells;
    }
    
}
