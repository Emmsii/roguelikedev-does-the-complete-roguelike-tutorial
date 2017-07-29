package com.mac.rltut.game.entity.item.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.game.effects.Effect;
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
            new BonusModifier("manaA", "mana", "mana regen", Sprite.get("spell_book_3"), 0.8f, EquipmentSlot.ARMOR){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setManaRegenAmountBonus(value);
                }
            },
            new BonusModifier("manaS", "mana", "mana regen speed", Sprite.get("spell_book_3"), 0.675f, EquipmentSlot.ARMOR){
                @Override
                public void apply(Spellbook book, int value) {
                    book.setManaRegenSpeedBonus(-value);
                }
            }
    };
    
    public static Spellbook generate(int z, Random random){
        
        //Pick a skill to give bonus in
        //Pick bonus count - 1
        //Chosen skill is regular value
        //Other skills are regular value / 2
        //Pick rarity
        
        
        
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
        
        if(book.strengthBonus() > 0) manaCost += (book.strengthBonus() / 2) + 1;
        if(book.defenseBonus() > 0) manaCost += (book.defenseBonus() / 2) + 1;
        if(book.accuracyBonus() > 0) manaCost += (book.accuracyBonus() / 2) + 1;
        if(book.intelligenceBonus() > 0) manaCost += (book.intelligenceBonus() / 2) + 1;
        if(book.manaRegenAmountBonus() > 0) manaCost += (book.manaRegenAmountBonus() / 2) + 1;
        if(book.manaRegenSpeedBonus() < 0) manaCost += ((book.manaRegenSpeedBonus() * -1) / 2) + 1;
        
        manaCost *= 5;
        book.setManaCost(manaCost);
        
        String rarity = "";
        if(bonusCount < 2) rarity = "common";
        else if(bonusCount <= 3) rarity = "uncommon";
        else if(bonusCount <= 5) rarity = "rare";

        if(bonusCount > 3 && random.nextFloat() <= 0.75){
            book.setEffect(EffectBuilder.randomItemEffect(z, random));
            Log.debug("Effect: " + book.effect().name());    
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
        
//        builder.append("The book grants abilities in ");
//        for(int i = 0; i < chosen.size(); i++){
//            BonusModifier bonus = chosen.get(i);
//            if(i == chosen.size() - 1 && chosen.size() != 1) builder.append(" and ");
//            builder.append(bonus.description);
//            if(i == chosen.size() - 1) builder.append(".");
//            else if(i != chosen.size() - 2)builder.append(", ");
//        }
//        return builder.toString().trim();        
    }
    
//    private static final BonusModifier[] BONUS_MODIFIERS = {
//            new BonusModifier("str", "strength", 1f),
//            new BonusModifier("def", "defense", 1f),
//            new BonusModifier("acc", "accuracy", 1f),
//            new BonusModifier("int", "intelligence", 1f),
//            new BonusModifier("manaA", "mana regen", 0.8f),
//            new BonusModifier("manaS", "mana regen speed", -0.675f)
//    };
//    
//    private static final String[] BOOK_DESC = { "common", "uncommon", "rare" };
//    
//    public static Spellbook generate(int z){
//        Sprite sprite = randomBookSprite();
//
//        Spellbook book = new Spellbook(null, null, sprite, null);
//
//        int bonusCount = (int) (1 + (z / 3) + Math.floor(((Math.random() + Math.random()) - 1.8) * 2));
//        if(bonusCount < 1) bonusCount = 1;
//        if(bonusCount >= BONUS_MODIFIERS.length) bonusCount = BONUS_MODIFIERS.length;
//
//        List<BonusModifier> toChoose = Arrays.asList(BONUS_MODIFIERS);
//        Collections.shuffle(toChoose);
//        List<BonusModifier> chosen = new ArrayList<BonusModifier>();
//        for(int i = 0; i < bonusCount; i++){
//            int bonusValue = (int) (((z / 2) + (Math.floor(Math.random() * 3))) * 0.75f) + 1;
//            BonusModifier chose = toChoose.get(i);
//            applyBonusModifier(book, chose.name, (int) (bonusValue * chose.multiplier));
//            chosen.add(chose);
//        }
//
//        book.setDescription(generateDescription(chosen));
//        book.setName(BOOK_DESC[(bonusCount - 1) / 2] + " spellbook of " + getName(book)); //TODO: out of bounds
//        return book;
//    }
//
//    private static String generateDescription(List<BonusModifier> chosen){
//        StringBuilder builder = new StringBuilder();
//        builder.append("The book grants abilities in ");
//        for(int i = 0; i < chosen.size(); i++){
//        BonusModifier bonus = chosen.get(i);
//            if(i == chosen.size() - 1 && chosen.size() != 1) builder.append(" and "); 
//            builder.append(bonus.description);
//            if(i == chosen.size() - 1) builder.append(".");
//            else if(i != chosen.size() - 2)builder.append(", ");
//        }
//        return builder.toString().trim();
//    }
//
    private static Spellbook applyBonusModifier(Spellbook book, String name, int value){
        switch (name.trim()){
            case "str": book.setStrengthBonus(value); break;
            case "def": book.setDefenseBonus(value); break;
            case "acc": book.setAccuracyBonus(value); break;
            case "int": book.setIntelligenceBonus(value); break;
            case "manaA": book.setManaRegenAmountBonus(value); break;
            case "manaS": book.setManaRegenSpeedBonus(value); break;
            default: break;
        }
        return book;
    }
//   
//    private static String getName(Spellbook book){
//        String highestName = "";
//        int highestStat = 0;
//        int lowestStat = 1000;
//        int count = 0;
//        
//        if(book.strengthBonus() != 0){
//            count++;
//            if(book.strengthBonus() < lowestStat) lowestStat = book.strengthBonus();
//            if(book.strengthBonus() > highestStat){
//                highestStat = book.strengthBonus();
//                highestName = "strength";
//            }
//        }
//
//        if(book.defenseBonus() != 0){
//            count++;
//            if(book.defenseBonus() < lowestStat) lowestStat = book.defenseBonus();
//            if(book.defenseBonus() > highestStat){
//                highestStat = book.defenseBonus();
//                highestName = "defense";
//            }
//        }
//
//        if(book.accuracyBonus() != 0){
//            count++;
//            if(book.accuracyBonus() < lowestStat) lowestStat = book.accuracyBonus();
//            if(book.accuracyBonus() > highestStat){
//                highestStat = book.accuracyBonus();
//                highestName = "accuracy";
//            }
//        }
//
//        if(book.intelligenceBonus() != 0){
//            count++;
//            if(book.intelligenceBonus() < lowestStat) lowestStat = book.intelligenceBonus();
//            if(book.intelligenceBonus() > highestStat){
//                highestStat = book.intelligenceBonus();
//                highestName = "intelligence";
//            }
//        }
//
//        if(book.manaRegenAmountBonus() != 0){
//            count++;
//            if(book.manaRegenAmountBonus() < lowestStat) lowestStat = book.manaRegenAmountBonus();
//            if(book.manaRegenAmountBonus() > highestStat){
//                highestStat = book.manaRegenAmountBonus();
//                highestName = "mana";
//            }
//        }
//
//        if(book.manaRegenSpeedBonus() != 0){
//            count++;
//            if(book.manaRegenSpeedBonus() * -1 < lowestStat) lowestStat = book.manaRegenSpeedBonus() * -1;
//            if(book.manaRegenSpeedBonus() * -1 > highestStat){
//                highestStat = book.manaRegenSpeedBonus() * -1;
//                highestName = "mana";
//            }
//        }
//        
//        if(highestStat == 0 || (lowestStat == highestStat && count > 1)) return "balance";
//        return highestName;
//    }
//    
//    private static Sprite randomBookSprite(){
//        List<Sprite> sprites = new ArrayList<Sprite>();
//        sprites.add(Sprite.get("spell_book_1"));
//        sprites.add(Sprite.get("spell_book_2"));
//        sprites.add(Sprite.get("spell_book_3"));
//        sprites.add(Sprite.get("spell_book_4"));
//        return sprites.get((int) (Math.random() * sprites.size()));
//    }
//    
//    
//    static class BonusModifier{
//        public String name;
//        public String description;
//        public float multiplier;
//        
//        public BonusModifier(String name, String description, float multiplier){
//            this.name = name;
//            this.description = description;
//            this.multiplier = multiplier;
//        }
//    }
}
