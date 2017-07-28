package com.mac.rltut.game.screen.game.inventory;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Consumeable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 10:48 AM.
 */
public class ConsumeScreen extends InventoryBasedScreen{
    
    public ConsumeScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Creature player) {
        super(x, y, w, h, title, inventory, player);
    }

    @Override
    protected String getVerb() {
        return "consume";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item instanceof Consumeable;
    }

    @Override
    protected Screen use(Item item) {
        ((Consumeable) item).consume(player);
        return null;
    }
}
