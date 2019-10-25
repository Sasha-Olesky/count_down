package erquar.com.countdown.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import erquar.com.countdown.R;
import erquar.com.countdown.configuration.GlobalVars;
import erquar.com.countdown.configuration.NationCalender;
import erquar.com.countdown.model.HolodayModel;

/**
 * Created by Administrator on 7/23/2016.
 */
public class HolodaysAdapter extends RecyclerView.Adapter<HolodaysAdapter.MyViewHolder> {

    Activity mActiviyt;
    String strStartDate;
    String strEndDate;

    List<HolodayModel> data;
    public HolodaysAdapter(Activity context) {
        this.mActiviyt = context;
        this.strStartDate = "";
        this.strEndDate = "";
        this.data = new ArrayList<HolodayModel>();
    }

    public void notifyData (String strStartDate, String strEndDate) {
        this.strStartDate = strStartDate;
        this.strEndDate = strEndDate;

        if(strStartDate.equals("") || strEndDate.equals("")) {
            notifyDataSetChanged();
            return;
        }
        for (int i = 0; i < NationCalender.nameOfEvent.size(); i ++) {
            String tempStart = NationCalender.startDates.get(i);
            if(compareDate(tempStart,strStartDate) >= 0 && compareDate(strEndDate, tempStart) >= 0) {
                HolodayModel holodayModel = new HolodayModel(NationCalender.nameOfEvent.get(i), NationCalender.startDates.get(i),
                                                            NationCalender.endDates.get(i), NationCalender.descriptions.get(i));
                this.data.add(holodayModel);
            }
        }

    }

    public int compareDate (String strDate1, String strDate2) {
        try {
            Date date1 = NationCalender.getDateFormat().parse(strDate1);
            Date date2 = NationCalender.getDateFormat().parse(strDate2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_holoday_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // init view
        HolodayModel model = this.data.get(position);
        holder.tvTitle.setText(model.name);
        holder.tvDate.setText(model.startDate + "-" + model.endDate);
        holder.tvDes.setText(model.strDes);

        // set listener
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVars.showAlert(mActiviyt, holder.tvTitle.getText().toString(),
                        holder.tvDes.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDate;
        private final TextView tvDes;
        private final View view;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDate = (TextView) itemView.findViewById(R.id.tvTime);
            tvDes = (TextView) itemView.findViewById(R.id.tvDes);
            view = itemView;
        }
    }
}
