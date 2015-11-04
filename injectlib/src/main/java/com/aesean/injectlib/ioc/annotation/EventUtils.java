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
