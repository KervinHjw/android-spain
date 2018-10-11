package com.ludees.scian.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ludees.scian.R;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/11/11:10
 * @Description: ${TODO}
 */
public class QuestionFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question,container,false);
    }
}
