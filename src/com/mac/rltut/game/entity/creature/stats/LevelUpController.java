package com.mac.rltut.game.entity.creature.stats;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:27 AM.
 */
public class LevelUpController {
    
    private static LevelUpOption[] options = new LevelUpOption[]{
            new LevelUpOption("Increase strength") {
                @Override
                public void invoke(Creature creature) {
                    creature.modifyStrength(1);
                    creature.doAction(new ColoredString("look a little stronger"));
                }
            },
            new LevelUpOption("Increase defense") {
                @Override
                public void invoke(Creature creature) {
                    creature.modifyDefense(1);
                    creature.doAction(new ColoredString("look a little tougher"));
                }
            },
            new LevelUpOption("Increase accuracy") {
                @Override
                public void invoke(Creature creature) {
                    creature.modifyAccuracy(1);
                    creature.doAction(new ColoredString("feel more focused"));
                }
            },
            new LevelUpOption("Increase intelligence") {
                @Override
                public void invoke(Creature creature) {
                    creature.modifyIntelligence(1);
                    creature.doAction(new ColoredString("feel a little smarter"));
                }
            },
            new LevelUpOption("Increase mana") {
                @Override
                public void invoke(Creature creature) {
                    creature.modifyMaxMana(10);
                    creature.doAction(new ColoredString("feel more magical"));
                }
            }
    };
    
    public void autoLevelUp(Creature creature){
        options[(int) (Math.random() * options.length)].invoke(creature);
    }
    
    public List<String> levelUpOptions(){
        List<String> names = new ArrayList<String>();
        for(LevelUpOption option : options) names.add(option.name());
        return names;
    }
    
    public LevelUpOption levelUpOption(String name){
        for(LevelUpOption option : options) if(option.name().equalsIgnoreCase(name)) return option;
        return null;
    }
}
