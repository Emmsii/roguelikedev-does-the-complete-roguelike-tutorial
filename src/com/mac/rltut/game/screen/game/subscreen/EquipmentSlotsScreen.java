package com.mac.rltut.game.screen.game.subscreen;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.graphics.Sprite;
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
        
        int xp = (width / 2);

        Equippable weapon = player.getEquippedAt(EquipmentSlot.WEAPON);
        Equippable head = player.getEquippedAt(EquipmentSlot.HEAD);
        Equippable necklace = player.getEquippedAt(EquipmentSlot.NECKLACE);
        Equippable chest = player.getEquippedAt(EquipmentSlot.CHEST);
        Equippable hands = player.getEquippedAt(EquipmentSlot.HANDS);
        Equippable ring = player.getEquippedAt(EquipmentSlot.RING);
        Equippable legs = player.getEquippedAt(EquipmentSlot.LEGS);
        Equippable feet = player.getEquippedAt(EquipmentSlot.FEET);
        Equippable shield= player.getEquippedAt(EquipmentSlot.SHIELD);
        
        renderBox(xp - 1, 3, 3, 3, false, renderer);
        if(head != null) renderer.renderSprite(head.sprite(), this.x + xp, this.y + 4);
        else {
            renderer.renderSprite(Sprite.get("helm_large"), this.x + xp, this.y + 4);
            renderer.colorizeSprite(this.x + xp, this.y + 4, 0x2E2E2E, 1f);
        }

        renderBox(xp - 1, 8, 3, 3, false, renderer);
        if(chest != null) renderer.renderSprite(chest.sprite(), this.x + xp, this.y + 9);
        else {
            renderer.renderSprite(Sprite.get("chest_plate"), this.x + xp, this.y + 9);
            renderer.colorizeSprite(this.x + xp, this.y + 9, 0x2E2E2E, 1f);
        }

        renderBox(xp - 5, 6, 3, 3, false, renderer);
        if(necklace != null) renderer.renderSprite(necklace.sprite(), this.x + xp - 4, this.y + 7);
        else {
            renderer.renderSprite(Sprite.get("necklace"), this.x + xp - 4, this.y + 7);
            renderer.colorizeSprite(this.x + xp - 4, this.y + 7, 0x2E2E2E, 1f);
        }

        renderBox(xp - 7, 14, 3, 3, false, renderer);
        if(hands != null) renderer.renderSprite(hands.sprite(), this.x + xp - 6, this.y + 15);
        else {
            renderer.renderSprite(Sprite.get("glove_silver"), this.x + xp - 6, this.y + 15);
            renderer.colorizeSprite(this.x + xp - 6, this.y + 15, 0x2E2E2E, 1f);
        }
        
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
        
        renderBox(xp + 5, 10, 3, 3, false, renderer);
        if(shield != null) renderer.renderSprite(shield.sprite(), this.x + xp + 6, this.y + 11);
        else {
            renderer.renderSprite(Sprite.get("shield_kite"), this.x + xp + 6, this.y + 11);
            renderer.colorizeSprite(this.x + xp + 6, this.y + 11, 0x2E2E2E, 1f);
        }

        renderBox(xp - 1, 13, 3, 3, false, renderer);
        if(legs != null) renderer.renderSprite(legs.sprite(), this.x + xp, this.y + 14);
        else {
            renderer.renderSprite(Sprite.get("platelegs"), this.x + xp, this.y + 14);
            renderer.colorizeSprite(this.x + xp, this.y + 14, 0x2E2E2E, 1f);
        }

        renderBox(xp - 1, 18, 3, 3, false, renderer);
        if(feet != null) renderer.renderSprite(feet.sprite(), this.x + xp, this.y + 19);
        else {
            renderer.renderSprite(Sprite.get("boots_silver"), this.x + xp, this.y + 19);
            renderer.colorizeSprite(this.x + xp, this.y + 19, 0x2E2E2E, 1f);
        }
    }
}