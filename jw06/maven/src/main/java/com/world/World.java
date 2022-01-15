package com.world;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.asciiPanel.AsciiPanel;

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
/**
 *
 * @author Aeranythe Echosong
 */
public class World implements Serializable{

    private Tile[][] tiles;
    private int width;
    private int height;
    private List<Creature> creatures;
    private List<Creature> paths;

    public static final int TILE_TYPES = 2;

    public World(){
        this.width = 30;
        this.height = 30;
        this.creatures = new ArrayList<>();
        this.paths = new ArrayList<>();
        tiles = new Tile[width][height];
        for (int width = 0; width < this.width; width++) {
            for (int height = 0; height < this.height; height++) {
                tiles[width][height] = Tile.FLOOR;        
            }
        }

    }
    public World(Tile[][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.paths = new ArrayList<>();
    }

    public Tile tile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Tile.BOUNDS;
        } else {
            return tiles[x][y];
        }
    }

    public char glyph(int x, int y) {
        return tiles[x][y].glyph();
    }

    public Color color(int x, int y) {
        return tiles[x][y].color();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void dig(int x, int y) {
        if (tile(x, y).isDiggable()) {
            tiles[x][y] = Tile.FLOOR;
        }
    }

    public void addlocation(Creature creature, int x, int y){
        creature.setX(x);
        creature.setY(y);
        this.creatures.add(creature);
        this.paths.add(creature);
    }
    public void addAtEmptyLocation(Creature creature) {
        int x = 0;
        int y = 0;

        do {
            x = (int) (Math.random() * this.width);
            y = (int) (Math.random() * this.height);
        } while (!tile(x, y).isGround() || this.creature(x, y) != null);
        //玩家是中间生成，怪物是随机生成
        //人物移动应该是固定速度的
        // for (x = 0; x < this.width; x++){
        //     boolean breaker = false;
        //     for (y = 0; y < this.height; y++){
        //         if (tile(x, y).isGround() && this.creature(x, y) == null){
        //             breaker = true;
        //             break;
        //         }
        //     }
        //     if (breaker){
        //         break;
        //     }
        // }

        creature.setX(x);
        creature.setY(y);

        this.creatures.add(creature);
    }

    public Creature creature(int x, int y) {
        for (Creature c : this.creatures) {
            if (c.x() == x && c.y() == y) {
                return c;
            }
        }
        return null;
    }

    public List<Creature> getCreatures() {
        return this.creatures;
    }

    public void remove(Creature target) {
        this.creatures.remove(target);
    }

    public void update() {
        ArrayList<Creature> toUpdate = new ArrayList<>(this.creatures);

        for (Creature creature : toUpdate) {
            creature.update();
        }
    }

    public void addpath(Creature target){
        if (tile(target.x(), target.y()).isGround()){
            //&& this.path(path.x(), path.y()) == null 且不与其他发生冲突，这个可以在吃掉之后写条件
            paths.add(target);
        }
    }

    public Creature path(int x, int y) {
        for (Creature p : this.paths) {
            if (p.x() == x && p.y() == y) {
                return p;
            }
        }
        return null;
    }

    public List<Creature> getPaths() {
        return this.paths;
    }

    public void removePath(Creature target) {
        this.paths.remove(target);
    }

    public void go(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height){
            paths.get(0).setstate(-1);
        }
        else if (tiles[x][y].isGround()) {
            int tempx = x;
            int tempy = y;
            for (Creature p : paths){
                int temp2x = p.x();
                int temp2y = p.y();
                p.setX(tempx);
                p.setY(tempy);
                tempx = temp2x;
                tempy = temp2y;
            }
        } else if (tiles[x][y].isDiggable()) {
            dig(x, y);
        }
    }

    public void belongger(int x, int y){
        if (tiles[x][y].isGround()) {
            int tempx = x;
            int tempy = y;
            for (Creature p : paths){
                int temp2x = p.x();
                int temp2y = p.y();
                p.setX(tempx);
                p.setY(tempy);
                tempx = temp2x;
                tempy = temp2y;
            }
            CreatureFactory creatureFactory = new CreatureFactory(this);
            creatureFactory.newbody(tempx, tempy, paths.size());
            // Creature newplayer = new Creature(this, (char)2, AsciiPanel.blue, 100, 20, 5, 9);
            // newplayer.setX(tempx);
            // newplayer.setY(tempy);
            // creatures.add(newplayer);
            // paths.add(newplayer);
        } else if (tiles[x][y].isDiggable()) {
            dig(x, y);
        }
    }
}
