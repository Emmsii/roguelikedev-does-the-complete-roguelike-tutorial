package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.GameScreen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 10:33 AM.
 */
public class TestMenu extends Screen{
    
    @Override
    public Screen input(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) return new GameScreen();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        renderer.writeCenter("Roguelikedev Tutorial Follow Along Week 2", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3, 0x7A7A7A);
        renderer.writeCenter("v0.2.1", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3 + 1, 0x636363);
        renderer.writeCenter("Press [SPACE] to generate a new map.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
    }
}
