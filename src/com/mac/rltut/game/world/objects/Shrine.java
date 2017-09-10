package com.mac.rltut.game.world.objects;

import com.mac.rltut.engine.graphics.Sprite;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.effects.Effect;
import com.mac.rltut.game.effects.EffectBuilder;
import com.mac.rltut.game.entity.creature.Creature;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 07/09/2017 at 11:40 AM.
 */
public class Shrine extends MapObject{

    private Random random;
    private Tile charged;
    private Tile uncharged;
    private boolean hasCharge;

    protected Shrine(){}

    public Shrine(String name, String description, Tile charged, Tile uncharged, Random random) {
        super(name, description, charged);
        this.charged = charged;
        this.uncharged = uncharged;
        this.hasCharge = true;
        this.random = random;
    }
    
    @Override
    public void update() {
        
    }
    
    public void bless(Item item, Creature creature, int z){
        if(!(item instanceof Equippable)) return;
        Equippable equippable = (Equippable) item;
        equippable.setUnique(true);
        
        creature.doAction(new ColoredString("bless the %s", Colors.BLUE), equippable.name());

        Effect effect = EffectBuilder.randomWeaponEffect(z, random);
        
        if(effect != null){
            equippable.setName(equippable.name() + " of " + effect.adjective());
            equippable.setEffect(effect);
        } else equippable.setName("Blessed " + equippable.name());
        

        List<String> availableBonuses = getAvailableBonuses(equippable);
        int bonusCount = (int) (1 + (z / 3) + Math.floor(((random.nextFloat() + random.nextFloat()) - 1.8) * 2));
        if(bonusCount < 1) bonusCount = 1;
        if(bonusCount >= availableBonuses.size()) bonusCount = availableBonuses.size() - 1;

        Collections.shuffle(availableBonuses);
        for(int i = 0; i < bonusCount; i++) {
            switch(availableBonuses.get(i)){
                case "STR": equippable.setStrengthBonus(equippable.strengthBonus() + getBonusValue(z)); break;
                case "DEF": equippable.setDefenseBonus(equippable.defenseBonus() + getBonusValue(z)); break;
                case "ACC": equippable.setAccuracyBonus(equippable.accuracyBonus() + getBonusValue(z)); break;
                case "INT": equippable.setIntelligenceBonus(equippable.intelligenceBonus() + getBonusValue(z)); break;
            }
        }

        hasCharge = false;
    }

    private int getBonusValue(int z){
        return (int) (((z / 2) + (Math.floor(random.nextFloat() * 3))) * 0.75f) + 1;
    }

    private List<String> getAvailableBonuses(Equippable equippable){
        List<String> result = new ArrayList<String>();
        if(EquipmentSlot.isJewelry(equippable.slot())) result.add("INT");
        else if(EquipmentSlot.isWeapon(equippable.slot())) result.add("STR");
        else if(EquipmentSlot.isArmor(equippable.slot())) result.add("DEf"); 
        result.add("ACC");
        return result;
    }

    public boolean hasCharge(){
        return hasCharge;
    }
    
    public Tile tile(){
        return hasCharge ? charged : uncharged;
    }

    @Override
    public Sprite sprite() {
        return tile().sprite();
    }

    @Override
    public String description() {
        return tile().description();
    }
}
