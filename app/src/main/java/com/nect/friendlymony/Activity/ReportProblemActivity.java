package com.nect.friendlymony.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.nect.friendlymony.R;
import com.nect.friendlymony.Utils.HawkAppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportProblemActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvReport)
    TextView tvReport;
    @BindView(R.id.etFname)
    EditText etFname;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.btnSend)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        ButterKnife.bind(this);

        setToolbar(toolbar, "", true);
        //CalligraphyUtils.applyFontToTextView(tvReport, fontExtraBoldMuli);


        etFname.setText(HawkAppUtils.getInstance().getUSERDATA().getData().getName() + " " + HawkAppUtils.getInstance().getUSERDATA().getData().getLastName());
        etEmail.setText(HawkAppUtils.getInstance().getUSERDATA().getData().getEmail() + "");
    }

    @OnClick(R.id.btnSend)
    public void onViewClicked() {
        etFname.setError(null);
        etEmail.setError(null);
        etDesc.setError(null);


        if (etFname.getText().toString().isEmpty()) {
            etFname.setError("Required");
            return;
        }
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Required");
            return;
        }
        if (etDesc.getText().toString().isEmpty()) {
            etDesc.setError("Required");
            return;
        }

        sendMail();

    }

    private void sendMail() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:android@friendlymony.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Report a Problem");
        intent.putExtra(Intent.EXTRA_TEXT, "" + etDesc.getText().toString());
        startActivity(Intent.createChooser(intent, "Send Email"));

    }
}
