package com.mac.rltut.engine.graphics;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 25/06/2017 at 10:54 AM.
 */
public class Sprite extends Bitmap{

    public static final Sprite empty = Spritesheet.textures.cutSprite(0, 0, 8, 8, 8);
    public static final Sprite player = Spritesheet.textures.cutSprite(5, 5, 8, 8, 8);//TEMP
  
    //TILES
    
    public static final Sprite wallTopRed = Spritesheet.textures.cutSprite(1, 0, 8, 8, 8);
    public static final Sprite wallTopBlue = Spritesheet.textures.cutSprite(2, 0, 8, 8, 8);
    public static final Sprite wallSide = Spritesheet.textures.cutSprite(3, 0, 8, 8, 8);
    public static final Sprite stairDown = Spritesheet.textures.cutSprite(4, 0, 8, 8, 8);
    public static final Sprite stairUp = Spritesheet.textures.cutSprite(5, 0, 8, 8, 8);
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
    public static final Sprite doorSilver = Spritesheet.textures.cutSprite(11, 1, 8, 8, 8);
    public static final Sprite doorGold = Spritesheet.textures.cutSprite(12, 1, 8, 8, 8);
    public static final Sprite doorGreen = Spritesheet.textures.cutSprite(13, 1, 8, 8, 8);
    public static final Sprite doorPurple = Spritesheet.textures.cutSprite(14, 1, 8, 8, 8);
    
    public static final Sprite chestGoldClosed = Spritesheet.textures.cutSprite(0, 2, 8, 8, 8);
    public static final Sprite chestGoldOpen = Spritesheet.textures.cutSprite(1, 2, 8, 8, 8);
    public static final Sprite chestSilverClosed = Spritesheet.textures.cutSprite(2, 2, 8, 8, 8);
    public static final Sprite chestSilverOpen = Spritesheet.textures.cutSprite(3, 2, 8, 8, 8);
    public static final Sprite gold = Spritesheet.textures.cutSprite(4, 2, 8, 8, 8);
    public static final Sprite amulet = Spritesheet.textures.cutSprite(5, 2, 8, 8, 8);
    public static final Sprite jewel = Spritesheet.textures.cutSprite(6, 2, 8, 8, 8);
    public static final Sprite ringGold = Spritesheet.textures.cutSprite(7, 2, 8, 8, 8);
    public static final Sprite ringSilver = Spritesheet.textures.cutSprite(8, 2, 8, 8, 8);
    public static final Sprite apple = Spritesheet.textures.cutSprite(12, 2, 8, 8, 8);

    public static final Sprite mushroom = Spritesheet.textures.cutSprite(0, 3, 8, 8, 8);
    public static final Sprite meat = Spritesheet.textures.cutSprite(1, 3, 8, 8, 8);
    public static final Sprite fish = Spritesheet.textures.cutSprite(2, 3, 8, 8, 8);
    public static final Sprite potionRed = Spritesheet.textures.cutSprite(4, 3, 8, 8, 8);
    public static final Sprite potionBlue = Spritesheet.textures.cutSprite(5, 3, 8, 8, 8);
    public static final Sprite potionPurple = Spritesheet.textures.cutSprite(6, 3, 8, 8, 8);
    public static final Sprite potionGreen = Spritesheet.textures.cutSprite(7, 3, 8, 8, 8);
    public static final Sprite potionYellow = Spritesheet.textures.cutSprite(8, 3, 8, 8, 8);

    public static final Sprite shieldRound = Spritesheet.textures.cutSprite(10, 12, 8, 8, 8);
    public static final Sprite shieldKite = Spritesheet.textures.cutSprite(11, 12, 8, 8, 8);
    
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