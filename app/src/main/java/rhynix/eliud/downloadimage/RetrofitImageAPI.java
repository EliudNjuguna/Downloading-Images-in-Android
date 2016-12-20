package rhynix.eliud.downloadimage;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by eliud on 12/20/16.
 */

public interface RetrofitImageAPI {
    @GET("api/RetrofitAndroidImageResponse")
    Call<ResponseBody> getImageDetails();
}
