package net.bdew.wurm.movetocenter;

import com.wurmonline.server.items.Item;

public class CenterActionCorner extends CenterAction {
    public CenterActionCorner() {
        super("Move to corner", "corner");
    }

    @Override
    protected void modifyPosition(Item item) {
        if (item.getPosX() % 4 - 2 > 0) {
            if (item.getPosY() % 4 - 2 > 0) {
                item.setPosXY(((int) item.getPosX() >> 2) * 4 + 3.99f, ((int) item.getPosY() >> 2) * 4 + 3.99f);
            } else {
                item.setPosXY(((int) item.getPosX() >> 2) * 4 + 3.99f, ((int) item.getPosY() >> 2) * 4 + 0.01f);
            }
        } else {
            if (item.getPosY() % 4 - 2 > 0) {
                item.setPosXY(((int) item.getPosX() >> 2) * 4 + 0.01f, ((int) item.getPosY() >> 2) * 4 + 3.99f);
            } else {
                item.setPosXY(((int) item.getPosX() >> 2) * 4 + 0.01f, ((int) item.getPosY() >> 2) * 4 + 0.01f);
            }
        }
        item.setRotation(0);
    }
}
