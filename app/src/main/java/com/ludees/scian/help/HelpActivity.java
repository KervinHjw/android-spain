package com.ludees.scian.help;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import com.ludees.scian.BaseActivity;
import com.ludees.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/11/10:52
 * @Description: ${TODO}
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener{

    private TabLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initView();
    }

    private void initView() {
        tableLayout = (TabLayout) findViewById(R.id.tablelayout);
        tableLayout.addTab(tableLayout.newTab().setText(getResources().getString(R.string.app_name)));
    }

    @Override
    public void onClick(View v) {

    }
}
