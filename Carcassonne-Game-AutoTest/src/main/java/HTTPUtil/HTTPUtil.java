package HTTPUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.PutPoint;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HTTPUtil {

    public static String BASE_ADDRESS = "http://localhost:90";
    public static String offline_userLogin = "/offline/userLogin";
    public static String wander_userCreateRoom = "/wander/userCreateRoom";
    public static String wander_userJoinRoom = "/wander/userJoinRoom";
    public static String waitStart_readyAndStartGame = "/waitStart/readyAndStartGame";
    public static String playing_getFrameInfo = "/playing/getFrameInfo";
    public static String playing_fanCard = "/playing/fanCard";
    public static String common_getChatInfo = "/common/getChatInfo";
    public static String common_sendChatInfo = "/common/sendChatInfo";

    public static final MediaType JSON  = MediaType.get("application/json; charset=utf-8");
    public static String result = null;
    public static String post(final String url, final String json,final String headName,final String headKey) throws IOException, InterruptedException {

//                System.out.println("===> 请求发送：URL ---->" +url+"   json ---> " + json);
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .header(headName,headKey)
                        .post(body)
                        .build();
                try {
                    try (Response response = client.newCall(request).execute()) {
                        try {
                            result = response.body().string();
                            System.out.println(" ~~~ > 接收："+result); System.out.println();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


        int turn = 0;
        while ( turn < 20 ){
            Thread.sleep(500);
            if(result != null){
                return result;
            }

            else turn++;
        }
        System.out.println("网络异常~");

        return "网络请求异常(或超时)";
    }

    public static ArrayList<PutPoint> formatFrameToGetPutPoint(JSONObject response){
        JSONObject roundPlayerOpInfo = response.getJSONObject("roundPlayerOpInfo");
        JSONArray ps = roundPlayerOpInfo.getJSONArray("roundPlayerCanPutPosition");
        ArrayList<PutPoint> putPointList = new ArrayList<>();
        for (int i = 0 ; i<ps.size();i++){
            JSONObject temp = ps.getJSONObject(i);
            putPointList.add(new PutPoint((Integer) temp.get("roundPlayerCanPutPositionX"),(Integer) temp.get("roundPlayerCanPutPositionY"),(Integer) temp.get("roundPlayerCanPutPositionRotation")));
        }
        Collections.shuffle(putPointList);
        return putPointList;
    }


}
