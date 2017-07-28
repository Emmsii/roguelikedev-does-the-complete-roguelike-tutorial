package com.mac.rltut.game.screen.game.inventory;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.objects.Chest;

import java.awt.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 04:33 PM.
 */
public class ChestScreen extends InventoryBasedScreen{
    
    public ChestScreen(int x, int y, int w, int h, String title, Chest chest, Creature player) {
        super(x, y, w, h, title, chest.inventory(), player);
    }

    @Override
    protected String getVerb() {
        return "take";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        if(player.inventory().isFull()){
            player.notify(new ColoredString("You are carrying too much.", Color.ORANGE.getRGB()));
            return this;
        }else{
            inventory.remove(item);
            player.inventory().add(item);
        }
        return null;
    }
}
