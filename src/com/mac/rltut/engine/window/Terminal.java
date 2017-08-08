package com.mac.rltut.engine.window;


import javax.swing.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Terminal extends JFrame {

    private Panel panel;
    
    public Terminal(boolean fullscreen, int widthInTiles, int heightInTiles, int windowScale, int tileSize, String title){
        this.panel = new Panel(widthInTiles, heightInTiles, windowScale, tileSize);
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowCloseHandler());
        if(fullscreen){
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setUndecorated(true);
        }
        this.getContentPane().add(panel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);        
        this.setVisible(true);
    }

    public Panel panel(){
        return panel;
    }
}
