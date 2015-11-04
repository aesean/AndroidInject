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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aesean.injectlib.ioc.InjectActivity;
import com.aesean.injectlib.ioc.annotation.FindViewById;
import com.aesean.injectlib.ioc.annotation.OnClick;
import com.aesean.injectlib.ioc.annotation.OnLongClick;
import com.aesean.injectlib.ioc.annotation.SetContentView;

@SetContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @FindViewById(R.id.bt_Inject)
    private Button bt_Inject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectActivity.inject(this);
    }

    /**
     * 被注入方法必须为public同时参数必须是View
     *
     * @param v 被点击的View
     */
    @OnClick({R.id.bt_Inject})
    public void myOnClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Inject:
                Toast.makeText(this, "自定义OnClick", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 被注入方法必须为public同时参数必须是View
     *
     * @param v 被点击的View
     * @return 返回boolean类型，表示是否拦截处理
     */
    @OnLongClick(R.id.bt_Inject)
    public boolean myOnLongClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Inject:
                Toast.makeText(this, "自定义OnLongClick", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
