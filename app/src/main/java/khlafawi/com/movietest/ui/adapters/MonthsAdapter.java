package khlafawi.com.movietest.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khlafawi.com.movietest.R;
import khlafawi.com.movietest.ui.main.MainActivity;

public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.MonthViewHolder> {

    private List<String> monthsList;
    private LayoutInflater Inflater;

    public MonthsAdapter(Context Context) {
        this.Inflater = LayoutInflater.from(Context);
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = Inflater.inflate(R.layout.item_month, viewGroup, false);
        return new MonthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MonthViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        final String date = monthsList.get(i);
        holder.month.setText(date);

        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.getInstance().selectedDates.contains(date)) {
                    MainActivity.getInstance().selectedDates.remove(date);
                    holder.checked.setChecked(false);
                } else {
                    MainActivity.getInstance().selectedDates.add(date);
                    holder.checked.setChecked(true);
                }
            }
        });

        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.getInstance().selectedDates.contains(date)) {
                    MainActivity.getInstance().selectedDates.remove(date);
                    holder.checked.setChecked(false);
                } else {
                    MainActivity.getInstance().selectedDates.add(date);
                    holder.checked.setChecked(true);
                }
            }
        });

        if (MainActivity.getInstance().selectedDates.contains(date)) {
            holder.checked.setChecked(true);
        } else {
            holder.checked.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return (monthsList == null) ? 0 : monthsList.size();
    }

    public void setDates(List<String> monthsList) {
        this.monthsList = new ArrayList<>();
        this.monthsList.addAll(monthsList);
        this.notifyDataSetChanged();
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {

        LinearLayout body;
        TextView month;
        CheckBox checked;

        MonthViewHolder(@NonNull View itemView) {
            super(itemView);
            this.body = itemView.findViewById(R.id.body);
            this.month = itemView.findViewById(R.id.month);
            this.checked = itemView.findViewById(R.id.checked);
        }
    }
}