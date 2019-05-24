package com.example.app_for_rightech_iot_cloud;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<NotificationsForRecycler> notifications;

    DataAdapter(Context context, List<NotificationsForRecycler> notifications) {
        this.notifications = notifications;
        this.inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
        NotificationsForRecycler notification = notifications.get(position);
        holder.imageView.setImageResource(notification.getImage());
        holder.indicatorView.setText(notification.getIndicator());
        holder.paramView.setText(notification.getParam());
        holder.dateView.setText(notification.getDate());
        holder.warningImageView.setImageResource(notification.getWarningImage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView, warningImageView;
        final TextView indicatorView, dateView, paramView;
        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image);
            indicatorView = (TextView) view.findViewById(R.id.indicator);
            dateView = (TextView) view.findViewById(R.id.date_and_time);
            paramView = (TextView) view.findViewById(R.id.param);
            warningImageView = (ImageView)view.findViewById(R.id.warning);
        }
    }
}