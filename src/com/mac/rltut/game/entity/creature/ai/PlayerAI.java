package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.game.MessageLog;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.creature.Player;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 17/07/2017 at 09:07 AM.
 */
public class PlayerAI extends CreatureAI{
    
    private MessageLog log;

    protected PlayerAI() {}
    
    public PlayerAI(Creature creature, MessageLog log) {
        super(creature);
        this.log = log;
    }

    @Override
    public void notify(ColoredString message) {
        log.add(message);
    }

    @Override
    public boolean onMove(int xp, int yp, int zp) {
        boolean moved = super.onMove(xp, yp, zp);
        if(moved) ((Player) creature).stats().incrementValue("tiles_traveled");
        return moved;
    }

    @Override
    public void onGainLevel() {
        
    }
}
