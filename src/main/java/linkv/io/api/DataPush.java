package linkv.io.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import linkv.io.api.bean.IMEntity;
import linkv.io.api.result.ResponseEntity;
import linkv.io.api.tuple.Pair;
import linkv.io.api.utils.CheckSumAlgoType;
import linkv.io.api.utils.CommUtils;
import linkv.io.api.utils.HttpUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class DataPush {
    private IMEntity _im;

    public DataPush(IMEntity im) {
        _im = im;
    }

    public class DataModel {
        @SerializedName("code")
        public int Code;
    }

    public Pair<Boolean, String> PushConverseData(String fromUID, String toUID, String objectName, String content, String pushContent,
                                                  String pushData, String deviceID, String toAppID, String toUserExtSysUserID, String isCheckSensitiveWords) {
        String nonce = CommUtils.GenGUID();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000L);

        String[] arr = new String[]{nonce, timestamp, _im.imAppSecret};
        Arrays.sort(arr);
        String cmimToken = CommUtils.genCheckSum(String.join("", arr), CheckSumAlgoType.MD5.getName());
        String sign = CommUtils.genCheckSum(String.join("|", _im.imAppID, _im.imAppKey, timestamp, nonce), CheckSumAlgoType.SHA_1.getName(), false);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("nonce", nonce);
        headers.put("timestamp", timestamp);
        headers.put("cmimToken", cmimToken);
        headers.put("sign", sign);
        headers.put("appkey", _im.imAppKey);
        headers.put("appId", _im.imAppID);
        headers.put("appUid", fromUID);

        HashMap<String, String> params = new HashMap<>();
        params.put("fromUserId", fromUID);
        params.put("toUserId", toUID);
        params.put("objectName", objectName);
        params.put("content", content);
        params.put("appId", _im.imAppID);

        addKV(params, pushContent, "pushContent");
        addKV(params, pushData, "pushData");
        addKV(params, deviceID, "deviceId");
        addKV(params, toAppID, "toUserAppid");
        addKV(params, toUserExtSysUserID, "toUserExtSysUserId");
        addKV(params, isCheckSensitiveWords, "isCheckSensitiveWords");

        String uri = _im.imHost + "/api/rest/message/converse/pushConverseData";
        String err = "";

        for (int i = 0; i < 3; i++) {
            ResponseEntity result = null;
            try {
                result = new HttpUtils().doPost(uri, params, headers);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result == null || result.respCode != 200) {
                return new Pair<>(false, String.format("httpStatusCode(%s) != 200", result.respCode));
            }

            Gson gson = new GsonBuilder().create();
            DataModel model = gson.fromJson(new String(result.bytes), DataModel.class);

            if (model.Code == 200) {
                return new Pair<>(true, null);
            }

            if (model.Code == 500) {
                err = String.format("message(%s)", model.Code);

                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            return new Pair<>(false, String.format("code not 200(%s)", model.Code));
        }

        return new Pair<>(false, err);
    }

    public Pair<Boolean, String> PushEventData(String fromUID, String toUID, String objectName, String content, String pushData,
                                               String toAppID, String toUserExtSysUserID, String isCheckSensitiveWords) {
        String nonce = CommUtils.GenGUID();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000L);

        String[] arr = new String[]{nonce, timestamp, _im.imAppSecret};
        Arrays.sort(arr);
        String cmimToken = CommUtils.genCheckSum(String.join("", arr), CheckSumAlgoType.MD5.getName());
        String sign = CommUtils.genCheckSum(String.join("|", _im.imAppID, _im.imAppKey, timestamp, nonce), CheckSumAlgoType.SHA_1.getName(), false);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("nonce", nonce);
        headers.put("timestamp", timestamp);
        headers.put("cmimToken", cmimToken);
        headers.put("sign", sign);
        headers.put("appkey", _im.imAppKey);
        headers.put("appId", _im.imAppID);
        headers.put("appUid", fromUID);

        HashMap<String, String> params = new HashMap<>();
        params.put("fromUserId", fromUID);
        params.put("toUserId", toUID);
        params.put("objectName", objectName);
        params.put("content", content);
        params.put("appId", _im.imAppID);

        addKV(params, pushData, "pushData");
        addKV(params, toAppID, "toUserAppid");
        addKV(params, toUserExtSysUserID, "toUserExtSysUserId");
        addKV(params, isCheckSensitiveWords, "isCheckSensitiveWords");

        String uri = _im.imHost + "/api/rest/sendEventMsg";
        String err = "";

        for (int i = 0; i < 3; i++) {
            ResponseEntity result = null;
            try {
                result = new HttpUtils().doPost(uri, params, headers);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result == null || result.respCode != 200) {
                return new Pair<>(false, String.format("httpStatusCode(%s) != 200", result.respCode));
            }

            Gson gson = new GsonBuilder().create();
            DataModel model = gson.fromJson(new String(result.bytes), DataModel.class);

            if (model.Code == 200) {
                return new Pair<>(true, null);
            }

            if (model.Code == 500) {
                err = String.format("message(%s)", model.Code);

                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            return new Pair<>(false, String.format("code not 200(%s)", model.Code));
        }

        return new Pair<>(false, err);
    }

    private void addKV(HashMap<String, String> params, String val, String key) {
        if (val != null && !val.trim().equals("")) {
            params.put(key, val);
        }
    }

}
