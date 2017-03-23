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
package com.wafitz.pixelspacebase;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.wafitz.pixelspacebase.actors.mobs.DarkLordGnoll;
import com.wafitz.pixelspacebase.actors.mobs.ToughXeno;
import com.wafitz.pixelspacebase.items.armor.enhancements.Lockdown;
import com.wafitz.pixelspacebase.items.artifacts.PortableMaker;
import com.wafitz.pixelspacebase.items.blasters.DominationBlaster;
import com.wafitz.pixelspacebase.items.blasters.FlameThrower;
import com.wafitz.pixelspacebase.items.blasters.FreezeThrower;
import com.wafitz.pixelspacebase.items.blasters.MissileBlaster;
import com.wafitz.pixelspacebase.items.blasters.VenomBlaster;
import com.wafitz.pixelspacebase.items.blasters.WaveBlaster;
import com.wafitz.pixelspacebase.items.scripts.EnhancementScript;
import com.wafitz.pixelspacebase.items.weapon.enhancements.Buggy;
import com.wafitz.pixelspacebase.items.weapon.melee.Spanner;
import com.wafitz.pixelspacebase.messages.Languages;
import com.wafitz.pixelspacebase.mines.AlienTrap;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.scenes.PixelScene;
import com.wafitz.pixelspacebase.scenes.WelcomeScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.util.Locale;

import javax.microedition.khronos.opengles.GL10;

public class PixelSpacebase extends Game {

    public PixelSpacebase() {
        super(WelcomeScene.class);

        // 0.2.4
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Shocking.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Piercing");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Shocking.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Swing");

        com.watabou.utils.Bundle.addAlias(
                EnhancementScript.class,
                "com.wafitz.pixelspacebase.items.scripts.WeaponUpgradeScript");

        // 0.2.4d
        com.watabou.utils.Bundle.addAlias(
                PortableMaker.class,
                "com.wafitz.pixelspacebase.items.PortableMaker");

        // 0.3.0, lots of blasters
        com.watabou.utils.Bundle.addAlias(
                VenomBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.VenomBlaster");
        com.watabou.utils.Bundle.addAlias(
                FreezeThrower.class,
                "com.wafitz.pixelspacebase.items.blasters.FreezeThrower");
        com.watabou.utils.Bundle.addAlias(
                FlameThrower.class,
                "com.wafitz.pixelspacebase.items.blasters.FlameThrower");
        com.watabou.utils.Bundle.addAlias(
                DominationBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.DominationBlaster");
        com.watabou.utils.Bundle.addAlias(
                WaveBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.WaveBlaster");
        com.watabou.utils.Bundle.addAlias(
                MissileBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.FlockBlaster");
        com.watabou.utils.Bundle.addAlias(
                MissileBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.TremorBlaster");
        com.watabou.utils.Bundle.addAlias(
                MissileBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.SpaceFolder");
        com.watabou.utils.Bundle.addAlias(
                MissileBlaster.class,
                "com.wafitz.pixelspacebase.items.blasters.Teleporter");

        //0.3.3
        com.watabou.utils.Bundle.addAlias(
                ToughXeno.class,
                "com.wafitz.pixelspacebase.actors.mobs.npcs.Hologram$ToughXeno");
        com.watabou.utils.Bundle.addAlias(
                DarkLordGnoll.class,
                "com.wafitz.pixelspacebase.actors.mobs.npcs.Hologram$DarkLordGnoll");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.actors.mobs.GreatCrab.class,
                "com.wafitz.pixelspacebase.actors.mobs.npcs.Hologram$GreatCrab");
        com.watabou.utils.Bundle.addAlias(
                AlienTrap.class,
                "com.wafitz.pixelspacebase.actors.mobs.npcs.Gunsmith$AlienTrap");
        com.watabou.utils.Bundle.addAlias(
                AlienTrap.Device.class,
                "com.wafitz.pixelspacebase.actors.mobs.npcs.Gunsmith$AlienTrap$Device");

        //0.4.0
        //equipment
        com.watabou.utils.Bundle.addAlias(
                Spanner.class,
                "com.wafitz.pixelspacebase.items.weapon.melee.ShortSword");
        //enhancements
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Grim.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Death");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Blazing.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.FireMine");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Eldritch.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Horror");
        com.watabou.utils.Bundle.addAlias(
                Buggy.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Instability");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Vampiric.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Leech");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Lucky.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Luck");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Stunning.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Paralysis");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Venomous.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Poison");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Shocking.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.Shock");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.weapon.enhancements.Chilling.class,
                "com.wafitz.pixelspacebase.items.weapon.enhancements.TimeSink");

        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.armor.enhancements.Repulsion.class,
                "com.wafitz.pixelspacebase.items.armor.enhancements.Bounce");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.armor.enhancements.Repulsion.class,
                "com.wafitz.pixelspacebase.items.armor.enhancements.Displacement");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.armor.enhancements.Potential.class,
                "com.wafitz.pixelspacebase.items.armor.enhancements.AntiEntropy");
        com.watabou.utils.Bundle.addAlias(
                Lockdown.class,
                "com.wafitz.pixelspacebase.items.armor.enhancements.Metabolism");
        com.watabou.utils.Bundle.addAlias(
                Lockdown.class,
                "com.wafitz.pixelspacebase.items.armor.enhancements.Multiplicity");
        com.watabou.utils.Bundle.addAlias(
                com.wafitz.pixelspacebase.items.armor.enhancements.Repulsion.class,
                "com.wafitz.pixelspacebase.items.armor.enhancements.Gas");

        com.watabou.utils.Bundle.exceptionReporter =
                new com.watabou.utils.Bundle.BundleExceptionCallback() {
                    @Override
                    public void call(Throwable t) {
                        PixelSpacebase.reportException(t);
                    }
                };

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateImmersiveMode();

        if (Preferences.INSTANCE.contains(Preferences.KEY_LANDSCAPE)) {
            landscape(Preferences.INSTANCE.getBoolean(Preferences.KEY_LANDSCAPE, false));

        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            if (immersed() && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
                getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            else
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
            boolean landscape = metrics.widthPixels > metrics.heightPixels;

            landscape(landscape);
        }

        Music.INSTANCE.enable(music());
        Sample.INSTANCE.enable(soundFx());
        Sample.INSTANCE.volume(SFXVol() / 10f);

        Sample.INSTANCE.load(
                Assets.SND_CLICK,
                Assets.SND_BADGE,
                Assets.SND_GOLD,

                Assets.SND_STEP,
                Assets.SND_WATER,
                Assets.SND_OPEN,
                Assets.SND_UNLOCK,
                Assets.SND_ITEM,
                Assets.SND_DEWDROP,
                Assets.SND_HIT,
                Assets.SND_MISS,

                Assets.SND_DESCEND,
                Assets.SND_EAT,
                Assets.SND_READ,
                Assets.SND_LULLABY,
                Assets.SND_DRINK,
                Assets.SND_SHATTER,
                Assets.SND_ZAP,
                Assets.SND_LIGHTNING,
                Assets.SND_LEVELUP,
                Assets.SND_DEATH,
                Assets.SND_CHALLENGE,
                Assets.SND_CURSED,
                Assets.SND_EVOKE,
                Assets.SND_TRAP,
                Assets.SND_TOMB,
                Assets.SND_ALERT,
                Assets.SND_MELD,
                Assets.SND_BOSS,
                Assets.SND_BLAST,
                Assets.SND_PLANT,
                Assets.SND_RAY,
                Assets.SND_BEACON,
                Assets.SND_TELEPORT,
                Assets.SND_CHARMS,
                Assets.SND_MASTERY,
                Assets.SND_PUFF,
                Assets.SND_ROCKS,
                Assets.SND_BURNING,
                Assets.SND_FALLING,
                Assets.SND_GHOST,
                Assets.SND_SECRET,
                Assets.SND_BONES,
                Assets.SND_BEE,
                Assets.SND_DEGRADE,
                Assets.SND_MIMIC);

        if (classicFont()) {
            RenderedText.setFont("pixelfont.ttf");
        } else {
            RenderedText.setFont(null);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            updateImmersiveMode();
        }
    }

    public static void switchNoFade(Class<? extends PixelScene> c) {
        switchNoFade(c, null);
    }

    public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
        PixelScene.noFade = true;
        switchScene(c, callback);
    }

	/*
     * ---> Prefernces
	 */

    public static void landscape(boolean value) {
        if (android.os.Build.VERSION.SDK_INT >= 9) {
            Game.instance.setRequestedOrientation(value ?
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            Game.instance.setRequestedOrientation(value ?
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Preferences.INSTANCE.put(Preferences.KEY_LANDSCAPE, value);
        ((PixelSpacebase) instance).updateDisplaySize();
    }

    public static boolean landscape() {
        return width > height;
    }

    public static void scale(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_SCALE, value);
    }

    private static boolean immersiveModeChanged = false;

    public static void immerse(boolean value) {
        Preferences.INSTANCE.put(Preferences.KEY_IMMERSIVE, value);

        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateImmersiveMode();
                immersiveModeChanged = true;
                //ensures surfacechanged is called if the view was previously set to be fixed.
                ((PixelSpacebase) instance).view.getHolder().setSizeFromLayout();
            }
        });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        super.onSurfaceChanged(gl, width, height);

        updateDisplaySize();

        if (immersiveModeChanged) {
            requestedReset = true;
            immersiveModeChanged = false;
        }
    }

    private void updateDisplaySize() {
        DisplayMetrics m = new DisplayMetrics();
        if (immersed() && Build.VERSION.SDK_INT >= 19)
            getWindowManager().getDefaultDisplay().getRealMetrics(m);
        else
            getWindowManager().getDefaultDisplay().getMetrics(m);
        dispHeight = m.heightPixels;
        dispWidth = m.widthPixels;

        float dispRatio = dispWidth / (float) dispHeight;

        float renderWidth = dispRatio > 1 ? PixelScene.MIN_WIDTH_L : PixelScene.MIN_WIDTH_P;
        float renderHeight = dispRatio > 1 ? PixelScene.MIN_HEIGHT_L : PixelScene.MIN_HEIGHT_P;

        //force power saver in this case as all devices must run at at least 2x scale.
        if (dispWidth < renderWidth * 2 || dispHeight < renderHeight * 2)
            Preferences.INSTANCE.put(Preferences.KEY_POWER_SAVER, true);

        if (powerSaver()) {

            int maxZoom = (int) Math.min(dispWidth / renderWidth, dispHeight / renderHeight);

            renderWidth *= Math.max(2, Math.round(1f + maxZoom * 0.4f));
            renderHeight *= Math.max(2, Math.round(1f + maxZoom * 0.4f));

            if (dispRatio > renderWidth / renderHeight) {
                renderWidth = renderHeight * dispRatio;
            } else {
                renderHeight = renderWidth / dispRatio;
            }

            final int finalW = Math.round(renderWidth);
            final int finalH = Math.round(renderHeight);
            if (finalW != width || finalH != height) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.getHolder().setFixedSize(finalW, finalH);
                    }
                });

            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.getHolder().setSizeFromLayout();
                }
            });
        }
    }

    public static void updateImmersiveMode() {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            try {
                // Sometime NullPointerException happens here
                instance.getWindow().getDecorView().setSystemUiVisibility(
                        immersed() ?
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                :
                                0);
            } catch (Exception e) {
                reportException(e);
            }
        }
    }

    public static boolean immersed() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_IMMERSIVE, false);
    }

    public static boolean powerSaver() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_POWER_SAVER, false);
    }

    public static void powerSaver(boolean value) {
        Preferences.INSTANCE.put(Preferences.KEY_POWER_SAVER, value);
        ((PixelSpacebase) instance).updateDisplaySize();
    }

    public static int scale() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_SCALE, 0);
    }

    public static void zoom(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_ZOOM, value);
    }

    public static int zoom() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_ZOOM, 0);
    }

    public static void music(boolean value) {
        Music.INSTANCE.enable(value);
        Music.INSTANCE.volume(musicVol() / 10f);
        Preferences.INSTANCE.put(Preferences.KEY_MUSIC, value);
    }

    public static boolean music() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_MUSIC, true);
    }

    public static void musicVol(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_MUSIC_VOL, value);
    }

    public static int musicVol() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_MUSIC_VOL, 10, 0, 10);
    }

    public static void soundFx(boolean value) {
        Sample.INSTANCE.enable(value);
        Preferences.INSTANCE.put(Preferences.KEY_SOUND_FX, value);
    }

    public static boolean soundFx() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_SOUND_FX, true);
    }

    public static void SFXVol(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_SFX_VOL, value);
    }

    public static int SFXVol() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_SFX_VOL, 10, 0, 10);
    }

    public static void brightness(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_BRIGHTNESS, value);
        GameScene.updateFog();
    }

    public static int brightness() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_BRIGHTNESS, 0, -2, 2);
    }

    public static void language(Languages lang) {
        Preferences.INSTANCE.put(Preferences.KEY_LANG, lang.code());
    }

    public static Languages language() {
        String code = Preferences.INSTANCE.getString(Preferences.KEY_LANG, null);
        if (code == null) {
            Languages lang = Languages.matchLocale(Locale.getDefault());
            if (lang.status() == Languages.Status.REVIEWED)
                return lang;
            else
                return Languages.ENGLISH;
        } else return Languages.matchCode(code);
    }

    public static void classicFont(boolean classic) {
        Preferences.INSTANCE.put(Preferences.KEY_CLASSICFONT, classic);
        if (classic) {
            RenderedText.setFont("pixelfont.ttf");
        } else {
            RenderedText.setFont(null);
        }
    }

    public static boolean classicFont() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_CLASSICFONT, true);
    }

    public static void lastClass(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_LAST_CLASS, value);
    }

    public static int lastClass() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_LAST_CLASS, 0, 0, 3);
    }

    public static void challenges(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_CHALLENGES, value);
    }

    public static int challenges() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_CHALLENGES, 0, 0, Challenges.MAX_VALUE);
    }

    public static void quickSlots(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_QUICKSLOTS, value);
    }

    public static int quickSlots() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_QUICKSLOTS, 4, 0, 4);
    }

    public static void flipToolbar(boolean value) {
        Preferences.INSTANCE.put(Preferences.KEY_FLIPTOOLBAR, value);
    }

    public static boolean flipToolbar() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_FLIPTOOLBAR, false);
    }

    public static void flipTags(boolean value) {
        Preferences.INSTANCE.put(Preferences.KEY_FLIPTAGS, value);
    }

    public static boolean flipTags() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_FLIPTAGS, false);
    }

    public static void toolbarMode(String value) {
        Preferences.INSTANCE.put(Preferences.KEY_BARMODE, value);
    }

    public static String toolbarMode() {
        return Preferences.INSTANCE.getString(Preferences.KEY_BARMODE, !landscape() ? "SPLIT" : "GROUP");
    }

    public static void intro(boolean value) {
        Preferences.INSTANCE.put(Preferences.KEY_INTRO, value);
    }

    public static boolean intro() {
        return Preferences.INSTANCE.getBoolean(Preferences.KEY_INTRO, true);
    }

    public static void version(int value) {
        Preferences.INSTANCE.put(Preferences.KEY_VERSION, value);
    }

    public static int version() {
        return Preferences.INSTANCE.getInt(Preferences.KEY_VERSION, 0);
    }

	/*
	 * <--- Preferences
	 */

    public static void reportException(Throwable tr) {
        Log.e("PD", Log.getStackTraceString(tr));
    }
}