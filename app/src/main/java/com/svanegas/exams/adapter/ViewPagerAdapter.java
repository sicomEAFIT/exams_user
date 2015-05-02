package com.svanegas.exams.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.svanegas.exams.R;
import com.svanegas.exams.view.FirstFragment;
import com.svanegas.exams.view.SecondFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  private Context context;

  public ViewPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    this.context = context;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return new FirstFragment();
      case 1:
        return new SecondFragment();
      default:
        return new FirstFragment();
    }
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return context.getResources().getStringArray(R.array.tab_titles)[position];
  }
}
