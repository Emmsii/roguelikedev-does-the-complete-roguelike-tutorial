package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.builders.WorldBuilder;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 10:33 AM.
 */
public class StartScreen extends Screen{
    
    @Override
    public Screen input(KeyEvent e) {
//        if(e.getKeyCode() == KeyEvent.VK_SPACE) return new PlayerNameScreen(12);
        
        //Temp
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            Player player = new Player("player", Sprite.get("player_1"));
            player.setStats(50, 50, 2, 20, 3, 3, 3, 3, 16, null);
            return new LoadingScreen(player, new WorldBuilder(92, 92, 20, System.currentTimeMillis()));
        }
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        renderer.writeCenter("Roguelikedev Tutorial Follow Along Week 6", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3, Colors.GRAY);
        renderer.writeCenter(Engine.instance().version(), Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3 + 1, Colors.darken(Colors.GRAY, 0.8f));
        renderer.writeCenter("Press [SPACE] to start", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
    }
}
