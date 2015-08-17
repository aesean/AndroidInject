package com.aesean.injectlib.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

/**
 * 单击事件注入
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@BaseEvent( listenerType = View.OnClickListener.class, listenerSetter = EventUtils.SET_ON_CLICK_LISTENER,listenerCallback = EventUtils.SET_ON_CLICK_LISTENER_CALLBACK)
public @interface OnClick {
	int[]value();
}
