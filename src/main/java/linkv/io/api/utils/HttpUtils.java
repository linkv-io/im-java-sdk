package linkv.io.api.utils;

import linkv.io.api.result.ResponseEntity;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtils {
    private String ErrServerError = "http code not 200";
    private int readTimeout = 30;// 30 秒
    private int cliPoolSize = 10;

    private String version;

    public void SetVersion(String ver) {
        this.version = ver;
    }

    public String GetVersion() {
        return this.version;
    }

    public ResponseEntity doGet(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        HttpUrl.Builder urlBulider = HttpUrl.parse(url).newBuilder();

        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                urlBulider.addQueryParameter(param.getKey(), param.getValue());
            }
        }

        Request.Builder reqBuilder = new Request.Builder()
                .url(urlBulider.build().toString())
                .get();

        reqBuilder.addHeader("User-Agent", "Golang SDK v" + version);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                reqBuilder.addHeader(header.getKey(), header.getValue());
            }
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        try {
            Response response = client.newCall(reqBuilder.build()).execute();
            ResponseBody respBody = response.body();

            ResponseEntity result = new ResponseEntity();
            if (response.isSuccessful()) {
                result.bytes = respBody.bytes();
                result.resp = response;
                result.respCode = response.code();

                return result;
            }

            result.bytes = respBody == null ? null : respBody.bytes();
            result.resp = response;
            result.respCode = response.code();

            return result;
        } catch (IOException e) {
            return null;
        }
    }

    public ResponseEntity doPost(String url, HashMap<String, String> params, HashMap<String, String> headers) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                formBodyBuilder.add(param.getKey(), param.getValue());
            }
        }
        RequestBody formBody = formBodyBuilder.build();

        Request.Builder reqBuilder = new Request.Builder()
                .url(url)
                .post(formBody);

        reqBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded");
        reqBuilder.addHeader("User-Agent", "Golang SDK v" + version);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                reqBuilder.addHeader(header.getKey(), header.getValue());
            }
        }

        Response resp = okHttpClient.newCall(reqBuilder.build()).execute();
        ResponseBody respBody = resp.body();

        ResponseEntity result = new ResponseEntity();
        if (resp.isSuccessful()) {
            result.bytes = respBody.bytes();
            result.resp = resp;
            result.respCode = resp.code();

            return result;
        }

        result.bytes = respBody == null ? null : respBody.bytes();
        result.resp = resp;
        result.respCode = resp.code();

        return result;
    }
}
