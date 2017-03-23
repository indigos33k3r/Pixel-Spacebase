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
package com.wafitz.pixelspacebase.actors.buffs;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.actors.Char;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Camoflaged extends Camoflage {

    protected float left;

    private static final String LEFT = "left";

    {
        type = buffType.SILENT;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat(LEFT);
    }

    @Override
    public boolean attachTo(Char target) {
        if (super.attachTo(target)) {
            Sample.INSTANCE.play(Assets.SND_MELD);
            if (Dungeon.level != null)
                Dungeon.observe();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        Dungeon.observe();
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend(TICK * 2);

            if (--left <= 0 || Dungeon.hero.visibleEnemies() > 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    public void prolong() {
        left = 2;
    }

    @Override
    public int icon() {
        return BuffIndicator.CAMOFLAGED;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }
}
