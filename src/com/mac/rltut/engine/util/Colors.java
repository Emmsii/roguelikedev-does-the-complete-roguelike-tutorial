package com.mac.rltut.engine.util;

import javafx.scene.paint.Color;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 26/06/2017 at 05:09 PM.
 */
public class Colors {
    
    private static final float FACTOR = 0.4f;
    
    public static int darken(int color){
        int r = (int) (((color & (0xff << 16)) >> 16) * FACTOR);
        int g = (int) (((color & (0xff << 8)) >> 8) * FACTOR);
        int b = (int) ((color & 0xff) * FACTOR);
        return r << 16 | g << 8 | b;
    }
}
