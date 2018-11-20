package com.en.scian.help;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/25/13:53
 * @Description: ${TODO}
 */
public class XueYaKnowledgeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().getIntExtra("xml",R.layout.activity_xuyaknowledge));
        findViewById(R.id.search_leftLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
