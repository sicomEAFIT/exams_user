package com.svanegas.exams.view;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.svanegas.exams.R;
import com.svanegas.exams.adapter.ViewPagerAdapter;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class MainActivity extends ActionBarActivity implements
        MaterialTabListener {

  private LinearLayout toolbarContainer;
  private Toolbar toolbar;
  private MaterialTabHost tabHost;
  private ViewPager viewPager;
  private ViewPagerAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    toolbarContainer = (LinearLayout) findViewById(R.id.app_bar_container);
    toolbar = (Toolbar) findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);

    tabHost = (MaterialTabHost) findViewById(R.id.tab_host);
    tabHost.setAccentColor(getResources().getColor(R.color.white));

    viewPager = (ViewPager) findViewById(R.id.view_pager);

    adapter = new ViewPagerAdapter(this,
            getSupportFragmentManager());
    viewPager.setAdapter(adapter);
    viewPager.setOnPageChangeListener(adapter);

    for (int i = 0; i < adapter.getCount(); ++i) {
      MaterialTab tab = tabHost.newTab();
      tab.setText(adapter.getPageTitle(i));
      tab.setTabListener(this);
      // Remover efecto 'ripple'. Porque es feo.
      tab.getView().findViewById(R.id.reveal).setVisibility(View.GONE);
      tabHost.addTab(tab);
    }
  }

  public void changeTabHostPosition(int position) {
    tabHost.setSelectedNavigationItem(position);
    if (position == 1) showToolbar();
  }

  public void translateToolbarContainer(int distance) {
    toolbarContainer.setTranslationY(-distance);
  }

  public void showToolbar() {
    toolbarContainer.animate().translationY(0).setInterpolator(
            new DecelerateInterpolator(2)).start();
    adapter.getFirstFragment().getHidingToolbarScrollListener()
            .setToolbarOffset(0);
  }

  public void hideToolbar() {
    toolbarContainer.animate().translationY(-getToolbarHeight())
            .setInterpolator(new AccelerateInterpolator(2)).start();
  }

  public int getToolbarHeight() {
    return toolbar.getMinimumHeight();
  }

  public int getTabsHeight() {
    return tabHost.getLayoutParams().height;
  }

  public int getToolbarAndTabsHeight() {
    return getToolbarHeight() + getTabsHeight();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onTabSelected(MaterialTab materialTab) {
    viewPager.setCurrentItem(materialTab.getPosition());
  }

  @Override
  public void onTabReselected(MaterialTab materialTab) {

  }

  @Override
  public void onTabUnselected(MaterialTab materialTab) {

  }
}
