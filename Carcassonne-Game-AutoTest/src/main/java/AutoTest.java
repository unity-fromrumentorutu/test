import HTTPUtil.HTTPUtil;
import com.alibaba.fastjson.JSONObject;
import entity.PutPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoTest {


    public static void main(String args[]) throws IOException, InterruptedException {
        String NULL = "null";
        String TOKEN = "token";
        JSONObject p1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876025@qq.com\"}",NULL,NULL)) ;
        System.out.println(p1);;

        JSONObject p2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.offline_userLogin,"{\"password\":\"111\",\"accountNum\":\"1072876026@qq.com\"}",NULL,NULL)) ;
        System.out.println(p2);

        String token1 = p1.getString("token");
        String token2 = p2.getString("token");

        JSONObject userCreateRoom1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_userCreateRoom,"{\"roomName\":\"myRoom\",\"roomPassword\":\"111\"}",TOKEN,token1)) ;

        String roomNum = userCreateRoom1.getString("roomNum"); System.out.println(roomNum);

        JSONObject userJoinRoom = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.wander_userJoinRoom,"{\"addMode\":\"select\",\"roomNum\":\""+roomNum+"\"}",TOKEN,token2)) ;

        JSONObject readyAndStartGame1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.waitStart_readyAndStartGame,NULL,TOKEN,token1)) ;

        JSONObject readyAndStartGame2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.waitStart_readyAndStartGame,NULL,TOKEN,token2)) ;

        JSONObject getFrameInfo1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_getFrameInfo,NULL,TOKEN,token1)) ;

        ArrayList<PutPoint> putPointList = HTTPUtil.formatFrameToGetPutPoint(getFrameInfo1);

        String request= "{\"putX\":\""+putPointList.get(0).getX()+"\",\"putY\":\""+putPointList.get(0).getY()+"\",\"rotation\":\""+putPointList.get(0).getRotation()+"\",\"occupyBlock\":\"999\"}" ;
        JSONObject fanCard1 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_fanCard,request,TOKEN,token1)) ;
        JSONObject fanCard2 = JSONObject.parseObject(HTTPUtil.post(HTTPUtil.BASE_ADDRESS + HTTPUtil.playing_fanCard,request,TOKEN,token2)) ;


    }
}
