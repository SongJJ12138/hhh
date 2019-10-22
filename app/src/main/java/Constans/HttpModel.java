package Constans;

import Bean.ElevatorInspect;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
    private String BaseUrl;

    public HttpModel(Context context,HttpClientListener listener) {
        this.httpClientListener=listener;
        this.context=context;
        sharedPreferences=context.getSharedPreferences("config", context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        BaseUrl="http://"+sharedPreferences.getString("ip","")+":"+sharedPreferences.getString("port","")+"/";
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
                .url(BaseUrl+url)
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

    public void addmtc(String pk,String url,String type){
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        FormBody formBody = new FormBody.Builder()
                .add("username", sharedPreferences.getString("name",""))
                .add("elevator_pk", pk)
                .add("type",type).build();
        Request request = new Request.Builder()
                .url(BaseUrl+url)
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
        String enURL=BaseUrl+url;
        Request request = new Request.Builder()
                .url(BaseUrl+url)
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
                    if (jsonObject.getBoolean("mcompany")){
                        editor.putString("permission","mcompany");
                    }else{
                        editor.putString("permission","pcompany");
                    }
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

    public void postLogByphoto(String remark, String pk, String url, List<Bitmap> map) {
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        List<File> files=convertBitmapToFile(map);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (File file:files){
            builder.addFormDataPart("photo", "photo", RequestBody.create(MediaType.parse("image/jpg"), file));
        }
        builder .addFormDataPart("pk", pk);
        builder .addFormDataPart("log", remark);
        RequestBody requestBody=builder.build();
        Request request = new Request.Builder()
                .url(BaseUrl+url)
                .addHeader("cookie",cookie)
                .post(requestBody)
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
                    httpClientListener.onaddWithCommit("ADDLOG");
                }else {
                    httpClientListener.onError();
                }
            }
        });
    }

    public void postLogNophoto(String remark, String pk,String url) {
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;

        FormBody formBody = new FormBody.Builder()
                .add("pk", pk).add("log", remark).build();
        Request request = new Request.Builder()
                .url(BaseUrl+url)
                .addHeader("cookie",cookie)
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
                    httpClientListener.onaddWithCommit("ADDLOG");
                }else {
                    httpClientListener.onError();
                }
            }
        });
    }
    //提交工单
    public void commitMtc(String elevator_pk,String url) {
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;

        FormBody formBody = new FormBody.Builder()
                .add("pk", elevator_pk).build();
        Request request = new Request.Builder()
                .url(BaseUrl+url)
                .addHeader("cookie",cookie)
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
                    httpClientListener.onaddWithCommit("COMMITMTC");
                }else {
                    httpClientListener.onError();
                }
            }
        });
    }

    //获取维保人员工单列表
    public void getmtcsbymcompany(String url) {
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        OkHttpUtils
                .get()
                .url(BaseUrl+url)
                .addHeader("Cookie",cookie)
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
    //获取未完成工单
    public void getmtc(String pk, String url) {
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        String urlk=BaseUrl+url+"?pk="+pk;
        OkHttpUtils
                .get()
                .url(urlk)
                .addHeader("Cookie",cookie)
                .addParams("module", String.valueOf(Constants.DEFAULT_MODULE))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        httpClientListener.onError();
                    }

                    @Override
                    public void onResponse(String res, int id) {
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
    //审核工单
    public void confirmMtc(String url, String pk) {
        String SESSION_SERVER_HEAD =sharedPreferences.getString("csrftoken","") ;
        String SESSION_SERVER_ID = sharedPreferences.getString("sessionid","");
        String cookie="csrftoken=" + SESSION_SERVER_HEAD+";sessionid="+SESSION_SERVER_ID;
        FormBody formBody = new FormBody.Builder()
                .add("pk", pk).build();
        Request request = new Request.Builder()
                .url(BaseUrl+url)
                .addHeader("cookie",cookie)
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
                    httpClientListener.onaddWithCommit("COMMITMTC");
                }else {
                    httpClientListener.onError();
                }
            }
        });
    }
    private List<File> convertBitmapToFile(List<Bitmap> bitmaps) {
        List<File> files=new ArrayList<>();
        for (Bitmap bitmap:bitmaps){
            try {
                // create a file to write bitmap data
                File f = new File(context.getCacheDir(), "portrait");
                f.createNewFile();
                // convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                // write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                files.add(f);
            } catch (Exception e) {
            }
        }
        return files;
    }
    public interface HttpClientListener{
        void onError();
        void onSuccess(Object obj);
        void onaddWithCommit(String type);
    }
}
