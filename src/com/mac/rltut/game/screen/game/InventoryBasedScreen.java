package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 04:33 PM.
 */
public abstract class InventoryBasedScreen extends Screen{
    
    protected Creature player;
    
    public InventoryBasedScreen(int x, int y, int w, int h, String title, Creature player){
        super(x, y, w, h, title);
        this.player = player;
    }

    protected abstract String getVerb();
    protected abstract boolean isAcceptable(Item item);
    protected abstract Screen use(Item item);

    @Override
    public Screen input(KeyEvent key) {
        char index = key.getKeyChar();
        List<Item> list = getList();
        if(letters.indexOf(index) > -1 && list.size() > letters.indexOf(index) && isAcceptable(list.get(letters.indexOf(index)))) return use(list.get(letters.indexOf(index)));
        else if(key.getKeyCode() == KeyEvent.VK_ESCAPE) return null;
        else return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        int xp = this.x + 3;
        int yp = this.y + 3;
        
        List<Item> lines = getList();
        renderer.writeCenter("What do you want to " + getVerb() + "?", this.x + (width / 2), yp++);
        
        yp += 2;
        for(int i = 0; i < lines.size(); i++){
            Item item = lines.get(i);
            yp++;
            drawItem(letters.charAt(i), item, xp, yp, renderer);
        }
    }
    
    private void drawItem(char index, Item item, int xp, int yp, Renderer renderer){
        renderer.write("[" + index + "]", xp, yp);
        renderer.renderSprite(item.sprite(), xp + 4, yp);
        renderer.write(item.name(), xp + 6, yp);
    }
    
    private List<Item> getList(){
        List<Item> newItems = new ArrayList<Item>();
        List<Item> items = player.inventory().items();
        for(Item i : items) if(isAcceptable(i)) newItems.add(i);
        return newItems;
    }
}