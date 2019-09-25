package Constans;

import Bean.ElevatorInspect;
import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpModel {
    private HttpClientListener httpClientListener;
    private  Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private OkHttpClient okHttpClient;
    public HttpModel(Context context,HttpClientListener listener) {
        this.httpClientListener=listener;
        this.context=context;
        sharedPreferences=context.getSharedPreferences("config", context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 网络请求电梯数据
     * @param json
     */
    public void get(String json,String url){
        Map<String, String> params = new HashMap<>();
        params.put("json", json);
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        OkHttpUtils
                .get()
                .url(Constants.HOST+url)
                .addHeader("Cookie",cookie)
                .params(params)
                .addParams("module", String.valueOf(Constants.DEFAULT_MODULE))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        httpClientListener.onError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.equals("")) {
                            httpClientListener.onSuccess(response);
                        } else {
                            httpClientListener.onError();
                        }
                    }
                });
    }

    public void post(String json,String url){
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        FormBody formBody = new FormBody.Builder()
                .add("username", "mcompany_staff")
                .add("elevator_pk", "7b125cbadd1511e99420525400bdc3cb")
                .add("type","month").build();
        Request request = new Request.Builder()
                .url(Constants.HOST+url)
                .addHeader("Cookie",cookie)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
                         @Override
                         public void onFailure(Call call, IOException e) {
                             httpClientListener.onError();
                         }

                         @Override
                         public void onResponse(Call call, Response response) throws IOException {
                             String res=response.body().string();
                             JSONObject jsonObject=JSONObject.parseObject(res);
                             if (!res.equals("")&&res.contains("mtc_logs")){
                                 Gson gson=new Gson();
                                 ElevatorInspect inspect=gson.fromJson(res, ElevatorInspect.class);
                                 httpClientListener.onSuccess(inspect);
                             } else {
                                 httpClientListener.onError();
                             }
                         }
            });
    }
    /**
     * 登录
     * @param url
     */
    public void login(String name,String password,String url){
        FormBody formBody = new FormBody.Builder()
                .add("username", name).add("password", password).build();
        Request request = new Request.Builder()
                .url(Constants.HOST+url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpClientListener.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                JSONObject jsonObject=JSONObject.parseObject(res);
                if (jsonObject.getInteger("code")==200){
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    String result="";
                    for (String cookie:cookies) {
                        String arg=cookie.substring(0,cookie.indexOf(";"));
                        String name=arg.substring(0,cookie.indexOf("="));
                        String value=arg.substring(cookie.indexOf("=")+1);
                        editor.putString(name,value);
                    }
                    editor.apply();
                    httpClientListener.onSuccess(res);
                }else {
                    httpClientListener.onError();
                }
            }
        });
    }

    public interface HttpClientListener{
        void onError();
        void onSuccess(Object obj);
    }
}
