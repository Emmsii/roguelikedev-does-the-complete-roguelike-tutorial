package com.mac.rltut.engine.util.maths;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 16/07/2017 at 12:05 PM.
 */
public class Dice {
    
    // http://www.redblobgames.com/articles/probability/damage-rolls.html
    
    private static final Random random = new Random();

    public static int roll(String roll){
        return roll(roll, random);
    }
    
    public static int roll(String roll, Random rand){
        int dice = parseDice(roll);
        int sides = parseSides(roll);
        int result = 0;
        for(int i = 0; i < dice; i++) result += 1 + rand.nextInt(sides);
        return result;     
    }

    private static int parseDice(String roll){
        return Integer.parseInt(roll.trim().toLowerCase().split("d")[0]);
    }

    private static int parseSides(String roll){
        return Integer.parseInt(roll.trim().toLowerCase().split("d")[1]);
    }
}
