package net.bdew.wurm.movetocenter;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;

import java.util.List;

public class MoveToCenterHook {
    public static void makeMoveSubMenuHook(Creature performer, Item target, List<ActionEntry> submenu) {
        if (!submenu.isEmpty() && CenterAction.canMove(performer, target)) {
            submenu.add(MoveToCenterMod.moveToCenterAction.actionEntry);
            submenu.add(MoveToCenterMod.moveToCornerAction.actionEntry);
        }
    }
}
