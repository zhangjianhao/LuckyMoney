package com.zjianhao.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjianhao.https.DownloadListner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by zjianhao on 15-12-6.
 */
public class DownloadUtil {
    public void download(String url, final String saveAbsolutePath, final String filename, final DownloadListner listner){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
//                long fileSize = response.header("ContentLength");
                InputStream inputStream = response.body().byteStream();
                File file = new File(saveAbsolutePath,filename);
                if (!file.getParentFile().exists())
                    file.mkdirs();
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                int completedSize = 0;
                while((len = inputStream.read(buf))>0){
                    outputStream.write(buf,0,len);
                    completedSize+=len;
                    listner.onProgress(completedSize,0);
                }

            }
        });
    }



}
