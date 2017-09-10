package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.screen.Screen;

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
        renderBorderFill(renderer);
        
        String hp = String.format("HP: %d/%d", player.hp(), player.maxHp());
        String mana = String.format("MANA: %d/%d", player.mana(), player.maxMana());
        String str = String.format("STR: %d", player.strength());
        String def = String.format("DEF: %d", player.defense());
        String acc = String.format("ACC: %d", player.accuracy());
        String intel = String.format("INT: %d", player.intelligence());
        String level = String.format("LVL: %d", player.level());
        String exp = String.format("XP: %d/%d", player.xp(), (int) (Math.pow(player.level(), 1.75) * 35));
        String day = String.format("Day: %d", player.world().dayNightController().day());
        String turn = String.format("Turn: %d", player.world().dayNightController().tick);
        
        int xp = 1;
        int yp = 1;
                
        renderer.write(hp, xp, yp++);
        renderer.write(mana, xp, yp--);
        xp += 15;
        
        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp);
        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp + 1);
        xp++;

        renderer.write(str, xp, yp++);
        renderer.write(acc, xp, yp--);
        xp+= 8;

        renderer.write(def, xp, yp++);
        renderer.write(intel, xp, yp--);
        xp+= 9;

        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp);
        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp + 1);
        xp++;
        
        renderer.write(level, xp, yp++);
        renderer.write(exp, xp, yp--);
        xp+= 15;

        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp);
        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp + 1);
        xp++;
        
        renderer.write(day, xp, yp++);
        renderer.write(turn, xp, yp--);
        xp+= 18;

        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp);
        renderer.renderSprite(Sprite.get("ui_border_ver"), xp - 1, yp + 1);
        xp++;
        
        renderer.renderSprite(Sprite.get("gold"), xp, yp);
        renderer.write(player.gold() + "", xp + 1, yp++);
//        renderer.renderSprite(Sprite.get("diamond"), xp, yp);
//        renderer.write(player.diamonds() + "", xp + 1, yp++);
    }
}
