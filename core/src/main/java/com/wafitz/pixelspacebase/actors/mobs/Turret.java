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
package com.wafitz.pixelspacebase.actors.mobs;

import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.actors.Actor;
import com.wafitz.pixelspacebase.actors.Char;
import com.wafitz.pixelspacebase.actors.buffs.Terror;
import com.wafitz.pixelspacebase.effects.particles.ShadowParticle;
import com.wafitz.pixelspacebase.items.weapon.enhancements.Grim;
import com.wafitz.pixelspacebase.levels.Level;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.sprites.TurretSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Turret extends Mob {

    private static final float SPAWN_DELAY = 2f;

    private int level;

    {
        spriteClass = TurretSprite.class;

        HP = HT = 1;
        EXP = 0;

        flying = true;

        properties.add(Property.UNDEAD);
    }

    private static final String LEVEL = "level";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEVEL, level);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        level = bundle.getInt(LEVEL);
        adjustStats(level);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(1 + level / 2, 2 + level);
    }

    @Override
    public int attackSkill(Char target) {
        return 10 + level;
    }

    private void adjustStats(int level) {
        this.level = level;
        defenseSkill = attackSkill(null) * 5;
        enemySeen = true;
    }

    @Override
    public boolean reset() {
        state = WANDERING;
        return true;
    }

    public static void spawnAround(int pos) {
        for (int n : PathFinder.NEIGHBOURS4) {
            int cell = pos + n;
            if (Level.passable[cell] && Actor.findChar(cell) == null) {
                spawnAt(cell);
            }
        }
    }

    public static Turret spawnAt(int pos) {
        if (Level.passable[pos] && Actor.findChar(pos) == null) {

            Turret w = new Turret();
            w.adjustStats(Dungeon.depth);
            w.pos = pos;
            w.state = w.HUNTING;
            GameScene.add(w, SPAWN_DELAY);

            w.sprite.alpha(0);
            w.sprite.parent.add(new AlphaTweener(w.sprite, 1, 0.5f));

            w.sprite.emitter().burst(ShadowParticle.MALFUNCTION, 5);

            return w;
        } else {
            return null;
        }
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();

    static {
        IMMUNITIES.add(Grim.class);
        IMMUNITIES.add(Terror.class);
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
