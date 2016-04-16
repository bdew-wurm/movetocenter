package net.bdew.wurm.movetocenter;

import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.server.structures.Structure;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

public abstract class CenterAction implements ModAction, ActionPerformer, BehaviourProvider {
    final ActionEntry actionEntry;
    final short actionId;
    final String name;
    final String position;

    public CenterAction(String name, String position) {
        actionId = (short) ModActions.getNextActionId();
        this.name = name;
        this.position = position;
        actionEntry = ActionEntry.createEntry(actionId, name, "moving to " + position, new int[]{
                6 /* ACTION_TYPE_NOMOVE */,
                48 /* ACTION_TYPE_ENEMY_ALWAYS */,
                37 /* ACTION_TYPE_NEVER_USE_ACTIVE_ITEM */
        });
        ModActions.registerAction(actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public short getActionId() {
        return actionId;
    }

    public static boolean canMove(Creature performer, Item target) {
        return performer instanceof Player && target != null && target.isMoveable(performer) && !target.isBoat() && !target.isVehicle();
    }

    abstract protected void modifyPosition(Item item);

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        return action(action, performer, target, num, counter);
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        try {
            Communicator comm = performer.getCommunicator();
            if (!canMove(performer, target)) {
                comm.sendNormalServerMessage("You can not move that right now.", (byte) 3);
                return true;
            }

            int tx = (int) target.getPosX() >> 2;
            int ty = (int) target.getPosY() >> 2;

            VolaTile vt = Zones.getOrCreateTile(tx, ty, performer.isOnSurface());
            Zone z = Zones.getZone(tx, ty, performer.isOnSurface());

            if (vt == null || z == null) {
                MoveToCenterMod.logWarning("Tile is null when moving item!");
                return true;
            }

            if (performer.getPower() < 2) {
                Village village = vt.getVillage();
                Structure structure = vt.getStructure();
                if ((village != null && !village.isActionAllowed((short) 99, performer)) || (structure != null && !structure.isActionAllowed(performer, (short) 99))) {
                    comm.sendNormalServerMessage("You are not allowed to move that.", (byte) 3);
                    return true;
                }
            }

            if (counter == 1.0f) {
                comm.sendNormalServerMessage(String.format("You start moving the %s to the %s of the tile.", target.getName(), position));
                Server.getInstance().broadCastAction(String.format("%s starts moving the %s to the %s of the tile.", performer.getName(), target.getName(), position), performer, 10);
                action.setTimeLeft(30);
                performer.sendActionControl("moving to " + position, true, 30);
            } else {
                int time = action.getTimeLeft();
                if (counter * 10.0f > time) {
                    z.removeItem(target);
                    modifyPosition(target);
                    z.addItem(target);
                    SoundPlayer.playSound("sound.object.move.pushpull", target, 0.0f);
                    comm.sendNormalServerMessage(String.format("You move the %s to the %s of the tile.", target.getName(), position));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            MoveToCenterMod.logException("Center action error", e);
            return true;
        }
    }
}
