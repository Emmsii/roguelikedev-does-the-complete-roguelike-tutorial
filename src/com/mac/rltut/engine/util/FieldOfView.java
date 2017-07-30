package com.mac.rltut.engine.util;

import com.mac.rltut.engine.util.maths.Direction;
import com.mac.rltut.engine.util.maths.Line;
import com.mac.rltut.engine.util.maths.Point;
import com.mac.rltut.game.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 05/07/2017 at 09:24 AM.
 * 
 * Shadowcast algorithm from Squidlib: https://github.com/SquidPony/SquidLib/blob/master/squidlib-util/src/main/java/squidpony/squidgrid/FOV.java
 */
public class FieldOfView {

    public enum FOVType { SHADOWCAST, LINECAST }

    private World world;
    private Set<Point> inFov;

    public FieldOfView(World world){
        this.world = world;
        this.inFov = new HashSet<Point>();
    }

    public void compute(int x, int y, int z, int radius, FOVType type){
        setInFov(x, y, z);

        if(type == FOVType.SHADOWCAST) shadowCast(x, y, z, radius);
        else if(type == FOVType.LINECAST) lineCast(x, y, z, radius);
    }

    private void lineCast(int xp, int yp, int z, int radius){
        for(int y = -radius; y < radius; y++){
            for(int x = -radius; x < radius; x++){
                if(x * x + y * y > radius * radius) continue;
                if(!world.inBounds(x + xp, y + yp, z)) continue;

                for(Point p : new Line(xp, yp, x + xp, y + yp)){
                    setInFov(p.x, p.y, z);
                    world.setExplored(p.x, p.y, z, true);
                    if(!world.tile(p.x, p.y, z).canSee()) break;
                }
            }
        }
    }

    private void shadowCast(int x, int y, int z, int radius){
        for(Direction d : Direction.DIAGONALS){
            castLight(1, 1.0f, 0.0f, x, y, 0, d.x, d.y, 0, z, radius);
            castLight(1, 1.0f, 0.0f, x, y, d.x, 0, 0, d.y, z, radius);
        }
    }

    private void castLight(int row, float start, float end, int startx, int starty, int xx, int xy, int yx, int yy, int z, int radius){
        float newStart = 0.0f;
        if(start < end) return;
        boolean blocked = false;


        for(int distance = row; distance <= radius && !blocked; distance++){
            int deltaY = -distance;
            for(int deltaX = -distance; deltaX <= 0; deltaX++){
                int currentX = startx + deltaX * xx + deltaY * xy;
                int currentY = starty + deltaX * yx + deltaY * yy;
                float leftSlope = (deltaX - 0.5f) / (deltaY + 0.5f);
                float rightSlope = (deltaX + 0.5f) / (deltaY - 0.5f);

                if (!(currentX >= 0 && currentY >= 0 && currentX < world.width() && currentY < world.height()) || start < rightSlope) {
                    continue;
                } else if (end > leftSlope) {
                    break;
                }

                //check if it's within the lightable area and light if needed
                if (radius(deltaX, deltaY) <= radius) {
                    setInFov(currentX, currentY, z);
                }

                if (blocked) { //previous cell was a blocking one
                    if(!world.tile(currentX, currentY, z).canSee()){
                        newStart = rightSlope;
                        continue;
                    } else {
                        blocked = false;
                        start = newStart;
                    }
                } else {
                    if (!world.tile(currentX, currentY, z).canSee() && distance < radius) {//hit a wall within sight line
                        blocked = true;
                        castLight(distance + 1, start, leftSlope, startx, starty, xx, xy, yx, yy, z, radius);
                        newStart = rightSlope;
                    }
                }
            }
        }
    }

    private double radius(int dx, int dy){
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean inFov(int x, int y, int z){
        return inFov.contains(new Point(x, y, z));
    }

    public void setInFov(int x, int y, int z){
        world.setExplored(x, y, z, true);
        inFov.add(new Point(x, y, z));
    }

    public void clearFov(){
        inFov.clear();
    }
}
