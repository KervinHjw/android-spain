package com.en.scian.help;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.en.scian.BaseActivity;
import com.en.scian.R;
import com.en.scian.adapter.SheQuShengHuoPagerAdapter;
import com.en.scian.main.fragment.HistoryDataFragment;
import com.en.scian.main.fragment.XueYaHistoryChartFragment;
import com.en.scian.main.fragment.XueYaHistoryDataFragment;
import com.en.scian.view.NoScrollViewPager;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/11/10:52
 * @Description: ${TODO}
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener{

    private TextView title;
    private RadioGroup group;
    private ImageView histroy_bg;
    private Bitmap btp;
    private NoScrollViewPager mTabPager;
    private SheQuShengHuoPagerAdapter adapter;
    private RadioButton help_xueyazhishi;
    private RadioButton help_question;
    private RadioButton help_shiyong;
    private LinearLayout back;
    private RelativeLayout knowledge_rl;
    private RelativeLayout faq_rl;
    private RelativeLayout guide_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.search_titleText);
        title.setText(getResources().getString(R.string.home_left_menu_help_text));
        back = (LinearLayout) findViewById(R.id.search_leftLayout);
        back.setOnClickListener(this);
        knowledge_rl = (RelativeLayout) findViewById(R.id.knowledge_rl);
        faq_rl = (RelativeLayout) findViewById(R.id.faq_rl);
        guide_rl = (RelativeLayout) findViewById(R.id.guide_rl);
        knowledge_rl.setOnClickListener(this);
        faq_rl.setOnClickListener(this);
        guide_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_leftLayout:
                finish();
                break;
            case R.id.knowledge_rl:
                startActivity(new Intent(this,KnowledgeActivity.class));
                break;
            case R.id.faq_rl:
                startActivity(new Intent(this,QuestionActivity.class));
                break;
            case R.id.guide_rl:
                startActivity(new Intent(this,GuideHelpActivity.class));
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}
