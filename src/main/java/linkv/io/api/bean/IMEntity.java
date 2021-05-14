package linkv.io.api.bean;

import com.google.gson.annotations.SerializedName;

public class IMEntity {
    @SerializedName("app_key")
    public String appKey;
    @SerializedName("app_secret")
    public String appSecret;
    @SerializedName("im_app_id")
    public String imAppID;
    @SerializedName("im_app_key")
    public String imAppKey;
    @SerializedName("im_app_secret")
    public String imAppSecret;
    @SerializedName("im_host")
    public String imHost;
}
