package com.carcassonne.gameserver.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.bean.Card;
import com.carcassonne.gameserver.bean.Player;
import com.carcassonne.gameserver.bean.Room;
import com.carcassonne.gameserver.bean.User;
import com.carcassonne.gameserver.configuration.RedisConfig;
import com.carcassonne.gameserver.manager.MainGameManager;
import org.apache.ibatis.javassist.bytecode.stackmap.TypeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.carcassonne.gameserver.configuration.RedisConfig.ROOM_ALIVE_TIME;

@Service
public class RoomService {
    @Autowired
    private RedisTemplate redisTemplate;

    public Integer createRoom(Room room , ArrayList<Card> cardArrayList){
        ValueOperations<String,JSONObject>  operations= redisTemplate.opsForValue();
        Random random = new Random();
        Integer randomRoomNum = random.nextInt(899999)+100000;
        while (redisTemplate.hasKey(RedisConfig.ACTIVE_ROOM + randomRoomNum)){
            randomRoomNum = random.nextInt(899999) + 100000;
        }
        room.setNum(randomRoomNum);
        room.createCardLibrary(cardArrayList);
        MainGameManager.getInstance().createRoom(room);
        operations.set(RedisConfig.ACTIVE_ROOM + randomRoomNum , room.toJSONObject() , ROOM_ALIVE_TIME, TimeUnit.HOURS);
        return randomRoomNum;
    }

    public void userJoinRoom(Player player,Integer roomNum){
        MainGameManager.getInstance().addPlayer(player,roomNum);
    }

    public void userExitRoom(String accountNum,Integer roomNum){
        MainGameManager.getInstance().deletePlayer(accountNum,roomNum);
    }


    public JSONArray searchRoom(String roomNum){
        JSONArray array = MainGameManager.getInstance().searchRoom(roomNum);
        return array;
    }

    public String readyAndStartGame(String accountNum){
        if (redisTemplate.hasKey(RedisConfig.WAIT_START_USER_LIST + accountNum)) {
            ValueOperations<String, JSONObject> operations = redisTemplate.opsForValue();
            JSONObject playerJsonObject = operations.get(RedisConfig.WAIT_START_USER_LIST + accountNum);
            redisTemplate.delete(RedisConfig.WANDER_USER_LIST + accountNum);
            Integer roomNum = playerJsonObject.getInteger("inRoomNum");
            return MainGameManager.getInstance().readyAndStartGame(accountNum,roomNum);
        }
        return "error";
    }

    public JSONObject getRoomInfo(Integer roomNum){
        return MainGameManager.getInstance().getRoomInfo(roomNum);
    }

    public JSONObject getFrameInfo(Integer roomNum){
        return MainGameManager.getInstance().getFrameInfo(roomNum);
    }

    public Boolean fanCard(Integer roomNum,String accountNum,Integer putX,Integer putY,Integer rotation){
        return MainGameManager.getInstance().fanCard(roomNum,accountNum,putX,putY,rotation);
    }

    public Boolean occupy(Integer roomNum,String accountNum,String isOccupyBlock,Integer occupyX,Integer occupyY
            ,String occupyEdge,Integer score){
        return MainGameManager.getInstance().occupy(roomNum,accountNum,isOccupyBlock, occupyX,occupyY
                ,occupyEdge,score);
    }

    public void addMsg(Integer roomNum,String accountNum ,String content){
        MainGameManager.getInstance().addMsg(roomNum,accountNum,content);
    }

    public JSONArray getMsgListToJSONArray(Integer roomNum){
        return MainGameManager.getInstance().getMsgListToJSONArray(roomNum);
    }




}
