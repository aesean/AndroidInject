package com.aesean.injectlib.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

/**
 * 长按事件注入
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@BaseEvent(listenerType = View.OnLongClickListener.class, listenerSetter = EventUtils.SET_ON_LONGCLICK_LISTENER, listenerCallback = EventUtils.SET_ON_LONGCLICK_LISTENER_CALLBACK)
public @interface OnLongClick {
	int[]value();
}
