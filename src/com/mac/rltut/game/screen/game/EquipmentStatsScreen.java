package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 04:47 PM.
 */
public class EquipmentStatsScreen extends Screen {
    
    private Creature player;
    
    public EquipmentStatsScreen(int x, int y, int width, int height, String title, Creature player){
        super(x, y, width, height, title);
        this.player = player;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);

        int xp = this.x + 2;
        int yp = this.y + 3;

        renderer.write("Head", xp, yp++);
        renderer.write("nothing", xp, yp++, 0x8C8C8C);
        yp++;

        renderer.write("Chest", xp, yp++);
        renderer.write("nothing", xp, yp++, 0x8C8C8C);
        yp++;

        renderer.write("Weapon", xp, yp++);
        if(player.weapon() != null) renderer.write(player.weapon().name(), xp, yp++);
        else renderer.write("nothing", xp, yp++, 0x8C8C8C);
        yp++;

        renderer.write("Shield", xp, yp++);
        renderer.write("nothing", xp, yp++, 0x8C8C8C);
        yp++;

        renderer.write("Legs", xp, yp++);
        renderer.write("nothing", xp, yp++, 0x8C8C8C);
        yp++;

        renderer.write("Feet", xp, yp++);
        renderer.write("nothing", xp, yp++, 0x8C8C8C);
    }
}
