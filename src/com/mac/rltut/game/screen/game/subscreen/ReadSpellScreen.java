package com.mac.rltut.game.screen.game.subscreen;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.effects.spells.Spell;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Spellbook;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.subscreen.target.CastSpellScreen;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 17/09/2017 at 01:19 PM.
 */
public class ReadSpellScreen extends Screen{
    
    private Player player;
    private Spellbook spellbook;
    
    private int sx, sy;
    
    public ReadSpellScreen(int x, int y, int w, int h, Player player, Spellbook spellbook, int sx, int sy){
        super(x, y, w, h, null);
        this.player = player;
        this.spellbook = spellbook;
        this.sx = sx;
        this.sy = sy;
    }
    
    @Override
    public Screen input(KeyEvent key) {
        char c = key.getKeyChar();
        
        List<Spell> spells = spellbook.writtenSpells();
        if(letters.indexOf(c) > -1 && spells.size() > letters.indexOf(c) && spells.get(letters.indexOf(c)) != null) return use(spells.get(letters.indexOf(c)));
        else if(key.getKeyCode() == KeyEvent.VK_ESCAPE) return null;
        else return this;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        renderer.writeCenter("What spell do you want to cast?", this.x + (width / 2), this.y + 3);

        List<Spell> spells = spellbook.writtenSpells();
        
        int xp = this.x + 2;
        int yp = this.y + 7;
        
        for(int i = 0; i < spells.size(); i++){
            Spell s = spells.get(i);
            renderer.write("[" + letters.charAt(i) + "] " + StringUtil.capitalizeFirst(s.name()) + " (" + s.manaCost() + ")", xp, yp++);
        }
        
    }
    
    protected Screen use(Spell spell){
        return new CastSpellScreen(x, y, width, height, player, sx, sy, true, spell);
    }
}
