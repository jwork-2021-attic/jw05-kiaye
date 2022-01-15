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

import java.awt.Color;
import java.io.Serializable;
import java.lang.Thread;


import com.screen.Screen;

/**
 *
 * @author Aeranythe Echosong
 */
public class Creature implements Serializable{

    private static final long serialVersionUID = 3L;
    private World world;

    private int score;

    private int state;

    private int x;

    public int getstate(){
        return this.state;
    }

    public void setstate(int x){
        this.state = x;
    }
    public int getscore(){
        return this.score;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int x() {
        return x;
    }

    private int y;

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    private int mx;
    
    public int getmx(){
        return this.mx;
    }
    public void setmx(int mx){
        this.mx = mx;
    }

    private int my;
    public int getmy(){
        return this.my;
    }
    public void setmy(int my){
        this.my = my;
    }
    private char glyph;

    public char glyph() {
        return this.glyph;
    }

    private Color color;

    public Color color() {
        return this.color;
    }

    private CreatureAI ai;

    public void setAI(CreatureAI ai) {
        this.ai = ai;
    }

    private int maxHP;

    public int maxHP() {
        return this.maxHP;
    }

    private int hp;

    public int hp() {
        return this.hp;
    }

    public void modifyHP(int amount) {
        this.hp += amount;

        if (this.hp < 1) {
            world.remove(this);
        }
    }

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    private int defenseValue;

    public int defenseValue() {
        return this.defenseValue;
    }

    private int visionRadius;

    public int visionRadius() {
        return this.visionRadius;
    }

    public boolean canSee(int wx, int wy) {
        return ai.canSee(wx, wy);
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public class creaturethread implements Runnable{
        public void run(){
            while(true){
                try{
                    Thread.sleep(250);
                    moveBy(mx, my);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void move(int mx, int my){
        this.setmx(mx);
        this.setmy(my);
        new Thread(new creaturethread()).start();

    }
    
    public int moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);
        Creature loseother = world.path(x + mx, y + my);
        if (loseother != null && loseother != this){
            //this.notify("Score: %d", this.score);
            //dead lose
            this.state = -1;
            return -1;
        }
        else if (other == null || loseother == this) {
            //ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
            world.go(this, x + mx, y + my);
            //this.notify("Score: %d", this.score);
            return 0;
        } else {
            attack(other);
            //ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
            //to be longger
            //world.go(x + mx, y + my);
            world.belongger(this, x + mx, y + my);
            this.score++;
            //this.notify("Score: %d", this.score);
            // if (world.getPaths().size() == world.height()*world.width()){
            //     this.state = 1;
            //     return 1;
            // }
            // else{
            CreatureFactory creaturefactory = new CreatureFactory(this.world);
            creaturefactory.newFungus();
            return 0;
            //}
        }
    }

    public void attack(Creature other) {
        int damage = Math.max(0, this.attackValue() - other.defenseValue());
        //damage = (int) (Math.random() * damage) + 1;

        other.modifyHP(-damage);

        // this.notify("You attack the '%s' for %d damage.", other.glyph, damage);
        // other.notify("The '%s' attacks you for %d damage.", glyph, damage);

    }

    public void update() {
        this.ai.onUpdate();
    }

    public boolean canEnter(int x, int y) {
        return world.tile(x, y).isGround();
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public Creature(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attackValue = attack;
        this.defenseValue = defense;
        this.visionRadius = visionRadius;
        this.score = 1;
        this.mx = 0;
        this.my = 0;
        this.state = 0;
    }
}
