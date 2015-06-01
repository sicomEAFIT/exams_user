package com.svanegas.exams.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.svanegas.exams.R;
import com.svanegas.exams.model.ExamItem;
import com.svanegas.exams.view.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamsAdapter extends
        RecyclerView.Adapter<ExamsAdapter.ExamViewHolder> {


  private Context context;
  private LayoutInflater inflater;
  private List<ExamItem> data = Collections.emptyList();
  private RecyclerView.ItemAnimator animator;

  public ExamsAdapter(Context context, RecyclerView.ItemAnimator animator) {
    this.context = context;
    inflater = LayoutInflater.from(context);
    data = new ArrayList();
    this.animator = animator;
  }

  @Override
  public ExamViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
    View view = inflater.inflate(R.layout.exam_item_view, parent, false);
    ExamViewHolder holder = new ExamViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(ExamViewHolder holder, int position) {
    ExamItem item = data.get(position);
    String title = context.getString(R.string.page_number, position + 1);
    holder.getTitle().setText(title);
    if (item.getPicture() != null) {
      holder.getIcon().setImageBitmap(item.getPicture());
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void addItem(ExamItem item) {
    data.add(item);
    notifyItemInserted(data.size() - 1);
  }

  public void removeItem(int position) {
    data.remove(position);
    notifyItemRemoved(position);
    // Se corre una tarea asincrónica que duerma por el tiempo que demora la
    // animación de remover un ítem del RecyclerView.
    // No se hace directamente acá porque no modificaba las vistas que no
    // estaban recicladas. Y el notifyDataSetChanged interrumpe la animación.
    new UpdateItemsPageNumberTask().execute(animator.getRemoveDuration());
  }

  class ExamViewHolder extends RecyclerView.ViewHolder implements
          View.OnClickListener {
    private ImageView icon;
    private TextView title;
    private ImageView deleteButton;

    public ExamViewHolder(View itemView) {
      super(itemView);
      icon = (ImageView) itemView.findViewById(R.id.picture);
      title = (TextView) itemView.findViewById(R.id.title);
      deleteButton = (ImageView) itemView.findViewById(R.id.delete_button);
      deleteButton.setOnClickListener(this);
    }

    public void setIcon(ImageView icon) {
      this.icon = icon;
    }

    public void setTitle(TextView title) {
      this.title = title;
    }

    public ImageView getIcon() {
      return icon;
    }

    public TextView getTitle() {
      return title;
    }

    @Override
    public void onClick(View v) {
      removeItem(getPosition());
    }
  }

  private class UpdateItemsPageNumberTask extends AsyncTask<Long, Void, Void> {

    protected Void doInBackground(Long... millisParams) {
      long timeToWait = millisParams[0];
      // Se adicionan 300 milisegundos para evitar cortar la animación
      timeToWait += 300;
      try {
        Thread.sleep(timeToWait);
      }
      catch (InterruptedException e) {
        return null;
      }
      return null;
    }

    protected void onPostExecute(Void result) {
      // Cuando pase el tiempo, se notifica que todos los datos cambiaron
      ExamsAdapter.this.notifyDataSetChanged();
      ((MainActivity) context).recyclerViewElementRemoved();
    }
  }
}
