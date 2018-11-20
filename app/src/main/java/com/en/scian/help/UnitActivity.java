package com.en.scian.help;

import android.os.Bundle;
import android.test.LoaderTestCase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.en.scian.BaseActivity;
import com.en.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/15/15:45
 * @Description: ${TODO}
 */
public class UnitActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout back;
    private EditText input;
    private float kpa;
    private EditText output;
    private Button count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        initView();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.search_titleText);
        title.setText(getResources().getString(R.string.home_left_menu_danwei_text));
        back = (LinearLayout) findViewById(R.id.search_leftLayout);
        back.setOnClickListener(this);
        count = (Button) findViewById(R.id.count);
        count.setOnClickListener(this);
        output = (EditText) findViewById(R.id.et_output);
        input = (EditText) findViewById(R.id.et_input);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!input.getText().toString().equals("")){
                    kpa = Float.parseFloat(input.getText().toString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_leftLayout:
                finish();
                break;
            case R.id.count:
                output.setText("");
                if(!input.getText().toString().equals("")) {
                    output.setText(Float.parseFloat(input.getText().toString()) * 7.5 + "");
                }
                break;
        }
    }
}
