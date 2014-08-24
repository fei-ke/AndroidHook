package com.fei_ke.AndroidHook.hook;


import com.fei_ke.AndroidHook.constant.Constant;
import com.fei_ke.AndroidHook.entity.HookEntity;
import com.fei_ke.AndroidHook.utils.HookEntityUtil;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    XSharedPreferences preHookableFW = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_HOOKABLE_FW);
    XSharedPreferences preHookableUser = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_HOOKABLE_USER);
    XSharedPreferences prefReturnResult = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_RETURN_RESULT);

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //        if (preHookableUser.contains(lpparam.packageName)) {
        //
        //        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        List<HookEntity> entities = HookEntityUtil.getAllHookEntity(preHookableFW);
        for (HookEntity entity : entities) {
            hookMethod(entity, null);
        }
    }

    private void hookMethod(final HookEntity he, ClassLoader classLoader) {
        try {

            XC_MethodHook xcMethodHook = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    prefReturnResult.reload();
                    Object returnResult = null;
                    switch (he.getReturnType()) {
                        case HookEntity.RETURN_TYPE_INT:
                            returnResult = prefReturnResult.getInt(he.getStoreKey(), 0);
                            break;
                        case HookEntity.RETURN_TYPE_FLOAT:
                            returnResult = prefReturnResult.getFloat(he.getStoreKey(), 0);
                            break;
                        case HookEntity.RETURN_TYPE_BOOLEAN:
                            returnResult = prefReturnResult.getBoolean(he.getStoreKey(), false);
                            break;
                        case HookEntity.RETURN_TYPE_STRING:
                            returnResult = prefReturnResult.getString(he.getStoreKey(), null);
                            break;
                    }

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
}
