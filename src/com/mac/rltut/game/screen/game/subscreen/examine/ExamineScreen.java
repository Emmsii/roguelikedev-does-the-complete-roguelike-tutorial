package com.mac.rltut.game.screen.game.subscreen.examine;

import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.subscreen.inventory.InventoryBasedScreen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 23/07/2017 at 10:42 AM.
 */
public class ExamineScreen extends InventoryBasedScreen {
    
    public ExamineScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Player player) {
        super(x, y, w, h, title, inventory, player, null);
    }

    @Override
    protected String getVerb() {
        return "examine";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        String article = "aeiou".contains(item.name().subSequence(0, 1)) ? "an " : "a ";
        return new ExamineItemScreen(x, y, width, height, item, this);
    }
}
