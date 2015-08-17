package com.aesean.injectlib.ioc.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xl on 8/17/15.
 */
public class ListenerInvocationHandler implements InvocationHandler {

    /**
     * 拦截目标对象
     */
    private Object target;

    /**
     * 拦截方法列表
     */
    private Map<String, Method> mMethodMaps = new HashMap<>();

    public ListenerInvocationHandler(Object tartget) {
        this.target = tartget;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (target != null) {
            String methodName = method.getName();
            method = mMethodMaps.get(methodName);
            if (method != null) {
                return method.invoke(target, args);
            }
        }
        return null;
    }

    /**
     * 添加拦截方法
     * @param key
     * @param method
     */
    public void addMethod(String key, Method method) {
        mMethodMaps.put(key, method);
    }
}
