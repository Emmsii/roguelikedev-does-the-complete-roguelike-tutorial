package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.Spellbook;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.subscreen.ReadSpellScreen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:13 AM.
 */
public class ReadScreen extends InventoryBasedScreen{
    
    private int sx, sy;
    
    public ReadScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Player player, int sx, int sy) {
        super(x, y, w, h, title, inventory, player, null);
        this.sx = sx;
        this.sy = sy;
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
        Spellbook book = (Spellbook) item;
        return new ReadSpellScreen(x, y, width, height, player, book, sx, sy);
    }
}
