package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.world.builders.WorldBuilder;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 10:33 AM.
 */
public class StartScreen extends Screen{
    
    @Override
    public Screen input(KeyEvent e) {
//        if(e.getKeyCode() == KeyEvent.VK_SPACE) return new SkillSelectScreen();
        if(e.getKeyCode() == KeyEvent.VK_SPACE) return new LoadingScreen(new WorldBuilder(92, 92, 20, System.currentTimeMillis()));
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        renderer.writeCenter("Roguelikedev Tutorial Follow Along Week 6", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3, 0x7A7A7A);
        renderer.writeCenter(Engine.instance().version(), Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3 + 1, 0x636363);
        renderer.writeCenter("Press [SPACE] to start", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
    }
}
