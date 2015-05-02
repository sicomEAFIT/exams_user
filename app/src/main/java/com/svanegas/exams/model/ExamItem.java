package com.svanegas.exams.model;

import android.graphics.Bitmap;

public class ExamItem {
  private int iconId;
  private Bitmap icon;
  private String title;

  public int getIconId() {
    return iconId;
  }

  public String getTitle() {
    return title;
  }

  public void setIconId(int iconId) {
    this.iconId = iconId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Bitmap getIcon() {
    return icon;
  }

  public void setIcon(Bitmap icon) {
    this.icon = icon;
  }
}
