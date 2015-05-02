package com.svanegas.exams;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.svanegas.exams.adapter.ExamsAdapter;
import com.svanegas.exams.model.ExamItem;

import java.util.ArrayList;
import java.util.List;


public class ExamActivity extends ActionBarActivity {

  private Toolbar toolbar;
  private RecyclerView recyclerView;
  private ExamsAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exam);
    toolbar = (Toolbar) findViewById(R.id.app_bar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    adapter = new ExamsAdapter(this);
    recyclerView.setAdapter(adapter);
    //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    ImageView imageView = new ImageView(this);
    imageView.setImageResource(R.drawable.ic_map_white_48dp);

    FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
            .setContentView(imageView)
            .build();
  }

  public static List<ExamItem> getData() {
    List<ExamItem> data = new ArrayList();
    int[] icons = {R.drawable.ic_launcher,
                   R.drawable.ic_map_black_18dp,
                   R.drawable.ic_map_white_24dp,
                   R.drawable.ic_map_grey600_48dp};
    String [] titles = {"Hola", "Santi", "Como", "estas"};

    for (int i = 0; i < 100; ++i) {
      ExamItem exam = new ExamItem();
      exam.setIconId(icons[i % icons.length]);
      exam.setTitle(titles[i % titles.length]);
      data.add(exam);
    }
    return data;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_exam, menu);
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
    if (id == R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
    }
    return super.onOptionsItemSelected(item);
  }
}
