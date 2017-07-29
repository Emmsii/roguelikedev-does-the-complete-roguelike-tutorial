package com.mac.rltut.game.entity.item;

import javafx.beans.property.adapter.JavaBeanObjectProperty;

import java.awt.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/07/2017 at 08:29 PM.
 */
public enum EquipmentSlot {
        
    WEAPON, HEAD, CHEST, HANDS, SHIELD, LEGS, FEET, NECKLACE, RING;
    
    public static final EquipmentSlot[] JEWELRY = { NECKLACE, RING };
    public static final EquipmentSlot[] ARMOR = { HEAD, CHEST, HANDS, SHIELD, LEGS, FEET };
    public static final EquipmentSlot[] ALL = { WEAPON, HEAD, CHEST, HANDS, SHIELD, LEGS, FEET, NECKLACE, RING };
    
    public static boolean isArmor(EquipmentSlot slot){
         for(EquipmentSlot s : ARMOR) if(s == slot) return true;
         return false;
    }
    
    public static boolean isJewelry(EquipmentSlot slot){
        for(EquipmentSlot s : JEWELRY) if(s == slot) return true;
        return false;
    }
    
    public static boolean isWeapon(EquipmentSlot slot){
        return slot == WEAPON;
    }
}
