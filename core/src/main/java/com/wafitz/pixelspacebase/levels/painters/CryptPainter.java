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
import com.wafitz.pixelspacebase.items.Generator;
import com.wafitz.pixelspacebase.items.Heap.Type;
import com.wafitz.pixelspacebase.items.Item;
import com.wafitz.pixelspacebase.items.armor.Armor;
import com.wafitz.pixelspacebase.items.keys.IronKey;
import com.wafitz.pixelspacebase.levels.Level;
import com.wafitz.pixelspacebase.levels.Room;
import com.wafitz.pixelspacebase.levels.Terrain;
import com.watabou.utils.Point;

public class CryptPainter extends Painter {

    public static void paint(Level level, Room room) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY);

        Point c = room.center();
        int cx = c.x;
        int cy = c.y;

        Room.Door entrance = room.entrance();

        entrance.set(Room.Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey(Dungeon.depth));

        if (entrance.x == room.left) {
            set(level, new Point(room.right - 1, room.top + 1), Terrain.STATUE);
            set(level, new Point(room.right - 1, room.bottom - 1), Terrain.STATUE);
            cx = room.right - 2;
        } else if (entrance.x == room.right) {
            set(level, new Point(room.left + 1, room.top + 1), Terrain.STATUE);
            set(level, new Point(room.left + 1, room.bottom - 1), Terrain.STATUE);
            cx = room.left + 2;
        } else if (entrance.y == room.top) {
            set(level, new Point(room.left + 1, room.bottom - 1), Terrain.STATUE);
            set(level, new Point(room.right - 1, room.bottom - 1), Terrain.STATUE);
            cy = room.bottom - 2;
        } else if (entrance.y == room.bottom) {
            set(level, new Point(room.left + 1, room.top + 1), Terrain.STATUE);
            set(level, new Point(room.right - 1, room.top + 1), Terrain.STATUE);
            cy = room.top + 2;
        }

        level.drop(prize(level), cx + cy * level.width()).type = Type.TOMB;
    }

    private static Item prize(Level level) {

        //1 floor set higher than normal
        Armor prize = Generator.randomArmor((Dungeon.depth / 5) + 1);

        //if it isn't already malfunctioning, give it a free upgrade
        if (!prize.malfunctioning) {
            prize.upgrade();
            //malfunction the armor, unless it has a enhancement
            if (!prize.hasGoodEnhancement()) {
                prize.malfunctioning = prize.malfunctioningKnown = true;
                prize.inscribe(Armor.Enhancement.randomMalfunction());
            }
        }

        return prize;
    }
}
