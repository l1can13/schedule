package com.schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class RecyclerViewFiles extends RecyclerView.Adapter<RecyclerViewFiles.ViewHolder> {

    private List<CustomFiles> filesList;
    private List<CustomFiles> selectedList = new LinkedList<>();
    private Context context;
    private MainViewModel mainViewModel;
    private boolean isEnable = false, isSelectedAll = false;

    public RecyclerViewFiles(List<CustomFiles> filenamesList, Context context) {
        this.filesList = filenamesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewFiles.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_files_layout, parent, false);
        mainViewModel = ViewModelProviders.of((FragmentActivity) context)
                .get(MainViewModel.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewFiles.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CustomFiles current = filesList.get(position);

        holder.filename.setText(current.getName());
        holder.recyclerViewItemsParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isEnable) {
                    ActionMode.Callback callback = new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                            MenuInflater menuInflater = actionMode.getMenuInflater();
                            menuInflater.inflate(R.menu.recycler_view_menu, menu);
                            return true;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                            isEnable = true;
                            clickItem(holder);

                            mainViewModel.getText().observe((LifecycleOwner) context, new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    actionMode.setTitle(String.format("%s выбрано", s));
                                }
                            });

                            return true;
                        }

                        @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            int id = menuItem.getItemId();

                            switch (id) {
                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("Вы действительно хотите удалить файл их сохранённых?")
                                            .setCancelable(false)
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    for (CustomFiles s : selectedList) {
                                                        filesList.remove(s);
                                                    }
                                                    actionMode.finish();
                                                    Toast.makeText(context,"Файл удалён из сохранённых!",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    actionMode.finish();
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.setTitle("Внимание!");
                                    alert.show();
                                    break;
                                case R.id.selectAll:
                                    if (selectedList.size() == filesList.size()) {
                                        isSelectedAll = false;
                                        selectedList.clear();
                                    } else {
                                        isSelectedAll = true;
                                        selectedList.clear();
                                        selectedList.addAll(filesList);
                                    }
                                    mainViewModel.setText(String.valueOf(selectedList.size()));
                                    notifyDataSetChanged();
                                    break;
                            }
                            return true;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDestroyActionMode(ActionMode actionMode) {
                            isEnable = false;
                            isSelectedAll = false;
                            selectedList.clear();
                            notifyDataSetChanged();
                        }
                    };
                    ((AppCompatActivity) view.getContext()).startActionMode(callback);
                } else {
                    clickItem(holder);
                }
                return true;
            }
        });

        if (isSelectedAll) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.recyclerViewItemsParent.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.recyclerViewItemsParent.setBackgroundColor(Color.WHITE);
        }

        holder.recyclerViewItemsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.open(context);
            }
        });
    }

    public void clickItem(ViewHolder holder) {
        CustomFiles current = filesList.get(holder.getAdapterPosition());

        if (holder.checkBox.getVisibility() == View.GONE) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.recyclerViewItemsParent.setBackgroundColor(Color.LTGRAY);
            selectedList.add(current);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.recyclerViewItemsParent.setBackgroundColor(Color.WHITE);
            selectedList.remove(current);
        }

        mainViewModel.setText(String.valueOf(selectedList.size()));
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView filename;
        public CheckBox checkBox;
        public RelativeLayout recyclerViewItemsParent;

        public ViewHolder(View itemView) {
            super(itemView);

            filename = itemView.findViewById(R.id.recyclerViewFileName);
            checkBox = itemView.findViewById(R.id.recyclerViewCheckBox);
            recyclerViewItemsParent = itemView.findViewById(R.id.recyclerViewItemsParent);
        }
    }

}
