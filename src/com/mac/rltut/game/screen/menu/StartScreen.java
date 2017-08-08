package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.GameScreen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/06/2017 at 10:33 AM.
 */
public class StartScreen extends Screen{
    
    private boolean gameExists;
    private String saveVersion;
    
    public StartScreen(){
        gameExists = FileHandler.gameSaveExists();
        saveVersion = FileHandler.gameVersion();
    }
    
    @Override
    public Screen input(KeyEvent e) {
        
        if(gameExists){
            if(e.getKeyCode() == KeyEvent.VK_A) return new GameScreen(FileHandler.loadGame());
            else if(e.getKeyCode() == KeyEvent.VK_B) return new ConfirmScreen("This will overwrite your previous game!", new PlayerNameScreen(12), this){
                @Override
                public Screen onYes() {
                    FileHandler.deleteGameSave();
                    return super.onYes();
                }
            };
            else if(e.getKeyCode() == KeyEvent.VK_C) System.exit(0);
        }else{
            if(e.getKeyCode() == KeyEvent.VK_A) return new PlayerNameScreen(12);
            else if(e.getKeyCode() == KeyEvent.VK_B) System.exit(0);
        }

        gameExists = FileHandler.gameSaveExists();
        saveVersion = FileHandler.gameVersion();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        renderer.writeCenter("Roguelikedev Tutorial Follow Along Week 8", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3, Colors.GRAY);
        renderer.writeCenter(Engine.instance().version(), Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3 + 1, Colors.darken(Colors.GRAY, 0.8f));
        
        if(gameExists) {
            renderer.write("[a] Continue", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2);
            renderer.write("[b] New Game", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 2);
            renderer.write("[c] Quit", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 3);
        }else{
            renderer.write("[a] New Game", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2);
            renderer.write("[b] Quit", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 1);
        }
        
        if(!Engine.instance().version().equals(saveVersion) && saveVersion != null){
            renderer.writeCenter("Game version doesn't match save file version (" + saveVersion + ")", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 3, Colors.GRAY);
            renderer.writeCenter("Loaded game might not be stable.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 2, Colors.GRAY);
        }
    }
}
