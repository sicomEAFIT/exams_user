package com.svanegas.exams.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.svanegas.exams.R;
import com.svanegas.exams.view.FirstFragment;
import com.svanegas.exams.view.MainActivity;
import com.svanegas.exams.view.SecondFragment;

import it.neokree.materialtabs.MaterialTabHost;

public class ViewPagerAdapter extends FragmentStatePagerAdapter implements
        ViewPager.OnPageChangeListener {

  private Context context;
  private FirstFragment firstFragment;
  private SecondFragment secondFragment;

  public ViewPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    this.context = context;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        if (firstFragment == null) firstFragment = new FirstFragment();
        return firstFragment;
      case 1:
        if (secondFragment == null) secondFragment = new SecondFragment();
        return secondFragment;
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

  @Override
  public void onPageScrolled(int position, float positionOffset,
                             int positionOffsetPixels) {
    // Solo movemos el botón si el primer fragmento no es nulo y estamos en
    // la primera posición. (Al finalizar el scroll de la página 'position'
    // se cambia a 1 y el offset queda en 0, por lo que el botón volvería
    // al mismo lugar.
    if (firstFragment != null && position == 0) {
      firstFragment.translateFloatingActionButton(positionOffsetPixels);
    }
  }

  @Override
  public void onPageSelected(int position) {
    if (context != null) {
      ((MainActivity) context).changeTabHostPosition(position);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
}
