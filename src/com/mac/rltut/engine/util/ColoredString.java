package com.mac.rltut.engine.util;

import java.awt.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 11:31 AM.
 */
public class ColoredString {
    
    public String text;
    public int color;
    
    public ColoredString(){
        
    }
    
    public ColoredString(String text){
        this(text, Color.white.getRGB());
    }
    
    public ColoredString(String text, int color){
        this.text = text;
        this.color = color;
    }
    
}
