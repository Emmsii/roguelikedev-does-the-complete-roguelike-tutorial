package com.mac.rltut.game.entity.item;

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
    
    public static EquipmentSlot[] combine(EquipmentSlot[] ... toCombine){
        int length = 0;
        for(EquipmentSlot[] slots : toCombine) length += slots.length;
        
        EquipmentSlot[] result = new EquipmentSlot[length];
        
        int offset = 0;
        for(EquipmentSlot[] slots : toCombine){
            System.arraycopy(slots, 0, result, offset, slots.length);
            offset += slots.length;
        }
        
        return result;
       
    }
    
    EquipmentSlot() {}
}
