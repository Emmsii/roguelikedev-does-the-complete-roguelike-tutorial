package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 10:44 AM.
 */
public class LooseScreen extends Screen{
    
    private Player player;
    
    public LooseScreen(Player player){
        this.player = player;
        FileHandler.deleteGameSave();
    }
    
    @Override
    public Screen input(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) return new StartScreen();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        
        renderer.writeCenter("You died!", Engine.instance().widthInTiles() / 2, (int) (Engine.instance().heightInTiles() * 0.15f), Colors.RED);
        renderer.write("Kills", 10, 10);
        int xp = 11;
        int yp = 12;
        for(String kill : player.stats().kills().keySet()){
            Creature c = Codex.creatures.get(kill.toLowerCase()).creature();
            renderer.renderSprite(c.sprite(), xp, yp);
            renderer.write("x" + player.stats().kills().get(kill), xp + 2, yp);
            if(c.size() == 1) yp += 2;
            else yp += 3;
        }

        renderer.writeCenter("Press [ESCAPE] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
}
