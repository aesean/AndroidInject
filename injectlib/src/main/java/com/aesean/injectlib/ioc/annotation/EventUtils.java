package com.aesean.injectlib.ioc.annotation;

/**
 * 用于提供事件常量
 */
public class EventUtils {
    /**
     * 全部都是静态常量禁止实例化
     */
    private EventUtils() {
    }

    public final static String SET_ON_CLICK_LISTENER = "setOnClickListener";
    public final static String SET_ON_LONGCLICK_LISTENER = "setOnLongClickListener";
    public final static String SET_ON_CLICK_LISTENER_CALLBACK = "onClick";
    public final static String SET_ON_LONGCLICK_LISTENER_CALLBACK = "onLongClick";

    public final static String FIND_VIEW_BY_ID = "findViewById";
    public final static String SET_CONTENT_VIEW = "setContentView";
}
