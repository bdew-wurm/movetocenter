package net.bdew.wurm.movetocenter;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;

import java.util.List;

public class MoveToCenterHook {
    public static void makeMoveSubMenuHook(Creature performer, Item target, List<ActionEntry> submenu) {
        if (!submenu.isEmpty() && CenterAction.canMove(performer, target)) {
            int pos = 0;
            while (pos < submenu.size() && submenu.get(pos).getNumber() != 864) {
                pos++;
            }
            submenu.add(pos, MoveToCenterMod.moveToCornerAction.actionEntry);
        }
    }
}
