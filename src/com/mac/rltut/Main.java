package com.mac.rltut;

import com.mac.rltut.engine.Engine;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:49 AM.
 */
public class Main {

    public static void main(String[] args){
        Engine.instance().init(64, 36, 2, 8, "RLTUT - Week 2");
    }
}