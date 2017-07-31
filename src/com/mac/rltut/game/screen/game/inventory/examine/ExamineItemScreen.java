package com.mac.rltut.game.screen.game.inventory.examine;

import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.StringUtil;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.entity.item.Consumable;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.Spellbook;
import com.mac.rltut.game.screen.Screen;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 24/07/2017 at 09:45 AM.
 */
public class ExamineItemScreen extends Screen {
    
    protected Item item;
    protected Screen previous;
    
    protected int xp, yp;
    
    public ExamineItemScreen(int x, int y, int w, int h, Item item, Screen previous){
        super(x, y, w, h, "Examine");
        this.item = item;
        this.previous = previous;
    }
    
    @Override
    public Screen input(KeyEvent e) {
        return previous;
    }

    @Override
    public void render(Renderer renderer) {
        renderBorderFill(renderer);
        
        xp = this.x + 3;
        yp = this.y + 3;
        
        renderBox(2, 2, 3, 3, true, renderer);
        renderer.renderSprite(item.sprite(), xp, yp);
        renderer.write(StringUtil.capitalizeEachWord(StringUtil.clean(item.name())), xp + 3, yp++);
        yp+=2;
        
        List<String> lines = StringUtil.lineWrap(item.description(), width - 4);
        renderBox(2, 5, width - 4, lines.size() + 2, false, renderer);
        
        for(String line : lines) renderer.writeCenter(line, this.x + width / 2, yp++);
        yp += 2;
        
        if(item instanceof Equippable){
            Equippable e = (Equippable) item;
            renderer.write("Damage", xp, yp++);
            
            if(e.damage() != null && !e.damage().equals("0")) renderer.write("Melee    " + e.damage(), xp + 1, yp++);
            if(e.rangedDamage() != null && !e.rangedDamage().equals("0")) renderer.write("Ranged   " + e.rangedDamage(), xp + 1, yp++);
            yp++;
            
            renderer.write("Slot: " + StringUtil.capitalizeFirst(e.slot().toString().toLowerCase()), xp, yp++);
            
            if(e.effect() != null){
                Effect effect = e.effect();
                renderer.write("Effect", xp, yp++);
                renderer.write(StringUtil.capitalizeEachWord(StringUtil.clean(effect.name())) + " (" + effect.chancePercent() + " chance)", xp + 1, yp++);
                List<String> desc = StringUtil.lineWrap(StringUtil.capitalizeFirst(effect.description()), width - 4);
                for(String s : desc) renderer.write(s, xp + 1, yp++);
            }
            yp++;
        }
        
        if(item instanceof Consumable){
            Consumable consumable = (Consumable) item;
            if(consumable.effect() != null){
                yp++;
                Effect effect = consumable.effect();
                renderer.write("Effect", xp, yp++);
                renderer.write(StringUtil.capitalizeEachWord(StringUtil.clean(effect.name())), xp + 1, yp++);
                List<String> desc = StringUtil.lineWrap(StringUtil.capitalizeFirst(effect.description()), width - 4);
                for(String s : desc) renderer.write(s, xp + 1, yp++);
            }
        }
        
        if(item instanceof Spellbook){
            Spellbook book = (Spellbook) item;
            renderer.write("Mana Cost: " + book.manaCost(), xp, yp++);
            if(book.effect() != null){
                yp++;
                Effect effect = book.effect();
                renderer.write("Effect", xp, yp++);
                renderer.write(StringUtil.capitalizeEachWord(StringUtil.clean(effect.name())) + " (" + effect.chancePercent() + " chance)", xp + 1, yp++);
                List<String> desc = StringUtil.lineWrap(StringUtil.capitalizeFirst(effect.description()), width - 4);
                for(String s : desc) renderer.write(s, xp + 1, yp++);
            }
            yp++;
        }
        
        if(hasBonus(item)) {
            renderer.write("Bonuses", xp, yp++);
            if(item.strengthBonus() != 0) renderer.write("STR " + (item.strengthBonus() > 0 ? "+" + item.strengthBonus() : item.strengthBonus()), xp + 1, yp++);
            if(item.defenseBonus() != 0) renderer.write("DEF " + (item.defenseBonus() > 0 ? "+" + item.defenseBonus() : item.defenseBonus()), xp + 1, yp++);
            if(item.accuracyBonus() != 0) renderer.write("ACC " + (item.accuracyBonus() > 0 ? "+" + item.accuracyBonus() : item.accuracyBonus()), xp + 1, yp++);
            if(item.intelligenceBonus() != 0) renderer.write("INT " + (item.intelligenceBonus() > 0 ? "+" + item.intelligenceBonus() : item.intelligenceBonus()), xp + 1, yp++);
            if(item.manaRegenSpeedBonus() != 0 || item.manaRegenAmountBonus() != 0) {
                renderer.write("Mana", xp, yp++);
                if (item.manaRegenAmountBonus() != 0) renderer.write("REGEN " + (item.manaRegenAmountBonus() > 0 ? "+" + item.manaRegenAmountBonus() : item.manaRegenAmountBonus()), xp + 1, yp++);
                if (item.manaRegenSpeedBonus() != 0) renderer.write("SPEED " + item.manaRegenSpeedBonus(), xp + 1, yp++);
            }
            yp++;
        }
        
    }
    
    private boolean hasBonus(Item item){
        return item.strengthBonus() != 0 || 
                item.defenseBonus() != 0 ||
                item.accuracyBonus() != 0 ||
                item.intelligenceBonus() != 0 ||
                item.manaRegenAmountBonus() != 0 ||
                item.manaRegenSpeedBonus() != 0;
    }
}
