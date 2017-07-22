package com.mac.rltut.game.screen.game;

import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Armor;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.Weapon;
import com.mac.rltut.game.screen.Screen;
import sun.awt.windows.WEmbeddedFrame;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 04:40 PM.
 */
public class EquipScreen extends InventoryBasedScreen{
    
    public EquipScreen(int x, int y, int w, int h, String title, Creature player) {
        super(x, y, w, h, title, player);
    }

    @Override
    protected String getVerb() {
        return "equip";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item instanceof Weapon || item instanceof Armor;
    }

    @Override
    protected Screen use(Item item) {
        //return player.equip(item);
        return null;
    }
}
