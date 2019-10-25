package erquar.com.countdown.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import erquar.com.countdown.R;

/**
 * Created by Administrator on 7/25/2016.
 */
public class WeekendAdapter  extends RecyclerView.Adapter<WeekendAdapter.MyViewHolder> {

    Activity mActiviyt;

    List<String> data;
    public WeekendAdapter(Activity context) {
        this.mActiviyt = context;
        this.data = new ArrayList<String>();
    }

    public void notifyData (List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_reduce_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // init view
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvDate.setText(this.data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvIndex;
        private final TextView tvDate;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvIndex = (TextView) itemView.findViewById(R.id.tvIndex);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }
}
