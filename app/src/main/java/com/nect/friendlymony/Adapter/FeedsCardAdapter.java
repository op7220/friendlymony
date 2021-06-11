package com.nect.friendlymony.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nect.friendlymony.Model.Feeddetail.ImagesItem;
import com.nect.friendlymony.Model.Feeds.DataItem;
import com.nect.friendlymony.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedsCardAdapter extends ArrayAdapter<DataItem> {

    Context mContext;
    OnClick onClick;
    Typeface fontExtraBoldMuli;
    private List<ImagesItem> listImage = new ArrayList<>();

    public FeedsCardAdapter(Context context, OnClick onClick) {
        super(context, 0);

        this.mContext = context;
        this.onClick = onClick;


        fontExtraBoldMuli = Typeface.createFromAsset(context.getAssets(), "font/Muli-ExtraBold.ttf");
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        final ViewHolder holder;
        DataItem di = getItem(position);
        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_feed, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }


        //CalligraphyUtils.applyFontToTextView(holder.tvName, fontExtraBoldMuli);

        holder.tvName.setText(di.getFirst() + "  " + di.getLast());
        holder.tvLocation.setText(di.getVAbout() + "");
        holder.tvMatch.setText(di.getMatchPercentage() + "% Match !");

        //List<ImagesItem> spot = di.getImages();

        if (di.getImages() != null && !di.getImages().isEmpty()) {

            Glide.with(mContext).load(di.getImages().get(0).getVPhoto()).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .error(R.drawable.com_facebook_profile_picture_blank_square).into(holder.ivProfile);

        } else {
            Glide.with(mContext).load(R.drawable.com_facebook_profile_picture_blank_square).into(holder.ivProfile);
        }


        if ((di.getBoostStatus() + "").equals("1")) {
            holder.rlMain.setBackground(mContext.getResources().getDrawable(R.drawable.bg_white_feed_highlight));

        } else {
            holder.rlMain.setBackground(mContext.getResources().getDrawable(R.drawable.bg_white_feed_rounded));

        }

        //holder.rlMain.setBackground(mContext.getResources().getDrawable(R.drawable.bg_white_feed_highlight));


        holder.ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.onInfoClicked(position);
                }
            }
        });
        holder.ivCrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.onCrush(position);
                }
            }
        });
        holder.ivInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.onLike(position);
                }
            }
        });
        holder.ivNotinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.onDislike(position);
                }
            }
        });
       /* holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*listImage.clear();
                if (di.getImages() != null) {
                    if (!di.getImages().isEmpty()) {
                        //listImages.addAll(di.getImages());

                        for (int i = 0;i<di.getImages().size();i++)
                        {
                            listImage.add(new ImagesItem(di.getImages().get(i).getVPhoto()));
                        }

                    }


                }
                if (listImage.size() > 0) {
                    showImageDialog();
                } else {
                    Toast.makeText(mContext, "No images available", Toast.LENGTH_SHORT).show();
                }*//*

                if (onClick != null) {
                    onClick.onInfoClicked(position);
                }
            }
        });*/




       /*
        holder.tvAge.setVisibility(View.GONE);

        if(spot.getVShowMyAge().equalsIgnoreCase("Yes")) {
            holder.name.setText(spot.getFirst()+", "+spot.getVAge());

        }else {
            holder.name.setText(spot.getFirst());

        }
        if(spot.getVDistanceInvisible().equalsIgnoreCase("No")) {
            holder.tvDistance.setText("" + spot.getKm()+" km away");
        }else {
            holder.tvDistance.setText("");
        }
       // Glide.with(getContext()).load(spot.url).into(holder.image);


*/


        return contentView;
    }


    public static class ViewHolder {

        @BindView(R.id.ivInfo)
        ImageView ivInfo;
        @BindView(R.id.tvMatch)
        TextView tvMatch;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.rlDetail)
        RelativeLayout rlDetail;
        @BindView(R.id.ivNotinterest)
        ImageView ivNotinterest;
        @BindView(R.id.ivCrush)
        ImageView ivCrush;
        @BindView(R.id.ivInterest)
        ImageView ivInterest;
        @BindView(R.id.ivProfile)
        ImageView ivProfile;
        @BindView(R.id.llFooter)
        LinearLayout llFooter;
        @BindView(R.id.rlMain)
        RelativeLayout rlMain;

        public ViewHolder(View cView) {
            ButterKnife.bind(this, cView);

        }
    }

    public interface OnClick {
        void onInfoClicked(int index);

        void onDislike(int index);

        void onLike(int index);

        void onCrush(int index);
    }

    void loadData() {


    }

    public void showImageDialog() {
        Dialog dialog = new Dialog(mContext, R.style.TransparentProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_full);
        dialog.setCancelable(true);

        ViewPager pagerImageD = dialog.findViewById(R.id.pagerImage);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ImageView ivLeft = dialog.findViewById(R.id.ivLeft);
        ImageView ivRight = dialog.findViewById(R.id.ivRight);

        PagerImageAdapter myViewPagerAdapter = new PagerImageAdapter(mContext, listImage, 0, 0);
        pagerImageD.setAdapter(myViewPagerAdapter);


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listImage.isEmpty()) {
                    return;
                }
                int positionP = pagerImageD.getCurrentItem();
                int CurrentitemP = 0;
                if (positionP < (listImage.size() - 1)) {
                    CurrentitemP = positionP + 1;
                }
                pagerImageD.setCurrentItem(CurrentitemP);

            }
        });
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (listImage.isEmpty()) {
                    return;
                }
                int position = pagerImageD.getCurrentItem();
                int Currentitem = (listImage.size() - 1);
                if (position != 0) {
                    Currentitem = position - 1;
                }

                pagerImageD.setCurrentItem(Currentitem);

            }
        });


        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


        dialog.show();


    }


}

