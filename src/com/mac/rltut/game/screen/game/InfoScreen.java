package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 12:03 PM.
 */
public class InfoScreen extends Screen{
    
    private Creature player;    
    
    public InfoScreen(int x, int y, int width, int height, String title, Creature player){
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
        
        String hp = String.format("HP: %d/%d", player.hp(), player.maxHp());
        String mana = String.format("MANA: %d/%d", player.mana(), player.maxMana());
        String str = String.format("STR: %d", player.strength());
        String def = String.format("DEF: %d", player.defense());
        String acc = String.format("ACC: %d", player.accuracy());
        String intel = String.format("INT: %d", player.intelligence());
        String level = String.format("Level: %d", player.level());
        String exp = String.format("XP: %d/%d", player.xp(), (int) (Math.pow(player.level(), 1.75) * 25));
        String time = String.format("Day: %d (%d)", player.world().dayNightController().day(), player.world().dayNightController().tick);
 
        int xp = this.x + 2;
        int yp = this.y + 2;
        
        renderer.write(hp, xp, yp++);
        renderer.write(str, xp, yp++);
        renderer.write(acc, xp, yp++);
        
        yp++;
        renderer.write(level, xp, yp++);

        yp++;
        renderer.write(time, xp, yp++);
        
        xp = this.x + (width / 2);
        yp = this.y + 2;

        renderer.write(mana, xp, yp++);
        renderer.write(def, xp, yp++);
        renderer.write(intel, xp, yp++);

        yp++;
        renderer.write(exp, xp, yp++);

        yp++;
        String gold = player.gold() + "";
        renderer.write(gold, xp - gold.length() + 11, yp);
        renderer.renderSprite(Sprite.get("gold"), xp + 12, yp);
    }
}
