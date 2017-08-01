package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/07/2017 at 08:34 PM.
 */
public class PlayerSpriteScreen extends Screen{
    
    private String name;
    private List<Sprite> sprites;
    private int selection;
    
    public PlayerSpriteScreen(String name){
        this.name = name;
        this.sprites = new ArrayList<Sprite>();
        
        sprites.add(Sprite.get("player_1"));
        sprites.add(Sprite.get("player_2"));
        sprites.add(Sprite.get("player_3"));
        sprites.add(Sprite.get("player_4"));
    }
    
    @Override
    public Screen input(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_LEFT) if(selection > 0) selection--;
        if(key.getKeyCode() == KeyEvent.VK_RIGHT) if(selection < sprites.size() - 1) selection++;
        if(key.getKeyCode() == KeyEvent.VK_ENTER) return new PlayerSkillSelectScreen(new Player(name, sprites.get(selection)));
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);

        renderer.writeCenter("What do you look like?", Engine.instance().widthInTiles() / 2, 8);
        
        int xp = Engine.instance().widthInTiles() / 2;
        int yp = Engine.instance().heightInTiles() / 2;
                
        for(int i = 0; i < sprites.size(); i++){
            Sprite s = sprites.get(i);
            if(selection == i) renderer.write("> <", xp + (i * 2) - ((sprites.size() * 2) / 2), yp);
            renderer.renderSprite(s, xp + (i * 2) - ((sprites.size() * 2) / 2) + 1, yp);
        }

        renderer.writeCenter("Press [ENTER] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
}
