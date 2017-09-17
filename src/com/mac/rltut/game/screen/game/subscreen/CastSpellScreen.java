package com.mac.rltut.game.screen.game.subscreen;

import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.creature.Player;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 17/09/2017 at 01:16 PM.
 */
public class CastSpellScreen extends TargetBasedScreen{
    
    private Spell spell;
    
    public CastSpellScreen(int x, int y, int w, int h, Player player, int sx, int sy, boolean closeOnSelect, Spell spell) {
        super(x, y, w, h, player, sx, sy, closeOnSelect);
        this.spell = spell;
    }

    @Override
    public void selectWorldCoordinate(int xa, int ya, int screenX, int screenY) {
        player.castSpell(spell, xa, ya);
    }
}
