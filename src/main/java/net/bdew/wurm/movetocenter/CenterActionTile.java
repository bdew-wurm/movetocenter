package net.bdew.wurm.movetocenter;

import com.wurmonline.server.items.Item;

public class CenterActionTile extends CenterAction {
    public CenterActionTile() {
        super("Move to center", "center");
    }

    @Override
    protected void modifyPosition(Item item) {
        item.setPosXY(((int) item.getPosX() >> 2) * 4 + 2, ((int) item.getPosY() >> 2) * 4 + 2);
        item.setRotation(0);
    }
}
