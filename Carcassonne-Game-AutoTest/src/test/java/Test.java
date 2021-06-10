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
        JSONObject p2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876026@qq.com\"}",NULL,NULL)) ;
        System.out.println(p2);
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
        JSONObject getFrameInfo1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_getFrameInfo,NULL,TOKEN,token1)) ;
//        System.out.println(getFrameInfo1);
        Thread.sleep(200);


<<<<<<< HEAD
        ArrayList<PutPoint> putPointList = HTTPUtil.formatFrameToGetPutPoint(getFrameInfo1);

        String request= "{\"putX\":\""+putPointList.get(0).getX()+"\",\"putY\":\""+putPointList.get(0).getY()+"\",\"rotation\":\""+putPointList.get(0).getRotation()+"\",\"occupyBlock\":\"999\"}" ;
        JSONObject fanCard1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_fanCard,request,TOKEN,token1)) ;




=======




        
>>>>>>> 979d6937fb082179163ad4de86c9ee076b4895be
//        String context1 = "这是第一条消息";
//        JSONObject sendChatInfo = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_sendChatInfo,"{\"type\":\"room\",\"context\":\"" + context1 + "\"}",TOKEN,token1));
//
//        JSONObject getChatInfo =  JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_getChatInfo,NULL,TOKEN,token2)) ;
//
//        String context2 = "这是第2条消息";
//        sendChatInfo = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_sendChatInfo,"{\"type\":\"room\",\"context\":\"" + context2 + "\"}",TOKEN,token1));
//        getChatInfo =  JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.common_getChatInfo,NULL,TOKEN,token2)) ;


    }

    @org.junit.Test
    public void getOnce(){

    }
}
