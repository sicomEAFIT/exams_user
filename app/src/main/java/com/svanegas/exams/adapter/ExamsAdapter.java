package com.svanegas.exams.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.svanegas.exams.R;
import com.svanegas.exams.model.ExamItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamsAdapter extends
        RecyclerView.Adapter<ExamsAdapter.ExamViewHolder> {


  private Context context;
  private LayoutInflater inflater;
  private List<ExamItem> data = Collections.emptyList();

  public ExamsAdapter(Context context) {
    this.context = context;
    inflater = LayoutInflater.from(context);
    data = new ArrayList();
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
    if (item.getIcon() != null) holder.getIcon().setImageBitmap(item.getIcon());
    else holder.getIcon().setImageResource(data.get(position).getIconId());
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
  }

  class ExamViewHolder extends RecyclerView.ViewHolder implements
          View.OnClickListener {
    private ImageView icon;
    private TextView title;
    private ImageView deleteButton;

    public ExamViewHolder(View itemView) {
      super(itemView);
      icon = (ImageView) itemView.findViewById(R.id.icon);
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
}
