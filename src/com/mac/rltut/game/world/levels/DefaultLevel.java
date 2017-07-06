package com.mac.rltut.game.world.levels;

import com.mac.rltut.game.world.builders.ForrestLevelBuilder;
import com.mac.rltut.game.world.builders.LevelBuilder;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 30/06/2017 at 09:42 AM.
 */
public class DefaultLevel extends ForrestLevelBuilder {

    public DefaultLevel(int width, int height, int minLevel, int maxLevel, int chance, float zMultiplier, Random random) {
        super("Default", width, height, minLevel, maxLevel, chance, zMultiplier, random);
    }

}