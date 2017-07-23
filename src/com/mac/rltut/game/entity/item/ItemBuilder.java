package com.mac.rltut.game.entity.item;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.game.entity.item.equipment.Armor;
import com.mac.rltut.game.entity.item.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 22/07/2017 at 09:52 AM.
 */
public class ItemBuilder {
    
    public static Weapon newDagger(int z){
        return (Weapon) new Weapon("Dagger", "a dagger", Sprite.get("dagger"), "1d5").newInstance();
    }

    public static Weapon newSword(int z){
        return (Weapon) new Weapon("Sword", "a sword", Sprite.get("sword"), "1d9").newInstance();
    }

    public static Weapon newBattleaxe(int z){
        return (Weapon) new Weapon("Battleaxe", "a battleaxe", Sprite.get("battleaxe"), "2d7").newInstance();
    }

    public static Weapon newMace(int z){
        return (Weapon) new Weapon("Mace", "a mace", Sprite.get("mace"), "1d8").newInstance();
    }

    public static Weapon newScimitar(int z){
        return (Weapon) new Weapon("Scimitar", "a scimitar", Sprite.get("scimitar"), "1d8").newInstance();
    }
    
    public static Armor newChainmail(int z){
        return (Armor) new Armor("Chainmail", "a chainmail thing", Sprite.get("chainmail"), 3).newInstance();
    }
    
    public static Item newSpellBook(int z){
        List<Sprite> sprites = new ArrayList<>();
        sprites.add(Sprite.get("spell_book_1"));
        sprites.add(Sprite.get("spell_book_2"));
        sprites.add(Sprite.get("spell_book_3"));
        
        return (Item) new Item("Spell Book", "description goes here", sprites.get((int) (Math.random() * sprites.size()))).newInstance();
    }
}
