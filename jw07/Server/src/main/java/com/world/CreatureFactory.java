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
package com.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import com.asciiPanel.AsciiPanel;

/**
 *
 * @author Aeranythe Echosong
 */
public class CreatureFactory implements Serializable{

    private World world;

    public CreatureFactory(World world) {
        this.world = world;
    }

    public Creature newPlayer(List<String> messages, Color color) {
        Creature player = new Creature(this.world, (char)2, color, 100, 20, 5, 9);
        world.addAtEmptyLocation(player);
        List<Creature> listplayer = new ArrayList<>();
        listplayer.add(player);
        world.addpath(listplayer);
        new PlayerAI(player, messages);
        return player;
    }

    public Creature newFungus() {
        Creature fungus = new Creature(this.world, (char)7, AsciiPanel.yellow, 10, 0, 0, 0);
        world.addAtEmptyLocation(fungus);
        new FungusAI(fungus, this);
        return fungus;
    }

    public Creature newbody(int x, int y, Color color, int index) {
        Creature fungus = new Creature(this.world, (char)2, color, 100, 20, 5, 0);
        world.addlocation(fungus, x, y, index);
        new FungusAI(fungus, this);
        return fungus;
    }
}
