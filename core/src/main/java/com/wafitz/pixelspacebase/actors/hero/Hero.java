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
import com.wafitz.pixelspacebase.Bones;
import com.wafitz.pixelspacebase.Dungeon;
import com.wafitz.pixelspacebase.GamesInProgress;
import com.wafitz.pixelspacebase.Statistics;
import com.wafitz.pixelspacebase.actors.Actor;
import com.wafitz.pixelspacebase.actors.Char;
import com.wafitz.pixelspacebase.actors.buffs.Berserk;
import com.wafitz.pixelspacebase.actors.buffs.Buff;
import com.wafitz.pixelspacebase.actors.buffs.Camoflage;
import com.wafitz.pixelspacebase.actors.buffs.Combo;
import com.wafitz.pixelspacebase.actors.buffs.Fury;
import com.wafitz.pixelspacebase.actors.buffs.Hunger;
import com.wafitz.pixelspacebase.actors.buffs.Knockout;
import com.wafitz.pixelspacebase.actors.buffs.Paralysis;
import com.wafitz.pixelspacebase.actors.buffs.Regeneration;
import com.wafitz.pixelspacebase.actors.buffs.Shielding;
import com.wafitz.pixelspacebase.actors.buffs.Targeted;
import com.wafitz.pixelspacebase.actors.buffs.Upgrade;
import com.wafitz.pixelspacebase.actors.buffs.Vertigo;
import com.wafitz.pixelspacebase.actors.mobs.Mob;
import com.wafitz.pixelspacebase.actors.mobs.npcs.NPC;
import com.wafitz.pixelspacebase.effects.CellEmitter;
import com.wafitz.pixelspacebase.effects.CheckedCell;
import com.wafitz.pixelspacebase.effects.Flare;
import com.wafitz.pixelspacebase.effects.Speck;
import com.wafitz.pixelspacebase.items.Amulet;
import com.wafitz.pixelspacebase.items.Clone;
import com.wafitz.pixelspacebase.items.Dewdrop;
import com.wafitz.pixelspacebase.items.ExperimentalTech.ExperimentalTech;
import com.wafitz.pixelspacebase.items.ExperimentalTech.PowerUpgrade;
import com.wafitz.pixelspacebase.items.ExperimentalTech.StrengthUpgrade;
import com.wafitz.pixelspacebase.items.Heap;
import com.wafitz.pixelspacebase.items.Heap.Type;
import com.wafitz.pixelspacebase.items.Item;
import com.wafitz.pixelspacebase.items.KindOfWeapon;
import com.wafitz.pixelspacebase.items.armor.Armor;
import com.wafitz.pixelspacebase.items.armor.enhancements.EMP;
import com.wafitz.pixelspacebase.items.armor.enhancements.Flow;
import com.wafitz.pixelspacebase.items.armor.enhancements.Forcefield;
import com.wafitz.pixelspacebase.items.armor.enhancements.Obfuscation;
import com.wafitz.pixelspacebase.items.armor.enhancements.Speed;
import com.wafitz.pixelspacebase.items.armor.enhancements.Viscosity;
import com.wafitz.pixelspacebase.items.artifacts.GravityGun;
import com.wafitz.pixelspacebase.items.artifacts.HoloPad;
import com.wafitz.pixelspacebase.items.artifacts.StrongForcefield;
import com.wafitz.pixelspacebase.items.artifacts.SurveyorModule;
import com.wafitz.pixelspacebase.items.artifacts.SurvivalModule;
import com.wafitz.pixelspacebase.items.artifacts.TimeFolder;
import com.wafitz.pixelspacebase.items.keys.Key;
import com.wafitz.pixelspacebase.items.modules.AttackModule;
import com.wafitz.pixelspacebase.items.modules.ElementsModule;
import com.wafitz.pixelspacebase.items.modules.EvasionModule;
import com.wafitz.pixelspacebase.items.modules.ForceModule;
import com.wafitz.pixelspacebase.items.modules.PowerModule;
import com.wafitz.pixelspacebase.items.modules.SpeedModule;
import com.wafitz.pixelspacebase.items.modules.SteelModule;
import com.wafitz.pixelspacebase.items.scripts.EnhancementScript;
import com.wafitz.pixelspacebase.items.scripts.MappingScript;
import com.wafitz.pixelspacebase.items.scripts.Script;
import com.wafitz.pixelspacebase.items.scripts.UpgradeScript;
import com.wafitz.pixelspacebase.items.weapon.Weapon;
import com.wafitz.pixelspacebase.items.weapon.melee.Flail;
import com.wafitz.pixelspacebase.items.weapon.missiles.MissileWeapon;
import com.wafitz.pixelspacebase.levels.Level;
import com.wafitz.pixelspacebase.levels.Terrain;
import com.wafitz.pixelspacebase.levels.features.Chasm;
import com.wafitz.pixelspacebase.levels.features.CraftingTerminal;
import com.wafitz.pixelspacebase.levels.features.Sign;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.mines.Healing;
import com.wafitz.pixelspacebase.mines.WeakForcefield;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.scenes.InterlevelScene;
import com.wafitz.pixelspacebase.scenes.SurfaceScene;
import com.wafitz.pixelspacebase.sprites.CharSprite;
import com.wafitz.pixelspacebase.sprites.HeroSprite;
import com.wafitz.pixelspacebase.ui.AttackIndicator;
import com.wafitz.pixelspacebase.ui.BuffIndicator;
import com.wafitz.pixelspacebase.ui.QuickSlotButton;
import com.wafitz.pixelspacebase.ui.StatusPane;
import com.wafitz.pixelspacebase.utils.BArray;
import com.wafitz.pixelspacebase.utils.GLog;
import com.wafitz.pixelspacebase.windows.WndBotMake;
import com.wafitz.pixelspacebase.windows.WndMessage;
import com.wafitz.pixelspacebase.windows.WndResurrect;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static com.wafitz.pixelspacebase.items.ExperimentalTech.HealingTech.heal;

public class Hero extends Char {

    {
        actPriority = 0; //acts at priority 0, baseline for the rest of behaviour.
    }

    public static final int MAX_LEVEL = 30;

    private static final int STARTING_STR = 10;

    private static final float TIME_TO_REST = 1f;
    private static final float TIME_TO_SEARCH = 2f;

    public HeroClass heroClass = HeroClass.SHAPESHIFTER;
    public HeroSubClass subClass = HeroSubClass.NONE;

    private int attackSkill = 10;
    private int defenseSkill = 5;

    public boolean ready = false;
    private boolean damageInterrupt = true;
    public HeroAction curAction = null;
    public HeroAction lastAction = null;

    private Char enemy;

    private Item theKey;

    public boolean resting = false;

    public MissileWeapon rangedWeapon = null;
    public Belongings belongings;

    public int STR;
    public boolean weakened = false;

    private float awareness;

    public int lvl = 1;
    public int exp = 0;

    private ArrayList<Mob> visibleEnemies;

    //This list is maintained so that some logic checks can be skipped
    // for enemies we know we aren't seeing normally, resultign in better performance
    public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

    public Hero() {
        super();
        name = Messages.get(this, "name");

        HP = HT = 20;
        STR = STARTING_STR;
        awareness = 0.1f;

        belongings = new Belongings(this);

        visibleEnemies = new ArrayList<>();
    }

    public int STR() {
        int STR = this.STR;

        STR += PowerModule.getBonus(this, PowerModule.Might.class);

        return weakened ? STR - 2 : STR;
    }

    private static final String ATTACK = "attackSkill";
    private static final String DEFENSE = "defenseSkill";
    private static final String STRENGTH = "STR";
    private static final String LEVEL = "lvl";
    private static final String EXPERIENCE = "exp";

    @Override
    public void storeInBundle(Bundle bundle) {

        super.storeInBundle(bundle);

        heroClass.storeInBundle(bundle);
        subClass.storeInBundle(bundle);

        bundle.put(ATTACK, attackSkill);
        bundle.put(DEFENSE, defenseSkill);

        bundle.put(STRENGTH, STR);

        bundle.put(LEVEL, lvl);
        bundle.put(EXPERIENCE, exp);

        belongings.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        heroClass = HeroClass.restoreInBundle(bundle);
        subClass = HeroSubClass.restoreInBundle(bundle);

        attackSkill = bundle.getInt(ATTACK);
        defenseSkill = bundle.getInt(DEFENSE);

        STR = bundle.getInt(STRENGTH);
        updateAwareness();

        lvl = bundle.getInt(LEVEL);
        exp = bundle.getInt(EXPERIENCE);

        belongings.restoreFromBundle(bundle);
    }

    public static void preview(GamesInProgress.Info info, Bundle bundle) {
        info.level = bundle.getInt(LEVEL);
    }

    public String className() {
        return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
    }

    public String givenName() {
        return name.equals(Messages.get(this, "name")) ? className() : name;
    }

    public void live() {
        Buff.affect(this, Regeneration.class);
        Buff.affect(this, Hunger.class);
    }

    public int tier() {
        return belongings.armor == null ? 0 : belongings.armor.tier;
    }

    public boolean shoot(Char enemy, MissileWeapon wep) {

        rangedWeapon = wep;
        boolean result = attack(enemy);
        Camoflage.dispel();
        rangedWeapon = null;

        return result;
    }

    @Override
    public int attackSkill(Char target) {
        float accuracy = 1;
        if (rangedWeapon != null && Dungeon.level.distance(pos, target.pos) == 1) {
            accuracy *= 0.5f;
        }

        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
        if (wep != null) {
            return (int) (attackSkill * accuracy * wep.accuracyFactor(this));
        } else {
            return (int) (attackSkill * accuracy);
        }
    }

    @Override
    public int defenseSkill(Char enemy) {

        int bonus = EvasionModule.getBonus(this, EvasionModule.Evasion.class);

        float evasion = (float) Math.pow(1.125, bonus);
        if (paralysed > 0) {
            evasion /= 2;
        }

        int aEnc = belongings.armor != null ? belongings.armor.STRReq() - STR() : 10 - STR();

        if (aEnc > 0) {
            return (int) (defenseSkill * evasion / Math.pow(1.5, aEnc));
        } else {

            bonus = 0;
            if (heroClass == HeroClass.SHAPESHIFTER) bonus += -aEnc;

            if (belongings.armor != null && belongings.armor.hasEnhancement(Speed.class))
                bonus += 5 + belongings.armor.level() * 1.5f;

            return Math.round((defenseSkill + bonus) * evasion);
        }
    }

    @Override
    public int drRoll() {
        int dr = 0;
        Shielding bark = buff(Shielding.class);

        if (belongings.armor != null) {
            dr += Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax());
            if (STR() < belongings.armor.STRReq()) {
                dr -= 2 * (belongings.armor.STRReq() - STR());
                dr = Math.max(dr, 0);
            }
        }
        if (belongings.weapon != null)
            dr += Random.NormalIntRange(0, belongings.weapon.defenseFactor(this));
        if (bark != null) dr += Random.NormalIntRange(0, bark.level());

        return dr;
    }

    @Override
    public int damageRoll() {
        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
        int dmg;
        int bonus = ForceModule.getBonus(this, ForceModule.Force.class);

        if (wep != null) {
            dmg = wep.damageRoll(this) + bonus;
        } else {
            if (bonus != 0) {
                dmg = ForceModule.damageRoll(this);
            } else {
                dmg = Random.NormalIntRange(1, Math.max(STR() - 8, 1));
            }
        }
        if (dmg < 0) dmg = 0;
        if (subClass == HeroSubClass.BERSERKER) {
            berserk = Buff.affect(this, Berserk.class);
            dmg = berserk.damageFactor(dmg);
        }
        return buff(Fury.class) != null ? (int) (dmg * 1.5f) : dmg;
    }

    @Override
    public float speed() {

        float speed = super.speed();

        int hasteLevel = SpeedModule.getBonus(this, SpeedModule.Haste.class);

        if (hasteLevel != 0)
            speed *= Math.pow(1.2, hasteLevel);

        Armor armor = belongings.armor;

        if (armor != null) {

            if (armor.hasEnhancement(Speed.class)) {
                speed *= (1.1f + 0.01f * belongings.armor.level());
            } else if (armor.hasEnhancement(Flow.class) && Level.water[pos]) {
                speed *= (1.5f + 0.05f * belongings.armor.level());
            }
        }

        int aEnc = armor != null ? armor.STRReq() - STR() : 0;
        if (aEnc > 0) {

            return (float) (speed / Math.pow(1.2, aEnc));

        } else {

            return ((HeroSprite) sprite).sprint(subClass == HeroSubClass.FREERUNNER && !isStarving()) ?
                    invisible > 0 ?
                            2f * speed :
                            1.5f * speed :
                    speed;

        }
    }

    public boolean canSurpriseAttack() {
        if (belongings.weapon == null || !(belongings.weapon instanceof Weapon))
            return true;

        if (STR() < ((Weapon) belongings.weapon).STRReq())
            return false;

        return !(belongings.weapon instanceof Flail && rangedWeapon == null);

    }

    public boolean canAttack(Char enemy) {
        if (enemy == null || pos == enemy.pos)
            return false;

        //can always attack adjacent enemies
        if (Dungeon.level.adjacent(pos, enemy.pos))
            return true;

        KindOfWeapon wep = Dungeon.hero.belongings.weapon;

        if (wep != null && Dungeon.level.distance(pos, enemy.pos) <= wep.reachFactor(this)) {

            boolean[] passable = BArray.not(Level.solid, null);
            for (Mob m : Dungeon.level.mobs)
                passable[m.pos] = false;

            PathFinder.buildDistanceMap(enemy.pos, passable, wep.reachFactor(this));

            return PathFinder.distance[pos] <= wep.reachFactor(this);

        } else {
            return false;
        }
    }

    public float attackDelay() {
        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
        if (wep != null) {

            return wep.speedFactor(this);

        } else {
            //Normally putting furor speed on unarmed attacks would be unnecessary
            //But there's going to be that one guy who gets a furor+force ring combo
            //This is for that one guy, you shall get your fists of fury!
            int bonus = AttackModule.getBonus(this, AttackModule.Furor.class);
            return (float) (0.2 + (1 - 0.2) * Math.pow(0.85, bonus));
        }
    }

    @Override
    public void spend(float time) {
        TimeFolder.timeFreeze buff = buff(TimeFolder.timeFreeze.class);
        if (!(buff != null && buff.processTime(time)))
            super.spend(time);
    }

    public void spendAndNext(float time) {
        busy();
        spend(time);
        next();
    }

    @Override
    public boolean act() {

        super.act();

        if (paralysed > 0) {

            curAction = null;

            spendAndNext(TICK);
            return false;
        }

        checkVisibleMobs();


        if (curAction == null) {

            if (resting) {
                spend(TIME_TO_REST);
                next();
                return false;
            }

            ready();
            return false;

        } else {

            resting = false;

            ready = false;

            if (curAction instanceof HeroAction.Move) {

                return actMove((HeroAction.Move) curAction);

            } else if (curAction instanceof HeroAction.Interact) {

                return actInteract((HeroAction.Interact) curAction);

            } else if (curAction instanceof HeroAction.WorkshopMake) {

                return actMake((HeroAction.WorkshopMake) curAction);

            } else if (curAction instanceof HeroAction.PickUp) {

                return actPickUp((HeroAction.PickUp) curAction);

            } else if (curAction instanceof HeroAction.OpenChest) {

                return actOpenChest((HeroAction.OpenChest) curAction);

            } else if (curAction instanceof HeroAction.Unlock) {

                return actUnlock((HeroAction.Unlock) curAction);

            } else if (curAction instanceof HeroAction.Descend) {

                return actDescend((HeroAction.Descend) curAction);

            } else if (curAction instanceof HeroAction.Ascend) {

                return actAscend((HeroAction.Ascend) curAction);

            } else if (curAction instanceof HeroAction.Attack) {

                return actAttack((HeroAction.Attack) curAction);

            } else if (curAction instanceof HeroAction.Make) {

                return actMake((HeroAction.Make) curAction);

            }
        }

        return false;
    }

    public void busy() {
        ready = false;
    }

    private void ready() {
        if (sprite.looping()) sprite.idle();
        curAction = null;
        damageInterrupt = true;
        ready = true;

        AttackIndicator.updateState();

        GameScene.ready();
    }

    public void interrupt() {
        if (isAlive() && curAction != null && curAction instanceof HeroAction.Move && curAction.dst != pos) {
            lastAction = curAction;
        }
        curAction = null;
    }

    public void resume() {
        curAction = lastAction;
        lastAction = null;
        damageInterrupt = false;
        next();
    }

    private boolean actMove(HeroAction.Move action) {

        if (getCloser(action.dst)) {

            return true;

        } else {
            if (Dungeon.level.map[pos] == Terrain.SIGN) {
                Sign.read(pos);
            }
            ready();

            return false;
        }
    }

    private boolean actInteract(HeroAction.Interact action) {

        NPC npc = action.npc;

        if (Dungeon.level.adjacent(pos, npc.pos)) {

            ready();
            sprite.turnTo(pos, npc.pos);
            return npc.interact();

        } else {

            if (Level.fieldOfView[npc.pos] && getCloser(npc.pos)) {

                return true;

            } else {
                ready();
                return false;
            }

        }
    }

    private boolean actMake(HeroAction.WorkshopMake action) {
        int dst = action.dst;
        if (pos == dst || Dungeon.level.adjacent(pos, dst)) {

            ready();

            Heap heap = Dungeon.level.heaps.get(dst);
            if (heap != null && heap.type == Type.TO_MAKE && heap.size() == 1) {
                GameScene.show(new WndBotMake(heap, true));
            }

            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actMake(HeroAction.Make action) {
        int dst = action.dst;
        if (Dungeon.visible[dst]) {

            ready();
            CraftingTerminal.operate(this, dst);
            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actPickUp(HeroAction.PickUp action) {
        int dst = action.dst;
        if (pos == dst) {

            Heap heap = Dungeon.level.heaps.get(pos);
            if (heap != null) {
                Item item = heap.peek();
                if (item.doPickUp(this)) {
                    heap.pickUp();

                    if (item instanceof Dewdrop
                            || item instanceof TimeFolder.TimeBattery
                            || item instanceof HoloPad.HoloBattery
                            || item instanceof Key) {
                        //Do Nothing
                    } else {

                        boolean important =
                                ((item instanceof UpgradeScript || item instanceof EnhancementScript) && ((Script) item).isKnown()) ||
                                        ((item instanceof StrengthUpgrade || item instanceof PowerUpgrade) && ((ExperimentalTech) item).isKnown());
                        if (important) {
                            GLog.p(Messages.get(this, "you_now_have", item.name()));
                        } else {
                            GLog.i(Messages.get(this, "you_now_have", item.name()));
                        }
                    }

                    if (!heap.isEmpty()) {
                        GLog.i(Messages.get(this, "something_else"));
                    }
                    curAction = null;
                } else {
                    heap.sprite.drop();
                    ready();
                }
            } else {
                ready();
            }

            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actOpenChest(HeroAction.OpenChest action) {
        int dst = action.dst;
        if (Dungeon.level.adjacent(pos, dst) || pos == dst) {

            Heap heap = Dungeon.level.heaps.get(dst);
            if (heap != null && (heap.type != Type.HEAP && heap.type != Type.TO_MAKE)) {

                if ((heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST)
                        && belongings.specialKeys[Dungeon.depth] < 1) {

                    GLog.w(Messages.get(this, "locked_chest"));
                    ready();
                    return false;

                }

                switch (heap.type) {
                    case TOMB:
                        Sample.INSTANCE.play(Assets.SND_TOMB);
                        Camera.main.shake(1, 0.5f);
                        break;
                    case SKELETON:
                    case REMAINS:
                        break;
                    default:
                        Sample.INSTANCE.play(Assets.SND_UNLOCK);
                }

                spend(Key.TIME_TO_UNLOCK);
                sprite.operate(dst);

            } else {
                ready();
            }

            return false;

        } else if (getCloser(dst)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actUnlock(HeroAction.Unlock action) {
        int doorCell = action.dst;
        if (Dungeon.level.adjacent(pos, doorCell)) {

            boolean hasKey = false;
            int door = Dungeon.level.map[doorCell];

            if (door == Terrain.LOCKED_DOOR
                    && belongings.ironKeys[Dungeon.depth] > 0) {

                hasKey = true;

            } else if (door == Terrain.LOCKED_EXIT
                    && belongings.specialKeys[Dungeon.depth] > 0) {

                hasKey = true;

            }

            if (hasKey) {

                spend(Key.TIME_TO_UNLOCK);
                sprite.operate(doorCell);

                Sample.INSTANCE.play(Assets.SND_UNLOCK);

            } else {
                GLog.w(Messages.get(this, "locked_door"));
                ready();
            }

            return false;

        } else if (getCloser(doorCell)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actDescend(HeroAction.Descend action) {
        int stairs = action.dst;
        if (pos == stairs && pos == Dungeon.level.exit) {

            curAction = null;

            Buff buff = buff(TimeFolder.timeFreeze.class);
            if (buff != null) buff.detach();

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                if (mob instanceof HoloPad.HologramHero) mob.destroy();

            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene(InterlevelScene.class);

            return false;

        } else if (getCloser(stairs)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAscend(HeroAction.Ascend action) {
        int stairs = action.dst;
        if (pos == stairs && pos == Dungeon.level.entrance) {

            if (Dungeon.depth == 1) {

                if (belongings.getItem(Amulet.class) == null) {
                    GameScene.show(new WndMessage(Messages.get(this, "leave")));
                    ready();
                } else {
                    Dungeon.win(Amulet.class);
                    Dungeon.deleteGame(Dungeon.hero.heroClass, true);
                    Game.switchScene(SurfaceScene.class);
                }

            } else {

                curAction = null;

                Hunger hunger = buff(Hunger.class);
                if (hunger != null && !hunger.isStarving()) {
                    hunger.reduceHunger(-Hunger.STARVING / 10);
                }

                Buff buff = buff(TimeFolder.timeFreeze.class);
                if (buff != null) buff.detach();

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
                    if (mob instanceof HoloPad.HologramHero) mob.destroy();

                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene(InterlevelScene.class);
            }

            return false;

        } else if (getCloser(stairs)) {

            return true;

        } else {
            ready();
            return false;
        }
    }

    private boolean actAttack(HeroAction.Attack action) {

        enemy = action.target;

        if (enemy.isAlive() && canAttack(enemy) && !isCharmedBy(enemy)) {

            Camoflage.dispel();
            spend(attackDelay());
            sprite.attack(enemy.pos);

            return false;

        } else {

            if (Level.fieldOfView[enemy.pos] && getCloser(enemy.pos)) {

                return true;

            } else {
                ready();
                return false;
            }

        }
    }

    public Char enemy() {
        return enemy;
    }

    public void rest(boolean fullRest) {
        spendAndNext(TIME_TO_REST);
        if (!fullRest) {
            sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
        }
        resting = fullRest;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;

        if (wep != null) damage = wep.proc(this, enemy, damage);

        switch (subClass) {
            case SNIPER:
                if (rangedWeapon != null) {
                    Buff.prolong(this, Targeted.class, attackDelay() * 1.1f).object = enemy.id();
                }
                break;
            default:
        }


        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {

        WeakForcefield.Armor armor = buff(WeakForcefield.Armor.class);
        if (armor != null) {
            damage = armor.absorb(damage);
        }

        Healing.Health health = buff(Healing.Health.class);
        if (health != null) {
            health.absorb(damage);
        }

        if (belongings.armor != null) {
            damage = belongings.armor.proc(enemy, this, damage);
        }

        return damage;
    }

    @Override
    public void damage(int dmg, Object src) {
        if (buff(TimeFolder.timeStasis.class) != null)
            return;

        if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
            interrupt();
            resting = false;
        }

        if (this.buff(Knockout.class) != null) {
            Buff.detach(this, Knockout.class);
            GLog.w(Messages.get(this, "pain_resist"));
        }

        StrongForcefield.Shield shield = buff(StrongForcefield.Shield.class);
        if (shield != null) {
            dmg = shield.proc(dmg, (src instanceof Char ? (Char) src : null), this);
        }

        int tenacity = SteelModule.getBonus(this, SteelModule.Tenacity.class);
        if (tenacity != 0) //(HT - HP)/HT = heroes current % missing health.
            dmg = (int) Math.ceil((float) dmg * Math.pow(0.85, tenacity * ((float) (HT - HP) / HT)));

        //TODO improve this when I have proper damage source logic
        if (belongings.armor != null && belongings.armor.hasEnhancement(EMP.class)
                && ElementsModule.FULL.contains(src.getClass())) {
            dmg -= Random.NormalIntRange(belongings.armor.DRMin(), belongings.armor.DRMax()) / 2;
        }

        if (subClass == HeroSubClass.BERSERKER && berserk == null) {
            berserk = Buff.affect(this, Berserk.class);
        }

        super.damage(dmg, src);
    }

    private void checkVisibleMobs() {
        ArrayList<Mob> visible = new ArrayList<>();

        boolean newMob = false;

        Mob target = null;
        for (Mob m : Dungeon.level.mobs) {
            if (Level.fieldOfView[m.pos] && m.hostile) {
                visible.add(m);
                if (!visibleEnemies.contains(m)) {
                    newMob = true;
                }

                if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1) {
                    if (target == null) {
                        target = m;
                    } else if (distance(target) > distance(m)) {
                        target = m;
                    }
                }
            }
        }

        if (target != null && (QuickSlotButton.lastTarget == null ||
                !QuickSlotButton.lastTarget.isAlive() ||
                !Dungeon.visible[QuickSlotButton.lastTarget.pos])) {
            QuickSlotButton.target(target);
        }

        if (newMob) {
            interrupt();
            resting = false;
        }

        visibleEnemies = visible;
    }

    public int visibleEnemies() {
        return visibleEnemies.size();
    }

    public Mob visibleEnemy(int index) {
        return visibleEnemies.get(index % visibleEnemies.size());
    }

    private boolean getCloser(final int target) {

        if (target == pos)
            return false;

        if (rooted) {
            Camera.main.shake(1, 1f);
            return false;
        }

        int step = -1;

        if (Dungeon.level.adjacent(pos, target)) {

            path = null;

            if (Actor.findChar(target) == null) {
                if (Level.pit[target] && !flying && !Level.solid[target]) {
                    if (!Chasm.jumpConfirmed) {
                        Chasm.heroJump(this);
                        interrupt();
                    } else {
                        Chasm.heroFall(target);
                    }
                    return false;
                }
                if (Level.passable[target] || Level.avoid[target]) {
                    step = target;
                }
            }

        } else {

            boolean newPath = false;
            if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
                newPath = true;
            else if (path.getLast() != target)
                newPath = true;
            else {
                //looks ahead for path validity, up to length-1 or 2.
                //Note that this is shorter than for mobs, so that mobs usually yield to the hero
                int lookAhead = (int) GameMath.gate(0, path.size() - 1, 2);
                for (int i = 0; i < lookAhead; i++) {
                    int cell = path.get(i);
                    if (!Level.passable[cell] || (Dungeon.visible[cell] && Actor.findChar(cell) != null)) {
                        newPath = true;
                        break;
                    }
                }
            }

            if (newPath) {

                int len = Dungeon.level.length();
                boolean[] p = Level.passable;
                boolean[] v = Dungeon.level.visited;
                boolean[] m = Dungeon.level.mapped;
                boolean[] passable = new boolean[len];
                for (int i = 0; i < len; i++) {
                    passable[i] = p[i] && (v[i] || m[i]);
                }

                path = Dungeon.findPath(this, pos, target, passable, Level.fieldOfView);
            }

            if (path == null) return false;
            step = path.removeFirst();

        }

        if (step != -1) {

            int moveTime = 1;
            if (belongings.armor != null && belongings.armor.hasEnhancement(Forcefield.class) &&
                    (Dungeon.level.map[pos] == Terrain.DOOR
                            || Dungeon.level.map[pos] == Terrain.OPEN_DOOR
                            || Dungeon.level.map[step] == Terrain.DOOR
                            || Dungeon.level.map[step] == Terrain.OPEN_DOOR)) {
                moveTime *= 2;
            }
            sprite.move(pos, step);
            move(step);

            spend(moveTime / speed());

            return true;

        } else {

            return false;

        }

    }

    public boolean handle(int cell) {

        if (cell == -1) {
            return false;
        }

        Char ch;
        Heap heap;

        if (Dungeon.level.map[cell] == Terrain.CRAFTING && cell != pos) {

            curAction = new HeroAction.Make(cell);

        } else if (Level.fieldOfView[cell] && (ch = Actor.findChar(cell)) instanceof Mob) {

            if (ch instanceof NPC) {
                curAction = new HeroAction.Interact((NPC) ch);
            } else {
                curAction = new HeroAction.Attack(ch);
            }

        } else if ((heap = Dungeon.level.heaps.get(cell)) != null) {
            // wafitz.v1: Auto pickup no matter what

            switch (heap.type) {
                case HEAP:
                    curAction = new HeroAction.PickUp(cell);
                    break;
                case TO_MAKE:
                    curAction = heap.size() == 1 && heap.peek().cost() > 0 ?
                            new HeroAction.WorkshopMake(cell) :
                            new HeroAction.PickUp(cell);
                    break;
                default:
                    curAction = new HeroAction.OpenChest(cell);
            }

        } else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

            curAction = new HeroAction.Unlock(cell);

        } else if (cell == Dungeon.level.exit && Dungeon.depth < 26) {

            curAction = new HeroAction.Descend(cell);

        } else if (cell == Dungeon.level.entrance) {

            curAction = new HeroAction.Ascend(cell);

        } else {

            curAction = new HeroAction.Move(cell);
            lastAction = null;

        }

        return true;
    }

    public void earnExp(int exp) {

        this.exp += exp;
        float percent = exp / (float) maxExp();

        GravityGun.gravityRecharge chains = buff(GravityGun.gravityRecharge.class);
        if (chains != null) chains.gainExp(percent);

        SurvivalModule.hornRecharge horn = buff(SurvivalModule.hornRecharge.class);
        if (horn != null) horn.gainCharge(percent);

        if (subClass == HeroSubClass.BERSERKER) {
            berserk = Buff.affect(this, Berserk.class);
            berserk.recover(percent);
        }

        boolean levelUp = false;
        while (this.exp >= maxExp()) {
            this.exp -= maxExp();
            if (lvl < MAX_LEVEL) {
                lvl++;
                levelUp = true;

                HT += 5;
                HP += 5;
                attackSkill++;
                defenseSkill++;

            } else {
                Buff.prolong(this, Upgrade.class, 30f);
                this.exp = 0;

                GLog.p(Messages.get(this, "level_cap"));
                Sample.INSTANCE.play(Assets.SND_LEVELUP);
            }

            if (lvl < 10) {
                updateAwareness();
            }
        }

        if (levelUp) {

            GLog.p(Messages.get(this, "new_level"), lvl);
            sprite.showStatus(CharSprite.POSITIVE, Messages.get(Hero.class, "level_up"));
            Sample.INSTANCE.play(Assets.SND_LEVELUP);

            Badges.validateLevelReached();
        }
    }

    public int maxExp() {
        return 5 + lvl * 5;
    }

    void updateAwareness() {
        awareness = (float) (1 - Math.pow(
                (heroClass == HeroClass.SHAPESHIFTER ? 0.85 : 0.90),
                (1 + Math.min(lvl, 9)) * 0.5
        ));
    }

    public boolean isStarving() {
        return buff(Hunger.class) != null && buff(Hunger.class).isStarving();
    }

    @Override
    public void add(Buff buff) {

        if (buff(TimeFolder.timeStasis.class) != null)
            return;

        super.add(buff);

        if (sprite != null) {
            String msg = buff.heroMessage();
            if (msg != null) {
                GLog.w(msg);
            }

            if (buff instanceof Paralysis || buff instanceof Vertigo) {
                interrupt();
            }

        }

        BuffIndicator.refreshHero();
    }

    @Override
    public void remove(Buff buff) {
        super.remove(buff);

        BuffIndicator.refreshHero();
    }

    @Override
    public int stealth() {
        int stealth = super.stealth();

        stealth += EvasionModule.getBonus(this, EvasionModule.Evasion.class);

        if (belongings.armor != null && belongings.armor.hasEnhancement(Obfuscation.class)) {
            stealth += belongings.armor.level();
        }
        return stealth;
    }

    @Override
    public void die(Object cause) {

        curAction = null;

        // wafitz.v1 - God Mode - Remove after testing (for release)
        heal(Dungeon.hero);
        if (isAlive()) {
            new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
            return;
        }

        Clone clone = null;

        //look for ankhs in player inventory, prioritize ones which are blessed.
        for (Item item : belongings) {
            if (item instanceof Clone) {
                if (clone == null || ((Clone) item).isBlessed()) {
                    clone = (Clone) item;
                }
            }
        }

        if (clone != null && clone.isBlessed()) {
            this.HP = HT / 4;

            //ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
            Buff.detach(this, Paralysis.class);
            spend(-cooldown());

            new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
            CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

            clone.detach(belongings.backpack);

            Sample.INSTANCE.play(Assets.SND_TELEPORT);
            GLog.w(Messages.get(this, "revive"));
            Statistics.clonesSpent++;

            return;
        }

        Actor.fixTime();
        super.die(cause);

        if (clone == null) {

            reallyDie(cause);

        } else {

            Dungeon.deleteGame(Dungeon.hero.heroClass, false);
            GameScene.show(new WndResurrect(clone, cause));

        }
    }

    public static void reallyDie(Object cause) {

        int length = Dungeon.level.length();
        int[] map = Dungeon.level.map;
        boolean[] visited = Dungeon.level.visited;
        boolean[] discoverable = Level.discoverable;

        for (int i = 0; i < length; i++) {

            int terr = map[i];

            if (discoverable[i]) {

                visited[i] = true;
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
                    Dungeon.level.discover(i);
                }
            }
        }

        Bones.leave();

        Dungeon.observe();
        GameScene.updateFog();

        Dungeon.hero.belongings.identify();

        int pos = Dungeon.hero.pos;

        ArrayList<Integer> passable = new ArrayList<>();
        for (Integer ofs : PathFinder.NEIGHBOURS8) {
            int cell = pos + ofs;
            if ((Level.passable[cell] || Level.avoid[cell]) && Dungeon.level.heaps.get(cell) == null) {
                passable.add(cell);
            }
        }
        Collections.shuffle(passable);

        ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
        for (Integer cell : passable) {
            if (items.isEmpty()) {
                break;
            }

            Item item = Random.element(items);
            Dungeon.level.drop(item, cell).sprite.drop(pos);
            items.remove(item);
        }

        GameScene.gameOver();

        if (cause instanceof Hero.Doom) {
            ((Hero.Doom) cause).onDeath();
        }

        Dungeon.deleteGame(Dungeon.hero.heroClass, true);
    }

    //effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
    //This is relevant because we call isAlive during drawing, which has both performance
    //and concurrent modification implications if that method calls buff(Berserk.class)
    private Berserk berserk;

    @Override
    public boolean isAlive() {
        if (subClass == HeroSubClass.BERSERKER
                && berserk != null
                && berserk.berserking()) {
            return true;
        }
        return super.isAlive();
    }

    @Override
    public void move(int step) {
        super.move(step);

        if (!flying) {

            if (Level.water[pos]) {
                Sample.INSTANCE.play(Assets.SND_WATER, 1, 1, Random.Float(0.8f, 1.25f));
            } else {
                Sample.INSTANCE.play(Assets.SND_STEP);
            }
            Dungeon.level.press(pos, this);
        }
    }

    @Override
    public void onMotionComplete() {
        Dungeon.observe();
        search(false);
    }

    @Override
    public void onAttackComplete() {

        AttackIndicator.target(enemy);

        boolean hit = attack(enemy);

        if (subClass == HeroSubClass.GLADIATOR) {
            if (hit) {
                Buff.affect(this, Combo.class).hit();
            } else {
                Combo combo = buff(Combo.class);
                if (combo != null) combo.miss();
            }
        }

        curAction = null;

        super.onAttackComplete();
    }

    @Override
    public void onOperateComplete() {

        if (curAction instanceof HeroAction.Unlock) {

            int doorCell = ((HeroAction.Unlock) curAction).dst;
            int door = Dungeon.level.map[doorCell];

            if (door == Terrain.LOCKED_DOOR) {
                belongings.ironKeys[Dungeon.depth]--;
                Level.set(doorCell, Terrain.DOOR);
            } else {
                belongings.specialKeys[Dungeon.depth]--;
                Level.set(doorCell, Terrain.UNLOCKED_EXIT);
            }
            StatusPane.needsKeyUpdate = true;

            Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
            GameScene.updateMap(doorCell);

        } else if (curAction instanceof HeroAction.OpenChest) {

            Heap heap = Dungeon.level.heaps.get(((HeroAction.OpenChest) curAction).dst);
            if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
                Sample.INSTANCE.play(Assets.SND_BONES);
            } else if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {
                belongings.specialKeys[Dungeon.depth]--;
            }
            StatusPane.needsKeyUpdate = true;
            heap.open(this);
        }
        curAction = null;

        super.onOperateComplete();
    }

    /*private static CellSelector.Listener informer = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer cell) {
            GameScene.examineCell(cell);
        }

        @Override
        public String prompt() {
            return "Select a cell to examine";
        }
    };*/

    public boolean search(boolean intentional) {

        boolean smthFound = false;

        int positive = 0;
        int negative = 0;

        int distance = 1 + positive + negative;

        float level = intentional ? (2 * awareness - awareness * awareness) : awareness;
        if (distance <= 0) {
            level /= 2 - distance;
            distance = 1;
        }

        int cx = pos % Dungeon.level.width();
        int cy = pos / Dungeon.level.width();
        int ax = cx - distance;
        if (ax < 0) {
            ax = 0;
        }
        int bx = cx + distance;
        if (bx >= Dungeon.level.width()) {
            bx = Dungeon.level.width() - 1;
        }
        int ay = cy - distance;
        if (ay < 0) {
            ay = 0;
        }
        int by = cy + distance;
        if (by >= Dungeon.level.height()) {
            by = Dungeon.level.height() - 1;
        }

        SurveyorModule.Survey survey = buff(SurveyorModule.Survey.class);

        //malfunctioning talisman of survey makes unintentionally finding things impossible.
        if (survey != null && survey.isMalfunctioning()) {
            level = -1;
        }

        for (int y = ay; y <= by; y++) {
            for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

                if (Dungeon.visible[p]) {

                    if (intentional) {
                        sprite.parent.addToBack(new CheckedCell(p));
                    }

                    if (Level.secret[p] && (intentional || Random.Float() < level)) {

                        int oldValue = Dungeon.level.map[p];

                        GameScene.discoverTile(p, oldValue);

                        Dungeon.level.discover(p);

                        MappingScript.discover(p);

                        smthFound = true;
                        //informer.onSelect(null);

                        if (survey != null && !survey.isMalfunctioning())
                            survey.charge();
                    }
                }
            }
        }


        if (smthFound && intentional) {
            sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "search"));
            sprite.operate(pos);
            if (survey != null && survey.isMalfunctioning()) {
                GLog.n(Messages.get(this, "search_distracted"));
                spendAndNext(TIME_TO_SEARCH * 3);
            } else {
                spendAndNext(TIME_TO_SEARCH);
            }

        }

        if (smthFound) {
            GLog.w(Messages.get(this, "noticed_smth"));
            Sample.INSTANCE.play(Assets.SND_SECRET);
            interrupt();
        /*} else if (intentional) {
                GameScene.selectCell(informer);*/
        }

        return smthFound;
    }

    public void resurrect(int resetLevel) {

        HP = HT;
        Dungeon.parts = 0;
        exp = 0;

        belongings.resurrect(resetLevel);

        live();
    }

    @Override
    public HashSet<Class<?>> resistances() {
        ElementsModule.Resistance r = buff(ElementsModule.Resistance.class);
        return r == null ? super.resistances() : r.resistances();
    }

    @Override
    public HashSet<Class<?>> immunities() {
        HashSet<Class<?>> immunities = new HashSet<>();
        for (Buff buff : buffs()) {
            for (Class<?> immunity : buff.immunities)
                immunities.add(immunity);
        }
        return immunities;
    }

    @Override
    public void next() {
        if (isAlive())
            super.next();
    }

    public interface Doom {
        void onDeath();
    }
}
