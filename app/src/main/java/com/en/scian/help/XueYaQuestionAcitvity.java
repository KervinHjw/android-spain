package com.en.scian.help;

import android.os.Bundle;
import android.view.View;

import com.en.scian.BaseActivity;
import com.en.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/25/17:18
 * @Description: ${TODO}
 */
public class XueYaQuestionAcitvity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getIntent().getIntExtra("xml",R.layout.activity_question01));
        findViewById(R.id.search_leftLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
