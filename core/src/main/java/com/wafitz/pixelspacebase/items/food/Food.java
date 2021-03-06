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
package com.wafitz.pixelspacebase.items.food;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Badges;
import com.wafitz.pixelspacebase.Statistics;
import com.wafitz.pixelspacebase.actors.buffs.Buff;
import com.wafitz.pixelspacebase.actors.buffs.Hunger;
import com.wafitz.pixelspacebase.actors.buffs.Recharging;
import com.wafitz.pixelspacebase.actors.hero.Hero;
import com.wafitz.pixelspacebase.effects.Speck;
import com.wafitz.pixelspacebase.effects.SpellSprite;
import com.wafitz.pixelspacebase.items.Item;
import com.wafitz.pixelspacebase.items.scripts.RechargingScript;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.sprites.ItemSpriteSheet;
import com.wafitz.pixelspacebase.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

    private static final float TIME_TO_EAT = 3f;

    static final String AC_USE = "EAT";

    public float energy = Hunger.HUNGRY;
    public String message = Messages.get(this, "eat_msg");

    public int hornValue = 3;

    {
        stackable = true;
        image = ItemSpriteSheet.RATION;

        bones = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_USE)) {

            detach(hero.belongings.backpack);

            (hero.buff(Hunger.class)).satisfy(energy);
            GLog.i(message);

            switch (hero.heroClass) {
                case COMMANDER:
                    if (hero.HP < hero.HT) {
                        hero.HP = Math.min(hero.HP + 5, hero.HT);
                        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    }
                    break;
                case DM3000:
                    //1 charge
                    Buff.affect(hero, Recharging.class, 4f);
                    RechargingScript.charge(hero);
                    break;
                case SHAPESHIFTER:
                case CAPTAIN:
                    break;
            }

            hero.sprite.operate(hero.pos);
            hero.busy();
            SpellSprite.show(hero, SpellSprite.FOOD);
            Sample.INSTANCE.play(Assets.SND_EAT);

            hero.spend(TIME_TO_EAT);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int cost() {
        return 10 * quantity;
    }
}
