package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 10:57 AM.
 */
public class HelpScreen extends Screen{
    
    public HelpScreen(){
        super(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), "");
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        int xp = 3;
        int yp = 3;
        
        renderer.write("Controls", xp++, yp++, Colors.GRAY);
        yp++;
        renderer.write("Movement:     Arrow Keys / Numpad", xp, yp++);
        renderer.write("Wait:         Comma / Numpad 5", xp, yp++);
        renderer.write("Open Chest:   Space", xp, yp++);
        renderer.write("Pickup:       P", xp, yp++);
        renderer.write("Drop:         D", xp, yp++);
        renderer.write("Read:         R", xp, yp++);
        renderer.write("Consume:      C", xp, yp++);
        renderer.write("Equip:        E", xp, yp++);
        renderer.write("Examine:      X", xp, yp++);
        renderer.write("Look:         L", xp, yp++);
        yp++;
        renderer.write("Equipment/Stats Panel: Q / T", xp, yp++);
        yp++;
        
        renderer.write("Debug", xp - 1, yp++, Colors.GRAY);
        yp++;
        renderer.write("Move Up/Down Levels: Page Up / Down", xp, yp++);
        renderer.write("Toggle FOV: F1", xp, yp++);
        
    }
}
