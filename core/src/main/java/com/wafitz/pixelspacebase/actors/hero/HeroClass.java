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
package com.wafitz.pixelspacebase.actors.hero;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.Badges;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.items.artifacts.CloakOfShadows;
import com.wafitz.pixelspacebase.items.potions.PotionOfHealing;
import com.wafitz.pixelspacebase.items.potions.PotionOfMindVision;
import com.wafitz.pixelspacebase.items.scrolls.ScrollOfMagicMapping;
import com.wafitz.pixelspacebase.items.scrolls.ScrollOfUpgrade;
import com.wafitz.pixelspacebase.items.wands.WandOfMagicMissile;
import com.wafitz.pixelspacebase.items.weapon.melee.Dagger;
import com.wafitz.pixelspacebase.items.weapon.melee.Knuckles;
import com.wafitz.pixelspacebase.items.weapon.melee.MagesStaff;
import com.wafitz.pixelspacebase.items.weapon.melee.WornShortsword;
import com.wafitz.pixelspacebase.items.weapon.missiles.Boomerang;
import com.wafitz.pixelspacebase.items.weapon.missiles.Dart;
import com.wafitz.pixelspacebase.messages.Messages;
import com.watabou.utils.Bundle;

public enum HeroClass {

    COMMANDER("warrior"),
    DM3000("mage"),
    SHAPESHIFTER("rogue"),
    CAPTAIN("huntress");

    private String title;

    HeroClass(String title) {
        this.title = title;
    }

    public void initHero(Hero hero) {

        hero.heroClass = this;

        initCommon(hero);

        switch (this) {
            case COMMANDER:
                initWarrior(hero);
                break;

            case DM3000:
                initMage(hero);
                break;

            case SHAPESHIFTER:
                initRogue(hero);
                break;

            case CAPTAIN:
                initHuntress(hero);
                break;
        }

        hero.updateAwareness();
    }

    private static void initCommon(Hero hero) {
        // wafitz.v1 - Hero has just woken up - should be naked I reckon - go hunt for clothes and weapons.

        //if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
        //    (hero.belongings.armor = new Uniform()).identify();

        //if (!Dungeon.isChallenged(Challenges.NO_FOOD))
        //    new Food().identify().collect();
    }

    public Badges.Badge masteryBadge() {
        switch (this) {
            case COMMANDER:
                return Badges.Badge.MASTERY_WARRIOR;
            case DM3000:
                return Badges.Badge.MASTERY_MAGE;
            case SHAPESHIFTER:
                return Badges.Badge.MASTERY_ROGUE;
            case CAPTAIN:
                return Badges.Badge.MASTERY_HUNTRESS;
        }
        return null;
    }

    private static void initWarrior(Hero hero) {
        (hero.belongings.weapon = new WornShortsword()).identify();
        Dart darts = new Dart(8);
        darts.identify().collect();

        // wafitz.v1 - Breaks naked plot
        /*if (Badges.isUnlocked(Badges.Badge.TUTORIAL_WARRIOR)) {
            if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
                hero.belongings.armor.affixSeal(new BrokenSeal());
            Dungeon.quickslot.setSlot(0, darts);
        } else {
            if (!Dungeon.isChallenged(Challenges.NO_ARMOR)) {
                BrokenSeal seal = new BrokenSeal();
                seal.collect();
                Dungeon.quickslot.setSlot(0, seal);
            }
            Dungeon.quickslot.setSlot(1, darts);
        }*/

        new PotionOfHealing().setKnown();
    }

    private static void initMage(Hero hero) {
        MagesStaff staff;

        if (Badges.isUnlocked(Badges.Badge.TUTORIAL_MAGE)) {
            staff = new MagesStaff(new WandOfMagicMissile());
        } else {
            staff = new MagesStaff();
            new WandOfMagicMissile().identify().collect();
        }

        (hero.belongings.weapon = staff).identify();
        hero.belongings.weapon.activate(hero);

        Dungeon.quickslot.setSlot(0, staff);

        new ScrollOfUpgrade().setKnown();
    }

    private static void initRogue(Hero hero) {
        (hero.belongings.weapon = new Dagger()).identify();

        CloakOfShadows cloak = new CloakOfShadows();
        (hero.belongings.misc1 = cloak).identify();
        hero.belongings.misc1.activate(hero);

        Dart darts = new Dart(8);
        darts.identify().collect();

        Dungeon.quickslot.setSlot(0, cloak);
        Dungeon.quickslot.setSlot(1, darts);

        new ScrollOfMagicMapping().setKnown();
    }

    private static void initHuntress(Hero hero) {

        (hero.belongings.weapon = new Knuckles()).identify();
        Boomerang boomerang = new Boomerang();
        boomerang.identify().collect();

        Dungeon.quickslot.setSlot(0, boomerang);

        new PotionOfMindVision().setKnown();
    }

    public String title() {
        return Messages.get(HeroClass.class, title);
    }

    public String spritesheet() {

        switch (this) {
            case COMMANDER:
                return Assets.COMMANDER;
            case DM3000:
                return Assets.DM3000;
            case SHAPESHIFTER:
                return Assets.SHAPESHIFTER;
            case CAPTAIN:
                return Assets.CAPTAIN;
        }

        return null;
    }

    public String[] perks() {

        switch (this) {
            case COMMANDER:
                return new String[]{
                        Messages.get(HeroClass.class, "warrior_perk1"),
                        Messages.get(HeroClass.class, "warrior_perk2"),
                        Messages.get(HeroClass.class, "warrior_perk3"),
                        Messages.get(HeroClass.class, "warrior_perk4"),
                        Messages.get(HeroClass.class, "warrior_perk5"),
                };
            case DM3000:
                return new String[]{
                        Messages.get(HeroClass.class, "mage_perk1"),
                        Messages.get(HeroClass.class, "mage_perk2"),
                        Messages.get(HeroClass.class, "mage_perk3"),
                        Messages.get(HeroClass.class, "mage_perk4"),
                        Messages.get(HeroClass.class, "mage_perk5"),
                };
            case SHAPESHIFTER:
                return new String[]{
                        Messages.get(HeroClass.class, "rogue_perk1"),
                        Messages.get(HeroClass.class, "rogue_perk2"),
                        Messages.get(HeroClass.class, "rogue_perk3"),
                        Messages.get(HeroClass.class, "rogue_perk4"),
                        Messages.get(HeroClass.class, "rogue_perk5"),
                        Messages.get(HeroClass.class, "rogue_perk6"),
                };
            case CAPTAIN:
                return new String[]{
                        Messages.get(HeroClass.class, "huntress_perk1"),
                        Messages.get(HeroClass.class, "huntress_perk2"),
                        Messages.get(HeroClass.class, "huntress_perk3"),
                        Messages.get(HeroClass.class, "huntress_perk4"),
                        Messages.get(HeroClass.class, "huntress_perk5"),
                };
        }

        return null;
    }

    private static final String CLASS = "class";

    public void storeInBundle(Bundle bundle) {
        bundle.put(CLASS, toString());
    }

    public static HeroClass restoreInBundle(Bundle bundle) {
        String value = bundle.getString(CLASS);
        return value.length() > 0 ? valueOf(value) : SHAPESHIFTER;
    }
}
