package com.nect.friendlymony.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.nect.friendlymony.Fragments.register.SignupFragment;
import com.nect.friendlymony.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupQuestionsActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_questions);
        ButterKnife.bind(this);
        // setToolbar(toolbar,"",true);
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initGoogle();
        loadFragment(SignupFragment.newInstance(), SignupFragment.class.getSimpleName(), false);
    }

    public void loadFragment(Fragment fragment, String simpleName, boolean isBack) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameQuestion, fragment, simpleName);
        if (isBack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }
}
