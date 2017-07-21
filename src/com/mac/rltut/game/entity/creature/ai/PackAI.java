package com.mac.rltut.game.entity.creature.ai;

import com.mac.rltut.engine.pathfinding.Path;
import com.mac.rltut.engine.util.MathUtil;
import com.mac.rltut.engine.util.Point;
import com.mac.rltut.game.entity.creature.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: complete-rltut
 * PC
 * Created by Matt on 18/07/2017 at 05:08 PM.
 */
public class PackAI {
    
    private List<PackMemberAI> members;
  
    private int maxWanderDistance = 10;
    
    public PackAI(){
        this.members = new ArrayList<PackMemberAI>();
    }
    
    public boolean canSeePlayer(){
        removeDeadMembers();
        for(PackMemberAI member : members){
            if(member.creature.canSee(member.creature.world().player())){
                return true;
            }
        }
        return false;
    }
    
    public Point getAveragePackPosition(){
        removeDeadMembers();
        if(members.size() == 1) return new Point(members.get(0).creature.x, members.get(0).creature.y, 0);
        int x = 0;
        int y = 0;
        for(PackMemberAI member : members){
            x += member.creature.x;
            y += member.creature.y;
        }
        return new Point(x / members.size(), y / members.size(), 0);
    }
    
    private void removeDeadMembers(){
        for(int i = members.size() - 1; i >= 0; i--) if(members.get(i).creature.hp() < 1) members.remove(i--);
    }

    public void addPackMember(PackMemberAI member){
        members.add(member);
    }
        
    public List<PackMemberAI> members(){
        return members;
    }
    
    public int maxWanderDistance(){
        return maxWanderDistance;
    }
}