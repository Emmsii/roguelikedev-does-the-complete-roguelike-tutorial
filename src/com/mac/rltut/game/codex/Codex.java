package com.mac.rltut.game.codex;

import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.util.CreatureSpawnProperty;
import com.mac.rltut.game.entity.util.ItemSpawnProperty;
import com.mac.rltut.game.world.builders.LevelBuilder;

import java.util.HashMap;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 11/07/2017 at 03:25 PM.
 */
public class Codex {
    
    public static HashMap<String, LevelBuilder> levelBuilders = new HashMap<String, LevelBuilder>();
    public static HashMap<String, CreatureSpawnProperty> creatures = new HashMap<String, CreatureSpawnProperty>();
    public static HashMap<String, ItemSpawnProperty> items = new HashMap<String, ItemSpawnProperty>();
    public static HashMap<String, Spell> spells = new HashMap<String, Spell>();
    
}
