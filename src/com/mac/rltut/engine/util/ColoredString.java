package com.mac.rltut.engine.util;

import com.mac.rltut.engine.Engine;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 11:31 AM.
 */
public class ColoredString {
    
    public String text;
    public int color;

    protected ColoredString(){}
    
    public ColoredString(String text){
        this(text, Engine.instance().defaultFontColor());
    }
    
    public ColoredString(String text, int color){
        this.text = text;
        this.color = color;
    }

}
