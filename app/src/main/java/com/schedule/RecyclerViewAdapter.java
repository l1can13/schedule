package com.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<CustomFiles> filenamesList;
    private Context context;

    public RecyclerViewAdapter(List<CustomFiles> filenamesList, Context context) {
        this.filenamesList = filenamesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        String current = filenamesList.get(position).getName();

        holder.filename.setText(current);
    }

    @Override
    public int getItemCount() {
        return filenamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView filename;

        public ViewHolder(View itemView) {
            super(itemView);

            filename = itemView.findViewById(R.id.recyclerViewFileName);
        }
    }

}
