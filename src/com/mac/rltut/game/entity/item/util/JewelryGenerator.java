package com.mac.rltut.game.entity.item.util;

import com.mac.rltut.game.entity.item.Equippable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 28/07/2017 at 09:11 AM.
 */
public class JewelryGenerator {
    
    public static Equippable generate(int z){
        return null;
    }
    
    public static Equippable generate(Equippable equippable, Random random){

        List<String> bonuses = new ArrayList<String>();
        bonuses.add("str");
        bonuses.add("def");
        bonuses.add("acc");
        bonuses.add("intel");

        Collections.shuffle(bonuses);
        
        for(String s : bonuses){
            addBonus(s, random.nextInt(3) + 1, equippable);
            if(random.nextFloat() >= 0.2) break;
        }
        
        return equippable;
    }
    
    private static Equippable addBonus(String bonus, int value, Equippable equippable){
        switch (bonus){
            case "str": equippable.setStrengthBonus(value); break;
            case "def": equippable.setDefenseBonus(value); break;
            case "acc": equippable.setAccuracyBonus(value); break;
            case "intel": equippable.setIntelligenceBonus(value); break;
        }
        
        return equippable;
    }
}
