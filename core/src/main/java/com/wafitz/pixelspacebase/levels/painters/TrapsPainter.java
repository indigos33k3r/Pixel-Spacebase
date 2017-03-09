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
package com.wafitz.pixelspacebase.levels.painters;

import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.PixelSpacebase;
import com.wafitz.pixelspacebase.items.Generator;
import com.wafitz.pixelspacebase.items.Heap;
import com.wafitz.pixelspacebase.items.Item;
import com.wafitz.pixelspacebase.items.potions.PotionOfLevitation;
import com.wafitz.pixelspacebase.levels.Level;
import com.wafitz.pixelspacebase.levels.Room;
import com.wafitz.pixelspacebase.levels.Terrain;
import com.wafitz.pixelspacebase.levels.traps.BlazingTrap;
import com.wafitz.pixelspacebase.levels.traps.ConfusionTrap;
import com.wafitz.pixelspacebase.levels.traps.DisintegrationTrap;
import com.wafitz.pixelspacebase.levels.traps.ExplosiveTrap;
import com.wafitz.pixelspacebase.levels.traps.FlockTrap;
import com.wafitz.pixelspacebase.levels.traps.GrimTrap;
import com.wafitz.pixelspacebase.levels.traps.ParalyticTrap;
import com.wafitz.pixelspacebase.levels.traps.SpearTrap;
import com.wafitz.pixelspacebase.levels.traps.SummoningTrap;
import com.wafitz.pixelspacebase.levels.traps.TeleportationTrap;
import com.wafitz.pixelspacebase.levels.traps.ToxicTrap;
import com.wafitz.pixelspacebase.levels.traps.Trap;
import com.wafitz.pixelspacebase.levels.traps.VenomTrap;
import com.wafitz.pixelspacebase.levels.traps.WarpingTrap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class TrapsPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);

        Class<? extends Trap> trapClass;
        switch (Random.Int(5)) {
            case 0:
            default:
                trapClass = SpearTrap.class;
                break;
            case 1:
                trapClass = !Dungeon.bossLevel(Dungeon.depth + 1) ? null : SummoningTrap.class;
                break;
            case 2:
            case 3:
            case 4:
                trapClass = Random.oneOf(levelTraps[Dungeon.depth / 5]);
                break;
        }

        if (trapClass == null) {
            fill(level, room, 1, Terrain.CHASM);
        } else {
            fill(level, room, 1, Terrain.TRAP);
        }

        Room.Door door = room.entrance();
        door.set(Room.Door.Type.REGULAR);

        int lastRow = level.map[room.left + 1 + (room.top + 1) * level.width()] == Terrain.CHASM ? Terrain.CHASM : Terrain.EMPTY;

        int x = -1;
        int y = -1;
        if (door.x == room.left) {
            x = room.right - 1;
            y = room.top + room.height() / 2;
            fill(level, x, room.top + 1, 1, room.height() - 1, lastRow);
        } else if (door.x == room.right) {
            x = room.left + 1;
            y = room.top + room.height() / 2;
            fill(level, x, room.top + 1, 1, room.height() - 1, lastRow);
        } else if (door.y == room.top) {
            x = room.left + room.width() / 2;
            y = room.bottom - 1;
            fill(level, room.left + 1, y, room.width() - 1, 1, lastRow);
        } else if (door.y == room.bottom) {
            x = room.left + room.width() / 2;
            y = room.top + 1;
            fill(level, room.left + 1, y, room.width() - 1, 1, lastRow);
        }

        for (Point p : room.getPoints()) {
            int cell = level.pointToCell(p);
            if (level.map[cell] == Terrain.TRAP) {
                try {
                    level.setTrap(trapClass.newInstance().reveal(), cell);
                } catch (Exception e) {
                    PixelSpacebase.reportException(e);
                }
            }
        }

        int pos = x + y * level.width();
        if (Random.Int(3) == 0) {
            if (lastRow == Terrain.CHASM) {
                set(level, pos, Terrain.EMPTY);
            }
            level.drop(prize(level), pos).type = Heap.Type.CHEST;
        } else {
            set(level, pos, Terrain.PEDESTAL);
            level.drop(prize(level), pos);
        }

        level.addItemToSpawn(new PotionOfLevitation());
    }

    private static Item prize(Level level) {

        Item prize;

        if (Random.Int(4) != 0) {
            prize = level.findPrizeItem();
            if (prize != null)
                return prize;
        }

        prize = Generator.random(Random.oneOf(
                Generator.Category.WEAPON,
                Generator.Category.ARMOR
        ));

        for (int i = 0; i < 3; i++) {
            Item another = Generator.random(Random.oneOf(
                    Generator.Category.WEAPON,
                    Generator.Category.ARMOR
            ));
            if (another.level() > prize.level()) {
                prize = another;
            }
        }

        return prize;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Trap>[][] levelTraps = new Class[][]{
            //sewers
            {ToxicTrap.class, TeleportationTrap.class, FlockTrap.class},
            //prison
            {ConfusionTrap.class, ExplosiveTrap.class, ParalyticTrap.class},
            //caves
            {BlazingTrap.class, VenomTrap.class, ExplosiveTrap.class},
            //city
            {WarpingTrap.class, VenomTrap.class, DisintegrationTrap.class},
            //halls, muahahahaha
            {GrimTrap.class}
    };
}