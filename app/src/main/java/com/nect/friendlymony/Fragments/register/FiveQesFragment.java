package com.nect.friendlymony.Fragments.register;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.nect.friendlymony.Activity.SignupQuestionsActivity;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.Questions.EmployeeItem;
import com.nect.friendlymony.Model.Questions.QuestionResponse;
import com.nect.friendlymony.Model.SignupModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiveQesFragment extends BaseFragment {

    View view;
    @BindView(R.id.etqes)
    EditText etqes;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.btnSkip)
    Button btnSkip;
    @BindView(R.id.llFooter)
    LinearLayout llFooter;


    ArrayList<String> listQuestion = new ArrayList<String>();
    PopupWindow popupWindow;

    private SignupModel sm;

    public FiveQesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        FiveQesFragment fragment = new FiveQesFragment();
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

            view = inflater.inflate(R.layout.fragment_five_qes, container, false);
            ButterKnife.bind(this, view);
            getQuestion();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.btnNext, R.id.btnSkip, R.id.etqes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (etqes.getText().toString().isEmpty()) {
                    etqes.setError("Required");
                    showToast("Choose your employment status");
                    return;
                }


                sm = HawkAppUtils.getInstance().getSIGNUP();
                sm.setIsEmployed(etqes.getText().toString());
                HawkAppUtils.getInstance().setSIGNUP(sm);
                ((SignupQuestionsActivity) getActivity()).loadFragment(SixQesFragment.newInstance(), SixQesFragment.class.getSimpleName(), true);

                break;
            case R.id.btnSkip:
                sm = HawkAppUtils.getInstance().getSIGNUP();
                sm.setIsEmployed(etqes.getText().toString());
                HawkAppUtils.getInstance().setSIGNUP(sm);
                ((SignupQuestionsActivity) getActivity()).loadFragment(SixQesFragment.newInstance(), SixQesFragment.class.getSimpleName(), true);

                break;
            case R.id.etqes:
                PopupWindow popUp = popupWindowsort();
                popUp.showAsDropDown(etqes, 0, 0);
                break;
        }
    }

    private void getQuestion() {
        Call<QuestionResponse> call = RetrofitBuilder.getInstance().getRetrofit().getQuestions();
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        List<EmployeeItem> listQ = response.body().getData().getEmployee();

                        listQuestion.clear();
                        for (int i = 0; i < listQ.size(); i++) {
                            listQuestion.add(listQ.get(i).getAreEmployee());
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {

            }
        });
    }

    private PopupWindow popupWindowsort() {

        // initialize a pop up window type
        popupWindow = new PopupWindow(getActivity());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spiner_text, R.id.tvText,
                listQuestion);
        // the drop down list is a list view
        ListView listViewSort = new ListView(getActivity());

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etqes.setText(listQuestion.get(i));
                popupWindow.dismiss();
            }
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        //popupWindow.setWidth(250);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the listview as popup content
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

}
