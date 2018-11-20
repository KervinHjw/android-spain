package com.en.scian.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.R;

import org.w3c.dom.Text;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/25/10:43
 * @Description: ${TODO}
 */
public class KnowledgeActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout back;
    private TextView title;
    private Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
        initView();
    }

    private void initView() {
        in = new Intent(this,XueYaKnowledgeActivity.class);
        back = (LinearLayout) findViewById(R.id.search_leftLayout);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.search_titleText);
        title.setText(getResources().getString(R.string.xueyazhishi));
        findViewById(R.id.xueya_knowledge01_rl).setOnClickListener(this);
        findViewById(R.id.xueya_knowledge02_rl).setOnClickListener(this);
        findViewById(R.id.xueya_knowledge03_rl).setOnClickListener(this);
        findViewById(R.id.xueya_knowledge04_rl).setOnClickListener(this);
        findViewById(R.id.xueya_knowledge05_rl).setOnClickListener(this);
        findViewById(R.id.xueya_knowledge06_rl).setOnClickListener(this);
        findViewById(R.id.xueya_knowledge07_rl).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_leftLayout:
                finish();
                break;
            case R.id.xueya_knowledge01_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge);
                startActivity(in);
                break;
            case R.id.xueya_knowledge02_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge02);
                startActivity(in);
                break;
            case R.id.xueya_knowledge03_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge03);
                startActivity(in);
                break;
            case R.id.xueya_knowledge04_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge04);
                startActivity(in);
                break;
            case R.id.xueya_knowledge05_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge05);
                startActivity(in);
                break;
            case R.id.xueya_knowledge06_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge06);
                startActivity(in);
                break;
            case R.id.xueya_knowledge07_rl:
                in.putExtra("xml",R.layout.activity_xuyaknowledge07);
                startActivity(in);
                break;
        }
    }
}
