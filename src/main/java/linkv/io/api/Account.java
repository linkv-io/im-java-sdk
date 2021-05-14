package linkv.io.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import linkv.io.api.bean.IMEntity;
import linkv.io.api.bean.SexEnum;
import linkv.io.api.result.ResponseEntity;
import linkv.io.api.tuple.Triplet;
import linkv.io.api.utils.CommUtils;
import linkv.io.api.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;

public class Account {
    private IMEntity _im;

    public Account(IMEntity im) {
        _im = im;
    }

    public class RespStatusModel {
        @SerializedName("status")
        public int Status;
        @SerializedName("msg")
        public String Msg;
        @SerializedName("data")
        public RespDataModel Data;
    }

    public class RespDataModel {
        @SerializedName("token")
        public String Token;
        @SerializedName("openId")
        public String OpenID;
        @SerializedName("im_token")
        public String IMToken;
    }

    public Triplet<String, String, String> GetTokenByThirdUID(String thirdUID, String aID, String userName, SexEnum sex,
                                                              String portraitURI, String userEmail, String countryCode, String birthday) {
        if (thirdUID == null
                || thirdUID.trim().equals("")
                || aID == null
                || aID.trim().equals("")) {
            return new Triplet<>("", "", "param error");
        }

        HashMap<String, String> params = new HashMap<>();
        String nonce = CommUtils.GenRandomString();
        params.put("nonce_str", nonce);
        params.put("app_id", _im.appKey);

        params.put("userId", thirdUID);
        params.put("aid", aID);

        addKV(params, userName, "name");
        addKV(params, portraitURI, "portraitUri");
        addKV(params, userEmail, "email");
        addKV(params, countryCode, "countryCode");
        addKV(params, birthday, "birthday");

        if (sex != SexEnum.Unknown) {
            if (sex == SexEnum.Male) {
                params.put("sex", "1");
            }

            if (sex == SexEnum.Female) {
                params.put("sex", "0");
            }
        }

        params.put("sign", CommUtils.GenSignature(params, _im.appSecret));

        String uri = "http://thr.linkv.sg/open/v0/thGetToken";

        String err = "";
        ResponseEntity result = null;

        for (int i = 0; i < 3; i++) {
            try {
                result = new HttpUtils().doPost(uri, params, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (result.respCode != 200) {
                return new Triplet<>("", "", result.resp.message());
            }

            Gson gson = new GsonBuilder().create();

            RespStatusModel model = gson.fromJson(new String(result.bytes), RespStatusModel.class);

            if (model.Status == 200) {
                return new Triplet<>(model.Data.IMToken, model.Data.OpenID, null);
            }

            if (model.Status == 500) {
                err = "message(" + model.Msg + ")";
                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            return new Triplet<>("", "", "message(" + model.Msg + ")");
        }
        return new Triplet<>("", "", err);
    }

    private void addKV(HashMap<String, String> params, String val, String key) {
        if (val != null && !val.trim().equals("")) {
            params.put(key, val);
        }
    }

}
