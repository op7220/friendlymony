package com.nect.friendlymony.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Images.DataItem;
import com.nect.friendlymony.Model.Images.ImageResponse;
import com.nect.friendlymony.Model.Login.Data;
import com.nect.friendlymony.Model.Login.ImagesItem;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.Login.UserResponse;
import com.nect.friendlymony.Model.Questions.EducationQualificationItem;
import com.nect.friendlymony.Model.Questions.EmployeeItem;
import com.nect.friendlymony.Model.Questions.QuestionResponse;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseAppCompatActivity {


    private static final int MY_PERMISSION_ALL_STORE = 111;
    private static final int CAMERA_REQUEST_1 = 11;
    private static final int GALLRY_REQUEST_1 = 12;
    private static final int CAMERA_REQUEST_2 = 21;
    private static final int GALLRY_REQUEST_2 = 22;
    private static final int CAMERA_REQUEST_3 = 31;
    private static final int GALLRY_REQUEST_3 = 32;
    private static final int CAMERA_REQUEST_4 = 41;
    private static final int GALLRY_REQUEST_4 = 42;
    private static final int CAMERA_REQUEST_5 = 51;
    private static final int GALLRY_REQUEST_5 = 52;
    int age;
    String imgString;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendarCurrent = Calendar.getInstance();
    Calendar myCalendar_min = Calendar.getInstance();
    Calendar myCalendar_max = Calendar.getInstance();
    ArrayList<String> listQuestion = new ArrayList<String>();
    ArrayList<String> listQualification = new ArrayList<String>();
    ArrayList<String> listEmployee = new ArrayList<String>();
    PopupWindow popupWindow;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvFilters)
    TextView tvFilters;
    @BindView(R.id.tvUpdate)
    TextView tvUpdate;
    @BindView(R.id.ivProfile2)
    ImageView ivProfile2;
    @BindView(R.id.ivProfile4)
    ImageView ivProfile4;
    @BindView(R.id.ivProfile1)
    ImageView ivProfile1;
    @BindView(R.id.ivProfile3)
    ImageView ivProfile3;
    @BindView(R.id.ivProfile5)
    ImageView ivProfile5;
    @BindView(R.id.etFname)
    EditText etFname;
    @BindView(R.id.etLname)
    EditText etLname;
    @BindView(R.id.etSex)
    EditText etSex;
    @BindView(R.id.etDob)
    EditText etDob;
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
    @BindView(R.id.etAbout)
    EditText etAbout;
    @BindView(R.id.etEducation)
    EditText etEducation;
    @BindView(R.id.etSmoke)
    EditText etSmoke;
    @BindView(R.id.etDrink)
    EditText etDrink;
    @BindView(R.id.etPolitics)
    EditText etPolitics;
    @BindView(R.id.etEmployee)
    EditText etEmployee;
    @BindView(R.id.etEarns)
    EditText etEarns;
    @BindView(R.id.ivClose2)
    ImageView ivClose2;
    @BindView(R.id.ivClose4)
    ImageView ivClose4;
    @BindView(R.id.ivClose1)
    ImageView ivClose1;
    @BindView(R.id.ivClose3)
    ImageView ivClose3;
    @BindView(R.id.ivClose5)
    ImageView ivClose5;
    private String imageId1 = "";
    private String imageId2 = "";
    private String imageId3 = "";
    private String imageId4 = "";
    private String imageId5 = "";
    private Data dataUser = new Data();
    private String intrested = "";
    private List<ImagesItem> listImages = new ArrayList<>();
    private String dateFormat = "dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    private String dateFormat2 = "yyyy-MM-dd";
    private DatePickerDialog.OnDateSetListener datePickerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setToolbar(toolbar, "", true);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        iniit();

    }

    private void iniit() {
        myCalendar_max.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 18);
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etDob.setText(sdf.format(myCalendar.getTime()));

            }
        };

        getQuestion();
        getprofile();
    }


    private void setDetail() {

        etFname.setText(dataUser.getName());
        etLname.setText(dataUser.getLastName());
        etSex.setText(dataUser.getVGender());
        etDob.setText(dataUser.getVBirthdate());
        etAbout.setText(dataUser.getVAbout());
        etSmoke.setText(dataUser.getIsSmoke());
        etDrink.setText(dataUser.getIsDrink());
        etEducation.setText(dataUser.getIsQualification());
        etPolitics.setText(dataUser.getIsPolitics());
        etEmployee.setText(dataUser.getIsEmployee());
        etEarns.setText(dataUser.getIsEarn());
        intrested = dataUser.getvShowMe();

        if (intrested.equalsIgnoreCase(Constants.MALE_SHOW)) {
            changeGender(0);
        } else if (intrested.equalsIgnoreCase(Constants.FEMALE_SHOW)) {
            changeGender(1);
        } else if (intrested.equalsIgnoreCase(Constants.OTHER_SHOW)) {
            changeGender(2);
        }
        listImages.clear();
        listImages = dataUser.getImages();


        String formattedDate = dataUser.getVBirthdate();
        try {

            SimpleDateFormat sdfConvert = new SimpleDateFormat(dateFormat2, Locale.US);
            SimpleDateFormat TFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date date = sdfConvert.parse(dataUser.getVBirthdate());
            formattedDate = TFormat.format(date);  // 20120821


        } catch (Exception e) {

        }
        etDob.setText(formattedDate);

        for (int i = 0; i < listImages.size(); i++) {

            if ((listImages.get(i).getVPhotoID()) == 1) {
                imageId1 = listImages.get(i).getId() + "";
                ivClose1.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(listImages.get(i).getVPhoto())
                        .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                .centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(ivProfile1);
            }
            if ((listImages.get(i).getVPhotoID()) == 2) {
                imageId2 = listImages.get(i).getId() + "";
                ivClose2.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(listImages.get(i).getVPhoto())
                        .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                .centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(ivProfile2);
            }
            if ((listImages.get(i).getVPhotoID()) == 3) {
                imageId3 = listImages.get(i).getId() + "";
                ivClose3.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(listImages.get(i).getVPhoto())
                        .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                .centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(ivProfile3);
            }
            if ((listImages.get(i).getVPhotoID()) == 4) {
                imageId4 = listImages.get(i).getId() + "";
                ivClose4.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(listImages.get(i).getVPhoto())
                        .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                .centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(ivProfile4);
            }
            if ((listImages.get(i).getVPhotoID()) == 5) {
                imageId5 = listImages.get(i).getId() + "";
                ivClose5.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(listImages.get(i).getVPhoto())
                        .apply(new RequestOptions().placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                                .centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(ivProfile5);
            }
        }

    }

    private void changeGender(int i) {
        if (i == 0) {
            intrested = Constants.MALE_SHOW;
            ivMale.setBorderWidth(5);
            ivFemale.setBorderWidth(0);
            ivOther.setBorderWidth(0);

            ivTrueM.setVisibility(View.VISIBLE);
            ivTrueF.setVisibility(View.INVISIBLE);
            ivTrueO.setVisibility(View.INVISIBLE);
        } else if (i == 1) {
            intrested = Constants.FEMALE_SHOW;
            ivMale.setBorderWidth(0);
            ivFemale.setBorderWidth(5);
            ivOther.setBorderWidth(0);

            ivTrueM.setVisibility(View.INVISIBLE);
            ivTrueF.setVisibility(View.VISIBLE);
            ivTrueO.setVisibility(View.INVISIBLE);
        } else if (i == 2) {
            intrested = Constants.OTHER_SHOW;
            ivMale.setBorderWidth(0);
            ivFemale.setBorderWidth(0);
            ivOther.setBorderWidth(5);

            ivTrueM.setVisibility(View.INVISIBLE);
            ivTrueF.setVisibility(View.INVISIBLE);
            ivTrueO.setVisibility(View.VISIBLE);
        }
    }

    private void openDialog(EditText etQes) {

        // Dialog adapter = new ResurantAdapter(this, resturantList);

// get the ListView and attach the adapter


        Dialog listDialog = new Dialog(this);
        //listDialog.setTitle("Select Item");
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_list, null, false);
        listDialog.setContentView(v);
        listDialog.setCancelable(true);
        //there are a lot of settings, for dialog, check them all out!

        ListView list1 = (ListView) listDialog.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spiner_profile, R.id.tvText,
                listQuestion);
        list1.setAdapter(adapter);


        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etQes.setText(listQuestion.get(i));
                listDialog.dismiss();
            }
        });

        listDialog.show();

    }


    private PopupWindow popupWindowsort(EditText etQes) {

        // initialize a pop up window type
        popupWindow = new PopupWindow(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spiner_profile, R.id.tvText,
                listQuestion);
        // the drop down list is a list view
        ListView listViewSort = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etQes.setText(listQuestion.get(i));
                popupWindow.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(5.0f);
        }
        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // set the listview as popup content
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    private void getQuestion() {
        Call<QuestionResponse> call = RetrofitBuilder.getInstance().getRetrofit().getQuestions();
        call.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        List<EducationQualificationItem> listQ = response.body().getData().getEducationQualification();
                        List<EmployeeItem> listE = response.body().getData().getEmployee();

                        listQualification.clear();
                        for (int i = 0; i < listQ.size(); i++) {
                            listQualification.add(listQ.get(i).getEducationQualification());
                        }
                        listEmployee.clear();
                        for (int i = 0; i < listE.size(); i++) {
                            listEmployee.add(listE.get(i).getAreEmployee());
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {

            }
        });

    }


    private void editUser() {
        String formattedDate = etDob.getText().toString();
        try {
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(etDob.getText().toString());
            formattedDate = targetFormat.format(date);  // 20120821

            DateFormat yearFormat = new SimpleDateFormat("yyyy");
            int selectedYear = Integer.parseInt(yearFormat.format(date));

            //Calendar myCalendarCurrent =


            age = myCalendarCurrent.get(Calendar.YEAR) - selectedYear;
            Log.e("age", age + "<<<>>>");

        } catch (Exception e) {

        }


        showProgress();
        final Map<String, Object> request = new HashMap<>();

        request.put("vGender", etSex.getText().toString());
        request.put("is_qualification", etEducation.getText().toString() + "");
        request.put("is_smoke", etSmoke.getText().toString() + "");
        request.put("is_drink", etDrink.getText().toString() + "");
        request.put("is_politics", etPolitics.getText().toString() + "");
        request.put("is_employee", etEmployee.getText().toString() + "");
        request.put("is_earn", etEarns.getText().toString() + "");
        request.put("last_name", etLname.getText().toString() + "");
        request.put("name", etFname.getText().toString() + "");
        request.put("vAbout", etAbout.getText().toString() + "");
        request.put("vShow_me", intrested + "");
        request.put("vBirthdate", formattedDate + "");
        request.put("vAge", age + "");

        //Log.e("age>",age+"<");
        Call<UserResponse> call = null;

        call = RetrofitBuilder.getInstance().getRetrofit().editProfile(request);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {


                    if (response.body().isSuccess()) {
                        showToast("Profile Updated");
                        LoginResponse lp = HawkAppUtils.getInstance().getUSERDATA();
                        lp.setData(response.body().getData());
                        HawkAppUtils.getInstance().setUSERDATA(lp);

                        finish();

                    } else {
                        showToast(response.body().getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
                Log.e("errr: ", ">>err" + t + "\n");

            }
        });
    }


    @OnClick({R.id.tvUpdate, R.id.ivProfile2, R.id.ivProfile4, R.id.ivProfile1, R.id.ivProfile3,
            R.id.ivProfile5, R.id.etFname, R.id.etLname, R.id.etSex, R.id.etDob, R.id.ivMale,
            R.id.ivFemale, R.id.ivOther, R.id.etAbout, R.id.etEducation, R.id.etSmoke, R.id.etDrink, R.id.etPolitics,
            R.id.etEmployee, R.id.etEarns, R.id.ivClose2, R.id.ivClose4, R.id.ivClose1, R.id.ivClose3, R.id.ivClose5})
    public void onViewClicked(View view) {
        PopupWindow popUp;
        switch (view.getId()) {
            case R.id.tvUpdate:
                if (etFname.getText().toString().isEmpty()) {
                    etFname.setError("Required");
                    showToast("Enter your first name");

                    return;
                }
                if (etLname.getText().toString().isEmpty()) {
                    etLname.setError("Required");
                    showToast("Enter your last name");
                    return;
                }
                if (etDob.getText().toString().isEmpty()) {
                    etDob.setError("Required");
                    showToast("Enter your date of birth");
                    return;
                }

                if (!isConnectedToInternet()) {
                    return;
                }
                editUser();
                break;


            case R.id.etSex:
                listQuestion.clear();
                listQuestion.add("Male");
                listQuestion.add("Female");
                listQuestion.add("Other");

                /*popUp = popupWindowsort(etSex);
                popUp.showAsDropDown(etSex, 0, 0);
*/
                openDialog(etSex);
                break;
            case R.id.etDob:


                DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                // datePickerDialog.getDatePicker().setMinDate(myCalendar_min.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(myCalendar_max.getTimeInMillis());

                datePickerDialog.show();

                break;

            case R.id.etEducation:

                listQuestion.clear();
                listQuestion.addAll(listQualification);
                openDialog(etEducation);
                break;
            case R.id.etSmoke:

                listQuestion.clear();
                listQuestion.add("Yes");
                listQuestion.add("No");
                listQuestion.add("Occasionally");

                openDialog(etSmoke);
              /*  popUp = popupWindowsort(etSmoke);
                popUp.showAsDropDown(etSmoke, 0, 0);
*/
                break;
            case R.id.etDrink:

                listQuestion.clear();
                listQuestion.add("Yes");
                listQuestion.add("No");
                listQuestion.add("Occasionally");

                openDialog(etDrink);
            /*    popUp = popupWindowsort(etDrink);
                popUp.showAsDropDown(etDrink, 0, 0);
*/

                break;
            case R.id.etPolitics:

                listQuestion.clear();
                listQuestion.add("Yes");
                listQuestion.add("No");
                listQuestion.add("Rarely");

                openDialog(etPolitics);
             /*   popUp = popupWindowsort(etPolitics);
                popUp.showAsDropDown(etPolitics, 0, 0);
*/
                break;
            case R.id.etEmployee:

                listQuestion.clear();
                listQuestion.addAll(listEmployee);


                openDialog(etEmployee);
/*

                popUp = popupWindowsort(etEmployee);
                popUp.showAsDropDown(etEmployee, 0, 0);
*/

                break;
            case R.id.etEarns:


                listQuestion.clear();
                listQuestion.add("Yes");
                listQuestion.add("No");
                listQuestion.add("Not sure");

                openDialog(etEarns);
             /*   popUp = popupWindowsort(etEarns);
                //popUp.showAsDropDown(etEarns, 0, 0);
                popUp.showAsDropDown(view, 0, 0);*/

                break;
            case R.id.ivMale:
                changeGender(0);
                break;
            case R.id.ivFemale:
                changeGender(1);
                break;
            case R.id.ivOther:
                changeGender(2);
                break;

            case R.id.ivProfile1:
                FetchImage(ivProfile1, GALLRY_REQUEST_1, CAMERA_REQUEST_1);
                break;
            case R.id.ivProfile2:
                FetchImage(ivProfile2, GALLRY_REQUEST_2, CAMERA_REQUEST_2);
                break;
            case R.id.ivProfile3:
                FetchImage(ivProfile3, GALLRY_REQUEST_3, CAMERA_REQUEST_3);
                break;
            case R.id.ivProfile4:
                FetchImage(ivProfile4, GALLRY_REQUEST_4, CAMERA_REQUEST_4);
                break;
            case R.id.ivProfile5:
                FetchImage(ivProfile5, GALLRY_REQUEST_5, CAMERA_REQUEST_5);
                break;
            case R.id.ivClose1:
                deleteImage(1, imageId1);
                break;
            case R.id.ivClose2:
                deleteImage(2, imageId2);
                break;
            case R.id.ivClose3:
                deleteImage(3, imageId3);
                break;
            case R.id.ivClose4:
                deleteImage(4, imageId4);
                break;
            case R.id.ivClose5:
                deleteImage(5, imageId5);
                break;
        }
    }

    private void FetchImage(ImageView ivProfile, int gallryRequest, int camerareq) {
        if (!AppUtils.hasStorageCameraPermissions(this, AppUtils.PERMISSIONS_PIC)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(AppUtils.PERMISSIONS_PIC, MY_PERMISSION_ALL_STORE);
            }
        } else {
            showImageDialog(ivProfile, gallryRequest, camerareq);
        }
    }

    private void showImageDialog(ImageView ivProfile, int gallryRequest, int camerareq) {
        PopupMenu popupMenu = new PopupMenu(this, ivProfile);
        popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.ac_camera:


                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        try {
                            photoFile = AppUtils.createImageFile(EditProfileActivity.this);
                            imgString = photoFile.getAbsolutePath();


                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(EditProfileActivity.this,
                                    getPackageName() + ".fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, camerareq);
                        }
                        break;
                    case R.id.ac_gallery:


                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(i, gallryRequest);

                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if ((requestCode == CAMERA_REQUEST_1 || requestCode == CAMERA_REQUEST_2 || requestCode == CAMERA_REQUEST_3 ||
                requestCode == CAMERA_REQUEST_4 | requestCode == CAMERA_REQUEST_5) && resultCode == RESULT_OK) {

            File output = new File(imgString);
            Log.e("im c", imgString + " " + requestCode);

            setImageviews(requestCode, Uri.fromFile(output));


        } else if (resultCode == Activity.RESULT_OK && (requestCode == GALLRY_REQUEST_1 || requestCode == GALLRY_REQUEST_2 ||
                requestCode == GALLRY_REQUEST_3 || requestCode == GALLRY_REQUEST_4 || requestCode == GALLRY_REQUEST_5)) {

            if (data != null) {


                Uri selectedImageUri = data.getData();
                imgString = AppUtils.getPathFromUri(EditProfileActivity.this, selectedImageUri);
                Log.e("im", imgString + " " + requestCode);
                setImageviews(requestCode, selectedImageUri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageviews(int requestCode, Uri fromUri) {
        if (requestCode == GALLRY_REQUEST_1 || requestCode == CAMERA_REQUEST_1) {
            ivClose1.setVisibility(View.VISIBLE);
            ivProfile1.setEnabled(false);
            Glide.with(this).load(fromUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .centerCrop().into(ivProfile1);
            uploadImage(1);
        } else if (requestCode == GALLRY_REQUEST_2 || requestCode == CAMERA_REQUEST_2) {
            ivClose2.setVisibility(View.VISIBLE);
            ivProfile2.setEnabled(false);
            Glide.with(this).load(fromUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .centerCrop().into(ivProfile2);
            uploadImage(2);
        } else if (requestCode == GALLRY_REQUEST_3 || requestCode == CAMERA_REQUEST_3) {
            ivClose3.setVisibility(View.VISIBLE);
            ivProfile3.setEnabled(false);
            Glide.with(this).load(fromUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .centerCrop().into(ivProfile3);
            uploadImage(3);
        } else if (requestCode == GALLRY_REQUEST_4 || requestCode == CAMERA_REQUEST_4) {
            ivClose4.setVisibility(View.VISIBLE);
            ivProfile4.setEnabled(false);
            Glide.with(this).load(fromUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .centerCrop().into(ivProfile4);
            uploadImage(4);
        } else if (requestCode == GALLRY_REQUEST_5 || requestCode == CAMERA_REQUEST_5) {
            ivClose5.setVisibility(View.VISIBLE);
            ivProfile5.setEnabled(false);
            Glide.with(this).load(fromUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .centerCrop().into(ivProfile5);
            uploadImage(5);
        }
    }

    private void getprofile() {
        showProgress();
        String uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "";
        final Map<String, Object> request = new HashMap<>();


        Call<UserResponse> call = RetrofitBuilder.getInstance().getRetrofit().userData();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        dataUser = response.body().getData();
                        setDetail();
                    }
                }
            }


            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void uploadImage(int pos) {
        showProgress();
        File output = new File(imgString);
        MultipartBody.Part imagenPerfil = null;
        if ((output) != null && output.exists()) {

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), output);
            // MultipartBody.Part is used to send also the actual file name
            imagenPerfil = MultipartBody.Part.createFormData("profileImage", output.getName(), requestFile);
        }


        String auth = HawkAppUtils.getInstance().getUSERDATA().getToken();
        Log.e("Token", "Token " + auth);

        Call<BaseResponse> callImg = RetrofitBuilder.getInstance().getRetrofit().userProfileImage(pos + "", auth, imagenPerfil);

        callImg.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {
                    //sendLocation();
                    imgString = "";
                    showToast(response.body().getMessage());
                    getImaged(pos);

                }


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("ResErr", t + "");
                hideProgress();
            }
        });

    }

    private void getImaged(int pos) {
        showProgress();
        Call<ImageResponse> call = RetrofitBuilder.getInstance().getRetrofit().userProfileImage();
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                hideProgress();
                if (response.isSuccessful()) {

                    if (response.body().isSuccess()) {

                        List<DataItem> listI = response.body().getData();
                        for (int i = 0; i < listI.size(); i++) {
                            if (pos == listI.get(i).getVPhotoID()) {
                                if (pos == 1) {
                                    imageId1 = listI.get(i).getId() + "";

                                } else if (pos == 2) {
                                    imageId2 = listI.get(i).getId() + "";

                                } else if (pos == 3) {
                                    imageId3 = listI.get(i).getId() + "";

                                } else if (pos == 4) {
                                    imageId4 = listI.get(i).getId() + "";

                                } else if (pos == 5) {
                                    imageId5 = listI.get(i).getId() + "";

                                }
                            }
                        }

                    }
                }


            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                hideProgress();
            }
        });

    }

    private void deleteImage(int pos, String imageId) {
        showProgress();
        String uid = HawkAppUtils.getInstance().getUSERDATA().getData().getId() + "";
        final Map<String, Object> request = new HashMap<>();
        request.put("id", imageId);


        Call<BaseResponse> call = RetrofitBuilder.getInstance().getRetrofit().deleteProfileImage(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {

                    showToast(response.body().getMessage());
                    if (response.body().isSuccess()) {

                        if (pos == 1) {
                            ivClose1.setVisibility(View.GONE);
                            ivProfile1.setEnabled(true);
                            ivProfile1.setImageResource(R.drawable.ic_img_placeholder);
                        } else if (pos == 2) {
                            ivClose2.setVisibility(View.GONE);
                            ivProfile2.setEnabled(true);
                            ivProfile2.setImageResource(R.drawable.ic_img_placeholder);
                        } else if (pos == 3) {
                            ivClose3.setVisibility(View.GONE);
                            ivProfile3.setEnabled(true);
                            ivProfile3.setImageResource(R.drawable.ic_img_placeholder);
                        } else if (pos == 4) {
                            ivProfile4.setEnabled(true);
                            ivClose4.setVisibility(View.GONE);
                            ivProfile4.setImageResource(R.drawable.ic_img_placeholder);
                        } else if (pos == 5) {
                            ivProfile5.setEnabled(true);
                            ivClose5.setVisibility(View.GONE);
                            ivProfile5.setImageResource(R.drawable.ic_img_placeholder);
                        }

                    }
                }
            }


            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

}
