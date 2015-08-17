package com.aesean.injectlib.ioc;

import android.util.Log;

import com.aesean.injectlib.ioc.annotation.BaseEvent;
import com.aesean.injectlib.ioc.annotation.EventUtils;
import com.aesean.injectlib.ioc.annotation.FindViewById;
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
 * Activity注入
 */
public class InjectActivity {
    private final static String TAG = "DEBUG";

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
        Log.e(TAG, "开始进行Class注入");
        // 获取activity的类类型用于反射注入
        Class<?> clazz = object.getClass();
        // 获取当前activity上的SetContentView注解
        SetContentView annotation = clazz.getAnnotation(SetContentView.class);
        // 可能当前类没有SetContentView注解，所以需要判断是否为空
        if (annotation != null) {
            try {
                // 通过反射拿到activity的setContentView方法
                Method method = clazz.getMethod(EventUtils.SET_CONTENT_VIEW, int.class);
                if (method != null) {
                    Log.e(TAG, "开始注入Method：" + method.getName());
                    // 注入．此处相当于平时Activity类中的setContentView方法．前面做那么多其实就是为了这个方法．
                    method.invoke(object, annotation.value());
                    Log.e(TAG, "Class注入成功：" + method.getName());
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
        Log.e(TAG, "开始进行Field注入");
        // 获取activity的类类型用于反射注入
        Class<?> clazz = object.getClass();
        // 获取所有属性
        Field[] declaredFields = clazz.getDeclaredFields();
        // 遍历所有属性
        for (Field field : declaredFields) {
            // 记录属性可见性
            boolean accessible = field.isAccessible();
            // 修改参数可见性
            field.setAccessible(true);
            // 获取FindViewById注解
            FindViewById annotation = field.getAnnotation(FindViewById.class);
            if (annotation != null) {
                try {
                    Method method = clazz.getMethod(EventUtils.FIND_VIEW_BY_ID, int.class);
                    if (method != null) {
                        field.set(object, method.invoke(object, annotation.value()));
                        Log.e(TAG, "注入参数成功：" + field.getName());
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
                Log.e(TAG, "获取FindViewById Annotation为空：" + field.getName());
            }
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
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClickAnnotation = method.getAnnotation(OnClick.class);
            //Class<? extends Annotation> annotationType = annotation.annotationType();
            if (onClickAnnotation != null) {
                Class<? extends Annotation> type = onClickAnnotation.annotationType();
                injectEvent(object, clazz, method, onClickAnnotation.value(), type);
            }

            OnLongClick onLongClickAnnotation = method.getAnnotation(OnLongClick.class);
            if (onLongClickAnnotation != null) {
                Class<? extends Annotation> type = onLongClickAnnotation.annotationType();
                injectEvent(object, clazz, method, onLongClickAnnotation.value(), type);
            }
        }
    }

    private static void injectEvent(Object object, Class<?> clazz, Method method, int[] values, Class<? extends Annotation> type) {
        BaseEvent baseEvent = type.getAnnotation(BaseEvent.class);
        Class<?> listenerType = baseEvent.listenerType();
        String listenerSetter = baseEvent.listenerSetter();
        String listenerCallback = baseEvent.listenerCallback();

        ListenerInvocationHandler handler = new ListenerInvocationHandler(object);
        handler.addMethod(listenerCallback, method);
        // 得到监听的代理对象
        Object listener = Proxy.newProxyInstance(object.getClass().getClassLoader(), new Class[]{listenerType}, handler);
        try {
            Method findViewById = clazz.getMethod(EventUtils.FIND_VIEW_BY_ID, int.class);
            //int[] ids = onClickAnnotation.value();
            for (int id : values) {
                try {
                    // 此处需要重新反射拿控件，否则将导致没有findViewById的控件无法触发事件．
                    // 后续可以考虑用缓存保存已经拿过的控件．
                    Object view = findViewById.invoke(object, id);
                    Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                    setter.invoke(view, listener);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IllegalAccessException：" + e.getMessage());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    Log.e(TAG, "InvocationTargetException：" + e.getMessage());
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, "NoSuchMethodException：" + e.getMessage());
        }
    }
}
