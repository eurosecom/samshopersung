package com.eusecom.samshopersung;

/**
 * Created by Shaon on 12/3/2016.
 */
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface SetImageApiService {
    @Multipart
    @POST("/androidshopper/uploadimage.php")
    Call<SetImageServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);
}