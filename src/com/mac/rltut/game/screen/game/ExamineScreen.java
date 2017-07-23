package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Inventory;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.screen.Screen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 23/07/2017 at 10:42 AM.
 */
public class ExamineScreen extends InventoryBasedScreen{
    
    public ExamineScreen(int x, int y, int w, int h, String title, Inventory<Item> inventory, Creature player) {
        super(x, y, w, h, title, inventory, player);
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
        player.notify(new ColoredString("It's " + article + item.name() + "."));
        return null;
    }
}
