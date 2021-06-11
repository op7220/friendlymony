package com.nect.friendlymony.Fragments.register;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.iid.FirebaseInstanceId;
import com.nect.friendlymony.Activity.SignupQuestionsActivity;
import com.nect.friendlymony.Model.SignupModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoQesFragment extends Fragment {

    View view;
    @BindView(R.id.btnYes)
    Button btnYes;
    @BindView(R.id.btnNo)
    Button btnNo;
    @BindView(R.id.btnOccasion)
    Button btnOccasion;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.btnSkip)
    Button btnSkip;
    @BindView(R.id.llFooter)
    LinearLayout llFooter;

    private String isQes = "";
    private SignupModel sm;

    public TwoQesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        TwoQesFragment fragment = new TwoQesFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {

            view = inflater.inflate(R.layout.fragment_two_qes, container, false);
            ButterKnife.bind(this, view);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.d("IntroActivity", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Pref.setStringValue(getContext(), PREF_FCM_TOKEN, token);
                        // Log and toast
                        Log.d("IntroActivity", token);
                    });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btnYes, R.id.btnNo, R.id.btnOccasion, R.id.btnNext, R.id.btnSkip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnYes:
                setButton(0);
                break;
            case R.id.btnNo:
                setButton(1);
                break;
            case R.id.btnOccasion:
                setButton(2);
                break;
            case R.id.btnNext:
                if (isQes.equals("")) {
                    Toast.makeText(getActivity(), "Please select option", Toast.LENGTH_LONG).show();
                    return;
                }


                sm = HawkAppUtils.getInstance().getSIGNUP();
                sm.setIsSmoke(isQes);
                HawkAppUtils.getInstance().setSIGNUP(sm);
                ((SignupQuestionsActivity) getActivity()).loadFragment(ThreeQesFragment.newInstance(), ThreeQesFragment.class.getSimpleName(), true);

                break;
            case R.id.btnSkip:
                sm = HawkAppUtils.getInstance().getSIGNUP();
                sm.setIsSmoke(isQes);
                HawkAppUtils.getInstance().setSIGNUP(sm);
                ((SignupQuestionsActivity) getActivity()).loadFragment(ThreeQesFragment.newInstance(), ThreeQesFragment.class.getSimpleName(), true);

                break;
        }
    }

    private void setButton(int i) {
        if (i == 0) {

            isQes = "Yes";
            btnYes.setBackgroundResource(R.drawable.bg_gradient_horizontal_rounded);
            btnNo.setBackgroundResource(R.drawable.bg_primary_border_rounded);
            btnOccasion.setBackgroundResource(R.drawable.bg_primary_border_rounded);

            btnYes.setTextColor(getResources().getColor(R.color.white));
            btnNo.setTextColor(getResources().getColor(R.color.grey_text));
            btnOccasion.setTextColor(getResources().getColor(R.color.grey_text));
        } else if (i == 1) {
            isQes = "No";
            btnYes.setBackgroundResource(R.drawable.bg_primary_border_rounded);
            btnNo.setBackgroundResource(R.drawable.bg_gradient_horizontal_rounded);
            btnOccasion.setBackgroundResource(R.drawable.bg_primary_border_rounded);

            btnYes.setTextColor(getResources().getColor(R.color.grey_text));
            btnNo.setTextColor(getResources().getColor(R.color.white));
            btnOccasion.setTextColor(getResources().getColor(R.color.grey_text));
        } else if (i == 2) {
            isQes = "Occasionally";
            btnYes.setBackgroundResource(R.drawable.bg_primary_border_rounded);
            btnNo.setBackgroundResource(R.drawable.bg_primary_border_rounded);
            btnOccasion.setBackgroundResource(R.drawable.bg_gradient_horizontal_rounded);

            btnYes.setTextColor(getResources().getColor(R.color.grey_text));
            btnNo.setTextColor(getResources().getColor(R.color.grey_text));
            btnOccasion.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
