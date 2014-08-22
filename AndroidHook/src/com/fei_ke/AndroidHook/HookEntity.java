package com.fei_ke.AndroidHook;


import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XposedBridge;

public class HookEntity {
    public static final int RETURN_TYPE_INT = 1;
    public static final int RETURN_TYPE_STRING = 2;
    public static final int RETURN_TYPE_FLOAT = 3;
    public static final int RETURN_TYPE_BOOLEAN = 4;

    private String packageName;
    private String className;
    private String methodName;
    private List<String> paramType;
    private int returnType;

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

    public Object[] getParamType(ClassLoader classLoader) {
        int length = paramType.size() + 1;
        Object[] os = new Object[length];
        for (int i = 0; i < length - 1; i++) {
            try {
                Class<?> c = Class.forName(paramType.get(i), false, classLoader);
                os[i] = c;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                XposedBridge.log(e);
            }
        }
        return os;
    }

    public void setParamType(List<String> paramType) {
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int getReturnType() {
        return returnType;
    }

    public String getResultKey() {
        return methodName;
    }

    public static HookEntity fromSet(Set<String> set) {
        //TODO Admin 2014/8/23 反序列化
        return null;
    }
}
