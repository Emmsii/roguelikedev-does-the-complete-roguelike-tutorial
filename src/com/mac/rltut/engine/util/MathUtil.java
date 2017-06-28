package com.mac.rltut.engine.util;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 01:11 PM.
 */
public class MathUtil {

    public static float range(float min, float max, Random rand){
        return rand.nextFloat() * (max - min) + min;
    }
}
