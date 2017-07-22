package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 09:52 AM.
 */
public class ItemBuilder {
    
    public static Item newDagger(int z){
        return (Weapon) new Weapon("Dagger", Sprite.get("dagger")).newInstance();
    }
    
    public static Item newSpellBook(int z){
        List<Sprite> sprites = new ArrayList<>();
        sprites.add(Sprite.get("spell_book_1"));
        sprites.add(Sprite.get("spell_book_2"));
        sprites.add(Sprite.get("spell_book_3"));
        
        return (Item) new Item("Spell Book", sprites.get((int) (Math.random() * sprites.size()))).newInstance();
    }
}
