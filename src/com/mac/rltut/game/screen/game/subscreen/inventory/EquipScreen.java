package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 04:40 PM.
 */
public class EquipScreen extends InventoryBasedScreen {
    
    public EquipScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Player player) {
        super(x, y, w, h, title, inventory, player, null);
    }

    @Override
    protected String getVerb() {
        return "equip";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item instanceof Equippable;
    }

    @Override
    protected Screen use(Item item) {
        player.equip(item);
        return this;
    }
}
