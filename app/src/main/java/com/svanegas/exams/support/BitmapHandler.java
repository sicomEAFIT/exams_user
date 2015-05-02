package com.svanegas.exams.support;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.TypedValue;

import java.io.IOException;

public class BitmapHandler {

  public static Bitmap rotateImage(String imagePath) throws IOException {
    BitmapFactory.Options bounds = new BitmapFactory.Options();
    bounds.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(imagePath, bounds);

    BitmapFactory.Options opts = new BitmapFactory.Options();
    Bitmap bm = BitmapFactory.decodeFile(imagePath, opts);
    ExifInterface exif = new ExifInterface(imagePath);
    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
    int orientation = orientString != null ? Integer.parseInt(orientString) :
            ExifInterface.ORIENTATION_NORMAL;

    int rotationAngle = 0;
    if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
      rotationAngle = 90;
    if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
      rotationAngle = 180;
    if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
      rotationAngle = 270;

    Matrix matrix = new Matrix();
    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2,
            (float) bm.getHeight() / 2);
    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,
            bounds.outWidth, bounds.outHeight, matrix, true);

    return ThumbnailUtils.extractThumbnail(rotatedBitmap, 800, 450);
    //return rotatedBitmap;
  }

  public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color,
                                              int cornerDips, int borderDips,
                                              Context context) {
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
            Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(output);

    final int borderSizePx = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
            context.getResources().getDisplayMetrics());
    final int cornerSizePx = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
            context.getResources().getDisplayMetrics());
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    final RectF rectF = new RectF(rect);

    // prepare canvas for transfer
    paint.setAntiAlias(true);
    paint.setColor(0xFFFFFFFF);
    paint.setStyle(Paint.Style.FILL);
    canvas.drawARGB(0, 0, 0, 0);
    canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

    // draw bitmap
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);

    // draw border
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth((float) borderSizePx);
    canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);
    return output;
  }
}
