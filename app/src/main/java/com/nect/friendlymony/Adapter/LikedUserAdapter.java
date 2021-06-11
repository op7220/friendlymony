package com.nect.friendlymony.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nect.friendlymony.Model.Crused.DataItem;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.ViewHolder> {


    String from;
    Typeface fontExtraBoldMuli;
    private List<DataItem> mResultList;
    private Context mContext;


    public LikedUserAdapter(Context context, List<DataItem> mResultList, String from) {
        mContext = context;
        this.mResultList = mResultList;
        this.from = from;
        fontExtraBoldMuli = Typeface.createFromAsset(context.getAssets(), "font/Muli-ExtraBold.ttf");

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_matches, viewGroup, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvName.setText(mResultList.get(position).getFirst() + " " + mResultList.get(position).getLast());
        holder.tvAge.setText("Age : " + mResultList.get(position).getVAge());
        holder.btnLike.setText("" + from);


        DataItem di = mResultList.get(position);
        if (di.getImages() != null && !di.getImages().isEmpty()) {

            Glide.with(mContext).load(di.getImages().get(0).getVPhoto()).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .error(R.drawable.com_facebook_profile_picture_blank_square).into(holder.ivImage);

        } else {
            Glide.with(mContext).load(R.drawable.com_facebook_profile_picture_blank_square).into(holder.ivImage);
        }

        if (from.equalsIgnoreCase("Crush")) {
            holder.ivLike.setImageResource(R.drawable.ic_crush);
            holder.llBottom.setVisibility(View.VISIBLE);
            holder.tvAge.setVisibility(View.VISIBLE);
        } else if (from.equalsIgnoreCase("Interested")) {
            holder.ivLike.setImageResource(R.drawable.ic_like);
            holder.llBottom.setVisibility(View.VISIBLE);
            holder.tvAge.setVisibility(View.VISIBLE);
        } else {
            holder.llBottom.setVisibility(View.GONE);
            holder.tvAge.setVisibility(View.GONE);
        }

        if (from.equalsIgnoreCase("match")) {
            holder.ivOnline.setVisibility(View.VISIBLE);
            if (mResultList.get(position).getIs_online().equalsIgnoreCase("1")) {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_green));
            } else {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_grey));
            }
        } else {
            holder.ivOnline.setVisibility(View.GONE);
        }

        /*if (String.valueOf(mResultList.get(position).getSender()).equalsIgnoreCase(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {
            if (mResultList.get(position).getIs_online_receiver().equalsIgnoreCase("1")) {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_green));
            } else {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_grey));
            }
        } else {
            if (mResultList.get(position).getIs_online_sender().equalsIgnoreCase("1")) {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_green));
            } else {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_grey));
            }
        }*/

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAge)
        TextView tvAge;
        @BindView(R.id.btnLike)
        TextView btnLike;
        @BindView(R.id.ivLike)
        ImageView ivLike;
        @BindView(R.id.ivClose)
        ImageView ivClose;
        @BindView(R.id.ivOnline)
        ImageView ivOnline;
        @BindView(R.id.llBottom)
        RelativeLayout llBottom;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  itemView.setOnClickListener(this);


            //CalligraphyUtils.applyFontToTextView(tvName, fontExtraBoldMuli);
        }


    }


}