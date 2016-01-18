package com.nicodelee.base.ui.activity;

import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.nicodelee.base.BaseActivity;
import com.nicodelee.base.R;
import com.nicodelee.common.commonview.CommonToast;
import com.nicodelee.util.Logger;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.nicodelee.http.okhttp.OkHttpUtils;
import com.nicodelee.http.okhttp.callback.StringCallback;
import java.io.IOException;
import okhttp3.Call;

/**
 * Created by NocodeLee on 15/12/24.
 * Email：lirizhilirizhi@163.com
 *
 * Demo Base @see https://github.com/hongyangAndroid/okhttp-utils
 */
public class HttpShowActivity extends BaseActivity {
  @Bind(R.id.httpReslut) TextView httpReslut;

  @Override protected int getLayoutResId() {
    return R.layout.activity_http;
  }

  @Override protected CharSequence getTitleName() {
    return "Http 请求演示";
  }

  //String url = "https://publicobject.com/helloworld.txt";
  String url = "http://www.baidu.com/";

  @OnClick(R.id.getHttp) void getData() {
    CommonToast.showToast(this, "请求中...");
    httpReslut.setText("");
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          getRun(url);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  @OnClick(R.id.get_http_async) void dogetAsync(){
    CommonToast.showToast(this,"异步请求");
    httpReslut.setText("");
    getAsync(url);
  }

  @OnClick(R.id.get_html) void getHtml(){
    OkHttpUtils.get().url(url).build().execute(new StringCallback() {
      @Override public void onError(Call call, Exception e) {
      }

      @Override public void onResponse(String response) {
        httpReslut.setText(response);
      }
    });
  }

  OkHttpClient client = new OkHttpClient();
  //同步get请求
  String getRun(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = client.newCall(request).execute();
    if (response.isSuccessful()) {
      final String res = response.body().string();
      Logger.e(res);
      runOnUiThread(new Runnable() {
        @Override public void run() {
          httpReslut.setText(res);
        }
      });
    }
    return response.body().string();
  }


  public void getAsync(String url){
    Request request = new Request.Builder()
        .url(url)
        .build();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Request request, IOException e) {
        e.printStackTrace();
      }

      @Override public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()){
          final String res = response.body().string();
          runOnUiThread(new Runnable() {
            @Override public void run() {
              httpReslut.setText(res);
            }
          });
        }

      }
    });
  }


}