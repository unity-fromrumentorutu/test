package com.carcassonne.gameserver.controller;


import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.bean.*;
import com.carcassonne.gameserver.configuration.StateCodeConfig;
import com.carcassonne.gameserver.service.CardService;
import com.carcassonne.gameserver.service.EdgeService;
import com.carcassonne.gameserver.service.RoomService;
import com.carcassonne.gameserver.service.UserService;
import com.carcassonne.gameserver.util.JwtTokenUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@EnableAutoConfiguration
@CrossOrigin
@RequestMapping(value = "/wander", produces = "application/json; charset=utf-8")
public class WanderController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    public EdgeService edgeService;

    @Autowired
    public CardService cardService;

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public JSONObject getUserInfo(HttpServletRequest request){
        JSONObject result = new JSONObject();
        String token = null;
        try {
            token = request.getHeader("token");
            String accountNum = JwtTokenUtil.getUsername(token);
            User user = null;
            user = userService.getWonderUserByAccountNum(accountNum);
            user.setPassword("null");
            result.put("code",200);
            result.put("message","OK, request successfully");
            result.put("userInfo",user.toJSONObject());
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message("null"));
            logger.error("/wander/getUserInfo api end with 500 , unknown error ! token :" + token);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public JSONObject updateUserInfo(HttpServletRequest request,@RequestBody String JSONBody){
        JSONObject result = new JSONObject();
        JSONObject requestBody = null;
        String token = null;
        User user = null;
        try {
            token = request.getHeader("token");
            String accountNum = JwtTokenUtil.getUsername(token);
            user = userService.getWonderUserByAccountNum(accountNum);
            user.setPassword("null");

            requestBody = JSONObject.parseObject(JSONBody);
            if(requestBody.containsKey("nickname")){
                user.setNickname((String) requestBody.get("nickname"));
            }
            if(requestBody.containsKey("sex")){
                user.setSex((String) requestBody.get("sex"));
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("request : "+JSONBody + " to [/wander/getUserInfo controller] with error Info ==> "+e.toString());
            result.put("code",400);
            result.put("message","Bad Request");
            logger.info("/wander/updateUserInfo api end with 400 , Bad Request ");
            return result;
        }

        try {
            userService.updateUser(user);
            userService.insertWonderUser(user);
            result.put("code",200);
            result.put("message","OK, Information update successfully");
            result.put("userInfo",user.toJSONObject());
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message",StateCodeConfig.when_500_message("token"+token));
            logger.error("/wander/updateUserInfo api end with 500 , unknown error ! "+ e.toString() +" ==> token :" + token);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/userExit",method = RequestMethod.POST)
    public JSONObject userExit(HttpServletRequest request){
        JSONObject result = new JSONObject();
        String token = null;
        try {
            token = request.getHeader("token");
            String accountNum = JwtTokenUtil.getUsername(token);
            userService.deleteWanderUserByAccountNum(accountNum);
            result.put("code",200);
            result.put("message","OK, User{accountNum:" + accountNum + "} exit successfully");
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message("null"));
            logger.error("/wander/userExit api end with 500 , unknown error ! "+e.toString()+"token :" + token);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/userCreateRoom",method = RequestMethod.POST)
    public JSONObject userCreateRoom(HttpServletRequest request,@RequestBody String JSONBody){
        JSONObject result = new JSONObject();
        JSONObject requestBody = new JSONObject();
        String token = null;
        String accountNum = null;
        String roomName = null;
        String roomPassword =null;
        try {
            token = request.getHeader("token");
            accountNum = JwtTokenUtil.getUsername(token);
            requestBody = JSONObject.parseObject(JSONBody);
            roomName = requestBody.getString("roomName");
            if (requestBody.get("roomPassword").toString().equals("null")) roomPassword = null;
            else roomPassword =requestBody.getString("roomPassword");
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",400);
            result.put("message", "Bad Request");
            logger.error("/wander/userCreateRoom api end with 400 , Bad Request "+e.toString()+" JSONBody :" + JSONBody );
            return result;
        }

        try {
            User user = userService.getWonderUserByAccountNum(accountNum);
            userService.deleteWanderUserByAccountNum(accountNum);

            //牌库初始化
            ArrayList<Card> cardArrayList = new ArrayList<>(cardService.selectAllCard());
            ArrayList<Edge> edgeArrayList = new ArrayList<>(edgeService.selectAllEdge());
            for (int i = 0 ; i < cardArrayList.size() ; i++){
                cardArrayList.get(i).formatPictureURL();
                for (int j = 0 ; j < edgeArrayList.size() ; j++){
                    if(Integer.parseInt(cardArrayList.get(i).getTopEdgeId() ) == edgeArrayList.get(j).getId()){
                        cardArrayList.get(i).setTop(edgeArrayList.get(j));
                    }
                    if(Integer.parseInt(cardArrayList.get(i).getRigEdgeId() ) == edgeArrayList.get(j).getId()){
                        cardArrayList.get(i).setRig(edgeArrayList.get(j));
                    }
                    if(Integer.parseInt(cardArrayList.get(i).getBotEdgeId() ) == edgeArrayList.get(j).getId()){
                        cardArrayList.get(i).setBot(edgeArrayList.get(j));
                    }
                    if(Integer.parseInt(cardArrayList.get(i).getLefEdgeId()) == edgeArrayList.get(j).getId()){
                        cardArrayList.get(i).setLef(edgeArrayList.get(j));
                    }
                }
            }

            logger.info("userCreateRoom控制器：牌库初始完毕");

            Integer roomNum = roomService.createRoom(new Room(null, roomName , roomPassword , Room.WAIT_START_STATE) , cardArrayList);
            Player player = new Player(false,null,null,false,"playing",roomNum,user);
            roomService.userJoinRoom(player,roomNum);
            userService.insertWaitStartPlayer(player);
            result.put("code",200);
            result.put("message","OK, Create room successfully");
            result.put("state","waitStart");
            result.put("roomNum",roomNum.toString());
            result.put("roomName",roomName);
            result.put("roomPassword",roomPassword == null ? "null" : roomPassword);
            result.put("playerRole","master");
            logger.info(accountNum +"create room , info :" + result.toJSONString());
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message(JSONBody));
            logger.error("/wander/userExit api end with 500 , unknown error ! "+e.toString()+" ==> JSONBody:" + JSONBody);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/userJoinRoom",method = RequestMethod.POST)
    public JSONObject userJoinRoom(HttpServletRequest request,@RequestBody String JSONBody){
        JSONObject result = new JSONObject();
        JSONObject requestBody = new JSONObject();
        String token = null;
        String accountNum = null;
        Integer roomNum= null;
        try {
            token = request.getHeader("token");
            accountNum = JwtTokenUtil.getUsername(token);
            requestBody = JSONObject.parseObject(JSONBody);
            roomNum = Integer.parseInt(requestBody.getString("roomNum"));
            //TODO 目前先做select模式的
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",400);
            result.put("message", "Bad Request");
            logger.error("/wander/userJoinRoom api end with 400 , Bad Request "+e.toString()+" JSONBody :" + JSONBody );
            return result;
        }

        try {
            User user = userService.getWonderUserByAccountNum(accountNum);
            userService.deleteWanderUserByAccountNum(accountNum);
            Player player = new Player(false,null,null,false,"playing",roomNum,user);
            roomService.userJoinRoom(player,roomNum);
            userService.insertWaitStartPlayer(player);
            result.put("code",200);
            result.put("message","OK, "+accountNum +"Join  room successfully");
            result.put("state","waitStart");
            result.put("playerRole","member");
            logger.info(accountNum +"Join  room successfully , info :" + result.toJSONString());
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message(JSONBody));
            logger.error("/wander/userJoinRoom api end with 500 , unknown error ! "+e.toString()+" ==> JSONBody:" + JSONBody);
            return result;
        }
    }


    @ResponseBody
    @RequestMapping(value = "/searchRoom",method = RequestMethod.POST)
    public JSONObject searchRoom(HttpServletRequest request,@RequestBody String JSONBody){
        JSONObject result = new JSONObject();
        JSONObject requestBody = new JSONObject();
        String token = null;
        String accountNum = null;
        Integer roomNum =null;
        JSONArray roomList = null;
        try {
            token = request.getHeader("token");
            accountNum = JwtTokenUtil.getUsername(token);
            requestBody = JSONObject.parseObject(JSONBody);
           if(!requestBody.getString("roomNum").equals("null")) roomNum = Integer.parseInt(requestBody.getString("roomNum"));
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",400);
            result.put("message", "Bad Request");
            logger.error("/wander/searchRoom api end with 400 , Bad Request "+e.toString()+" JSONBody :" + JSONBody );
            return result;
        }

        try {
            roomList = roomService.searchRoom(requestBody.getString("roomNum"));
            result.put("code",200);
            result.put("message","OK, Room search successfully");
            result.put("roomList",roomList);
            logger.info(accountNum +" Room search successfully :" + result.toJSONString());
            return  result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message(JSONBody));
            logger.error("/wander/userJoinRoom api end with 500 , unknown error ! "+e.toString()+" ==> JSONBody:" + JSONBody);
            return result;
        }
    }

}
