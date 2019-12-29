package com.example.directproject.contract;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

public interface StartStudyContract{

    interface View extends BaseContract.View{
        void showToast(String text);

        void startCrop(Uri targetUri);

    }

    interface Presenter extends  BaseContract.Presenter <View>{

        void setView(View view);

        void releaseView();

        void saveBitmapToJpeg(Bitmap bitmap , String targetFileName);

        void startCrop(String targetFileName);

        void cropResult(int requestCode, int resultCode, Intent data);

        void sendCropImage ();
    }
}
