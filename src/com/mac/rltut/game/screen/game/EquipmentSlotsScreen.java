package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.Ammo;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 04:47 PM.
 */
public class EquipmentSlotsScreen extends Screen {
    
    private Creature player;
    
    public EquipmentSlotsScreen(int x, int y, int width, int height, String title, Creature player){
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
        
        int xp = (width / 2) - 1;
        

        Equippable weapon = player.getEquippedAt(EquipmentSlot.WEAPON);
        Equippable head = player.getEquippedAt(EquipmentSlot.HEAD);
        Equippable necklace = player.getEquippedAt(EquipmentSlot.NECKLACE);
        Equippable chest = player.getEquippedAt(EquipmentSlot.CHEST);
        Equippable hands = player.getEquippedAt(EquipmentSlot.HANDS);
        Equippable ring = player.getEquippedAt(EquipmentSlot.RING);
        Equippable legs = player.getEquippedAt(EquipmentSlot.LEGS);
        Equippable feet = player.getEquippedAt(EquipmentSlot.FEET);
        Equippable shield= player.getEquippedAt(EquipmentSlot.SHIELD);

        Ammo ammo = player.ammo();

        renderBox(2, 2, 3, 3, false, renderer);
        if(ammo != null){
            renderer.renderSprite(ammo.sprite(), this.x + 3, this.y + 3);
            renderer.write("x" + ammo.amount(), this.x + 5, this.y + 3);
        } else{
            renderer.renderSprite(Sprite.get("arrow"), this.x + 3, this.y + 3);
            renderer.colorizeSprite(this.x + 3, this.y + 3, 0x2E2E2E, 1f);
        }
        
        renderBox(xp - 1, 3, 3, 3, false, renderer);
        if(head != null) renderer.renderSprite(head.sprite(), this.x + xp, this.y + 4);
         else {
            renderer.renderSprite(Sprite.get("helm_large"), this.x + xp, this.y + 4);
            renderer.colorizeSprite(this.x + xp, this.y + 4, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp, this.y + 6);
        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp, this.y + 7);
        
        renderBox(xp - 1, 8, 3, 3, false, renderer);
        if(chest != null) renderer.renderSprite(chest.sprite(), this.x + xp, this.y + 9);
        else {
            renderer.renderSprite(Sprite.get("chest_plate"), this.x + xp, this.y + 9);
            renderer.colorizeSprite(this.x + xp, this.y + 9, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp, this.y + 11);
        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp, this.y + 12);

        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp - 2, this.y + 4);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp - 3, this.y + 4);
        renderer.renderSprite(Sprite.get("ui_border_tl"), this.x + xp - 4, this.y + 4);
        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp - 4, this.y + 5);
        
        renderBox(xp - 5, 6, 3, 3, false, renderer);
        if(necklace != null) renderer.renderSprite(necklace.sprite(), this.x + xp - 4, this.y + 7);
        else {
            renderer.renderSprite(Sprite.get("necklace"), this.x + xp - 4, this.y + 7);
            renderer.colorizeSprite(this.x + xp - 4, this.y + 7, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_tl"), this.x + xp - 6, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp - 5, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp - 4, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp - 3, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp - 2, this.y + 9);

        renderBox(xp - 7, 14, 3, 3, false, renderer);
        if(hands != null) renderer.renderSprite(hands.sprite(), this.x + xp - 6, this.y + 15);
        else {
            renderer.renderSprite(Sprite.get("glove_silver"), this.x + xp - 6, this.y + 15);
            renderer.colorizeSprite(this.x + xp - 6, this.y + 15, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp - 6, this.y + 13);
        
        renderBox(xp + 5, 14, 3, 3, false, renderer);
        if(ring != null) renderer.renderSprite(ring.sprite(), this.x + xp + 6, this.y + 15);
        else {
            renderer.renderSprite(Sprite.get("ring_gold"), this.x + xp + 6, this.y + 15);
            renderer.colorizeSprite(this.x + xp + 6, this.y + 15, 0x2E2E2E, 1f);
        }
        
        renderBox(xp - 7, 10, 3, 3, false, renderer);
        if(weapon != null) renderer.renderSprite(weapon.sprite(), this.x + xp - 6, this.y + 11);
        else{
            renderer.renderSprite(Sprite.get("sword"), this.x + xp - 6, this.y + 11);
            renderer.colorizeSprite(this.x + xp - 6, this.y + 11, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_tr"), this.x + xp + 6, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp + 5, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp + 4, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp + 3, this.y + 9);
        renderer.renderSprite(Sprite.get("ui_border_hor"), this.x + xp + 2, this.y + 9);
        
        renderBox(xp + 5, 10, 3, 3, false, renderer);
        if(shield != null) renderer.renderSprite(shield.sprite(), this.x + xp + 6, this.y + 11);
        else {
            renderer.renderSprite(Sprite.get("shield_kite"), this.x + xp + 6, this.y + 11);
            renderer.colorizeSprite(this.x + xp + 6, this.y + 11, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp + 6 , this.y + 13);
        
        renderBox(xp - 1, 13, 3, 3, false, renderer);
        if(legs != null) renderer.renderSprite(legs.sprite(), this.x + xp, this.y + 14);
        else {
            renderer.renderSprite(Sprite.get("platelegs"), this.x + xp, this.y + 14);
            renderer.colorizeSprite(this.x + xp, this.y + 14, 0x2E2E2E, 1f);
        }

        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp , this.y + 17);
        renderer.renderSprite(Sprite.get("ui_border_ver"), this.x + xp , this.y + 16);
        
        renderBox(xp - 1, 18, 3, 3, false, renderer);
        if(feet != null) renderer.renderSprite(feet.sprite(), this.x + xp, this.y + 19);
        else {
            renderer.renderSprite(Sprite.get("boots_silver"), this.x + xp, this.y + 19);
            renderer.colorizeSprite(this.x + xp, this.y + 19, 0x2E2E2E, 1f);
        }

        xp = this.x + 19;
        int yp = this.y + 3;

        String str = "STR";
        String def = "DEF";
        String acc = "ACC";
        String intel = "INT";

        str += (player.strengthBonus() >= 0 ? " +" + player.strengthBonus() : " -" + player.strengthBonus());
        def += (player.defenseBonus() >= 0 ? " +" + player.defenseBonus() : " -" + player.defenseBonus());
        acc += (player.accuracyBonus() >= 0 ? " +" + player.accuracyBonus() : " -" + player.accuracyBonus());
        intel += (player.intelligenceBonus() >= 0 ? " +" + player.intelligenceBonus() : " -" + player.intelligenceBonus());

        
        renderer.writeCenter(str, xp, yp++, Colors.GRAY);
        renderer.writeCenter(def, xp, yp++, Colors.GRAY);
        renderer.writeCenter(acc, xp, yp++, Colors.GRAY);
        renderer.writeCenter(intel, xp, yp++, Colors.GRAY);

        renderBox(this.width - 10, 2, 9, 6, false, renderer);
        
//        renderer.writeCenter(str, this.x + (width / 4) + 1, yp++);
//        renderer.writeCenter(def, this.x + (width / 4) + 1, yp++);
//        yp = this.y + 20;

//        renderer.writeCenter(acc, this.x + ((width / 4) * 3) - 1, yp++);
//        renderer.writeCenter(intel, this.x + ((width / 4) * 3) - 1, yp++);
//        yp++;

        int manaRegenAmount = player.manaRegenAmount() + player.manaRegenAmountBonus();
        int manaRegenSpeed = player.getManaRegenSpeed() + player.manaRegenSpeedBonus();
        if(manaRegenSpeed < 1) manaRegenSpeed = 1;

        yp = this.y + 25;
        
        String manaRegAmountStr = String.format("+%d mana every ", manaRegenAmount) + (manaRegenSpeed > 1 ? manaRegenSpeed + " turns" : "turn");
        if(manaRegenAmount != 0) renderer.writeCenter(manaRegAmountStr, this.x + (width / 2), yp++);
        yp++;
    }
}
