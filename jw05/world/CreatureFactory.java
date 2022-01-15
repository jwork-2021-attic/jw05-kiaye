/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package world;

import java.util.List;

import asciiPanel.AsciiPanel;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory {

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages) {
        Creature player = new Creature(this.world, (char)2, AsciiPanel.red, 100, 20, 5, 9);
        world.addAtEmptyLocation(player);
        world.addpath(player);
        new PlayerAI(player, messages);
        return player;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(this.world, (char)7, AsciiPanel.yellow, 10, 0, 0, 0);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus, this);
        return fungus;
    }

    public Creature newbody(int x, int y, int n) {
        n = (n+1) % 7;
        Creature fungus;
        switch (n){
            case 1: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.red, 100, 20, 5, 0);
                break;
            case 2: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.yellow, 100, 20, 5, 0);
                break; 
            case 3: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.green, 100, 20, 5, 0);
                break;
            case 4: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.blue, 100, 20, 5, 0);
                break;
            case 5: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.magenta, 100, 20, 5, 0);
                break;
            case 6: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.cyan, 100, 20, 5, 0);
                break;
            case 7: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.white, 100, 20, 5, 0);
                break;
            default: 
                fungus = new Creature(this.world, (char)2, AsciiPanel.white, 100, 20, 5, 0);
                break;
        }
        world.addlocation(fungus, x, y);
        new FungusAI(fungus, this);
        return fungus;
    }
}
