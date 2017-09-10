package com.mac.rltut.game.screen.game.subscreen.inventory;

import com.mac.rltut.engine.file.Config;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.entity.item.Item;
import com.mac.rltut.game.entity.item.ItemStack;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.world.objects.Chest;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 04:33 PM.
 */
public class ChestScreen extends InventoryBasedScreen{
    
    public ChestScreen(int x, int y, int w, int h, String title, Chest chest, Player player) {
        super(x, y, w, h, title, chest.inventory(), player, null);
    }

    @Override
    protected String getVerb() {
        return "take";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        if(player.inventory().isFull() && !item.name().equalsIgnoreCase("gold")){
            player.notify(new ColoredString("You are carrying too much.", Colors.ORANGE));
            return this;
        }else{
            inventory.remove(item);
            
            if(item instanceof ItemStack){
                ItemStack stack = (ItemStack) item;
                player.doAction(new ColoredString("take %d %s"), stack.amount(), item.name());
                if(stack.name().equalsIgnoreCase("gold")){
                    player.modifyGold(stack.amount());
//                    if(inventory.isEmpty()){
//                        player.notify(new ColoredString("The chest is empty.", Colors.ORANGE));
//                        return null;
//                    }
//                    return this;
                }else if(stack.name().equalsIgnoreCase("diamonds")){
                    player.modifyDefense(stack.amount());
//                    return thi
                }
            }else {
                player.doAction(new ColoredString("take the %s"), item.name());
                player.inventory().add(item);
                if (item instanceof Equippable) {
                    Equippable e = (Equippable) item;
                    if (player.getEquippedAt(e.slot()) == null && Config.autoEquip) e.equip(player);
                }
            }
        }
        
        if(inventory.isEmpty()){
            player.notify(new ColoredString("The chest is empty.", Colors.ORANGE));
            return null;
        }
        return this;
    }
}
