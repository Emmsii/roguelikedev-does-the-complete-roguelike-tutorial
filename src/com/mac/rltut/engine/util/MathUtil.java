package com.mac.rltut.engine.util;

import com.esotericsoftware.minlog.Log;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 01:11 PM.
 */
public class MathUtil {

    public static int distance(int xa, int ya, int xb, int yb){
        return (int) Math.sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
    }
    
    public static float range(float min, float max, Random rand){
        return rand.nextFloat() * (max - min) + min;
    }

    public static int range(int min, int max, Random rand){
        return rand.nextInt((max - min) + 1) + min;
    }
    
    public static float randomFloatFromString(String input, Random random){
        String[] split = input.trim().split("-");
        if(split.length != 2) throw new IllegalArgumentException("Input must contain two float values either side of a '-' character.");
        return range(Float.parseFloat(split[0]), Float.parseFloat(split[1]), random);
    }

    public static int randomIntFromString(String input, Random random){
        String[] split = input.trim().split("-");
        if(split.length != 2) throw new IllegalArgumentException("Input must contain two int values either side of a '-' character.");
        return range(Integer.parseInt(split[0]), Integer.parseInt(split[1]), random);
    }
}
