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
package com.wafitz.pixelspacebase.items.blasters;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.actors.Actor;
import com.wafitz.pixelspacebase.actors.Char;
import com.wafitz.pixelspacebase.actors.buffs.Buff;
import com.wafitz.pixelspacebase.actors.buffs.Camoflage;
import com.wafitz.pixelspacebase.actors.buffs.LockedFloor;
import com.wafitz.pixelspacebase.actors.buffs.Recharging;
import com.wafitz.pixelspacebase.actors.buffs.SoulMark;
import com.wafitz.pixelspacebase.actors.hero.Hero;
import com.wafitz.pixelspacebase.actors.hero.HeroClass;
import com.wafitz.pixelspacebase.actors.hero.HeroSubClass;
import com.wafitz.pixelspacebase.effects.MagicMissile;
import com.wafitz.pixelspacebase.items.Item;
import com.wafitz.pixelspacebase.items.containers.BlasterHolster;
import com.wafitz.pixelspacebase.items.containers.Container;
import com.wafitz.pixelspacebase.items.weapon.melee.DM3000Launcher;
import com.wafitz.pixelspacebase.mechanics.Ballistica;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.scenes.CellSelector;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.ui.QuickSlotButton;
import com.wafitz.pixelspacebase.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Blaster extends Item {

    private static final int USAGES_TO_KNOW = 20;

    private static final String AC_SHOOT = "SHOOT";

    private static final float TIME_TO_ZAP = 1f;

    public int maxCharges = initialCharges();
    public int curCharges = maxCharges;
    public float partialCharge = 0f;

    private Charger charger;

    private boolean curChargeKnown = false;

    int usagesToKnow = USAGES_TO_KNOW;

    int collisionProperties = Ballistica.MAGIC_BOLT;

    {
        defaultAction = AC_SHOOT;
        usesTargeting = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (curCharges > 0 || !curChargeKnown) {
            actions.add(AC_SHOOT);
        }

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {

            curUser = hero;
            curItem = this;
            GameScene.selectCell(zapper);

        }
    }

    protected abstract void onZap(Ballistica attack);

    public abstract void onHit(DM3000Launcher launcher, Char attacker, Char defender, int damage);

    @Override
    public boolean collect(Container container) {
        if (super.collect(container)) {
            if (container.owner != null) {
                if (container instanceof BlasterHolster)
                    charge(container.owner, BlasterHolster.HOLSTER_SCALE_FACTOR);
                else
                    charge(container.owner);
            }
            return true;
        } else {
            return false;
        }
    }

    public void charge(Char owner) {
        if (charger == null) charger = new Charger();
        charger.attachTo(owner);
    }

    public void charge(Char owner, float chargeScaleFactor) {
        charge(owner);
        charger.setScaleFactor(chargeScaleFactor);
    }

    protected void processSoulMark(Char target, int chargesUsed) {
        if (target != Dungeon.hero &&
                Dungeon.hero.subClass == HeroSubClass.WARLOCK &&
                Random.Float() < .15f + (level() * chargesUsed * 0.03f)) {
            SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + level());
        }
    }

    @Override
    public void onDetach() {
        stopCharging();
    }

    public void stopCharging() {
        if (charger != null) {
            charger.detach();
            charger = null;
        }
    }

    public void level(int value) {
        super.level(value);
        updateLevel();
    }

    @Override
    public Item identify() {

        curChargeKnown = true;
        super.identify();

        updateQuickslot();

        return this;
    }

    @Override
    public String info() {
        String desc = desc();

        desc += "\n\n" + statsDesc();

        if (malfunctioning && malfunctioningKnown)
            desc += "\n\n" + Messages.get(Blaster.class, "malfunctioning");

        return desc;
    }

    public String statsDesc() {
        return Messages.get(this, "stats_desc");
    }

    @Override
    public boolean isIdentified() {
        return super.isIdentified() && curChargeKnown;
    }

    @Override
    public String status() {
        if (levelKnown) {
            return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
        } else {
            return null;
        }
    }

    @Override
    public Item upgrade() {

        super.upgrade();

        if (Random.Float() > Math.pow(0.9, level()))
            malfunctioning = false;

        updateLevel();
        curCharges = Math.min(curCharges + 1, maxCharges);
        updateQuickslot();

        return this;
    }

    @Override
    public Item degrade() {
        super.degrade();

        updateLevel();
        updateQuickslot();

        return this;
    }

    private void updateLevel() {
        maxCharges = Math.min(initialCharges() + level(), 10);
        curCharges = Math.min(curCharges, maxCharges);
    }

    protected int initialCharges() {
        return 2;
    }

    protected int chargesPerCast() {
        return 1;
    }

    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.whiteLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
        Sample.INSTANCE.play(Assets.SND_ZAP);
    }

    public void launcherFx(DM3000Launcher.launcherParticle particle) {
        particle.color(0xFFFFFF);
        particle.am = 0.3f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 2f);
        particle.setSize(1f, 2f);
        particle.radiateXY(0.5f);
    }

    void blasterUsed() {
        usagesToKnow -= malfunctioning ? 1 : chargesPerCast();
        curCharges -= malfunctioning ? 1 : chargesPerCast();
        if (!isIdentified() && usagesToKnow <= 0) {
            identify();
            GLog.w(Messages.get(Blaster.class, "identify", name()));
        } else {
            if (curUser.heroClass == HeroClass.DM3000) levelKnown = true;
            updateQuickslot();
        }

        curUser.spendAndNext(TIME_TO_ZAP);
    }

    @Override
    public Item random() {
        int n = 0;

        if (Random.Int(3) == 0) {
            n++;
            if (Random.Int(5) == 0) {
                n++;
            }
        }

        upgrade(n);
        if (Random.Float() < 0.3f) {
            malfunctioning = true;
            malfunctioningKnown = false;
        }

        return this;
    }

    @Override
    public int cost() {
        int price = 75;
        if (malfunctioning && malfunctioningKnown) {
            price /= 2;
        }
        if (levelKnown) {
            if (level() > 0) {
                price *= (level() + 1);
            } else if (level() < 0) {
                price /= (1 - level());
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String CUR_CHARGES = "curCharges";
    private static final String CUR_CHARGE_KNOWN = "curChargeKnown";
    private static final String PARTIALCHARGE = "partialCharge";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(UNFAMILIRIARITY, usagesToKnow);
        bundle.put(CUR_CHARGES, curCharges);
        bundle.put(CUR_CHARGE_KNOWN, curChargeKnown);
        bundle.put(PARTIALCHARGE, partialCharge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if ((usagesToKnow = bundle.getInt(UNFAMILIRIARITY)) == 0) {
            usagesToKnow = USAGES_TO_KNOW;
        }
        curCharges = bundle.getInt(CUR_CHARGES);
        curChargeKnown = bundle.getBoolean(CUR_CHARGE_KNOWN);
        partialCharge = bundle.getFloat(PARTIALCHARGE);
    }

    private static CellSelector.Listener zapper = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final Blaster curBlaster = (Blaster) Blaster.curItem;

                final Ballistica shot = new Ballistica(curUser.pos, target, curBlaster.collisionProperties);
                int cell = shot.collisionPos;

                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i(Messages.get(Blaster.class, "self_target"));
                    return;
                }

                curUser.sprite.zap(cell);

                //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                if (curBlaster.curCharges >= (curBlaster.malfunctioning ? 1 : curBlaster.chargesPerCast())) {

                    curUser.busy();

                    if (curBlaster.malfunctioning) {
                        MalfunctioningBlaster.malfunctioningZap(curBlaster, curUser, new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT));
                        if (!curBlaster.malfunctioningKnown) {
                            curBlaster.malfunctioningKnown = true;
                            GLog.n(Messages.get(Blaster.class, "malfunction_discover", curBlaster.name()));
                        }
                    } else {
                        curBlaster.fx(shot, new Callback() {
                            public void call() {
                                curBlaster.onZap(shot);
                                curBlaster.blasterUsed();
                            }
                        });
                    }

                    Camoflage.dispel();

                } else {

                    GLog.w(Messages.get(Blaster.class, "fizzles"));

                }

            }
        }

        @Override
        public String prompt() {
            return Messages.get(Blaster.class, "prompt");
        }
    };

    public class Charger extends Buff {

        private static final float BASE_CHARGE_DELAY = 10f;
        private static final float SCALING_CHARGE_ADDITION = 40f;
        private static final float NORMAL_SCALE_FACTOR = 0.875f;

        private static final float CHARGE_BUFF_BONUS = 0.25f;

        float scalingFactor = NORMAL_SCALE_FACTOR;

        @Override
        public boolean attachTo(Char target) {
            super.attachTo(target);

            return true;
        }

        @Override
        public boolean act() {
            if (curCharges < maxCharges)
                recharge();

            if (partialCharge >= 1 && curCharges < maxCharges) {
                partialCharge--;
                curCharges++;
                updateQuickslot();
            }

            spend(TICK);

            return true;
        }

        private void recharge() {
            int missingCharges = maxCharges - curCharges;

            float turnsToCharge = (float) (BASE_CHARGE_DELAY
                    + (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));

            LockedFloor lock = target.buff(LockedFloor.class);
            if (lock == null || lock.regenOn())
                partialCharge += 1f / turnsToCharge;

            Recharging bonus = target.buff(Recharging.class);
            if (bonus != null && bonus.remainder() > 0f) {
                partialCharge += CHARGE_BUFF_BONUS * bonus.remainder();
            }
        }

        public void gainCharge(float charge) {
            partialCharge += charge;
            while (partialCharge >= 1f) {
                curCharges++;
                partialCharge--;
            }
            curCharges = Math.min(curCharges, maxCharges);
            updateQuickslot();
        }

        private void setScaleFactor(float value) {
            this.scalingFactor = value;
        }
    }
}
