/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.wafitz.pixelspacebase.levels;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.DungeonTilemap;
import com.wafitz.pixelspacebase.actors.mobs.npcs.Wandmaker;
import com.wafitz.pixelspacebase.effects.Halo;
import com.wafitz.pixelspacebase.effects.particles.FlameParticle;
import com.wafitz.pixelspacebase.levels.Room.Type;
import com.wafitz.pixelspacebase.levels.traps.AlarmTrap;
import com.wafitz.pixelspacebase.levels.traps.ChillingTrap;
import com.wafitz.pixelspacebase.levels.traps.ConfusionTrap;
import com.wafitz.pixelspacebase.levels.traps.FireTrap;
import com.wafitz.pixelspacebase.levels.traps.FlashingTrap;
import com.wafitz.pixelspacebase.levels.traps.FlockTrap;
import com.wafitz.pixelspacebase.levels.traps.GrippingTrap;
import com.wafitz.pixelspacebase.levels.traps.LightningTrap;
import com.wafitz.pixelspacebase.levels.traps.OozeTrap;
import com.wafitz.pixelspacebase.levels.traps.ParalyticTrap;
import com.wafitz.pixelspacebase.levels.traps.PoisonTrap;
import com.wafitz.pixelspacebase.levels.traps.SpearTrap;
import com.wafitz.pixelspacebase.levels.traps.SummoningTrap;
import com.wafitz.pixelspacebase.levels.traps.TeleportationTrap;
import com.wafitz.pixelspacebase.levels.traps.ToxicTrap;
import com.wafitz.pixelspacebase.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class PrisonLevel extends RegularLevel {

    {
        color1 = 0x6a723d;
        color2 = 0x88924c;
    }

    @Override
    public String tilesTex() {
        return Assets.TILES_PRISON;
    }

    @Override
    public String waterTex() {
        return Assets.WATER_PRISON;
    }

    protected boolean[] water() {
        return Patch.generate(this, feeling == Feeling.WATER ? 0.65f : 0.45f, 4);
    }

    protected boolean[] grass() {
        return Patch.generate(this, feeling == Feeling.GRASS ? 0.60f : 0.40f, 3);
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{ChillingTrap.class, FireTrap.class, PoisonTrap.class, SpearTrap.class, ToxicTrap.class,
                AlarmTrap.class, FlashingTrap.class, GrippingTrap.class, ParalyticTrap.class, LightningTrap.class, OozeTrap.class,
                ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class,};
    }

    @Override
    protected float[] trapChances() {
        return new float[]{4, 4, 4, 4,
                2, 2, 2, 2, 2, 2,
                1, 1, 1, 1};
    }

    @Override
    protected boolean assignRoomType() {
        if (!super.assignRoomType()) return false;

        for (Room r : rooms) {
            if (r.type == Type.TUNNEL) {
                r.type = Type.PASSAGE;
            }
        }

        return Wandmaker.Quest.spawn(this, roomEntrance, rooms);
    }

    @Override
    protected void decorate() {

        for (int i = width() + 1; i < length() - width() - 1; i++) {
            if (map[i] == Terrain.EMPTY) {

                float c = 0.05f;
                if (map[i + 1] == Terrain.WALL && map[i + width()] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i - 1] == Terrain.WALL && map[i + width()] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i + 1] == Terrain.WALL && map[i - width()] == Terrain.WALL) {
                    c += 0.2f;
                }
                if (map[i - 1] == Terrain.WALL && map[i - width()] == Terrain.WALL) {
                    c += 0.2f;
                }

                if (Random.Float() < c) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i = 0; i < width(); i++) {
            if (map[i] == Terrain.WALL &&
                    (map[i + width()] == Terrain.EMPTY || map[i + width()] == Terrain.EMPTY_SP) &&
                    Random.Int(6) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        for (int i = width(); i < length() - width(); i++) {
            if (map[i] == Terrain.WALL &&
                    map[i - width()] == Terrain.WALL &&
                    (map[i + width()] == Terrain.EMPTY || map[i + width()] == Terrain.EMPTY_SP) &&
                    Random.Int(3) == 0) {

                map[i] = Terrain.WALL_DECO;
            }
        }

        placeSign();
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(PrisonLevel.class, "water_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(PrisonLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(PrisonLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    @Override
    public Group addVisuals() {
        super.addVisuals();
        addPrisonVisuals(this, visuals);
        return visuals;
    }

    public static void addPrisonVisuals(Level level, Group group) {
        for (int i = 0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WALL_DECO) {
                group.add(new Torch(i));
            }
        }
    }

    public static class Torch extends Emitter {

        private int pos;

        public Torch(int pos) {
            super();

            this.pos = pos;

            PointF p = DungeonTilemap.tileCenterToWorld(pos);
            pos(p.x - 1, p.y + 3, 2, 0);

            pour(FlameParticle.FACTORY, 0.15f);

            add(new Halo(16, 0xFFFFCC, 0.2f).point(p.x, p.y));
        }

        @Override
        public void update() {
            if (visible = Dungeon.visible[pos]) {
                super.update();
            }
        }
    }
}