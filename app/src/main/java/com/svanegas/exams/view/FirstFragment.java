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
import android.widget.ImageView;
import android.widget.Toast;

import com.melnykov.fab.ScrollDirectionListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.svanegas.exams.adapter.ExamsAdapter;
import com.svanegas.exams.R;
import com.svanegas.exams.model.ExamItem;
import com.svanegas.exams.support.BitmapHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirstFragment extends Fragment {

  private static final int REQUEST_IMAGE_CAPTURE = 1;
  private Uri imageUri;
  private RecyclerView recyclerView;
  private ExamsAdapter adapter;
  private FloatingActionButton actionButton;
  private FloatingActionMenu actionMenu;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View layout = inflater.inflate(R.layout.first_fragment, container, false);

    recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
    adapter = new ExamsAdapter(getActivity());
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    /*final com.melnykov.fab.FloatingActionButton fab =
            (com.melnykov.fab.FloatingActionButton) layout.findViewById(R.id.fab);
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

    fab.setOnClickListener(takePhotoListener);*/

    SubActionButton.Builder itemBuilder = new SubActionButton
            .Builder(getActivity());
    // Ítem para agregar nuevo
    ImageView addIcon = new ImageView(getActivity());
    addIcon.setImageResource(R.drawable.ic_plus);
    SubActionButton addItem = itemBuilder.setContentView(addIcon).build();
    addItem.setOnClickListener(takePhotoListener);

    // Ítem finalizar
    ImageView doneIcon = new ImageView(getActivity());
    doneIcon.setImageResource(R.drawable.ic_check);
    SubActionButton doneItem = itemBuilder.setContentView(doneIcon).build();


    ImageView imageView = new ImageView(getActivity());
    imageView.setImageResource(R.drawable.ic_action_new);

    // Botón para abrir menú
    actionButton = new FloatingActionButton.Builder(getActivity())
            .setContentView(imageView)
            .setBackgroundDrawable(R.drawable.button_action_selector)
            .build();

    int radius = getActivity().getResources().getDimensionPixelSize(R.dimen.action_menu_radius);
    // Menú
    actionMenu = new FloatingActionMenu.Builder(getActivity())
            .addSubActionView(addItem, 100, 100)
            .addSubActionView(doneItem, 100, 100)
            .attachTo(actionButton)
            .setRadius((int) (radius * 0.75))
            .build();
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
      actionMenu.close(true);
      openImageIntent();
    }
  };

  private void openImageIntent() {
    // En caso de seleccionar cámara se configura el destino.
    final File photo = createImageFile();
    imageUri = Uri.fromFile(photo);

    // Camera.
    final List<Intent> cameraIntents = new ArrayList();
    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    final PackageManager packageManager = getActivity().getPackageManager();
    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
            captureIntent, 0);
    for (ResolveInfo res : listCam) {
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

  private File createImageFile() {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(new Date());
    String imageFileName = timeStamp + "_";
    String rootPath = Environment.getExternalStorageDirectory().toString();
    String appName = getResources().getString(R.string.app_name);
    File storageDir = new File(rootPath + "/" + appName +
            "/Media/Exams Captures");
    storageDir.mkdirs();
    File image = null;
    try {
      image = File.createTempFile(
              imageFileName,  /* prefix */
              ".jpg",         /* suffix */
              storageDir      /* directory */
      );
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return image;
  }

  private void deleteImageFile(Uri url) {
    if (url != null) {
      File fileToDelete = new File(url.getPath());
      if (fileToDelete.exists()) fileToDelete.delete();
    }
  }

  private String getPath(Uri uri) throws Exception {
    // just some safety built in
    if (uri == null) throw new Exception("Uri is null");
    // try to retrieve the image from the media store first
    // this will only work for images selected from gallery
    String[] projection = {MediaStore.Images.Media.DATA};
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

  public void translateFloatingActionButton(int pixels) {
    if (actionButton != null) {
      if (actionMenu.isOpen()) actionMenu.close(true);
      actionButton.setTranslationX(-1 * pixels);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        try {
          final boolean fromCamera;
          if (data == null) fromCamera = true;
          else {
            final String action = data.getAction();
            if (action == null) fromCamera = false;
            else fromCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
          }

          Uri selectedImage;
          if (fromCamera) selectedImage = imageUri;
          else {
            deleteImageFile(imageUri);
            selectedImage = data == null ? null : data.getData();
          }
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
          Log.e("RESULT", "OnActivityResult FRAG - > ERR: " + e.getMessage());
        }
      }
    }
    else if (resultCode == Activity.RESULT_CANCELED) {
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        actionMenu.open(true);
        deleteImageFile(imageUri);
      }
    }
  }
}
