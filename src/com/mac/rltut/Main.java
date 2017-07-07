package com.mac.rltut;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.parser.DataObject;
import com.mac.rltut.engine.parser.DataParser;

import java.io.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:49 AM.
 */
public class Main {

    public static final int DEFAULT_SCALE = 2;
    
    public static void main(String[] args){
        
        int scale = args.length < 1 ? DEFAULT_SCALE : Integer.parseInt(args[0]);
        if(scale > 4){
            scale = 4;
            Log.warn("Window scale cannot be larger than 4.");
        }
        if(scale < 1){
            scale = 1;
            Log.warn("Window scale cannot be less than 1.");
        }
        
        Engine.instance().init(80, 45, scale, 8, "RLTUT","v0.3.4");
    }
}