package com.nect.friendlymony.Fragments.pager;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nect.friendlymony.Activity.MyLikesActivity;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeYouFragment extends BaseFragment {


    View view;
    @BindView(R.id.tvWhoLike)
    TextView tvWhoLike;
    @BindView(R.id.btnCheck)
    Button btnCheck;

    public LikeYouFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_like_you, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.btnCheck)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), MyLikesActivity.class);
        intent.putExtra("from","Interested");
        startActivity(intent);
    }
}
