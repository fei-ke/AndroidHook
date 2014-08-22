package com.fei_ke.AndroidHook;


import java.util.Map;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    XSharedPreferences preHookableFW = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_HOOKABLE_FW);
    XSharedPreferences preHookableUser = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_HOOKABLE_USER);
    XSharedPreferences prefReturnResult = new XSharedPreferences(Constant.THIS_PACKAGE_NAME, Constant.PREF_RETURN_RESULT);

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (preHookableUser.contains(lpparam.packageName)) {

        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        Map<String, ?> all = preHookableFW.getAll();
        for (String key : all.keySet()) {
            Set<String> set = preHookableFW.getStringSet(key, null);
            HookEntity he = HookEntity.fromSet(set);
            hookMethod(he, null);
        }
    }

    private void hookMethod(final HookEntity he, ClassLoader classLoader) {
        XC_MethodHook xcMethodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                prefReturnResult.reload();
                Object returnResult = null;
                switch (he.getReturnType()) {
                    case HookEntity.RETURN_TYPE_INT:
                        returnResult = prefReturnResult.getInt(he.getResultKey(), 0);
                        break;
                    case HookEntity.RETURN_TYPE_FLOAT:
                        returnResult = prefReturnResult.getFloat(he.getResultKey(), 0);
                        break;
                    case HookEntity.RETURN_TYPE_BOOLEAN:
                        returnResult = prefReturnResult.getBoolean(he.getResultKey(), false);
                        break;
                    case HookEntity.RETURN_TYPE_STRING:
                        returnResult = prefReturnResult.getString(he.getResultKey(), null);
                        break;
                }

                if (returnResult != null) {
                    param.setResult(returnResult);
                }
            }
        };

        Object[] paramTypes = he.getParamType(classLoader);
        paramTypes[paramTypes.length - 1] = xcMethodHook;

        XposedHelpers.findAndHookMethod(he.getClassName(), null, he.getMethodName(), he, paramTypes);
    }
}
