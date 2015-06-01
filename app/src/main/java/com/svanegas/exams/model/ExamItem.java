package com.svanegas.exams.model;

import android.graphics.Bitmap;

public class ExamItem {

  private Bitmap picture;
  private String path;

  public Bitmap getPicture() {
    return picture;
  }

  public void setPicture(Bitmap picture) {
    this.picture = picture;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
