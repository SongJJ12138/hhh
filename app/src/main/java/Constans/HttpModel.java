package Constans;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;

import java.util.HashMap;
import java.util.Map;

public class HttpModel {
    private HttpClientListener httpClientListener;
    public HttpModel(HttpClientListener listener) {
        this.httpClientListener=listener;
    }

    /**
     * 网络请求站点数据
     * @param json
     */
    public void get(String json,String url){
        Map<String, String> params = new HashMap<>();
        params.put("json", json);
        OkHttpUtils
                .get()
                .url(Constants.HOST + url)
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


    public interface HttpClientListener{
        void onError();
        void onSuccess(String obj);
    }
}
