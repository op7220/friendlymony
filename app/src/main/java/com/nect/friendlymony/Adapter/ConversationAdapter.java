package com.nect.friendlymony.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nect.friendlymony.Model.Conversation.DataItem;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {


    Typeface fontBoldMuli;
    private List<DataItem> mResultList;
    private Context mContext;


    public ConversationAdapter(Context context, List<DataItem> mResultList) {
        mContext = context;
        this.mResultList = mResultList;
        fontBoldMuli = Typeface.createFromAsset(context.getAssets(), "font/Muli-Bold.ttf");

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_conversation, viewGroup, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvName.setText(mResultList.get(position).getWholename());
        if (mResultList.get(position).getLastMsg().equalsIgnoreCase("Photo")) {
            holder.tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_local_see_black_24dp, 0, 0, 0);
            holder.tvLastMsg.setText("  Image");
        } else if (mResultList.get(position).getLastMsg().equalsIgnoreCase("Location")) {
            holder.tvLastMsg.setText("  Location");
            holder.tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_on_black_24dp, 0, 0, 0);
        } else {
            holder.tvLastMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.tvLastMsg.setText(mResultList.get(position).getLastMsg());
        }


        String IUrl;

        holder.ivOnline.setVisibility(View.VISIBLE);
        if (String.valueOf(mResultList.get(position).getSender()).equalsIgnoreCase(HawkAppUtils.getInstance().getUSERDATA().getData().getId())) {
            IUrl = mResultList.get(position).getImage_receiver();
            if (mResultList.get(position).getIs_online_receiver().equalsIgnoreCase("1")) {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_green));
            } else {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_grey));
            }
        } else {
            IUrl = mResultList.get(position).getImage_sender();
            if (mResultList.get(position).getIs_online_sender().equalsIgnoreCase("1")) {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_green));
            } else {
                holder.ivOnline.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_oval_grey));
            }
        }
        //String IUrl = AppUtils.IMAGE_BASE_URL + mResultList.get(position).getPhoto();
        String count = mResultList.get(position).getUnread_count();
        //long MilliSec = AppUtils.getMilliFromDate(mResultList.get(position).getConvertsationsDate());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String oldTime = mResultList.get(position).getMessage_date_time_info();
        Date oldDate = null;
        holder.tvDateTime.setText(mResultList.get(position).getChat_date_time_info());
            /*oldTime = oldTime.replaceAll("[^\\d-]", "");
            oldTime = oldTime.substring(0, oldTime.length() - 6);
            oldDate = formatter.parse(oldTime);
            long oldMillis = oldDate.getTime();
            String date = AppUtils.getFormattedDate(mContext, oldMillis);
            DateFormat formatter1 = new SimpleDateFormat("hh:mma");
            if (date.equalsIgnoreCase("Today")) {
                *//*SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss z", Locale.ENGLISH);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date currentDate = df.parse(mResultList.get(position).getMessage_date_time_info());
                df.setTimeZone(TimeZone.getDefault());

                holder.tvDateTime.setText(formatter1.format(new Date(currentDate.getTime())));*//*

             *//*DateTimeFormatter fmt = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
                    TemporalAccessor timeZone = fmt.parse(mResultList.get(position).getMessage_date_time_info());
                    Instant currentTime = Instant.from(timeZone);

                    DateTimeFormatter fmtOut = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneOffset.UTC);
                    holder.tvDateTime.setText(fmtOut.format(currentTime));
                } else {
                    SimpleDateFormat fmt1 = new SimpleDateFormat("hh:mm aa");
                    Date date1 = fmt1.parse(mResultList.get(position).getMessage_date_time_info());

                    @SuppressLint({"NewApi", "LocalSuppress"}) TemporalAccessor timeZone = fmt.parse(mResultList.get(position).getMessage_date_time_info());
                    @SuppressLint({"NewApi", "LocalSuppress"}) Instant currentTime = Instant.from(timeZone);

                    SimpleDateFormat fmtOut1 = new SimpleDateFormat("hh:mm aa");
                    long oldMillis1 = date1.getTime();
                    holder.tvDateTime.setText(fmtOut1.format(currentTime));
                }*//*
                holder.tvDateTime.setText(mResultList.get(position).getChat_date_time_info());
                //return fmtOut.format(time);

            } else {
                holder.tvDateTime.setText(date);
            }*/

        if (count != null) {
            if (count.equalsIgnoreCase("") || count.equalsIgnoreCase("0")) {
                holder.tvCount.setText("");
                holder.tvCount.setVisibility(View.GONE);
            } else {
                holder.tvCount.setText(count);
                holder.tvCount.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tvCount.setText("");
            holder.tvCount.setVisibility(View.GONE);
        }

        /*if (mResultList.get(position).getIs_online_receiver() != null) {
            if (mResultList.get(position).getIs_online_receiver().equalsIgnoreCase("1")) {
                holder.ivOnline.setVisibility(View.VISIBLE);
            } else {
                holder.ivOnline.setVisibility(View.GONE);
            }
        }*/
        Log.e("photo==", IUrl);

        Glide.with(mContext).load(IUrl).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square).into(holder.ivProfile);

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfile)
        CircleImageView ivProfile;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDateTime)
        TextView tvDateTime;
        @BindView(R.id.tvLastMsg)
        TextView tvLastMsg;
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.ivOnline)
        ImageView ivOnline;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //  itemView.setOnClickListener(this);


            //CalligraphyUtils.applyFontToTextView(tvName, fontBoldMuli);
        }


    }


}