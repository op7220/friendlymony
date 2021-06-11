package com.nect.friendlymony.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Images.DataItem;
import com.nect.friendlymony.Model.Images.ImageResponse;
import com.nect.friendlymony.Model.Login.ImagesItem;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.HawkAppUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class AddImagesActivity extends BaseAppCompatActivity {


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

    private String imageId1 = "";
    private String imageId2 = "";
    private String imageId3 = "";
    private String imageId4 = "";
    private String imageId5 = "";


    String imgString;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        ButterKnife.bind(this);


        toolbar.setTitle("");
        setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().detectAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

    }


    @OnClick({R.id.btnNext, R.id.ivProfile2, R.id.ivProfile4, R.id.ivProfile1, R.id.ivProfile3,
            R.id.ivProfile5, R.id.ivClose2, R.id.ivClose4, R.id.ivClose1, R.id.ivClose3, R.id.ivClose5})
    public void onViewClicked(View view) {
        PopupWindow popUp;
        switch (view.getId()) {

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
            case R.id.btnNext:
                onBackPressed();
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
                            photoFile = AppUtils.createImageFile(AddImagesActivity.this);
                            imgString = photoFile.getAbsolutePath();


                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(AddImagesActivity.this,
                                    getPackageName() + ".fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, camerareq);
                        }
                        break;
                    case R.id.ac_gallery:


                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                imgString = AppUtils.getPathFromUri(AddImagesActivity.this, selectedImageUri);
                Log.e("im", imgString + " " + requestCode);
                setImageviews(requestCode, selectedImageUri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImageviews(int requestCode, Uri fromUri) {
        Log.e("Zzz", requestCode + "");
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


    private void uploadImage(int pos) {
        showProgress();
        File output = new File(imgString);
        MultipartBody.Part imagenPerfil = null;
        if ((output) != null && output.exists()) {

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), output);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from",1);
        startActivity(intent);
        finishAffinity();
    }
}
