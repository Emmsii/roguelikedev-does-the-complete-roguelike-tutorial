package com.mac.rltut.engine.window;


import javax.swing.*;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Terminal extends JFrame {

    private final String OS = System.getProperty("os.name");

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
        if(isLinux() && fullscreen) this.setResizable(true);
        else this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);        
        this.setVisible(true);
    }

    public Panel panel(){
        return panel;
    }
    
    public void setIcon(ImageIcon icon){
        this.setIconImage(icon.getImage());
    }

    private boolean isWindows(){
        return OS.indexOf("win") >= 0;
    }

    private boolean isMac(){
        return OS.indexOf("mac") >= 0;
    }

    private boolean isLinux(){
        return OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") >= 0;
    }
}
