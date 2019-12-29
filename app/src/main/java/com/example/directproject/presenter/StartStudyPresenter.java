package com.example.directproject.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.directproject.contract.StartStudyContract;
import com.example.directproject.network.APIClient;
import com.example.directproject.network.FileUploadAPI;
import com.example.directproject.utils.FileUtils;
import com.example.directproject.utils.SettingInfo;
import com.example.directproject.view.StartStudyActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StartStudyPresenter implements StartStudyContract.Presenter {

    StartStudyContract.View view;

    FileUtils fileUtils = new FileUtils();




    @Override
    public void setView(StartStudyContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        this.view = null;
    }

    @Override
    public void saveBitmapToJpeg(Bitmap bitmap, String targetFileMane) {

        fileUtils.saveBitmapToJpeg(bitmap,targetFileMane);
    }


    @Override
    public void startCrop(String targetFileName) {
        String filePath = fileUtils.getTempFilePath(targetFileName );
        Uri targetUri = fileUtils.stringPathToUri(filePath);
        view.startCrop(targetUri);
    }


    @Override
    public void cropResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == ((StartStudyActivity)view).RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;

                CropImage.ActivityResult result22 = CropImage.getActivityResult(data);

                CropImageView cropImageView = new CropImageView(((StartStudyActivity)view));
                cropImageView.setImageUriAsync(result22.getUri());

                try{
                    bitmap = MediaStore.Images.Media.getBitmap(((StartStudyActivity)view).getContentResolver() , resultUri);
                }catch (Exception e ){

                }

                if (bitmap == null){
                    view.showToast("등록에 실패하였습니다.");
                }else {
                    fileUtils.saveBitmapToJpeg(bitmap , "question");
                    this.sendCropImage();

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void sendCropImage() {
        FileUploadAPI apiInterface = APIClient.getClient().create(FileUploadAPI.class);
        File file = new File(fileUtils.getTempFilePath(SettingInfo.CROP_IMAGE_NAME));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<JSONObject> call = apiInterface.uploadImages(uploadFile);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()) {
                    Log.i("test", "isSuccessful: if ");
                    view.showToast("질문 등록 완료.");
                }else {
                    Log.i("test", "isSuccessful: else");
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.i("test", "onFailure: "+t.getMessage());
            }
        });
    }


}
