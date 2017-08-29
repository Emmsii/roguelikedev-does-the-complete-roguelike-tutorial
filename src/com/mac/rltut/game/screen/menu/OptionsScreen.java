package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 23/08/2017 at 11:02 AM.
 */
public class OptionsScreen extends Screen{
    
    private int maxMonitors;
    
    public OptionsScreen(){
        this.maxMonitors = Engine.instance().getMonitorDevices() != null ? Engine.instance().getMonitorDevices().length : 1; 
    }
        
    @Override
    public Screen input(KeyEvent key) {
        
        if(key.getKeyCode() == KeyEvent.VK_A) Config.fullscreen = !Config.fullscreen;
        else if(key.getKeyCode() == KeyEvent.VK_B) {
            Config.monitor += 1;
            if(Config.monitor >= maxMonitors) Config.monitor = 0;
        }else if(key.getKeyCode() == KeyEvent.VK_C) Config.autoEquip = !Config.autoEquip;
        else if(key.getKeyCode() == KeyEvent.VK_D) Config.blood = !Config.blood;
        else if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
            Config.save();
            return new StartScreen();
        }
        
        return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        renderer.writeCenter("Options", Engine.instance().widthInTiles() / 2, 4);
        
        int xp = 5;
        int yp = 10;
        
        renderer.write("[a] Fullscreen: " + Config.fullscreen, xp, yp++);
        renderer.write("Toggle fullscreen mode. Will apply on restart", xp, yp++, Colors.GRAY);
        yp++;
        
        renderer.write("[b] Monitor: " + Config.monitor, xp, yp++);
        renderer.write("Choose the monitor to fullscreen mode into.", xp, yp++, Colors.GRAY);
        yp++;
        
        renderer.write("[c] Auto Equip: " + Config.autoEquip, xp, yp++);
        renderer.write("Auto equip items when picked up.", xp, yp++, Colors.GRAY);
        yp++;
            
        renderer.write("[d] Blood: " + Config.blood, xp, yp++);
        renderer.write("Toggle rendering blood.", xp, yp++, Colors.GRAY);
        
        renderer.writeCenter("Press [ENTER] to save and return.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }
    
}
