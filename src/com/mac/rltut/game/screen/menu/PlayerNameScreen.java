package com.mac.rltut.game.screen.menu;

import com.mac.rltut.engine.Engine;
import com.mac.rltut.engine.graphics.Renderer;
import com.mac.rltut.engine.util.Colors;
import com.mac.rltut.game.screen.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 21/07/2017 at 03:18 PM.
 */
public class PlayerNameScreen extends Screen{
    
    private String name;
    private int maxNameLength;
    private String lastKey;
    
    public PlayerNameScreen(int maxNameLength){
        this.name = "";    
        this.lastKey = "";
        this.maxNameLength = maxNameLength;
    }
    
    @Override
    public void render(Renderer renderer) {
        renderBorder(renderer);
        
        renderer.writeCenter("Who are you?", Engine.instance().widthInTiles() / 2, 8);
        renderer.write(name + (name.length() != maxNameLength ? "_" : ""), 12, 15);

        renderer.writeCenter("Press [ENTER] to continue.", Engine.instance().widthInTiles() / 2, Engine.instance().heightInTiles() - 4, Colors.GRAY);
    }

    @Override
    public Screen input(KeyEvent key) {
        switch(key.getKeyChar()){
            case 'a': addKey("a"); break;
            case 'b': addKey("b"); break;
            case 'c': addKey("c"); break;
            case 'd': addKey("d"); break;
            case 'e': addKey("e"); break;
            case 'f': addKey("f"); break;
            case 'g': addKey("g"); break;
            case 'h': addKey("h"); break;
            case 'i': addKey("i"); break;
            case 'j': addKey("j"); break;
            case 'k': addKey("k"); break;
            case 'l': addKey("l"); break;
            case 'm': addKey("m"); break;
            case 'n': addKey("n"); break;
            case 'o': addKey("o"); break;
            case 'p': addKey("p"); break;
            case 'q': addKey("q"); break;
            case 'r': addKey("r"); break;
            case 's': addKey("s"); break;
            case 't': addKey("t"); break;
            case 'u': addKey("u"); break;
            case 'v': addKey("v"); break;
            case 'w': addKey("w"); break;
            case 'x': addKey("x"); break;
            case 'y': addKey("y"); break;
            case 'z': addKey("z"); break;

            case 'A': addKey("A"); break;
            case 'B': addKey("B"); break;
            case 'C': addKey("C"); break;
            case 'D': addKey("D"); break;
            case 'E': addKey("E"); break;
            case 'F': addKey("F"); break;
            case 'G': addKey("G"); break;
            case 'H': addKey("H"); break;
            case 'I': addKey("I"); break;
            case 'J': addKey("J"); break;
            case 'K': addKey("K"); break;
            case 'L': addKey("L"); break;
            case 'M': addKey("M"); break;
            case 'N': addKey("N"); break;
            case 'O': addKey("O"); break;
            case 'P': addKey("P"); break;
            case 'Q': addKey("Q"); break;
            case 'R': addKey("R"); break;
            case 'S': addKey("S"); break;
            case 'T': addKey("T"); break;
            case 'U': addKey("U"); break;
            case 'V': addKey("V"); break;
            case 'W': addKey("W"); break;
            case 'X': addKey("X"); break;
            case 'Y': addKey("Y"); break;
            case 'Z': addKey("Z"); break;
        }

        switch(key.getKeyCode()){
            case KeyEvent.VK_SPACE: addKey(" "); break;
            case KeyEvent.VK_BACK_SPACE:
                if(name.length() - 1 >= 0){
                    name = name.substring(0, name.length() -1);
                    lastKey = "";
                }
                break;
            case KeyEvent.VK_ESCAPE: return new StartScreen();
            case KeyEvent.VK_ENTER:
                if(name.length() == 0) break;
//                return new PlayerSkillSelectScreen(new Player(name, Sprite.get("player")));
            return new PlayerSpriteScreen(name);
        }
        
        return this;
    }

    private void addKey(String key){
        if(name.length() >= maxNameLength) return;
        if(lastKey == " " && key == " " || name.length() == 0 && key == " ") return;
        name = name + key;
        lastKey = key;
    }
}
