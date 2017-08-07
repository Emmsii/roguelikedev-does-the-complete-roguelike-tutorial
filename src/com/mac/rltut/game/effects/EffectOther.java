package com.mac.rltut.game.effects;

import com.mac.rltut.game.entity.creature.Creature;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 01/08/2017 at 10:24 AM.
 */
public class EffectOther extends Effect{
    
    private Effect effectWith;

    protected EffectOther(){}
    
    public EffectOther(Effect effectWith){
        this.effectWith = effectWith;
    }

    @Override
    public void onUseOther(Creature other) {
        other.addEffect(effectWith);
    }
}
