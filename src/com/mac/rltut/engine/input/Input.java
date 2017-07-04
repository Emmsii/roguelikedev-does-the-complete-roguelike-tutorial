package com.mac.rltut.engine.input;

import com.mac.rltut.engine.Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Input implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Engine.instance().input(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

