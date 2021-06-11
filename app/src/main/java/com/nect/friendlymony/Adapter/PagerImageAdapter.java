package com.nect.friendlymony.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nect.friendlymony.Model.Feeddetail.ImagesItem;
import com.nect.friendlymony.R;

import java.util.List;

public class PagerImageAdapter extends PagerAdapter {


    List<ImagesItem> listImage;
    private Context mContext;
    int iscrop;
    int isclick;

    public PagerImageAdapter(Context context, List<ImagesItem> listImage, int iscrop, int isClick) {
        mContext = context;
        this.listImage = listImage;
        this.iscrop = iscrop;
        this.isclick = isClick;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_pager_image, collection, false);
        collection.addView(layout);

        ImageView imageView = layout.findViewById(R.id.ivImage_);
        if (iscrop == 1) {
            Glide.with(mContext).load(listImage.get(position).getVPhoto())
                    .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .centerCrop())
                    .into(imageView);
        } else {
            Glide.with(mContext).load(listImage.get(position).getVPhoto())
                    .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square).error(R.drawable.com_facebook_profile_picture_blank_square)
                    )
                    .into(imageView);

           /* PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imageView);
            pAttacher.update();*/

        }
        if (isclick == 1) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isclick == 1) {
                        showImageDialog(position);
                    }
                }
            });
        }
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public interface onPagerItemClickListener {
        void onItmClik(int position);
    }

    public void showImageDialog(int clickPos) {
        Dialog dialog = new Dialog(mContext,R.style.TransparentProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_full);
        dialog.setCancelable(true);

        ViewPager pagerImageD = dialog.findViewById(R.id.pagerImage);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ImageView ivLeft = dialog.findViewById(R.id.ivLeft);
        ImageView ivRight = dialog.findViewById(R.id.ivRight);

        PagerImageAdapter myViewPagerAdapter = new PagerImageAdapter(mContext, listImage, 0, 0);
        pagerImageD.setAdapter(myViewPagerAdapter);

        pagerImageD.setCurrentItem(clickPos);

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
