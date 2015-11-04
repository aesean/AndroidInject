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

package com.aesean.aeseaninject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aesean.injectlib.ioc.InjectActivity;

/**
 * 主要是添加注入入口，注解本身是不会主动执行自己的，需要被反射调用，所以需要一个调用入口。
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这里的this其实是继承之后的子类。
        InjectActivity.inject(this);
    }
}
