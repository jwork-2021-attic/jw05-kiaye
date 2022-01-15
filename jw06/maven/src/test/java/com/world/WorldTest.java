package com.world;

import org.junit.Test;

import jdk.jfr.Timestamp;

public class WorldTest {
    private World world = new World();

    @Test
    public void testtile(){
        Tile tile = world.tile(world.width()-1, world.height()-1);
        assert(tile == Tile.FLOOR);
    }

    @Test
    public void testglyph(){
        assert(world.glyph(world.width()-1, world.height()-1) == Tile.FLOOR.glyph());
    }

    @Test
    public void testcolor(){
        assert(world.color(world.width()-1, world.height()-1) == Tile.FLOOR.color());
    }

    @Test 
    public void testwidth(){
        assert(world.width() != 0);
    }

    @Test
    public void testheight(){
        assert(world.height() != 0);
    }

}

