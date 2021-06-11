package com.nect.friendlymony.Fragments.register;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.nect.friendlymony.Activity.EditProfileActivity;
import com.nect.friendlymony.Activity.MainActivity;
import com.nect.friendlymony.Fragments.BaseFragment;
import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.SignupModel;
import com.nect.friendlymony.R;
import com.nect.friendlymony.Retrofit.RetrofitBuilder;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.nect.friendlymony.Utils.Constants.PREF_FCM_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPhotoFragment extends BaseFragment {

    private static final int MY_PERMISSION_ALL_STORE = 111;
    private static final int REQUST_IMAGE = 222;
    private static final int GALLRY_REQUEST = 333;
    private static final int CAMERA_REQUEST = 334;

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


    private View view;
    private String imgString = "";
    private SignupModel sm;


    @BindView(R.id.ivProfile2)
    ImageView ivProfile2;
    @BindView(R.id.ivClose2)
    ImageView ivClose2;
    @BindView(R.id.ivProfile4)
    ImageView ivProfile4;
    @BindView(R.id.ivClose4)
    ImageView ivClose4;
    @BindView(R.id.ivProfile1)
    ImageView ivProfile1;
    @BindView(R.id.ivClose1)
    ImageView ivClose1;
    @BindView(R.id.ivProfile3)
    ImageView ivProfile3;
    @BindView(R.id.ivClose3)
    ImageView ivClose3;
    @BindView(R.id.ivProfile5)
    ImageView ivProfile5;
    @BindView(R.id.ivClose5)
    ImageView ivClose5;

    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.btnNext)
    Button btnNext;


    public UploadPhotoFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
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
            view = inflater.inflate(R.layout.fragment_upload_photo, container, false);
            ButterKnife.bind(this, view);
            sm = HawkAppUtils.getInstance().getSIGNUP();


            if (!AppUtils.hasStorageCameraPermissions(getActivity(), AppUtils.PERMISSIONS_PIC)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(AppUtils.PERMISSIONS_PIC, MY_PERMISSION_ALL_STORE);
                }
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void showImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), ivProfile);
        popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.ac_camera:


                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        try {
                            photoFile = AppUtils.createImageFile(getActivity());
                            imgString = photoFile.getAbsolutePath();

                            Log.e("ST", imgString);

                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                    getActivity().getPackageName() + ".fileprovider",
                                    photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                        break;
                    case R.id.ac_gallery:


                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        i.setType("image/*");
                        startActivityForResult(i, GALLRY_REQUEST);

                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }



    private void uploadImage(LoginResponse body) {
        File output = new File(imgString);
        MultipartBody.Part imagenPerfil = null;
        if ((output) != null && output.exists()) {

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), output);
            // MultipartBody.Part is used to send also the actual file name
            imagenPerfil = MultipartBody.Part.createFormData("profileImage", output.getName(), requestFile);
        }


        // String auth = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjozMCwiaWF0IjoxNTY1Nzg5MDEzLCJleHAiOjE1OTczMjUwMTN9.OgVCZAgmI3JolWvmCYkkiIf5E6WUphLE_iknStszC7A";
        String auth = body.getToken();
        Log.e("Token", "Token " + auth);

        Call<BaseResponse> callImg = RetrofitBuilder.getInstance().getRetrofit().userProfileImage("1", auth, imagenPerfil);

        callImg.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                if (response.isSuccessful()) {
                    //sendLocation();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("from",1);
                    startActivity(intent);
                    getActivity().finish();
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("ResErr", t + "");
                hideProgress();
            }
        });

    }



    @OnClick({R.id.ivProfile, R.id.btnNext, R.id.ivProfile2, R.id.ivClose2, R.id.ivProfile4, R.id.ivClose4, R.id.ivProfile1, R.id.ivClose1, R.id.ivProfile3, R.id.ivClose3, R.id.ivProfile5, R.id.ivClose5})
    public void onViewClicked(View view) {
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

            case R.id.ivClose2:
                break;
            case R.id.ivClose4:
                break;
            case R.id.ivClose1:
                break;
            case R.id.ivClose3:
                break;
            case R.id.ivClose5:
                break;

            case R.id.ivProfile:
                if (!AppUtils.hasStorageCameraPermissions(getActivity(), AppUtils.PERMISSIONS_PIC)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(AppUtils.PERMISSIONS_PIC, MY_PERMISSION_ALL_STORE);
                    }
                } else {
                    showImageDialog();
                }
                break;
            case R.id.btnNext:

                if (imgString.equals("")) {
                    showToast("Please upload image");
                } else {

                    //SignupUser();
                    //uploadImage(new LoginResponse());

                }
                break;
        }
    }

    private void FetchImage(ImageView ivProfile, int gallryRequest, int camerareq) {
        if (!AppUtils.hasStorageCameraPermissions(getActivity(), AppUtils.PERMISSIONS_PIC)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(AppUtils.PERMISSIONS_PIC, MY_PERMISSION_ALL_STORE);
            }
        } else {
            showImageDialog(ivProfile, gallryRequest, camerareq);
        }
    }

    private void showImageDialog(ImageView ivProfile, int gallryRequest, int camerareq) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), ivProfile);
        popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.ac_camera:


                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = null;
                        try {
                            photoFile = AppUtils.createImageFile(getActivity());
                            imgString = photoFile.getAbsolutePath();


                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                    getActivity().getPackageName() + ".fileprovider",
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

        if (requestCode == REQUST_IMAGE && resultCode == RESULT_OK) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            //  getSelectedImage(requestCode, images.get(0));
            Log.e("UPLOAD", "getSelectedImage: " + images.get(0).getPath());
            ivProfile.setImageURI(Uri.fromFile(new File(images.get(0).getPath())));

            // String destinationFileName = "";
/*
            UCrop.of(Uri.parse(images.get(0).getPath()),  Uri.fromFile(new File(getCacheDir(), destinationFileName)))
                    .useSourceImageAspectRatio()
                    .start(ProfilePictureActivity.this);*/

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            File output = new File(imgString);
            Log.e("im c", imgString);

            Glide.with(getActivity()).load(Uri.fromFile(output)).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                    .into(ivProfile);
               /* if (output.exists()) {

                    iv_camera.setImageURI(Uri.fromFile(output));
                }*/
            /*output = new File(outputPath);

            if (output.exists()) {
                ivProfile.setImageURI(Uri.fromFile(output));
                String picturePath = output.getAbsolutePath();
                uploadImage(picturePath);
            }*/


        } else if (resultCode == RESULT_OK && requestCode == GALLRY_REQUEST) {

            if (data != null) {


                Uri selectedImageUri = data.getData();
                //iv_camera.setImageURI(selectedImageUri);
                imgString = AppUtils.getPathFromUri(getActivity(), selectedImageUri);
                // output = new File(imgString);
                Log.e("im", imgString);

                Glide.with(getActivity()).load(selectedImageUri).placeholder(R.drawable.ic_image_placeholder).error(R.drawable.ic_image_placeholder)
                        .into(ivProfile);
            }
        } else if ((requestCode == CAMERA_REQUEST_1 || requestCode == CAMERA_REQUEST_2 || requestCode == CAMERA_REQUEST_3 ||
                requestCode == CAMERA_REQUEST_4 | requestCode == CAMERA_REQUEST_5) && resultCode == RESULT_OK) {

            File output = new File(imgString);
            Log.e("im c", imgString + " " + requestCode);

            //  setImageviews(requestCode, Uri.fromFile(output));


        } else if (resultCode == Activity.RESULT_OK && (requestCode == GALLRY_REQUEST_1 || requestCode == GALLRY_REQUEST_2 ||
                requestCode == GALLRY_REQUEST_3 || requestCode == GALLRY_REQUEST_4 || requestCode == GALLRY_REQUEST_5)) {

            if (data != null) {


                Uri selectedImageUri = data.getData();
                imgString = AppUtils.getPathFromUri(getActivity(), selectedImageUri);
                Log.e("im", imgString + " " + requestCode);
                // setImageviews(requestCode, selectedImageUri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
/*
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

*/
/*
    private void checkPhoneLogin() {

        showProgress();
        final Map<String, Object> request = new HashMap<>();
        request.put("country_code", sm.getCountrycode());
        request.put("mobile_no", sm.getPhonenumber());


        Call<LoginResponse> call = RetrofitBuilder.getInstance().getRetrofit().UserRegisteredV1(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().isSuccess() && response.body().isRegistered()) {

                        showToast("Mobile No Already Register.");

                        hideProgress();

                    } else {
                        registerUser();
                    }
                } else {
                    registerUser();

                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                registerUser();
            }
        });
    }
*//*

}
*/
