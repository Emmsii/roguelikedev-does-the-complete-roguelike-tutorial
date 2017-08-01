package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.GameScreen;
import com.mac.rltut.game.world.builders.WorldBuilder;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 10:33 AM.
 */
public class StartScreen extends Screen{
    
    private boolean gameExists;
    
    public StartScreen(){
        gameExists = FileHandler.gameSaveExists();
    }
    
    @Override
    public Screen input(KeyEvent e) {
        
        if(gameExists){
            if(e.getKeyCode() == KeyEvent.VK_A) return new GameScreen(FileHandler.loadGame());
            else if(e.getKeyCode() == KeyEvent.VK_B) return new PlayerNameScreen(12);
            else if(e.getKeyCode() == KeyEvent.VK_C) System.exit(0);
        }else{
            if(e.getKeyCode() == KeyEvent.VK_A) return new PlayerNameScreen(12);
            else if(e.getKeyCode() == KeyEvent.VK_B) System.exit(0);
        }

        gameExists = FileHandler.gameSaveExists();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        renderer.writeCenter("Roguelikedev Tutorial Follow Along Week 7", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3, Colors.GRAY);
        renderer.writeCenter(Engine.instance().version(), Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3 + 1, Colors.darken(Colors.GRAY, 0.8f));
        
        if(gameExists) {
            renderer.writeCenter("[a] Continue", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
            renderer.writeCenter("[b] New Game", Engine.instance().widthInTiles() / 2 , Engine.instance().heightInTiles() / 2 + 1);
            renderer.writeCenter("[c] Quit", Engine.instance().widthInTiles() / 2 , Engine.instance().heightInTiles() / 2 + 2);
        }else{
            renderer.writeCenter("[a] New Game", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2);
            renderer.writeCenter("[b] Quit", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2 + 1);
        }
    }
}
