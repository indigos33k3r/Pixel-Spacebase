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
package com.wafitz.pixelspacebase.items.scrolls;

import com.wafitz.pixelspacebase.Assets;
import com.wafitz.pixelspacebase.actors.buffs.Invisibility;
import com.wafitz.pixelspacebase.items.Item;
import com.wafitz.pixelspacebase.messages.Messages;
import com.wafitz.pixelspacebase.scenes.GameScene;
import com.wafitz.pixelspacebase.windows.WndBag;
import com.wafitz.pixelspacebase.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class InventoryScroll extends Scroll {

    protected String inventoryTitle = Messages.get(this, "inv_title");
    protected WndBag.Mode mode = WndBag.Mode.ALL;

    @Override
    protected void doRead() {

        if (!isKnown()) {
            setKnown();
            identifiedByUse = true;
        } else {
            identifiedByUse = false;
        }

        GameScene.selectItem(itemSelector, mode, inventoryTitle);
    }

    private void confirmCancelation() {
        GameScene.show(new WndOptions(name(), Messages.get(this, "warning"),
                Messages.get(this, "yes"), Messages.get(this, "no")) {
            @Override
            protected void onSelect(int index) {
                switch (index) {
                    case 0:
                        curUser.spendAndNext(TIME_TO_READ);
                        identifiedByUse = false;
                        break;
                    case 1:
                        GameScene.selectItem(itemSelector, mode, inventoryTitle);
                        break;
                }
            }

            public void onBackPressed() {
            }
        });
    }

    protected abstract void onItemSelected(Item item);

    protected static boolean identifiedByUse = false;
    protected static WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item != null) {

                ((InventoryScroll) curItem).onItemSelected(item);
                ((InventoryScroll) curItem).readAnimation();

                Sample.INSTANCE.play(Assets.SND_READ);
                Invisibility.dispel();

                // wafitz.v1 - Lost count how many times I lost this scroll accidentally - no more!
                //} else if (identifiedByUse && !((Scroll) curItem).ownedByBook) {

                //((InventoryScroll) curItem).confirmCancelation();

            } else if (!((Scroll) curItem).ownedByBook) {

                curItem.collect(curUser.belongings.backpack);

            }
        }
    };
}