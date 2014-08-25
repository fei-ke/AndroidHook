package com.fei_ke.AndroidHook.hook;


import com.fei_ke.AndroidHook.constant.Constant;
import com.fei_ke.AndroidHook.entity.HookEntity;
import com.fei_ke.AndroidHook.utils.HookEntityUtil;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private XSharedPreferences prefReturnResult = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_RETURN_RESULT);
    private static List<HookEntity> entitiesUser;
    private static List<HookEntity> entitiesFW;

    static {
        XSharedPreferences preHookableUser = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_HOOKABLE);
        List<HookEntity> list = HookEntityUtil.getAllHookEntity(preHookableUser);
        entitiesFW = new ArrayList<HookEntity>();
        entitiesUser = new ArrayList<HookEntity>();
        for (HookEntity he : list) {
            switch (he.getHookType()) {
                case HookEntity.HOOK_TYPE_FW:
                    entitiesFW.add(he);
                    break;
                case HookEntity.HOOK_TYPE_USER:
                case HookEntity.HOOK_TYPE_USER_FIELD:
                    entitiesUser.add(he);
                    break;
            }
        }


    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (int i = entitiesUser.size() - 1; i >= 0; i--) {
            HookEntity he = entitiesUser.get(i);
            if (lpparam.packageName.equals(he.getPackageName())) {
                if (he.getHookType() == HookEntity.HOOK_TYPE_USER) {
                    hookMethod(he, lpparam.classLoader);
                } else if (he.getHookType() == HookEntity.HOOK_TYPE_USER_FIELD) {
                    hookField(he, lpparam.classLoader);
                }

            }
        }


    }

    private void hookField(HookEntity he, ClassLoader classLoader) {
        try {
            XposedHelpers.setStaticObjectField(
                    XposedHelpers.findClass(he.getClassName(), classLoader),
                    he.getMethodName(),
                    getResult(he));
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (HookEntity entity : entitiesFW) {
            hookMethod(entity, null);
        }
    }

    private void hookMethod(final HookEntity he, ClassLoader classLoader) {
        try {

            XC_MethodHook xcMethodHook = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    prefReturnResult.reload();
                    if (!prefReturnResult.contains(he.getStoreKey())) return;
                    Object returnResult = getResult(he);

                    if (returnResult != null) {
                        param.setResult(returnResult);
                    }
                }
            };

            Class[] paramTypes = he.getParamType(classLoader);
            Object[] paramTypesAndCallback = new Object[(paramTypes != null ? paramTypes.length : 0) + 1];
            if (paramTypes != null) {
                System.arraycopy(paramTypes, 0, paramTypesAndCallback, 0, paramTypes.length);
            }
            paramTypesAndCallback[paramTypesAndCallback.length - 1] = xcMethodHook;

            XposedHelpers.findAndHookMethod(he.getClassName(), classLoader, he.getMethodName(), paramTypesAndCallback);
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }

    private Object getResult(HookEntity he) {
        Object returnResult = null;
        switch (he.getReturnType()) {
            case HookEntity.RETURN_TYPE_INT:
                returnResult = Integer.valueOf(prefReturnResult.getString(he.getStoreKey(), null));
                break;
            case HookEntity.RETURN_TYPE_FLOAT:
                returnResult = Float.valueOf(prefReturnResult.getString(he.getStoreKey(), null));
                break;
            case HookEntity.RETURN_TYPE_BOOLEAN:
                returnResult = Boolean.valueOf(prefReturnResult.getString(he.getStoreKey(), null));
                break;
            case HookEntity.RETURN_TYPE_STRING:
                returnResult = prefReturnResult.getString(he.getStoreKey(), null);
                break;
        }
        return returnResult;
    }
}
