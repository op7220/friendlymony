package com.nect.friendlymony.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nect.friendlymony.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvantagesAdapter extends RecyclerView.Adapter<AdvantagesAdapter.ViewHolder> {


    int[] mResultList;
    private Context mContext;


    public AdvantagesAdapter(Context context, int[] mResultList) {
        mContext = context;
        this.mResultList = mResultList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_advantages, viewGroup, false);
        return new ViewHolder(convertView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvTitle.setText(mContext.getResources().getString(mResultList[position]));

    }

    @Override
    public int getItemCount() {
        return mResultList.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  itemView.setOnClickListener(this);
        }


    }


}