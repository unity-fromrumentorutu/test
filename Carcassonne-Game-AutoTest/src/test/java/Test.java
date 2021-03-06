import HTTPUtil.HTTPUtil;
import com.alibaba.fastjson.JSONObject;
import entity.PutPoint;

import java.io.IOException;
import java.util.ArrayList;

public class Test {


    @org.junit.Test
    public void chatTest() throws InterruptedException, IOException {
        String NULL = "null";
        String TOKEN = "token";
        JSONObject p1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876025@qq.com\"}",NULL,NULL)) ;
        System.out.println(p1);;
        String accountNum_1 = p1.getJSONObject("userInfo").getString("accountNum");
        JSONObject p2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876026@qq.com\"}",NULL,NULL)) ;
        System.out.println(p2);
        String accountNum_2 = p2.getJSONObject("userInfo").getString("accountNum");
        String token1 = p1.getString("token");
        String token2 = p2.getString("token");
        JSONObject userCreateRoom1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_userCreateRoom,"{\"roomName\":\"myRoom\",\"roomPassword\":\"111\"}",TOKEN,token1)) ;
//        System.out.println(userCreateRoom1);
        String roomNum = userCreateRoom1.getString("roomNum"); System.out.println(roomNum);

        JSONObject userJoinRoom = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_userJoinRoom,"{\"addMode\":\"select\",\"roomNum\":\""+roomNum+"\"}",TOKEN,token2)) ;
        System.out.println(userJoinRoom);
        Thread.sleep(200);
        JSONObject readyAndStartGame1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.waitStart_readyAndStartGame,NULL,TOKEN,token1)) ;
//        System.out.println(readyAndStartGame1);
        Thread.sleep(200);
        JSONObject readyAndStartGame2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.waitStart_readyAndStartGame,NULL,TOKEN,token2)) ;
//        System.out.println(readyAndStartGame2);
        Thread.sleep(200);


        for (int i=0 ;i<46;i++){
            JSONObject getFrameInfo1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_getFrameInfo,NULL,TOKEN,token1)) ;
            ArrayList<PutPoint> putPointList = HTTPUtil.formatFrameToGetPutPoint(getFrameInfo1);
            if(getFrameInfo1.getString("roundPlayerAccountNum").equals(accountNum_1)){
                String request= "{\"putX\":\""+putPointList.get(0).getX()+"\",\"putY\":\""+putPointList.get(0).getY()+"\",\"rotation\":\""+putPointList.get(0).getRotation()+"\"}" ;
                JSONObject fanCard1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_fanCard,request,TOKEN,token1)) ;
                request = "{\"isOccupyBlock\":\"true\",\"occupyX\":15,\"occupyY\":16,\"occupyEdge\":\"top\",\"score\":2}";
                JSONObject occupy1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_occupy,request,TOKEN,token1)) ;
            }
            else if (getFrameInfo1.getString("roundPlayerAccountNum").equals(accountNum_2)){
                String request= "{\"putX\":\""+putPointList.get(0).getX()+"\",\"putY\":\""+putPointList.get(0).getY()+"\",\"rotation\":\""+putPointList.get(0).getRotation()+"\"}" ;
                JSONObject fanCard1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_fanCard,request,TOKEN,token2)) ;
                request = "{\"isOccupyBlock\":\"true\",\"occupyX\":16,\"occupyY\":17,\"occupyEdge\":\"lef\",\"score\":4}";
                JSONObject occupy1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_occupy,request,TOKEN,token2)) ;
            }
        }






//
//        String context1 = "?????????????????????";
//        JSONObject sendChatInfo = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_sendChatInfo,"{\"type\":\"room\",\"context\":\"" + context1 + "\"}",TOKEN,token1));
//
//        JSONObject getChatInfo =  JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_getChatInfo,NULL,TOKEN,token2)) ;
//
//        String context2 = "?????????2?????????";
//        sendChatInfo = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_sendChatInfo,"{\"type\":\"room\",\"context\":\"" + context2 + "\"}",TOKEN,token1));
//        getChatInfo =  JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_getChatInfo,NULL,TOKEN,token2)) ;
//

    }

    @org.junit.Test
    public void getOnce(){

    }

    @org.junit.Test
    public void roomTest() throws IOException, InterruptedException {
        String NULL = "null";
        String TOKEN = "token";
        JSONObject p1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876025@qq.com\"}",NULL,NULL)) ;
        System.out.println(p1);;
        String accountNum_1 = p1.getJSONObject("userInfo").getString("accountNum");
        JSONObject p2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876026@qq.com\"}",NULL,NULL)) ;
        System.out.println(p2);
        String accountNum_2 = p2.getJSONObject("userInfo").getString("accountNum");
        String token1 = p1.getString("token");
        String token2 = p2.getString("token");
        JSONObject userCreateRoom1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_userCreateRoom,"{\"roomName\":\"myRoom\",\"roomPassword\":\"111\"}",TOKEN,token1)) ;
//        System.out.println(userCreateRoom1);
        String roomNum = userCreateRoom1.getString("roomNum"); System.out.println(roomNum);
        JSONObject search = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_searchRoom,"{\"roomNum\":\"null\"}",TOKEN,token1)) ;
        JSONObject userJoinRoom = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_userJoinRoom,"{\"addMode\":\"select\",\"roomNum\":\""+roomNum+"\"}",TOKEN,token2)) ;
         search = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_searchRoom,"{\"roomNum\":\"null\"}",TOKEN,token1)) ;
        JSONObject userExitRoom = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.waitStart_userExitRoom,"{\"roomNum\":\""+roomNum+"\"}",TOKEN,token2)) ;
        search = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_searchRoom,"{\"roomNum\":\"null\"}",TOKEN,token1)) ;
        userExitRoom = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.waitStart_userExitRoom,"{\"roomNum\":\""+roomNum+"\"}",TOKEN,token1)) ;
        search = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_searchRoom,"{\"roomNum\":\"null\"}",TOKEN,token1)) ;


    }
}
