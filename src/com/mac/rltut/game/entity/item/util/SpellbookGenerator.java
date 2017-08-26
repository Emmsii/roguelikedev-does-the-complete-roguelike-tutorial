package com.mac.rltut.game.entity.item.util;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.effects.EffectBuilder;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Spellbook;

import java.util.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/07/2017 at 05:18 PM.
 */
public class SpellbookGenerator {
    
    static abstract class BonusModifier{
        
        public String id;
        public String bookName;
        public String description;
        public Sprite sprite;
        public float multiplier;
        public EquipmentSlot[] slots;
        
        public BonusModifier(String id, String bookName, String description, Sprite sprite, float multiplier, EquipmentSlot ... slots){
            this.id = id;
            this.bookName = bookName;
            this.description = description;
            this.sprite = sprite;
            this.multiplier = multiplier;
            this.slots = slots;
        }
        
        public abstract void apply(Spellbook book, int value);
    }
    
    private static final BonusModifier[] MODIFIERS = {
            new BonusModifier("str", "strength", "strength", Sprite.get("spell_book_2"), 1f, EquipmentSlot.WEAPON){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setStrengthBonus(value);
                }
            },
            new BonusModifier("def", "defense", "defense", Sprite.get("spell_book_2"), 1f, EquipmentSlot.ARMOR){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setDefenseBonus(value);
                }
            },
            new BonusModifier("acc", "accuracy", "accuracy", Sprite.get("spell_book_4"), 1f, EquipmentSlot.ALL){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setAccuracyBonus(value);
                }
            },
            new BonusModifier("int", "intelligence", "intelligence", Sprite.get("spell_book_1"), 1f, EquipmentSlot.JEWELRY){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setIntelligenceBonus(value);
                }
            },
            new BonusModifier("manaA", "mana", "mana regen", Sprite.get("spell_book_3"), 0.8f, EquipmentSlot.combine(EquipmentSlot.ARMOR, EquipmentSlot.JEWELRY)){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setManaRegenAmountBonus(value);
                }
            },
            new BonusModifier("manaS", "mana", "mana regen speed", Sprite.get("spell_book_3"), 0.675f, EquipmentSlot.combine(EquipmentSlot.ARMOR, EquipmentSlot.JEWELRY)){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setManaRegenSpeedBonus(-value);
                }
            }
    };
    
    public static Spellbook generate(int z, Random random){
        BonusModifier chosenSkill = MODIFIERS[random.nextInt(MODIFIERS.length)];
        int bonusCount = (int) (1 + (z / 3) + Math.floor(((Math.random() + Math.random()) - 1.8) * 2));
        if(bonusCount < 1) bonusCount = 1;
        if(bonusCount >= MODIFIERS.length - 1) bonusCount = MODIFIERS.length - 1;

        Spellbook book = new Spellbook(null, null, chosenSkill.sprite, chosenSkill.slots);
        int bonusValue = (int) (((z / 2) + (Math.floor(Math.random() * 3))) * 0.75f) + 1;
        chosenSkill.apply(book, (int) Math.ceil(bonusValue * chosenSkill.multiplier));
        List<BonusModifier> chosen = new ArrayList<BonusModifier>();
        
        if(bonusCount != 1){
            List<BonusModifier> toChoose = Arrays.asList(MODIFIERS);
            Collections.shuffle(toChoose);

            for(int i = 0; i < bonusCount; i++){
                BonusModifier mod = toChoose.get(i);
                if(mod.id.equals(chosenSkill.id)) continue;
                int newBonusValue = (int) (((z / 2) + (Math.floor(Math.random() * 3))) * 0.75f) + 1;
                
                mod.apply(book, (int) (Math.ceil((newBonusValue * chosenSkill.multiplier) / 2)));
                chosen.add(mod);
            }
        }
        
        int manaCost = 0;
        
        manaCost += (book.strengthBonus() / 2) + 1;
        manaCost += (book.defenseBonus() / 2) + 1;
        manaCost += (book.accuracyBonus() / 2) + 1;
        manaCost += (book.intelligenceBonus() / 2) + 1;
        manaCost += (book.manaRegenAmountBonus() / 2) + 1;
        manaCost += ((book.manaRegenSpeedBonus() * -1) / 2) + 1;
        
        manaCost *= 10 + (z / 5);
        book.setManaCost(manaCost);
        
        String rarity = "";
        if(bonusCount < 2) rarity = "common";
        else if(bonusCount <= 3) rarity = "uncommon";
        else if(bonusCount <= 5) rarity = "rare";

        if(bonusCount > 3 && random.nextFloat() <= 0.75) {
            for (EquipmentSlot slots : chosenSkill.slots) {
                if (slots == EquipmentSlot.WEAPON) {
                    book.setEffect(EffectBuilder.randomWeaponEffect(z, random));
                    break;
                }
            }
        }
                        
        book.setName(rarity + " spellbook of " + chosenSkill.bookName);
        book.setDescription(generateDescription(chosenSkill, rarity, chosen));
        return book;
    }
    
    private static String generateDescription(BonusModifier chosenSkill, String rarity, List<BonusModifier> chosen){
        StringBuilder builder = new StringBuilder();
        builder.append("The " + rarity + " book grants a bonus towards ").append(chosenSkill.description);
        
        if(chosen.isEmpty()) builder.append(".");
        else{
            builder.append(" as well as ");
            for(int i = 0; i < chosen.size(); i++){
                BonusModifier bonus = chosen.get(i);
                if(i == chosen.size() - 1 && chosen.size() != 1) builder.append(" and ");
                builder.append(bonus.description);
                if(i == chosen.size() - 1) builder.append(".");
                else if(i != chosen.size() - 2) builder.append(", "); 
            }
        }
        
        return builder.toString().trim();
    }

}
