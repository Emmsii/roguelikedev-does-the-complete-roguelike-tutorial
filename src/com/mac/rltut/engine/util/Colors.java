package com.mac.rltut.engine.util;

import javafx.scene.paint.Color;
import sun.util.resources.cldr.ar.CalendarData_ar_KW;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 26/06/2017 at 05:09 PM.
 */
public class Colors {
    
    public static final float DARKEN_FACTOR = 0.4f;
    
    public static int darken(int color){
       return darken(color, DARKEN_FACTOR);
    }

    public static int darken(int color, float factor){
        int r = (int) (((color & (0xff << 16)) >> 16) * factor);
        int g = (int) (((color & (0xff << 8)) >> 8) * factor);
        int b = (int) ((color & 0xff) * factor);
        return r << 16 | g << 8 | b;
    }
}
