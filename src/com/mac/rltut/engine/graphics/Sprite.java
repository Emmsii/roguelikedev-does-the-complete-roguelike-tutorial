package com.mac.rltut.engine.graphics;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Sprite extends Bitmap{

    public static final Sprite empty = Spritesheet.textures.cutSprite(0, 0, 8, 8, 8);
    public static final Sprite player = Spritesheet.textures.cutSprite(5, 5, 8, 8, 8);

    //TILES
    public static final Sprite grassYellow = Spritesheet.textures.cutSprite(9, 0, 8, 8, 8);
    public static final Sprite grassGreen = Spritesheet.textures.cutSprite(10, 0, 8, 8, 8);
    public static final Sprite grassBlue = Spritesheet.textures.cutSprite(11, 0, 8, 8, 8);
    public static final Sprite grassPurple = Spritesheet.textures.cutSprite(12, 0, 8, 8, 8);
    public static final Sprite treeDeciduous = Spritesheet.textures.cutSprite(14, 0, 8, 8, 8);
    public static final Sprite treeConifer = Spritesheet.textures.cutSprite(15, 0, 8, 8, 8);

    public static final Sprite lavaYellow = Spritesheet.textures.cutSprite(0, 1, 8, 8, 8);
    public static final Sprite lavaBrown = Spritesheet.textures.cutSprite(1, 1, 8, 8, 8);
    public static final Sprite waterPurple = Spritesheet.textures.cutSprite(2, 1, 8, 8, 8);
    public static final Sprite waterBrown = Spritesheet.textures.cutSprite(3, 1, 8, 8, 8);
    public static final Sprite waterBlue = Spritesheet.textures.cutSprite(4, 1, 8, 8, 8);
    public static final Sprite waterLilypad = Spritesheet.textures.cutSprite(5, 1, 8, 8, 8);
    public static final Sprite waterBonesPurple1 = Spritesheet.textures.cutSprite(6, 1, 8, 8, 8);
    public static final Sprite waterBonesPurple2 = Spritesheet.textures.cutSprite(7, 1, 8, 8, 8);
    public static final Sprite waterBonesBrown1 = Spritesheet.textures.cutSprite(8, 1, 8, 8, 8);
    public static final Sprite waterBonesBrown2 = Spritesheet.textures.cutSprite(9, 1, 8, 8, 8);
    public static final Sprite blood = Spritesheet.textures.cutSprite(10, 1, 8, 8, 8);

    public static final Sprite portal = Spritesheet.textures.cutSprite(13, 12, 8, 8, 8);
    
    //UI
    public static final Sprite uiBorderVer = Spritesheet.textures.cutSprite(12, 9, 8, 8, 8);
    public static final Sprite uiBorderHor = Spritesheet.textures.cutSprite(10, 9, 8, 8, 8);
    public static final Sprite uiBorderBL = Spritesheet.textures.cutSprite(9, 9, 8, 8, 8);
    public static final Sprite uiBorderBR = Spritesheet.textures.cutSprite(11, 9, 8, 8, 8);
    public static final Sprite uiBorderTL = Spritesheet.textures.cutSprite(12, 10, 8, 8, 8);
    public static final Sprite uiBorderTR = Spritesheet.textures.cutSprite(13, 9, 8, 8, 8);
    
    public Sprite(int width, int height, int[] pixels){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }
}