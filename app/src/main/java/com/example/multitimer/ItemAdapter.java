package com.example.multitimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {


    private Context mContext;
    private List<Item> itemsList;

    public ItemAdapter(@NonNull Context context, ArrayList<Item> list) {
        super(context, 0, list);
        this.mContext = context;
        itemsList = list;
    }

    @Override
    public Item getItem(int position) {
        return itemsList.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        final Item currentItem = getItem(position);

        if (convertView == null) {

            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_layout, null, true);

            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_daysPassed = convertView.findViewById(R.id.tv_days_passed);
            holder.tv_daysUntilAlert = convertView.findViewById(R.id.tv_days_left);


            holder.tv_timeOfDay = convertView.findViewById(R.id.tv_time_of_day);
            holder.tv_interval = convertView.findViewById(R.id.tv_interval);
            holder.cb_alertActive = convertView.findViewById(R.id.cb_alert_active);
            holder.tv_restart = convertView.findViewById(R.id.tv_reset);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(currentItem.getmTitle());
        holder.tv_daysPassed.setText(currentItem.getDaysPassed());
        holder.tv_daysUntilAlert.setText(getStringForTvDaysUntilAlert(currentItem));
        holder.tv_interval.setText(getStringForTvInterval(currentItem));
        holder.cb_alertActive.setChecked(currentItem.getmAlertActive());
   //     holder.cb_alertActive.setTag(R.integer.checkboxbview, convertView);
        holder.cb_alertActive.setTag(position);
        holder.cb_alertActive.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {

                // View tempview = (View) holder.cb_alertActive.getTag(R.integer.checkboxbview);
                 //    TextView tv = (TextView) tempview.findViewById(R.id.animal);
                 Integer pos = (Integer) holder.cb_alertActive.getTag();
                 Toast.makeText(mContext, "Checkbox "+pos+" clicked!", Toast.LENGTH_SHORT).show();

                 if (itemsList.get(pos).getmAlertActive()) {
                     itemsList.get(pos).setmAlertActive(0);
                     SharedPreferencesHelper.setInt(mContext, "alert_active_" + currentItem.getmID(), 0);
                  //   ((MainActivity) mContext).cancelAlert(currentItem.getmTitle(), currentItem.getmID());


                 } else {
                     itemsList.get(pos).setmAlertActive(1);
                     SharedPreferencesHelper.setInt(mContext, "alert_active_" + currentItem.getmID(), 1);
                  //   ((MainActivity) mContext).setAlert(currentItem.getmMillisEnd(), currentItem.getmTitle(), currentItem.getmID());

                 }
             }

         });


            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //access method in mainActivity from adapter
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).changeTitle(view, position);
                    }
                }
            });

            holder.tv_daysUntilAlert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //access method in mainActivity from adapter
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).changeDaysLeft(view, position);
                    }
                }
            });

            holder.tv_timeOfDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //access method in mainActivity from adapter
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).changeTimeOfDay(view, position);
                    }
                }
            });

            holder.tv_interval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //access method in mainActivity from adapter
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).changeInterval(view, position);
                    }
                }
            });


            holder.tv_restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //access method in mainActivity from adapter
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).restartAlert(currentItem.getmID());
                    }
                }
            });


            holder.tv_daysPassed.setClickable(false);
            holder.tv_daysUntilAlert.setClickable(false);
            holder.tv_timeOfDay.setClickable(false);


        return convertView;
    }

    private String getStringForTvDaysUntilAlert(Item item) {
        int daysUntilAlert = item.getDaysLeft();
        String string = new String();
        if (daysUntilAlert > 1) {
            string = mContext.getString(R.string.alert_first_part) + " " + daysUntilAlert + " " + mContext.getString(R.string.alert_multi_days_until);
        } else if (daysUntilAlert == 1) {
            string = mContext.getString(R.string.alert_one_day_until);
        } else if (daysUntilAlert == 0){
            string = mContext.getString(R.string.alert_today);
        } else if (daysUntilAlert == -1) {
            string = mContext.getString(R.string.alert_one_day_ago);
        } else {
            string =  daysUntilAlert * -1 +  " " + mContext.getString(R.string.alert_multi_days_ago);
        }
        return string;
    }


    private String getStringForTvInterval(Item item) {
        int interval = item.getmInterval();
        String string = new String();
        if (interval == -1) {
            string = mContext.getString(R.string.interval_first_part) + " " + mContext.getString(R.string.interval_not_set);
        } else if (interval == 1) {
            string = mContext.getString(R.string.interval_first_part) + " " + interval + " " + mContext.getString(R.string.interval_one_day);
        } else {
            string = mContext.getString(R.string.interval_first_part) + " " + interval + " " + mContext.getString(R.string.interval_multi_days);
        }
        return string;
    }

}

class ViewHolder {
    protected TextView tv_title;
    protected TextView tv_daysPassed;
    protected TextView tv_daysUntilAlert;

    protected TextView tv_timeOfDay;
    protected TextView tv_interval;
    protected CheckBox cb_alertActive;
    protected TextView tv_restart;
}




    /*
    cb_alertActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                cb_alertActive.setChecked(true);
                currentItem.setmAlertActive(1);
                SharedPreferencesHelper.setInt(mContext, "alert_active_" + currentItem.getmID(), 1);
                ((MainActivity) mContext).setAlert(currentItem.getmMillisEnd(), currentItem.getmTitle(), currentItem.getmID());
            } else {
                cb_alertActive.setChecked(false);
                currentItem.setmAlertActive(0);
                SharedPreferencesHelper.setInt(mContext, "alert_active_" + currentItem.getmID(), 0);
                ((MainActivity) mContext).cancelAlert(currentItem.getmTitle(), currentItem.getmID());
            }
        }
    });
}

     */