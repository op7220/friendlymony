package com.nect.friendlymony.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nect.friendlymony.Model.ReportUser.DataReportUserReasons;
import com.nect.friendlymony.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {
    private static MyClickListener myClickListener;
    private Typeface fontBoldMuli;
    private Context mContext;
    private List<DataReportUserReasons> reasonsList;

    public ReportListAdapter(Context mContext, List<DataReportUserReasons> reasonsList) {
        this.mContext = mContext;
        this.reasonsList = reasonsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.adapter_report_user_list, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.rbName.setText(reasonsList.get(position).getName());
        if (reasonsList.get(position).isChecked()) {
            holder.rbName.setChecked(true);
        } else {
            holder.rbName.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return reasonsList == null ? 0 : reasonsList.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        ReportListAdapter.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.adp_rbName)
        RadioButton rbName;
        @BindView(R.id.adp_llMain)
        LinearLayout adp_llMain;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  itemView.setOnClickListener(this);


            //CalligraphyUtils.applyFontToTextView(rbName, fontBoldMuli);
            adp_llMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == adp_llMain) {
                myClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
