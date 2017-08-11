package com.mac.rltut.game.screen.game.subscreen;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 04:47 PM.
 */
public class EquipmentStatsScreen extends Screen {
    
    private Creature player;
    
    public EquipmentStatsScreen(int x, int y, int width, int height, String title, Creature player){
        super(x, y, width, height, title);
        this.player = player;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return null;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);

        int xp = this.x + 2;
        int yp = this.y + 3;

        String str = "STR";
        String def = "DEF";
        String acc = "ACC";
        String intel = "INT";

        String manaRegAmount = String.format("+%d mana every ", player.manaRegenAmount()) + (player.getManaRegenSpeed() > 1 ? player.getManaRegenSpeed() + " turns" : "turn");
        
        str += (player.strengthBonus() > 0 ? " +" + player.strengthBonus() : " +" + player.strengthBonus());
        def += (player.defenseBonus() > 0 ? " +" + player.defenseBonus() : " +" + player.defenseBonus());
        acc += (player.accuracyBonus() > 0 ? " +" + player.accuracyBonus() : " +" + player.accuracyBonus());
        intel += (player.intelligenceBonus() > 0 ? " +" + player.intelligenceBonus() : " +" + player.intelligenceBonus());

        renderer.writeCenter(str, this.x + (width / 4) + 1, yp++);
        renderer.writeCenter(def, this.x + (width / 4) + 1, yp++);
        yp = this.y + 3;
        
        renderer.writeCenter(acc, this.x + ((width / 4) * 3) - 1, yp++);
        renderer.writeCenter(intel, this.x + ((width / 4) * 3) - 1, yp++);
        yp++;
        
        if(player.manaRegenAmount() != 0) renderer.writeCenter(manaRegAmount, this.x + (width / 2), yp++);
        yp++;
        
        Equippable weapon = player.getEquippedAt(EquipmentSlot.WEAPON); 
        if(weapon != null){
            renderer.write(StringUtil.capitalizeEachWord(weapon.name()), xp, yp++);
            if(weapon.damage() != null) renderer.write("Melee: " + weapon.damage(), xp + 1, yp++);
            if(weapon.rangedDamage() != null) renderer.write("Ranged: " + weapon.rangedDamage(), xp + 1, yp++);
        }
    }
}
