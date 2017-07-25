package com.mac.rltut.game.screen.game;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.Spellbook;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 11:15 AM.
 */
public class ApplySpellbookScreen extends InventoryBasedScreen{
    
    private Spellbook spellbook;
    
    public ApplySpellbookScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Creature player, Spellbook spellbook) {
        super(x, y, w, h, title, inventory, player);
        this.spellbook = spellbook;
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
        spellbook.bless((Equippable) item);
        return null;
    }
}
