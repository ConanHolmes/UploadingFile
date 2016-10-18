package com.ningjiahao.uploadingfile;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 甯宁寧 on 2016-10-17.
 */
public interface Myretrofit {
    @GET
    Call<ResponseBody> getBaidu(@Url() String url);
    @POST
    Call<ResponseBody> getHttp(@Url() String url);
    @Streaming
    @POST
    Call<ResponseBody> downloadFile(@Url()String url);
    @Multipart
    @POST("Test/UploadFile")
    Call<ResponseBody> uploadFile(@Part("ceshi") RequestBody body);
}
