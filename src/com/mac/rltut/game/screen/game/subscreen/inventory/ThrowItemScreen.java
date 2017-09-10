package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.util.Inventory;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.LevelScreen;
import com.mac.rltut.game.screen.game.subscreen.ThrowTargetScreen;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 13/08/2017 at 08:34 AM.
 */
public class ThrowItemScreen extends InventoryBasedScreen{
    
    private int sx, sy;
    private LevelScreen levelScreen;    
    
    public ThrowItemScreen(int x, int y, int w, int h, int sx, int sy, Inventory<Item> inventory, Player player, LevelScreen levelScreen) {
        super(x, y, w, h, "", inventory, player, null);
        this.sx = sx;
        this.sy = sy;
        this.levelScreen = levelScreen;
    }

    @Override
    protected String getVerb() {
        return "throw";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        return new ThrowTargetScreen(levelScreen.x(), levelScreen.y(), levelScreen.width(), levelScreen.height(), player, sx, sy, item);
    }
}
