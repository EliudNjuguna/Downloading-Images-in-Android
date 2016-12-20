package rhynix.eliud.downloadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    String url = "http://www.androidtutorialpoint.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ButtonArray = (Button)findViewById(R.id.RetrofitImage);

        ButtonArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View VisibleImage = findViewById(R.id.RetrofitImage);
                VisibleImage.setVisibility(View.GONE);
                getRetrofitImage();
            }
        });

    }
    void getRetrofitImage(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitImageAPI service = retrofit.create(RetrofitImageAPI.class);
        Call<ResponseBody> call = service.getImageDetails();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try{
                    Log.d("onResponse","Response came from Server");

                    boolean FileDownLoaded = DownloadImage(response.body());
                    Log.d("onResponse","Image is downloaded and saved ?" + FileDownLoaded);

                }catch (Exception e){
                    Log.d("onFailure","There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure",t.toString());

            }
        });
    }

    private boolean DownloadImage(ResponseBody body){
        try {
            Log.d("DownloadImage","Reading and Writting file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg" );
                int c;
                while ((c=in.read())!= -1){
                    out.write(c);
                }
            }catch (IOException e){
                Log.d("DownloadImage",e.toString());
                return false;
            }
            finally {
                if (in !=null){
                    in.close();
                }
                if (out !=null){
                    out.close();
                }
            }

            int width,height;
            ImageView image = (ImageView)findViewById(R.id.imageViewId);
            Bitmap bMap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
            width = 2*bMap.getWidth();
            height = 6*bMap.getHeight();
            Bitmap bitmap = Bitmap.createScaledBitmap(bMap,width,height,false);
            image.setImageBitmap(bitmap);

            return true;
        }catch (IOException e){
            Log.d("DownloadImage",e.toString());
            return false;
        }
    }
}
