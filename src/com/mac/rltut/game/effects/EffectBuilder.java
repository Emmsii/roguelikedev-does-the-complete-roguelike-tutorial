package com.mac.rltut.game.effects;

import com.mac.rltut.engine.util.Pool;

import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 29/07/2017 at 02:08 PM.
 */
public class EffectBuilder {
    
    public EffectBuilder() {}
    
    public static Effect randomItemEffect(int z, Random random){
        Pool<Effect> pool = new Pool<Effect>(random);
        pool.add(new Leach(5 + ((z / 3) * 5), 0.1f), 30);
        pool.add(new Poison(1 + ((z / 2) + 1), 5 + ((z / 3) * 2), 0.75f), 100);
        pool.add(new Blind(10 + (z / 4) * 2, 0.5f), 50);
        return pool.get();
    }
    
    public static Effect randomPotionEffect(int z, Random random){
        Pool<Effect> pool = new Pool<Effect>(random);
        
        pool.add(new Heal(10 + (z / 2) * 2, 1f), 95);
        pool.add(new HealthRegen(2 + (z / 3) + 1, 5 + (z / 3) + 1, 1f), 100);
        pool.add(new ManaRegen(2 + z, 10 + (z / 3) + 1, 1f), 80);
        pool.add(new Rage(3 + z, 15 + (z / 5) * 5, 1f), 25);
        pool.add(new NightVision(16, 20 + (z / 4) * 4, 1f), 40);
        
        return pool.get();
    }
}
