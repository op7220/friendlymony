package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.nect.friendlymony.Model.FilterModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FilterActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvFilters)
    TextView tvFilters;
    @BindView(R.id.ivMale)
    CircleImageView ivMale;
    @BindView(R.id.ivTrueM)
    ImageView ivTrueM;
    @BindView(R.id.ivFemale)
    CircleImageView ivFemale;
    @BindView(R.id.ivTrueF)
    ImageView ivTrueF;
    @BindView(R.id.ivOther)
    CircleImageView ivOther;
    @BindView(R.id.ivTrueO)
    ImageView ivTrueO;
    @BindView(R.id.tvRange)
    TextView tvRange;
    @BindView(R.id.rangeAge)
    CrystalRangeSeekbar rangeAge;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.rangeDistance)
    CrystalSeekbar rangeDistance;
    @BindView(R.id.btnKm)
    Button btnKm;
    @BindView(R.id.btnMiles)
    Button btnMiles;
    @BindView(R.id.btnApply)
    Button btnApply;


    String intrested = "";
    String type = "km";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        setToolbar(toolbar, "", true);

        //CalligraphyUtils.applyFontToTextView(tvFilters, fontExtraBoldMuli);

        rangeAge.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvRange.setText(minValue + " - " + maxValue);

            }
        });

        rangeDistance.setMinStartValue(50).apply();
         rangeDistance.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                tvDistance.setText("" + value);
            }
        });

        FilterModel fm = HawkAppUtils.getInstance().getFiltr();

        if (fm != null) {

            Log.e(">>>>>>", ":" + fm.getTypeDistance() + "\n" + fm.getRadius() + "\n" + fm.getAge_min() + "\n" +
                    fm.getAge_max() + "\n" + fm.getShow_me());
            try {

                if ((fm.getShow_me() + "").equalsIgnoreCase("Male")) {
                    changeGender(0);
                } else if ((fm.getShow_me() + "").equalsIgnoreCase("Female")) {
                    changeGender(1);
                } else if ((fm.getShow_me() + "").equalsIgnoreCase("Other")) {
                    changeGender(2);
                }


                if ((fm.getTypeDistance() + "").equalsIgnoreCase("Km")) {
                    btnKm.setBackgroundResource(R.drawable.bg_primary_border_rounded);
                    btnKm.setTextColor(getResources().getColor(R.color.pink));
                    btnMiles.setBackgroundResource(R.drawable.bg_gray_border_rounded);
                    btnMiles.setTextColor(getResources().getColor(R.color.grey_text));

                    type = "km";
                } else if ((fm.getTypeDistance() + "").equalsIgnoreCase("miles")) {

                    btnMiles.setBackgroundResource(R.drawable.bg_primary_border_rounded);
                    btnMiles.setTextColor(getResources().getColor(R.color.pink));
                    btnKm.setBackgroundResource(R.drawable.bg_gray_border_rounded);
                    btnKm.setTextColor(getResources().getColor(R.color.grey_text));
                    type = "miles";
                }

                if (!fm.getAge_max().equals("")) {
                    rangeAge.setMaxStartValue(Float.parseFloat(fm.getAge_max())).apply();;
                }
                if (!fm.getAge_min().equals("")) {
                    rangeAge.setMinStartValue(Float.parseFloat(fm.getAge_min())).apply();
                }
                if (!fm.getRadius().equals("")) {
                    rangeDistance.setMinStartValue(Integer.parseInt(fm.getRadius())).apply();;
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            Log.e("Null", "Null");
        }

    }

    @OnClick({R.id.ivMale, R.id.ivFemale, R.id.ivOther, R.id.btnKm, R.id.btnMiles, R.id.btnApply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMale:
                changeGender(0);
                break;
            case R.id.ivFemale:
                changeGender(1);
                break;
            case R.id.ivOther:
                changeGender(2);
                break;
            case R.id.btnKm:
                btnKm.setBackgroundResource(R.drawable.bg_primary_border_rounded);
                btnKm.setTextColor(getResources().getColor(R.color.pink));
                btnMiles.setBackgroundResource(R.drawable.bg_gray_border_rounded);
                btnMiles.setTextColor(getResources().getColor(R.color.grey_text));

                type = "km";
                break;
            case R.id.btnMiles:

                btnMiles.setBackgroundResource(R.drawable.bg_primary_border_rounded);
                btnMiles.setTextColor(getResources().getColor(R.color.pink));
                btnKm.setBackgroundResource(R.drawable.bg_gray_border_rounded);
                btnKm.setTextColor(getResources().getColor(R.color.grey_text));
                type = "miles";
                break;
            case R.id.btnApply:

                Log.e("DATA", rangeAge.getSelectedMaxValue() + "\n" +
                        rangeAge.getSelectedMinValue() + "\n" +
                        rangeDistance.getSelectedMinValue() + "\n" +
                        intrested + "\n" +
                        type + "\n");
                FilterModel fm = new FilterModel();

                fm.setAge_max(rangeAge.getSelectedMaxValue() + "");
                fm.setAge_min(rangeAge.getSelectedMinValue() + "");
                fm.setRadius(rangeDistance.getSelectedMinValue() + "");
                fm.setShow_me(intrested);
                fm.setTypeDistance(type);

                HawkAppUtils.getInstance().setFILTER(fm);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }

    private void changeGender(int i) {
        if (i == 0) {
            intrested = "Male";
            ivMale.setBorderWidth(5);
            ivFemale.setBorderWidth(0);
            ivOther.setBorderWidth(0);

            ivTrueM.setVisibility(View.VISIBLE);
            ivTrueF.setVisibility(View.INVISIBLE);
            ivTrueO.setVisibility(View.INVISIBLE);
        } else if (i == 1) {
            intrested = "Female";
            ivMale.setBorderWidth(0);
            ivFemale.setBorderWidth(5);
            ivOther.setBorderWidth(0);

            ivTrueM.setVisibility(View.INVISIBLE);
            ivTrueF.setVisibility(View.VISIBLE);
            ivTrueO.setVisibility(View.INVISIBLE);
        } else if (i == 2) {
            intrested = "Other";
            ivMale.setBorderWidth(0);
            ivFemale.setBorderWidth(0);
            ivOther.setBorderWidth(5);

            ivTrueM.setVisibility(View.INVISIBLE);
            ivTrueF.setVisibility(View.INVISIBLE);
            ivTrueO.setVisibility(View.VISIBLE);
        }
    }
}
