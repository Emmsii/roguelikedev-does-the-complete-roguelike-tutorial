package com.mac.rltut.engine.window;

import com.mac.rltut.engine.graphics.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:53 AM.
 */
public class Panel extends JPanel {

    private final int widthInTiles, heightInTiles;
    private final int windowScale, tileSize;

    private BufferedImage image;
    private int[] pixels;

    private Renderer renderer;

    public Panel(int widthInTiles, int heightInTiles, int windowScale, int tileSize){
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.windowScale = windowScale;
        this.tileSize = tileSize;
        this.image = new BufferedImage(widthInPixels(), heightInPixels(), BufferedImage.TYPE_INT_RGB);
        this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        this.setPreferredSize(new Dimension(widthInTiles * tileSize * windowScale, heightInTiles * tileSize * windowScale));

        if(windowScale == 0) throw new IllegalArgumentException("Panel scale must be greater than 0.");
    }

    @Override
    public void paint(Graphics g) {
        if(renderer == null) return;

        for(int i = 0; i < pixels.length; i++) pixels[i] = renderer.pixels()[i];
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, (getWidth() - widthInPixels() * windowScale) / 2, (getHeight() - heightInPixels() * windowScale) / 2, widthInPixels() * windowScale, heightInPixels() * windowScale, null);
    }

    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }

    public int widthInPixels(){
        return widthInTiles * tileSize;
    }

    public int heightInPixels(){
        return heightInTiles * tileSize;
    }
}
