package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 24/07/2017 at 09:45 AM.
 */
public class ExamineItemScreen extends Screen {
    
    protected Item item;
    protected Screen previous;
    
    public ExamineItemScreen(int x, int y, int w, int h, Item item, Screen previous){
        super(x, y, w, h, "Examine");
        this.item = item;
        this.previous = previous;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return previous;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
    }
}
