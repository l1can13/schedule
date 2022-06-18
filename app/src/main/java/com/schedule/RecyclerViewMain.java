package com.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.print.PrintAttributes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class RecyclerViewMain extends RecyclerView.Adapter<RecyclerViewMain.ViewHolder> {

    private Week week;
    private Context context;
    private MainViewModel mainViewModel;

    public RecyclerViewMain(Week week, Context context) {
        this.week = week;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_schedule_layout, parent, false);
        mainViewModel = ViewModelProviders.of((FragmentActivity) context)
                .get(MainViewModel.class);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMain.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        List<Day> days = week.getDays();
        Day currentDay = days.get(position);
        List<Subject> subjects = currentDay.getSubjects();

        holder.dayOfWeek.setText(currentDay.getName());
        for (int i = 0; i < subjects.size(); ++i) {
            /* Initializing subjectBox */
            LinearLayout subjectBox = new LinearLayout(context);

            LinearLayout.LayoutParams subjectBoxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(60 * context.getResources().getDisplayMetrics().density));
            subjectBoxParams.gravity = Gravity.CENTER_VERTICAL;
            if (i != subjects.size() - 1) {
                subjectBoxParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
            }
            subjectBox.setLayoutParams(subjectBoxParams);

            subjectBox.setOrientation(LinearLayout.HORIZONTAL);
            subjectBox.setGravity(Gravity.CENTER_VERTICAL);
            subjectBox.setBackgroundColor(Color.WHITE);

            /* Inside elements */
            /* SubjectTime */
            TextView time = new TextView(context);

            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams((int)(60 * context.getResources().getDisplayMetrics().density), (int)(60 * context.getResources().getDisplayMetrics().density));
            timeParams.gravity = Gravity.CENTER;
            time.setLayoutParams(timeParams);

            time.setGravity(Gravity.CENTER);
            time.setTextSize(18);
            time.setTextColor(context.getResources().getColor(R.color.def));
            time.setTypeface(ResourcesCompat.getFont(context, R.font.tenor_sans));
            time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            time.setText(subjects.get(i).getStartTime() + '\n' + subjects.get(i).getEndTime());
            subjectBox.addView(time);

            /* Splitter */
            LinearLayout splitter = new LinearLayout(context);
            splitter.setLayoutParams(new LinearLayout.LayoutParams((int)(3 * context.getResources().getDisplayMetrics().density), LinearLayout.LayoutParams.MATCH_PARENT));
            switch (subjects.get(i).getType().toLowerCase(Locale.ROOT)) {
                case "лекция":
                    splitter.setBackgroundResource(R.color.lecture);
                    break;
                case "практика":
                    splitter.setBackgroundResource(R.color.practice);
                    break;
                case "лабораторная":
                    splitter.setBackgroundResource(R.color.labs);
                    break;
            }
            subjectBox.addView(splitter);

            /* Helpful linear */
            LinearLayout leftRightParent = new LinearLayout(context);

            LinearLayout.LayoutParams leftRightParentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            leftRightParentParams.gravity = Gravity.CENTER_VERTICAL;
            leftRightParentParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
            leftRightParent.setLayoutParams(leftRightParentParams);

            leftRightParent.setGravity(Gravity.CENTER_VERTICAL);
            leftRightParent.setOrientation(LinearLayout.HORIZONTAL);
            leftRightParent.setWeightSum(4);

            /* LinearLayout (name, building, audition) */
            LinearLayout leftLinear = new LinearLayout(context);

            LinearLayout.LayoutParams leftLinearParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            leftLinearParams.gravity = Gravity.CENTER_VERTICAL;
            leftLinear.setLayoutParams(leftLinearParams);

            leftLinear.setGravity(Gravity.CENTER_VERTICAL);
            leftLinear.setOrientation(LinearLayout.VERTICAL);

            /* Subject name */
            TextView subjectName = new TextView(context);

            LinearLayout.LayoutParams subjectNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            subjectNameParams.gravity = Gravity.START;
            subjectName.setLayoutParams(subjectNameParams);

            subjectName.setGravity(Gravity.START);
            subjectName.setTextSize(16);

            subjectName.setTextColor(context.getResources().getColor(R.color.def));
            subjectName.setTypeface(ResourcesCompat.getFont(context, R.font.tenor_sans));
            subjectName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            subjectName.setText(subjects.get(i).getName());
            subjectName.setGravity(View.TEXT_ALIGNMENT_CENTER);
            leftLinear.addView(subjectName);

            /* Building and audition */
            TextView building = new TextView(context);

            LinearLayout.LayoutParams buildingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            buildingParams.gravity = Gravity.START;
            building.setLayoutParams(buildingParams);

            building.setGravity(Gravity.START);
            building.setTextSize(14);
            building.setTextColor(context.getResources().getColor(R.color.gray));
            building.setTypeface(ResourcesCompat.getFont(context, R.font.tenor_sans));
            building.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            building.setText("корпус " + subjects.get(i).getBuilding() + ", аудитория " + subjects.get(i).getAudition());
            building.setGravity(View.TEXT_ALIGNMENT_CENTER);
            leftLinear.addView(building);

            leftRightParent.addView(leftLinear);

            /* LinearLayout (lecturer, type) */
            LinearLayout rightLinear = new LinearLayout(context);

            LinearLayout.LayoutParams rightLinearParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            rightLinearParams.gravity = Gravity.CENTER_VERTICAL;
            rightLinearParams.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, context.getResources().getDisplayMetrics());
            rightLinear.setLayoutParams(rightLinearParams);

            rightLinear.setGravity(Gravity.CENTER_VERTICAL);
            rightLinear.setOrientation(LinearLayout.VERTICAL);

            /* Lecturer name */
            TextView lecturerName = new TextView(context);

            LinearLayout.LayoutParams lecturerNameParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lecturerNameParams.gravity = Gravity.END;
            lecturerName.setLayoutParams(lecturerNameParams);

            lecturerName.setGravity(Gravity.END);
            lecturerName.setTextSize(14);
            lecturerName.setTextColor(context.getResources().getColor(R.color.def));
            lecturerName.setTypeface(ResourcesCompat.getFont(context, R.font.tenor_sans));
            lecturerName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            lecturerName.setText(subjects.get(i).getLecturer().getName());
            rightLinear.addView(lecturerName);

            /* Subject type */
            TextView type = new TextView(context);

            LinearLayout.LayoutParams typeParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            typeParams.gravity = Gravity.END;
            type.setLayoutParams(typeParams);

            type.setGravity(Gravity.END);
            type.setTextSize(14);
            type.setTextColor(context.getResources().getColor(R.color.gray));
            type.setTypeface(ResourcesCompat.getFont(context, R.font.tenor_sans));
            type.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            type.setText(subjects.get(i).getType());
            rightLinear.addView(type);

            leftRightParent.addView(rightLinear);

            subjectBox.addView(leftRightParent);

            /* Add to parent */
            holder.subjectsParent.addView(subjectBox);
        }
    }

    @Override
    public int getItemCount() {
        return week.getDays().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dayOfWeek;
        public LinearLayout subjectsParent;

        public ViewHolder(View itemView) {
            super(itemView);

            dayOfWeek = itemView.findViewById(R.id.recyclerViewDayOfWeek);
            subjectsParent = itemView.findViewById(R.id.subjectsParent);
        }
    }

}
