package com.mac.rltut.game.screen.game.inventory;

import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.menu.StartScreen;

import java.awt.event.KeyEvent;
import java.security.Key;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 05:28 PM.
 */
public class GameOptionsMenu extends Screen{
    
    private Game game;
    private boolean saved;
    
    public GameOptionsMenu(int x, int y, int w, int h, Game game) {
        super(x, y, w, h, "");
        this.game = game;
        this.saved = false;
    }
    
    @Override
    public Screen input(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_A) return null;
        else if(key.getKeyCode() == KeyEvent.VK_B && !saved){
            FileHandler.saveGame(game);
            saved = true;
        } else if(key.getKeyCode() == KeyEvent.VK_C){
            FileHandler.saveGame(game);
            return new StartScreen();
        }else if(key.getKeyCode() == KeyEvent.VK_D){
            FileHandler.saveGame(game);
            System.exit(0);
        }
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        int xp = this.x + this.width / 2;
        int yp = this.y + 2;
        
        renderer.writeCenter("[a] Resume", xp, yp++);
        renderer.writeCenter("[b] Save Game", xp, yp++, !saved ? Colors.WHITE : Colors.GRAY);
        renderer.writeCenter("[c] Main Menu", xp, yp++);
        renderer.writeCenter("[d] Quit Game", xp, yp++);
    }
}
