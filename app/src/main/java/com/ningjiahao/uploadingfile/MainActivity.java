package com.ningjiahao.uploadingfile;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Button btn,download_btn;
    private ProgressBar progress,down_load_progress;
    private TextView img;
    private Myretrofit myretrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRetrofit();
        btn= (Button) findViewById(R.id.inter_btn);
        download_btn= (Button) findViewById(R.id.download_btn);
        down_load_progress= (ProgressBar) findViewById(R.id.dowm_loadprogress);
        img= (TextView) findViewById(R.id.img);
        progress= (ProgressBar) findViewById(R.id.progress);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new download().execute();
                Log.e("Tag","开始任务");
            }
        });
    }

    private void initRetrofit() {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://10.2.153.234:8080/").build();
        myretrofit=retrofit.create(Myretrofit.class);
    }
    class download extends AsyncTask<Void,Integer,Void>{
        long progress;

        @Override
        protected Void doInBackground(Void... params) {

            Call<ResponseBody> call=myretrofit.downloadFile("http://ww1.sinaimg.cn/large/006y8lVajw1f8p8qab7dkj31kw0zjah5.jpg");
            try {
                retrofit2.Response<ResponseBody> response=call.execute();
                InputStream is=response.body().byteStream();
                long max=response.body().contentLength();
                Log.e("Tag","长度"+max);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/bbb.jpg");
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] array = new byte[1024];
                int index = 0;
                while ((index=is.read(array))!=-1) {
                    outputStream.write(array, 0, index);
                    progress=progress+index;
                    int per= (int) (progress*100/max);
                    publishProgress(per);
                }
                outputStream.flush();
                outputStream.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Tag","出现异常");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            down_load_progress.setProgress(values[0]);
            img.setText(values[0]+"");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            img.setText("下载成功");
        }
    }
    class upload extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/aa.png");
            RequestBody body = new MultipartBody.Builder()
                    .addFormDataPart("username", "测试")
                    .addFormDataPart("app", "app.png",
                            MultipartBody.create(MultipartBody.FORM, file))
                    .build();
            Call<ResponseBody> call = myretrofit.uploadFile(body);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    Log.d("TAG", "上传成功");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }

            });
            return null;
        }
    }
}
