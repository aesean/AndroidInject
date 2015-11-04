/*
 * Copyright (C) 2015.  Aesean
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aesean.injectlib.ioc.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
     * @param key key
     * @param method method
     */
    public void addMethod(String key, Method method) {
        mMethodMaps.put(key, method);
    }
}
