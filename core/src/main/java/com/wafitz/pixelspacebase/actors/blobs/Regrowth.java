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
package com.wafitz.pixelspacebase.actors.blobs;

import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.actors.Actor;
import com.wafitz.pixelspacebase.actors.Char;
import com.wafitz.pixelspacebase.actors.buffs.Buff;
import com.wafitz.pixelspacebase.actors.buffs.LockedDown;
import com.wafitz.pixelspacebase.effects.BlobEmitter;
import com.wafitz.pixelspacebase.effects.particles.LeafParticle;
import com.wafitz.pixelspacebase.levels.Level;
import com.wafitz.pixelspacebase.levels.Terrain;
import com.wafitz.pixelspacebase.scenes.GameScene;

public class Regrowth extends Blob {

    @Override
    protected void evolve() {
        super.evolve();

        if (volume > 0) {
            int cell;
            for (int i = area.left; i < area.right; i++) {
                for (int j = area.top; j < area.bottom; j++) {
                    cell = i + j * Dungeon.level.width();
                    if (off[cell] > 0) {
                        int c = Dungeon.level.map[cell];
                        int c1 = c;
                        if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO) {
                            c1 = cur[cell] > 9 ? Terrain.OFFVENT : Terrain.LIGHTEDVENT;
                        } else if (c == Terrain.LIGHTEDVENT && cur[cell] > 9 && Dungeon.level.mines.get(cell) == null) {
                            c1 = Terrain.OFFVENT;
                        }

                        if (c1 != c) {
                            Level.set(cell, c1);
                            GameScene.updateMap(cell);
                        }

                        Char ch = Actor.findChar(cell);
                        if (ch != null && off[cell] > 1) {
                            Buff.prolong(ch, LockedDown.class, TICK);
                        }
                    }
                }
            }
            Dungeon.observe();
        }
    }

    @Override
    public void use(BlobEmitter emitter) {
        super.use(emitter);

        emitter.start(LeafParticle.LEVEL_SPECIFIC, 0.2f, 0);
    }
}
