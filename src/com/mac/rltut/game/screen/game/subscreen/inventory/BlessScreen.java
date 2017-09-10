package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.objects.Shrine;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:15 AM.
 */
public class BlessScreen extends InventoryBasedScreen{
    
    private Shrine shrine;
    
    public BlessScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Player player, Shrine shrine) {
        super(x, y, w, h, title, inventory, player, null);
        this.shrine = shrine;
    }

    @Override
    protected String getVerb() {
        return "bless";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item instanceof Equippable && !item.unique();
    }

    @Override
    protected Screen use(Item item) {
        shrine.bless(item, player, player.z);
        return null;
    }
}
