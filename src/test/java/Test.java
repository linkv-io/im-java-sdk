import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import linkv.io.api.Account;
import linkv.io.api.DataPush;
import linkv.io.api.bean.IMEntity;
import linkv.io.api.bean.SexEnum;
import linkv.io.api.tuple.Pair;
import linkv.io.api.tuple.Triplet;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Test {

    @org.junit.Test
    public void Test() {
        try {
            String secret = "";
            Base64.Decoder decoder = Base64.getDecoder();
            String text = new String(decoder.decode(secret.getBytes(StandardCharsets.UTF_8)));

            Gson gson = new GsonBuilder().create();
            IMEntity im = gson.fromJson(text, IMEntity.class);

            String thirdUID = "test-go-tob";
            String aID = "test";
            Triplet<String, String, String> result = new Account(im).GetTokenByThirdUID(thirdUID, aID, "test-go",
                    SexEnum.Unknown, "http://xxxxx/app/rank-list/static/img/defaultavatar.cd935fdb.png",
                    "", "", "");
            if (result.three != null && !result.three.trim().equals("")) {
                System.out.println(result.three);
            }

            System.out.println(String.format("token:%s,openID:%s", result.first, result.second));

            String toUID = "1100";
            String objectName = "RC:textMsg";
            String content = "测试单聊";

            DataPush dataPush = new DataPush(im);

            Pair<Boolean, String> res = dataPush.PushConverseData(thirdUID, toUID, objectName, content, "", "", "", "", "", "");
            if (!res.first) {
                System.out.println(String.format("PushConverseData exeute fail. %s", res.second));
            } else {
                System.out.println("PushConverseData exeute finish.");
            }

            content = "测试 事件";
            res = dataPush.PushEventData(thirdUID, toUID, objectName, content, "", "", "", "");
            if (!res.first) {
                System.out.println(String.format("PushEventData exeute fail. %s", res.second));
            } else {
                System.out.println("PushEventData exeute finish.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

    }
}
