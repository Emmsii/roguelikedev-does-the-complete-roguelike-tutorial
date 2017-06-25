package com.mac.rltut.game.screen;

import com.mac.rltut.engine.graphics.Renderer;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:57 AM.
 */
public class TestScreen extends Screen{

    @Override
    public Screen input(KeyEvent e) {
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderer.write("Hello world!", 2, 2, 0xff00ff);
    }
}