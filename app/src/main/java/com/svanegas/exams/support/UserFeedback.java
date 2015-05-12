package com.svanegas.exams.support;

import android.app.Activity;
import android.content.Context;

import com.github.mrengineer13.snackbar.SnackBar;
import com.svanegas.exams.R;

public class UserFeedback {

  public static SnackBar showSnackBar(Context contextActivity, int resMessage,
                                  int resButtonText,
                                  SnackBar.OnVisibilityChangeListener
                                          visibilityChangeListener) {
    return new SnackBar.Builder((Activity) contextActivity)
                  .withMessageId(resMessage)
                  .withActionMessageId(resButtonText)
                  .withTextColorId(R.color.accent_color)
                  .withVisibilityChangeListener(visibilityChangeListener)
                  .withDuration(SnackBar.LONG_SNACK)
                  .show();
  }
}
