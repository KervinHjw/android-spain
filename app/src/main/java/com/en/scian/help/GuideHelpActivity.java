package com.en.scian.help;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/25/10:44
 * @Description: ${TODO}
 */
public class GuideHelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_help);
        TextView title = (TextView)findViewById(R.id.search_titleText);
        title.setText(getResources().getString(R.string.xueyaceliangzhidaoactivity_xyclzd));
        findViewById(R.id.search_leftLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
