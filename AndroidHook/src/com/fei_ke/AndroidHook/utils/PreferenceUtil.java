package com.fei_ke.AndroidHook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fei_ke.AndroidHook.BaseApplication;
import com.fei_ke.AndroidHook.constant.Constant;
import com.fei_ke.AndroidHook.entity.HookEntity;

public class PreferenceUtil {
    public static SharedPreferences preHookable;
    public static SharedPreferences preResult;

    static {
        Context context = BaseApplication.getInstance();
        preHookable = context.getSharedPreferences(Constant.PREF_HOOKABLE, Context.MODE_WORLD_READABLE);
        preResult = context.getSharedPreferences(Constant.PREF_RETURN_RESULT, Context.MODE_WORLD_READABLE);
    }

    public static void putFWHookEntity(HookEntity entity, int type) {
        preHookable.edit().putString(entity.getStoreKey(), entity.getStoreString()).commit();
    }

    public static void removeEntity(HookEntity entity) {
        String storeKey = entity.getStoreKey();
        preHookable.edit().remove(storeKey).commit();
        preResult.edit().remove(storeKey).commit();
    }
}
