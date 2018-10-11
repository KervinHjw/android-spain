package com.ludees.scian.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kevin
 * @organization 上海鸣鹿信息科技有限公司
 * @date 2018/10/11/11:57
 * @Description: ${TODO}
 */
public class PagerAdapter extends FragmentPagerAdapter{
    private int postion;
    private Map<Integer , Fragment> mFragmentHashMap = new HashMap<>();
    public PagerAdapter (FragmentManager fm , int postion){
        super(fm);
        this.postion = postion;
    }
    @Override
    public Fragment getItem(int position) {
        return creartFragment(position);
    }

    private Fragment creartFragment(int position) {
        Fragment fragment = mFragmentHashMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new XueYaZhiShiFragment();
                    break;
                case 1:
                    fragment = new QuestionFragment();
                    break;
                case 2:
                    fragment = new TutorialFragment();
                    break;
                    default:
                        break;
            }
            mFragmentHashMap.put(position, fragment);
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return postion;
    }
}
