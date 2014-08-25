package com.fei_ke.AndroidHook.utils;

import android.content.SharedPreferences;

import com.fei_ke.AndroidHook.entity.HookEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HookEntityUtil {
    public static List<HookEntity> getAllHookEntity(SharedPreferences preferences) {
        List<HookEntity> entities = new ArrayList<HookEntity>();
        Map<String, ?> all = preferences.getAll();
        for (String key : all.keySet()) {
            String set = preferences.getString(key, null);
            HookEntity he = HookEntity.fromString(set);
            entities.add(he);
        }
        return entities;
    }
}
