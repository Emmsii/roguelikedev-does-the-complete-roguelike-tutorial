package com.mac.rltut.game.effects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.Spellbook;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:07 AM.
 */
public class Burn extends Effect{
    
    private int amount;

    public Burn(){}

    public Burn(int amount, int duration, float chance){
        super("burn", "burning", "take " + amount + " damage for " + duration + " turn" + (duration > 1 ? "s" : ""), duration, chance, Sprite.get("ui_burning"));
        this.amount = amount;
    }

    @Override
    public void start(Creature creature) {
        creature.doAction(new ColoredString("burn"));
    }

    @Override
    public void update(Creature creature) {
        super.update(creature);
        creature.modifyHp(-amount, "burning");
        
        for(Item item : creature.inventory().items()){
            if(item instanceof Spellbook && Math.random() < 0.1){
                creature.notify(new ColoredString("A spellbook catches fire and burns!", Colors.ORANGE));
                creature.inventory().remove(item);
                break;
            }
        }
    }
}
