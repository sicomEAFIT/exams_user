package com.svanegas.exams.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.svanegas.exams.view.MainActivity;

public abstract class HidingToolbarScrollListener extends
        RecyclerView.OnScrollListener {

  private static final float HIDE_THRESHOLD = 10;
  private static final float SHOW_THRESHOLD = 70;

  private int toolbarOffset = 0;
  private int toolbarHeight;
  private int totalScrolledDistance;
  private boolean controlsVisible = true;

  public HidingToolbarScrollListener(Context context) {
    // TODO It crashes here when screen goes to landscape and viewpager is used
    this.toolbarHeight = ((MainActivity) context).getToolbarHeight();
  }

  public void setToolbarOffset(int toolbarOffset) {
    this.toolbarOffset = toolbarOffset;
  }

  public void setTotalScrolledDistance(int totalScrolledDistance) {
    this.totalScrolledDistance = totalScrolledDistance;
  }

  @Override
  public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);

    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
      // Si no se ha hecho scroll lo suficiente, no se puede ocultar la barra
      if(totalScrolledDistance < toolbarHeight) {
        setVisible();
      }
      else {
        // Si la barra está visible
        if (controlsVisible) {
          if (toolbarOffset > HIDE_THRESHOLD) setInvisible();
          else setVisible();
        }
        else {
          if ((toolbarHeight - toolbarOffset) > SHOW_THRESHOLD) setVisible();
          else setInvisible();
        }
      }
    }

  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    clipToolbarOffset();
    onMoved(toolbarOffset);

    if ((toolbarOffset < toolbarHeight && dy > 0) ||
        (toolbarOffset > 0 && dy < 0)) {
      toolbarOffset += dy;
    }

    totalScrolledDistance += dy;
    if (totalScrolledDistance < 0) totalScrolledDistance = 0;
  }

  private void clipToolbarOffset() {
    if (toolbarOffset > toolbarHeight) {
      toolbarOffset = toolbarHeight;
    }
    else if (toolbarOffset < 0) {
      toolbarOffset = 0;
    }
  }

  private void setVisible() {
    if (toolbarOffset > 0) {
      onShow();
      toolbarOffset = 0;
    }
    controlsVisible = true;
  }

  private void setInvisible() {
    if (toolbarOffset < toolbarHeight) {
      onHide();
      toolbarOffset = toolbarHeight;
    }
    controlsVisible = false;
  }

  public abstract void onMoved(int distance);
  public abstract void onShow();
  public abstract void onHide();
}

