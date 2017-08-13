package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;
import javafx.print.PageLayout;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 12:25 PM.
 */
public class DropScreen extends InventoryBasedScreen{
    
    public DropScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Player player) {
        super(x, y, w, h, title, inventory, player, null);
    }

    @Override
    protected String getVerb() {
        return "drop";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        player.drop(item);
        return this;
    }
}
