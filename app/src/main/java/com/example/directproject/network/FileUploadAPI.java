package com.example.directproject.network;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadAPI {

    /**
     * @author YunJin Choi
     * @since 2019-12-27 18:53
     * @description /jpa/upload 경로로 파일 전송.
     *
     */
    @Multipart
    @POST("/jpa/upload")
    Call<JSONObject> uploadImages(@Part MultipartBody.Part uploadFile);

}
