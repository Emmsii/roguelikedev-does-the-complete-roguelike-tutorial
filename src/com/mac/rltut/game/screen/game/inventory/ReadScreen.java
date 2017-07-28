package com.mac.rltut.game.screen.game.inventory;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.Spellbook;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:13 AM.
 */
public class ReadScreen extends InventoryBasedScreen{
    
    public ReadScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Creature player) {
        super(x, y, w, h, title, inventory, player);
    }

    @Override
    protected String getVerb() {
        return "read";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item instanceof Spellbook;
    }

    @Override
    protected Screen use(Item item) {
        return new ApplySpellbookScreen(x, y, width, height, null, inventory, player, (Spellbook) item);
    }
}
