package com.mac.rltut.game.entity.item.util;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.effects.Heal;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.item.Spellbook;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 17/09/2017 at 01:10 PM.
 */
public class SpellbookGenerator {
    
    public static Spellbook newSpellbook(int z, Random random){
        Spellbook spellbook = new Spellbook("Spellbook", "A magical book of spells.", Sprite.get("spell_book_1"));
        
        spellbook.addWrittenSpell("heal", new Heal(10, 1f), null, 5);
        
        return spellbook;
    }

}
