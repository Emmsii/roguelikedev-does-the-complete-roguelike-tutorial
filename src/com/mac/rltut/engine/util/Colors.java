package com.mac.rltut.engine.util;

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
    
    public static int blend(int a, int b, float factor){
        if(a == 0 || b == 0) return 0;
        if(factor == 0f) return a;
        else if(factor == 1f) return b;
        if(factor > 1f) factor = 1f;
        else if(factor < 0f) factor = 0f;
        float iFact = 1f - factor;
        
        int ar = ((a & 0xff0000) >> 16);
        int ag = ((a & 0xff00) >> 8);
        int ab = (a & 0xff);

        int br = ((b & 0xff0000) >> 16);
        int bg = ((b & 0xff00) >> 8);
        int bb = (b & 0xff);
        
        int re = (int) ((ar * iFact) + (br * factor));
        int gr = (int) ((ag * iFact) + (bg * factor));
        int bl = (int) ((ab * iFact) + (bb * factor));
        
        return re << 16 | gr << 8 | bl;
    }
}
