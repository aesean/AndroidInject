package com.aesean.injectlib.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件基础注解
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseEvent {
	/**
	 * 事件类型
	 * 
	 * @return 事件类类型
	 */
	Class<?>listenerType();

	/**
	 * 设置事件方法名称
	 * 
	 * @return 方法名称
	 */
	String listenerSetter();

	/**
	 * 事件回调方法名称
	 * 
	 * @return 方法名称
	 */
	String listenerCallback();
}
