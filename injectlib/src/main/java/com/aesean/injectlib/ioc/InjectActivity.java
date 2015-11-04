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

package com.aesean.injectlib.ioc;

import android.util.Log;

import com.aesean.injectlib.ioc.annotation.BaseEvent;
import com.aesean.injectlib.ioc.annotation.EventUtils;
import com.aesean.injectlib.ioc.annotation.FindViewById;
import com.aesean.injectlib.ioc.annotation.OnCheckedChanged;
import com.aesean.injectlib.ioc.annotation.OnClick;
import com.aesean.injectlib.ioc.annotation.OnLongClick;
import com.aesean.injectlib.ioc.annotation.SetContentView;
import com.aesean.injectlib.ioc.handler.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Activity注入，后续请考虑使用封装的日志类进行日志记录，
 * 当前的注入其实仅仅是节省了前端调用的代码量，增加了性能消耗，
 * 并没有从根本上利用ioc的特性。
 * 已知缺陷：如果通过注解参数获取的结果为null，将导致空指针，
 * 但是空指针的异常将报在当前类，
 * 导致debug的时候比较麻烦。
 * 建议后续可以用id的int值通过反射拿到int值对应的变量名称。
 */
public class InjectActivity {
    private final static String TAG = InjectActivity.class.getName();

    /**
     * 禁止实例化
     */
    private InjectActivity() {
    }

    /**
     * 注入
     *
     * @param object 当前Activity实例
     */
    public static void inject(Object object) {
        /**
         * I know it for sure how it's killing you when reading these codes.
         * It's only God and I that know what these codes mean when I write these codes.
         * However , now God is the only one knowing the meaning.
         */
        injectClass(object);
        injectField(object);
        injectMethod(object);
    }

    /**
     * 类注入
     *
     * @param object Activity类实例，由于需要反射执行Activity中的非静态方法，
     *               所以参数需要一个类类型和执行类类型反射方法的方法调用者．
     */
    private static void injectClass(Object object) {
        Log.i(TAG, "开始进行Class注入");
        // 获取object的类类型用于反射注入
        Class<?> clazz = object.getClass();
        // 获取当前activity上的SetContentView注解
        SetContentView annotation = clazz.getAnnotation(SetContentView.class);
        // 可能当前类没有SetContentView注解，所以需要判断是否为空
        if (annotation != null) {
            try {
                // 通过反射拿到activity的setContentView方法
                Method method = clazz.getMethod(EventUtils.SET_CONTENT_VIEW, int.class);
                if (method != null) {
                    String methodName = method.getName();
                    Log.i(TAG, "开始注入Method：" + methodName);
                    // 注入．此处相当于平时Activity类中的setContentView方法．前面做那么多其实就是为了这个方法．
                    method.invoke(object, annotation.value());
                    Log.i(TAG, "Class注入成功：" + methodName);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Log.e(TAG, "NoSuchMethodException：" + e.getMessage());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Log.e(TAG, "IllegalAccessException：" + e.getMessage());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Log.e(TAG, "IllegalArgumentException：" + e.getMessage());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Log.e(TAG, "InvocationTargetException：" + e.getMessage());
            }
        }
    }

    /**
     * 属性注入
     *
     * @param object Activity类实例，由于需要反射执行Activity中的非静态方法，
     *               所以参数需要一个类类型和执行类类型反射方法的方法调用者．
     */
    private static void injectField(Object object) {
        Log.i(TAG, "开始进行Field注入");
        // 获取object的类类型用于反射注入
        Class<?> clazz = object.getClass();
        // 获取所有属性，注意此处需要用DeclaredFields，因为需要注入的属性一定是当前类的，可能是私有也可能公有，但是忽略父类的所有属性．
        Field[] declaredFields = clazz.getDeclaredFields();
        // 遍历所有属性
        for (Field field : declaredFields) {
            // 记录属性可见性
            boolean accessible = field.isAccessible();
            // 修改属性可见性
            field.setAccessible(true);
            String fieldName = field.getName();
            // 获取FindViewById注解
            FindViewById annotation = field.getAnnotation(FindViewById.class);
            if (annotation != null) {
                try {
                    // 通过反射拿到findViewById方法
                    Method method = clazz.getMethod(EventUtils.FIND_VIEW_BY_ID, int.class);
                    if (method != null) {
                        // 将反射方法的结果赋值给属性
                        field.set(object, method.invoke(object, annotation.value()));
                        Log.i(TAG, "注入参数成功：" + fieldName);
                    } else {
                        Log.e(TAG, "获取方法：findViewById失败");
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    Log.e(TAG, "NoSuchMethodException：" + e.getMessage());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IllegalAccessException：" + e.getMessage());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IllegalArgumentException：" + e.getMessage());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    Log.e(TAG, "InvocationTargetException：" + e.getMessage());
                }
            } else {
                Log.i(TAG, "注解为空：" + fieldName);
            }
            // 还原属性可见性
            field.setAccessible(accessible);
        }
    }

    /**
     * 方法注入
     *
     * @param object Activity类实例，由于需要反射执行Activity中的非静态方法，
     *               所以参数需要一个类类型和执行类类型反射方法的方法调用者．
     */
    private static void injectMethod(Object object) {
        Log.i(TAG, "开始进行Method注入");
        // 获取object的类类型用于反射注入
        Class<?> clazz = object.getClass();
        // 获取当前类本身的公开方法，因为需要回调，此处必须是declared，否则将抛出：java.lang.IllegalAccessException: access to method denied异常
        Method[] methods = clazz.getDeclaredMethods();
        // 遍历所有方法
        for (Method method : methods) {
            String methodName = method.getName();
            Log.i(TAG, "MethodName:" + methodName);
            // 此处暂时没想到更好的封装方式，后续可以考虑做个更好的封装，可以让添加新注入更加简单。
            OnClick onClickAnnotation = method.getAnnotation(OnClick.class);
            // 如果当前方法有OnClick注解
            if (onClickAnnotation != null) {
                // 反射获取OnClick上的注解
                Class<? extends Annotation> type = onClickAnnotation.annotationType();
                Log.i(TAG, "OnClick:" + object.toString());
                // 注入
                injectEvent(object, clazz, method, onClickAnnotation.value(), type);
            }

            OnLongClick onLongClickAnnotation = method.getAnnotation(OnLongClick.class);
            // 如果当前方法有OnLongClick注解
            if (onLongClickAnnotation != null) {
                // 反射获取OnLongClick上的注解
                Class<? extends Annotation> type = onLongClickAnnotation.annotationType();
                Log.i(TAG, "OnLongClick:" + object.toString());
                // 注入
                injectEvent(object, clazz, method, onLongClickAnnotation.value(), type);
            }

            OnCheckedChanged onCheckedChanged = method.getAnnotation(OnCheckedChanged.class);
            // 如果当前方法有OnCheckedChanged注解
            if (onCheckedChanged != null) {
                // 反射获取OnLongClick上的注解
                Class<? extends Annotation> type = onCheckedChanged.annotationType();
                Log.i(TAG, "OnCheckedChanged:" + object.toString());
                // 注入
                injectEvent(object, clazz, method, onCheckedChanged.value(), type);
            }
        }
    }

    /**
     * 注入事件
     *
     * @param object 对象
     * @param clazz  对象的类类型
     * @param method 拦截后要执行的方法
     * @param ids    所有需要添加事件的控件id
     * @param type   注解类类型
     */
    private static void injectEvent(Object object, Class<?> clazz, Method method, int[] ids, Class<? extends Annotation> type) {
        String methodName = method.getName();
        Log.i(TAG, "开始注入" + methodName);
        // 反射获取BaseEvent
        BaseEvent baseEvent = type.getAnnotation(BaseEvent.class);
        // 通过BaseEvent获取事件的类型，设置方法和回调方法
        Class<?> listenerType = baseEvent.listenerType();
        String listenerSetter = baseEvent.listenerSetter();
        String listenerCallback = baseEvent.listenerCallback();
        // 代理handler
        ListenerInvocationHandler handler = new ListenerInvocationHandler(object);
        // 添加代理方法
        handler.addMethod(listenerCallback, method);
        // 得到监听的代理对象
        Object listener = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{listenerType}, handler);
        try {
            // 通过反射获取控件
            Method findViewById = clazz.getMethod(EventUtils.FIND_VIEW_BY_ID, int.class);
            // 遍历所有控件id
            for (int id : ids) {
                try {
                    // 此处需要重新反射拿控件，否则将导致没有findViewById的控件无法触发事件．
                    // 所以此处的事件回调，无需事先实例化控件
                    // 后续可以考虑用缓存保存已经拿过的控件．
                    Object view = findViewById.invoke(object, id);
                    // 通过反射拿到设置事件的方法
                    Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                    // 执行方法，此处执行的对象就不是object了，此处的执行对象应该是控件
                    setter.invoke(view, listener);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IllegalAccessException：" + e.getMessage());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    Log.e(TAG, "InvocationTargetException：" + e.getMessage());
                }
            }
            Log.i(TAG, "注入完成：" + methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, "NoSuchMethodException：" + e.getMessage());
        }
    }
}

