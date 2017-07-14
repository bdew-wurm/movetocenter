package net.bdew.wurm.movetocenter;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmMod;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MoveToCenterMod implements WurmMod, Initable, PreInitable, ServerStartedListener {
    private static final Logger logger = Logger.getLogger("MoveToCenter");

    static CenterAction moveToCornerAction;

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    public static void logWarning(String msg) {
        if (logger != null)
            logger.log(Level.WARNING, msg);
    }

    public static void logInfo(String msg) {
        if (logger != null)
            logger.log(Level.INFO, msg);
    }


    @Override
    public void preInit() {
        ModActions.init();
    }

    @Override
    public void init() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            classPool.getCtClass("com.wurmonline.server.behaviours.ItemBehaviour").getMethod("makeMoveSubMenu", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;)Ljava/util/List;").instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("isEmpty".equals(m.getMethodName())) {
                        m.replace("net.bdew.wurm.movetocenter.MoveToCenterHook.makeMoveSubMenuHook(performer,target,$0); $_ = $proceed($$);");
                        logInfo("Installed makeMoveSubMenu hook on line " + m.getLineNumber());
                    }
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onServerStarted() {
        moveToCornerAction = new CenterActionCorner();
        ModActions.registerAction(moveToCornerAction);
    }
}
