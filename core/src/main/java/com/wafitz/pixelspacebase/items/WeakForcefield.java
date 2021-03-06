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
package com.wafitz.pixelspacebase.items;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Badges;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.actors.buffs.Buff;
import com.wafitz.pixelspacebase.actors.hero.Hero;
import com.wafitz.pixelspacebase.items.armor.Armor;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.sprites.ItemSpriteSheet;
import com.wafitz.pixelspacebase.utils.GLog;
import com.wafitz.pixelspacebase.windows.WndContainer;
import com.wafitz.pixelspacebase.windows.WndItem;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class WeakForcefield extends Item {

    private static final String AC_APPLY = "APPLY";

    //only to be used from the quickslot, for tutorial purposes mostly.
    private static final String AC_INFO = "INFO_WINDOW";

    {
        image = ItemSpriteSheet.FORCEFIELD;

        malfunctioningKnown = levelKnown = true;
        unique = true;
        bones = false;

        defaultAction = AC_INFO;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_APPLY);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_APPLY)) {
            curItem = this;
            GameScene.selectItem(armorSelector, WndContainer.Mode.ARMOR, Messages.get(this, "prompt"));
        } else if (action.equals(AC_INFO)) {
            GameScene.show(new WndItem(null, this, true));
        }
    }

    @Override
    //script of upgrade can be used directly once, same as upgrading armor the forcefield is applied to then removing it.
    public boolean isUpgradable() {
        return level() == 0;
    }

    private static WndContainer.Listener armorSelector = new WndContainer.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null && item instanceof Armor) {
                Armor armor = (Armor) item;
                if (!armor.levelKnown) {
                    GLog.w(Messages.get(WeakForcefield.class, "unknown_armor", armor.name()));
                } else if (armor.malfunctioning || armor.level() < 0) {
                    GLog.w(Messages.get(WeakForcefield.class, "malfunctioning_armor", armor.name()));
                } else {
                    GLog.p(Messages.get(WeakForcefield.class, "apply", armor.name()));
                    Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                    Sample.INSTANCE.play(Assets.SND_UNLOCK);
                    armor.applyForcefield((WeakForcefield) curItem);
                    curItem.detach(Dungeon.hero.belongings.backpack);
                    Badges.validateTutorial();
                }
            }
        }
    };

    public static class CommanderShield extends Buff {

        private Armor armor;
        private float partialShield;

        @Override
        public boolean act() {
            if (armor == null) detach();
            else if (armor.isEquipped((Hero) target)) {
                if (target.SHLD < maxShield()) {
                    partialShield += 1 / (35 * Math.pow(0.885f, (maxShield() - target.SHLD - 1)));
                }
            }
            while (partialShield >= 1) {
                target.SHLD++;
                partialShield--;
            }
            spend(TICK);
            return true;
        }

        public void setArmor(Armor arm) {
            armor = arm;
        }

        public int maxShield() {
            return 1 + armor.tier + armor.level();
        }
    }
}
