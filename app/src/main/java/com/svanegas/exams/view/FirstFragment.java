package com.svanegas.exams.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;
import com.svanegas.exams.adapter.ExamsAdapter;
import com.svanegas.exams.R;
import com.svanegas.exams.model.ExamItem;
import com.svanegas.exams.support.BitmapHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private Uri imageUri;
  private RecyclerView recyclerView;
  private ExamsAdapter adapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View layout = inflater.inflate(R.layout.first_fragment, container, false);

    recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
    adapter = new ExamsAdapter(getActivity());
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    final FloatingActionButton fab =
            (FloatingActionButton) layout.findViewById(R.id.fab);
    fab.attachToRecyclerView(recyclerView, new ScrollDirectionListener() {
      @Override
      public void onScrollDown() {
      }

      @Override
      public void onScrollUp() {
      }
    }, new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
        // Si se paró de hacer scroll mostrar el Floating Action Button
        if (scrollState == 0) fab.show(true);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
      }
    });

    fab.setOnClickListener(takePhotoListener);
    return layout;
  }

  public ExamItem buildExamItem(Bitmap image) {
    ExamItem exam = new ExamItem();
    exam.setIcon(image);
    return exam;
  }

  private View.OnClickListener takePhotoListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      //captureImage(v);
      openImageIntent();
    }
  };

  /*private void captureImage(View v) {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    File photo = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "temp_picture.jpg");
    imageUri = Uri.fromFile(photo);
    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
  }*/

  public String getPath(Uri uri) throws Exception {
    // just some safety built in
    if( uri == null ) throw new Exception("Uri is null");
    // try to retrieve the image from the media store first
    // this will only work for images selected from gallery
    String[] projection = { MediaStore.Images.Media.DATA };
    Cursor cursor = getActivity().managedQuery(uri, projection, null, null,
            null);
    if (cursor != null) {
      int column_index = cursor
              .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    }
    // this is our fallback here
    return uri.getPath();
  }

  private void openImageIntent() {
    // En caso de seleccionar cámara se configura el destino.
    final File photo = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), "temp_picture.jpg");
    imageUri = Uri.fromFile(photo);

    // Camera.
    final List<Intent> cameraIntents = new ArrayList();
    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    final PackageManager packageManager = getActivity().getPackageManager();
    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
            captureIntent, 0);
    for(ResolveInfo res : listCam) {
      final String packageName = res.activityInfo.packageName;
      final Intent intent = new Intent(captureIntent);
      intent.setComponent(new ComponentName(res.activityInfo.packageName,
              res.activityInfo.name));
      intent.setPackage(packageName);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
      cameraIntents.add(intent);
    }

    // Filesystem.
    final Intent galleryIntent = new Intent();
    galleryIntent.setType("image/*");
    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

    // Chooser of filesystem options.
    final Intent chooserIntent = Intent.createChooser(galleryIntent,
            getResources().getString(R.string.select_image_source));

    // Add the camera options.
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents
            .toArray(new Parcelable[cameraIntents.size()]));

    startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
  }




  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.i("RESULT", "OnActivityResult del fragmento");
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        try {
          Uri selectedImage;
          if (data == null) selectedImage = imageUri;
          else selectedImage = data.getData();
          if (selectedImage == null) throw new Exception("Uri image is null");
          String path = getPath(selectedImage);
          ContentResolver resolver = getActivity().getContentResolver();
          resolver.notifyChange(selectedImage, null);
          Bitmap bitmapResult;
          bitmapResult = BitmapHandler.rotateImage(path);
          bitmapResult = BitmapHandler.getRoundedCornerBitmap(bitmapResult,
                  Color.TRANSPARENT, 5, 0, getActivity());
          adapter.addItem(buildExamItem(bitmapResult));
        }
        catch (Exception e) {
          Toast.makeText(getActivity(), getResources()
                          .getString(R.string.could_not_load_image),
                          Toast.LENGTH_SHORT).show();
          Log.i("RESULT", "OnActivityResult FRAG - > ERR: " + e.getMessage());
        }
      }
    }
  }
}
