package com.mac.rltut.game.world.levels;

import com.mac.rltut.game.world.builders.ForestLevelBuilder;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 09:42 AM.
 */
public class DefaultLevel extends ForestLevelBuilder {

    public DefaultLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, float creatureSpawnMultiplier, int visibilityModifier, Random random) {
        super("Default", width, height, minLevel, maxLevel, chance, zMultiplier, creatureSpawnMultiplier, visibilityModifier, random);
    }

}
