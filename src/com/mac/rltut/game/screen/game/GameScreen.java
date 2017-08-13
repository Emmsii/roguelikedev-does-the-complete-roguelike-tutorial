package com.mac.rltut.game.screen.game;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.ColoredString;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.Game;
import com.mac.rltut.game.entity.creature.Player;
import com.mac.rltut.game.entity.item.EquipmentSlot;
import com.mac.rltut.game.entity.item.Equippable;
import com.mac.rltut.game.screen.Screen;
import com.mac.rltut.game.screen.game.subscreen.*;
import com.mac.rltut.game.screen.game.subscreen.examine.ExamineScreen;
import com.mac.rltut.game.screen.game.subscreen.inventory.*;
import com.mac.rltut.game.screen.menu.LooseScreen;
import com.mac.rltut.game.world.objects.Chest;

import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 27/06/2017 at 09:28 AM.
 */
public class GameScreen extends Screen{

    private Game game;

    private LevelScreen levelScreen;
    private LogScreen logScreen;
    private InfoScreen infoScreen;
    private Screen equipmentScreen;
    private Screen subscreen;
    
    private boolean shouldUpdate;
    
    public GameScreen(Game game){
        this.game = game;
        initScreens();
    }
    
    private void initScreens(){
        logScreen = new LogScreen(Engine.instance().widthInTiles(), game.log(), 9, "Log");
        infoScreen = new InfoScreen(Engine.instance().widthInTiles() - 29, 0, 29, logScreen.height(), game.player().name(), game.player());
        equipmentScreen = new EquipmentSlotsScreen(Engine.instance().widthInTiles() - 29, infoScreen.height(), 29, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "[q] Equipment [t] Stats", player());
        levelScreen = new LevelScreen(0, 0, Engine.instance().widthInTiles() - infoScreen.width(), Engine.instance().heightInTiles() - logScreen.height(), "", game.world(), game.player());
    }

    @Override
    public Screen input(KeyEvent key){
        int level = player().level();
        
        game.log().resetNewEntryCount();
        shouldUpdate = false;
        player().setHasPerformedAction(false);
                
        if(subscreen != null) subscreen = subscreen.input(key);
        else{
            switch (key.getKeyCode()){
                case KeyEvent.VK_NUMPAD5:
                case KeyEvent.VK_COMMA: shouldUpdate = true; break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_NUMPAD8: shouldUpdate = player().moveBy(0, -1, 0); break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_NUMPAD2: shouldUpdate = player().moveBy(0, 1, 0); break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_NUMPAD4: shouldUpdate = player().moveBy(-1, 0, 0); break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_NUMPAD6: shouldUpdate = player().moveBy(1, 0, 0); break;
                case KeyEvent.VK_NUMPAD7: shouldUpdate = player().moveBy(-1, -1, 0); break;
                case KeyEvent.VK_NUMPAD9: shouldUpdate = player().moveBy(1, -1, 0); break;
                case KeyEvent.VK_NUMPAD1: shouldUpdate = player().moveBy(-1, 1, 0); break;
                case KeyEvent.VK_NUMPAD3: shouldUpdate = player().moveBy(1, 1, 0); break;

                case KeyEvent.VK_SPACE:
                    Chest chest = player().tryOpen();
                    if(chest != null){
                        if(chest.inventory().isEmpty()) player().notify(new ColoredString("The chest is empty", Colors.ORANGE));
                        else subscreen = new ChestScreen(levelScreen.width() / 2 - 22, Engine.instance().heightInTiles() / 2  - 20, 44, 30, "Chest", chest, player());
                    }
                    else player().notify(new ColoredString("There is nothing there.", Colors.ORANGE));
                    break;
                                    
                case KeyEvent.VK_PAGE_DOWN: game.world().moveDown(player()); break;
                case KeyEvent.VK_PAGE_UP: game.world().moveUp(player()); break;

                case KeyEvent.VK_P: player().pickup(); break;
                case KeyEvent.VK_K: game.player().tryTalk(); break; 

                case KeyEvent.VK_Q: equipmentScreen = new EquipmentSlotsScreen(Engine.instance().widthInTiles() - 29, infoScreen.height(), 29, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "[q] Equipment [t] Stats", player()); break;
                case KeyEvent.VK_T: equipmentScreen = new EquipmentStatsScreen(Engine.instance().widthInTiles() - 29, infoScreen.height(), 29, Engine.instance().heightInTiles() - logScreen.height() - infoScreen.height(), "[q] Equipment [t] Stats", player()); break;

                case KeyEvent.VK_ESCAPE: subscreen = new GameEscapeMenu(Engine.instance().widthInTiles() / 2 - 10, Engine.instance().heightInTiles() / 2 - 4, 21, 8, game); break;

                case KeyEvent.VK_W: subscreen = new ThrowItemScreen(levelScreen.width() / 2 - (44 / 2), Engine.instance().heightInTiles() / 2  - 20, 44, 30, player().x - levelScreen.getScrollX() + 1, player().y - levelScreen.getScrollY() + 1, levelScreen.width(), levelScreen.height(), player().inventory(), player()); break;
                case KeyEvent.VK_D: subscreen = new DropScreen(levelScreen.width() / 2 - (44 / 2), Engine.instance().heightInTiles() / 2  - 20, 44, 30, null, player().inventory(), player()); break;
                case KeyEvent.VK_R: subscreen = new ReadScreen(levelScreen.width() / 2 - (44 / 2), Engine.instance().heightInTiles() / 2  - 20, 44, 30, null, player().inventory(), player()); break;
                case KeyEvent.VK_C: subscreen = new ConsumeScreen(levelScreen.width() / 2 - (44 / 2), Engine.instance().heightInTiles() / 2  - 20, 44, 30, null, player().inventory(), player()); break;
                case KeyEvent.VK_X: subscreen = new ExamineScreen(levelScreen.width() / 2 - (44 / 2), Engine.instance().heightInTiles() / 2  - 20, 44, 30, null, player().inventory(), player()); break;
                case KeyEvent.VK_E: subscreen = new EquipScreen(levelScreen.width() / 2 - (44 / 2), Engine.instance().heightInTiles() / 2  - 20, 44, 30, null, player().inventory(), player()); break;
                case KeyEvent.VK_L: subscreen = new LookScreen(0, 0, levelScreen.width(), levelScreen.height(), player(), player().x - levelScreen.getScrollX() + 1, player().y - levelScreen.getScrollY() + 1); break;
                case KeyEvent.VK_F:
                    Equippable weapon = player().getEquippedAt(EquipmentSlot.WEAPON);
                    if(weapon != null && (weapon.rangedDamage() != null && !weapon.rangedDamage().equals("0"))){
                        subscreen = new FireWeaponScreen(0, 0, levelScreen.width(), levelScreen.height(), player(), player().x - levelScreen.getScrollX() + 1, player().y - levelScreen.getScrollY() + 1);
                    }else player().notify(new ColoredString("You don't have a ranged weapon equipped.", Colors.ORANGE));
                    break;

                case KeyEvent.VK_F1: LevelScreen.showFov = !LevelScreen.showFov; break;
            }

            if(key.getKeyChar() == '?') subscreen = new HelpScreen();
        }
        
        if(player().level() > level){
            subscreen = new LevelUpScreen(Engine.instance().widthInTiles() / 2 - 20, Engine.instance().heightInTiles() / 2 - 6, 40, 11, player(), player().level() - level);
            return this;
        }
        
        if(subscreen == null && shouldUpdate || player().hasPerformedAction()) game.update();
        if(player().hp() < 1) return new LooseScreen(player());

        return this;
    }

    @Override
    public void render(Renderer renderer) {
        levelScreen.render(renderer);
        logScreen.render(renderer);
        infoScreen.render(renderer);
        equipmentScreen.render(renderer);
        if(subscreen != null) subscreen.render(renderer);
    }
    
    public Player player(){
        return game.player();
    }
    
    public Game game(){
        return game;
    }
}
