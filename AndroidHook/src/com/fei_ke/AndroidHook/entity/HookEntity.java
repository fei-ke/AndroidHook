package com.fei_ke.AndroidHook.entity;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.Arrays;

import de.robv.android.xposed.XposedBridge;

public class HookEntity implements Serializable {
    public static final int RETURN_TYPE_VOID = 0;
    public static final int RETURN_TYPE_INT = 1;
    public static final int RETURN_TYPE_STRING = 2;
    public static final int RETURN_TYPE_FLOAT = 3;
    public static final int RETURN_TYPE_BOOLEAN = 4;

    private String packageName;
    private String className;
    private String methodName;
    private String paramType = "";
    private int returnType;
    private String alias;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamType(ClassLoader classLoader) {
        if (!TextUtils.isEmpty(paramType)) {
            String[] paramArr = paramType.split(",");
            int size = paramArr.length;
            Class[] cs = new Class[size];
            for (int i = 0; i < size; i++) {
                try {
                    Class<?> c = Class.forName(paramArr[i], false, classLoader);
                    cs[i] = c;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    XposedBridge.log(e);
                }
            }
            return cs;
        } else {
            return null;
        }
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }


    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int getReturnType() {
        return returnType;
    }

    public String getStoreKey() {
        return (methodName + paramType).hashCode() + "";
    }

    public String getParamType() {
        return paramType;
    }

    public static HookEntity fromSet(String set) {
        String[] strArr = set.split("\\|");
        System.out.println(Arrays.toString(strArr));
        HookEntity entity = new HookEntity();
        entity.packageName = strArr[0];
        entity.className = strArr[1];
        entity.methodName = strArr[2];
        entity.paramType = strArr[3];
        entity.returnType = Integer.valueOf(strArr[4]);
        entity.alias = strArr[5];
        return entity;
    }

    public String getStoreString() {
        return packageName + "|"
                + className + "|"
                + methodName + "|"
                + paramType + "|"
                + returnType + "|"
                + alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        if (TextUtils.isEmpty(alias)) {
            return getStoreKey();
        } else {
            return alias;
        }
    }
}
