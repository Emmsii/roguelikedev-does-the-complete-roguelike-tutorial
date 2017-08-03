package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.FileHandler;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 02/08/2017 at 08:59 PM.
 */
public class ConfirmScreen extends Screen{
    
    private String message;
    private Screen yes, no;
    
    public ConfirmScreen(String message, Screen yes, Screen no){
        super(0, 0, Engine.instance().widthInTiles(), Engine.instance().heightInTiles(), "");
        this.message = message;
        this.yes = yes;
        this.no = no;
    }
    
    public Screen onYes(){
        return yes;
    }
    
    public Screen onNo(){
        return no;
    }
    
    @Override
    public Screen input(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_ENTER) return onYes();
        else if(key.getKeyCode() == KeyEvent.VK_ESCAPE) return onNo();
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        renderer.writeCenter(message, Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() / 2 - 3);
        renderer.writeCenter("Press [ENTER] to continue or [ESCAPE] to return.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
}
