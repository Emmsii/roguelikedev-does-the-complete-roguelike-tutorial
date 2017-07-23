package com.mac.rltut.engine.util;

import com.esotericsoftware.minlog.Log;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 01:11 PM.
 */
public class MathUtil {

    private static final Random random = new Random();
    
    public static int distance(int xa, int ya, int xb, int yb){
        return (int) Math.sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
    }
    
    public static float range(float min, float max, Random rand){
        return rand.nextFloat() * (max - min) + min;
    }
        
    public static int range(int min, int max, Random rand){
        return rand.nextInt((max - min) + 1) + min;
    }
    
    public static float randomFloatFromString(String input, Random rand){
        String[] split = input.trim().split("-");
        if(split.length < 2) return Float.parseFloat(split[0]);
        float left = Float.parseFloat(split[0].trim());
        float right = Float.parseFloat(split[1].trim());
        return range(Math.min(left, right), Math.max(left, right), rand);
    }

    public static int randomIntFromString(String input){
        return randomIntFromString(input, random);
    }
    
    //TODO: I might be broken
    public static int randomIntFromString(String input, Random rand){
        String[] split = input.trim().split("-");
        if(split.length < 2) return Integer.parseInt(split[0]);
        int left = Integer.parseInt(split[0].trim());
        int right = Integer.parseInt(split[1].trim());
        return range(Math.min(left, right), Math.max(left, right), rand);
    }
}
