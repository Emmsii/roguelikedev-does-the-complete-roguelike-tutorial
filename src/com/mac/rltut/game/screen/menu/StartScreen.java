package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.FileHandler;
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
        saveVersion = FileHandler.saveGameVersion();
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
            else if(e.getKeyCode() == KeyEvent.VK_C) return new HighscoresScreen();
            else if(e.getKeyCode() == KeyEvent.VK_D) return new AboutScreen();
            else if(e.getKeyCode() == KeyEvent.VK_E) return new OptionsScreen();
            else if(e.getKeyCode() == KeyEvent.VK_F) System.exit(0);
        }else{
            if(e.getKeyCode() == KeyEvent.VK_A) return new PlayerNameScreen(12);
            else if(e.getKeyCode() == KeyEvent.VK_B) return new HighscoresScreen();
            else if(e.getKeyCode() == KeyEvent.VK_C) return new AboutScreen();
            else if(e.getKeyCode() == KeyEvent.VK_D) return new OptionsScreen();
            else if(e.getKeyCode() == KeyEvent.VK_E) System.exit(0);
        }
        
        gameExists = FileHandler.gameSaveExists();
        saveVersion = FileHandler.saveGameVersion();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);

        renderer.writeCenter(Engine.instance().title(), Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 3, Colors.WHITE);
        renderer.write(Engine.instance().version(), 1, Engine.instance().heightInTiles() - 2, Colors.darken(Colors.GRAY, 0.8f));
        
        if(gameExists) {
            renderer.write("[a] Continue", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2);
            renderer.write("[b] New Game", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 1);
            renderer.write("[c] Highscores", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 3);
            renderer.write("[d] About", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 4);
            renderer.write("[e] Options", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 5);
            renderer.write("[f] Quit", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 6);
        }else{
            renderer.write("[a] New Game", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2);
            renderer.write("[b] Highscores", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 2);
            renderer.write("[c] About", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 3);
            renderer.write("[d] Options", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 4);
            renderer.write("[e] Quit", Engine.instance().widthInTiles() / 2 - 6, Engine.instance().heightInTiles() / 2 + 5);
        }
        
        if(!Engine.instance().version().equals(saveVersion) && saveVersion != null){
            renderer.writeCenter("Game version doesn't match save file version (" + saveVersion + ")", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 3, Colors.GRAY);
            renderer.writeCenter("Loaded game might not be stable. Check changelog.txt for details.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 2, Colors.GRAY);
        }
    }
}
