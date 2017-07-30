package com.mac.rltut.game.entity.util;

import com.mac.rltut.game.entity.item.Item;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/07/2017 at 05:09 PM.
 */
public class ItemSpawnProperty extends EntitySpawnProperty{
    
    public ItemSpawnProperty(Item item, String spawnLevels, String spawnTypes, int chance, float depthMultiplier) {
        super(item, spawnLevels, spawnTypes, chance, depthMultiplier);
    }
}
