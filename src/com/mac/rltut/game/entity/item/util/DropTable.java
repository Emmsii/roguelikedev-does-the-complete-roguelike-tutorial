package com.mac.rltut.game.entity.item.util;

import com.esotericsoftware.minlog.Log;
import com.mac.rltut.engine.util.Pool;
import com.mac.rltut.engine.util.maths.MathUtil;
import com.mac.rltut.game.codex.Codex;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 06:08 PM.
 */
public class DropTable {
    
    private List<Drop> drops;
        
    public DropTable(){
        this.drops = new ArrayList<Drop>();
    }
    
    public void addDrop(String input){
        String[] split = input.split(":");
        if(split.length != 3){
            Log.error("Invalid drop syntax [" + input + "]");
            return;
        }
        
        String name = split[0];
        String amount = split[1];
        int chance = Integer.parseInt(split[2].trim());
        if(!Codex.items.containsKey(name)) Log.warn("Cannot add item [" + name + "] to drop table.");
        else drops.add(new Drop((Item) Codex.items.get(name.toLowerCase().trim()).entity(), amount, chance));
    }
    
    public Item getItem(){
        if(drops.isEmpty()){
            Log.warn("Drop table is empty.");
            return null;
        }
        
        Log.debug("Getting item!");
        Pool<Drop> pool = new Pool<Drop>();
        for(Drop d : drops) pool.add(d, d.chance);
        pool.add(new Drop(null, null, 100), 100);
        
        Drop drop = pool.get();
        if(drop.item == null) return null;
        else{
            if(drop.item instanceof ItemStack){
                ItemStack stack = (ItemStack) drop.item;
                stack.modifyAmount(MathUtil.randomIntFromString(drop.amount));
            }
            return (Item) drop.item.newInstance();
        }
    }
    
    public List<Drop> drops(){
        return drops;
    }
    
    public int count(){
        return drops.size();
    }

    public static class Drop{

        public Item item;
        public String amount;
        public int chance;

        public Drop(){}
        
        public Drop(Item item, String amount, int chance){
            this.item = item;
            this.amount = amount;
            this.chance = chance;
        }
    }
}

