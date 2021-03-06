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
package com.wafitz.pixelspacebase.levels.vents;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.actors.mobs.Mob;
import com.wafitz.pixelspacebase.actors.mobs.OldWarBot;
import com.wafitz.pixelspacebase.effects.CellEmitter;
import com.wafitz.pixelspacebase.effects.Speck;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.sprites.OldWarBotSprite;
import com.wafitz.pixelspacebase.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class GuardianVent extends Vent {

    {
        color = RED;
        shape = STARS;
    }

    @Override
    public void activate() {

        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon(pos);
        }

        if (Dungeon.visible[pos]) {
            GLog.w(Messages.get(this, "alarm"));
            CellEmitter.center(pos).start(Speck.factory(Speck.SCREAM), 0.3f, 3);
        }

        Sample.INSTANCE.play(Assets.SND_ALERT);

        for (int i = 0; i < (Dungeon.depth - 5) / 5; i++) {
            Guardian guardian = new Guardian();
            guardian.state = guardian.WANDERING;
            guardian.pos = Dungeon.level.randomRespawnCell();
            GameScene.add(guardian);
            guardian.beckon(Dungeon.hero.pos);
        }

    }

    private static class Guardian extends OldWarBot {

        {
            spriteClass = GuardianSprite.class;

            EXP = 0;
            state = WANDERING;
        }

        public Guardian() {
            super();

            weapon.enhance(null);
            weapon.degrade(weapon.level());
        }

        @Override
        public void beckon(int cell) {
            //Beckon works on these ones, unlike their superclass.
            notice();

            if (state != HUNTING) {
                state = WANDERING;
            }
            target = cell;
        }

    }

    private static class GuardianSprite extends OldWarBotSprite {

        public GuardianSprite() {
            super();
            tint(0, 0, 1, 0.2f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(0, 0, 1, 0.2f);
        }
    }
}
