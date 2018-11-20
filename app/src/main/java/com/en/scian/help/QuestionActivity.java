package com.en.scian.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/25/10:37
 * @Description: ${TODO}
 */
public class QuestionActivity extends BaseActivity implements View.OnClickListener{

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.search_titleText);
        title.setText(getResources().getString(R.string.changjianwenti));
        findViewById(R.id.search_leftLayout).setOnClickListener(this);
        intent = new Intent(this,XueYaQuestionAcitvity.class);
        findViewById(R.id.faq_rl01).setOnClickListener(this);
        findViewById(R.id.faq_rl02).setOnClickListener(this);
        findViewById(R.id.faq_rl03).setOnClickListener(this);
        findViewById(R.id.faq_rl04).setOnClickListener(this);
        findViewById(R.id.faq_rl05).setOnClickListener(this);
        findViewById(R.id.faq_rl06).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_leftLayout:
                finish();
                break;
            case R.id.faq_rl01:
                intent.putExtra("xml",R.layout.activity_question01);
                startActivity(intent);
                break;
            case R.id.faq_rl02:
                intent.putExtra("xml",R.layout.activity_question02);
                startActivity(intent);
                break;
            case R.id.faq_rl03:
                intent.putExtra("xml",R.layout.activity_question03);
                startActivity(intent);
                break;
            case R.id.faq_rl04:
                intent.putExtra("xml",R.layout.activity_question04);
                startActivity(intent);
                break;
            case R.id.faq_rl05:
                intent.putExtra("xml",R.layout.activity_question05);
                startActivity(intent);
                break;
            case R.id.faq_rl06:
                intent.putExtra("xml",R.layout.activity_question06);
                startActivity(intent);
                break;
        }
    }
}
