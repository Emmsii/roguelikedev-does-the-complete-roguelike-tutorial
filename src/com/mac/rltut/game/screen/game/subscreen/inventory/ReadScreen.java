package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Player;
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
    
    public ReadScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Player player) {
        super(x, y, w, h, title, inventory, player, null);
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
        if(player.mana() < book.manaCost()){
            player.notify(new ColoredString("Not enough mana, need [" + book.manaCost() + "] mana to use this book.", Colors.RED));
            return null;
        }
        
//        return new BlessScreen(x, y, width, height, null, inventory, player,  this);
        return null;
    }
}
