package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.creature.stats.LevelUpController;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 04:08 PM.
 */
public class LevelUpScreen extends Screen{
    
    private LevelUpController controller;
    
    private Player player;
    private int picks;
    
    public LevelUpScreen(int x, int y, int w, int h, Player player, int picks){
        super(x, y, w, h, "Level up");
        this.player = player;
        this.picks = picks;
        this.controller = new LevelUpController();
    }
        
    @Override
    public Screen input(KeyEvent e) {
        List<String> options = controller.levelUpOptions();
        String chars = "";
        
        for(int i = 0; i < options.size(); i++) chars = chars + Integer.toString(i + 1);
        int i = chars.indexOf(e.getKeyChar());
        if(i < 0) return this;
        
        controller.levelUpOption(options.get(i)).invoke(player);
        if(--picks < 1) return null;
        else return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        List<String> options = controller.levelUpOptions();
        
        int xp = this.x + 3; 
        int yp = this.y + 3;
        
        for(int i = 0; i < options.size(); i++) renderer.write(String.format("[%d] %s", i + 1, options.get(i)), xp, yp++);
    }
}
