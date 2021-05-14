## im-java-sdk
im for java，jdk1.8，okhttp3-3.0.0，gson 2.8.6

## 两种使用方式
 maven，看下文；
 非maven，可以将代码下载本地，编译，导入项目中

## 在项目pom文件中，引入依赖

```xml
<dependency>
   <groupId>sg.linkv</groupId>
   <artifactId>im-java-sdk</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
```
## 引入依赖对应的仓库地址

```xml
<repositories>
 <repository>
  <id>linkv-mvn-repo</id>
  <url>https://github.com/linkv-io/im-java-sdk/tree/main/repo</url>
 </repository>
</repositories>
```

## 代码示例
``` java
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
```
