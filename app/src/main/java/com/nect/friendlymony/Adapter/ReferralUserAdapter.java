package com.nect.friendlymony.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nect.friendlymony.Model.Referral.DataItem;
import com.nect.friendlymony.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferralUserAdapter extends RecyclerView.Adapter<ReferralUserAdapter.ViewHolder> {


    private List<DataItem> mResultList;
    private Context mContext;


    public ReferralUserAdapter(Context context, List<DataItem> mResultList) {
        mContext = context;
        this.mResultList = mResultList;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_users, viewGroup, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvUser.setText("\u2022 "+mResultList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUser)
        TextView tvUser;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  itemView.setOnClickListener(this);
        }


    }


}