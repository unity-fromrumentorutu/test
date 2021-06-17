package com.carcassonne.gameserver.controller;


import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.configuration.StateCodeConfig;
import com.carcassonne.gameserver.service.RoomService;
import com.carcassonne.gameserver.service.UserService;
import com.carcassonne.gameserver.util.JwtTokenUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Semaphore;

@RestController
@EnableAutoConfiguration
@CrossOrigin
@RequestMapping(value = "/playing", produces = "application/json; charset=utf-8")
public class Playingcontroller {

    @Autowired
    public UserService userService;

    @Autowired
    public RoomService roomService;

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping(value = "/getFrameInfo",method = RequestMethod.POST)
    public JSONObject getFrameInfo(HttpServletRequest request){
        synchronized(this){
            JSONObject result = new JSONObject();
            String token = null;
            try {
                token = request.getHeader("token");
                String accountNum = JwtTokenUtil.getUsername(token);
                Integer roomNum = userService.getWaitStartPlayerByAccountNum(accountNum).getInteger("inRoomNum");
                result = roomService.getFrameInfo(roomNum);
                result.put("code", 200);
                result.put("message", "OK, request successfully");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result.put("code", 500);
                result.put("message", StateCodeConfig.when_500_message("null"));
                logger.error("/playing/getFrameInfo api end with 500 , unknown error ! token :" + token);
                return result;
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/fanCard",method = RequestMethod.POST)
    public JSONObject fanCard(HttpServletRequest request,@RequestBody String JSONBody) {
        JSONObject result = new JSONObject();
        JSONObject requestBody = new JSONObject();
        String token = null;
        Integer putX = null;
        Integer putY = null;
        Integer rotation = null;
        Integer occupyBlock = null;
        String blockType = null;
        String accountNum = null;
        Integer roomNum =null;
        try {
            token = request.getHeader("token");
            accountNum = JwtTokenUtil.getUsername(token);
            requestBody = JSONObject.parseObject(JSONBody);
            putX = requestBody.getInteger("putX");
            putY = requestBody.getInteger("putY");
            rotation = requestBody.getInteger("rotation");
            occupyBlock = requestBody.getInteger("occupyBlock");
            roomNum = userService.getWaitStartPlayerByAccountNum(accountNum).getInteger("inRoomNum");
            if (requestBody.containsKey("blockType"))
                blockType = requestBody.getString("blockType");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 400);
            result.put("message", "Bad Request");
            logger.error("/playing/fanCard api end with 400 , Bad Request " + e.toString() + " JSONBody :" + JSONBody);
            return result;
        }

        try {
            if (roomService.fanCard(roomNum,accountNum,putX,putY,rotation) == false ) {
                result.put("code",200);
                result.put("message","OK, request successfully");
                return result;
            }else {
                result.put("code",500);
                result.put("message", StateCodeConfig.when_500_message(JSONBody));
                logger.error("/playing/fanCard api end with 500 ,fancard return true ! token :" + token);
                return result;
            }

        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message(JSONBody));
            logger.error("/playing/fanCard api end with 500 , unknown error ! token :" + token);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/occupy",method = RequestMethod.POST)
    public JSONObject occupy(HttpServletRequest request,@RequestBody String JSONBody) {
        JSONObject result = new JSONObject();
        JSONObject requestBody = new JSONObject();
        String token = null;
        String isOccupyBlock = null;
        Integer occupyX = null;
        Integer occupyY = null;
        String occupyEdge = null;
        Integer score = null;
        String blockType = null;
        String accountNum = null;
        Integer roomNum =null;
        try {
            token = request.getHeader("token");
            accountNum = JwtTokenUtil.getUsername(token);
            requestBody = JSONObject.parseObject(JSONBody);
            isOccupyBlock = requestBody.getString("isOccupyBlock");
            if(isOccupyBlock.equals("true")){
                occupyX = requestBody.getInteger("occupyX");
                occupyY = requestBody.getInteger("occupyY");
                occupyEdge = requestBody.getString("occupyEdge");
                score = requestBody.getInteger("score");

            }
            roomNum = userService.getWaitStartPlayerByAccountNum(accountNum).getInteger("inRoomNum");
            if (requestBody.containsKey("blockType"))
                blockType = requestBody.getString("blockType");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("code", 400);
            result.put("message", "Bad Request");
            logger.error("/playing/occupy api end with 400 , Bad Request " + e.toString() + " JSONBody :" + JSONBody);
            return result;
        }

        try {
            if (! roomService.occupy(roomNum,accountNum,isOccupyBlock, occupyX,occupyY,occupyEdge,score)) {
                result.put("code",200);
                result.put("message","OK, request successfully");
                return result;
            }else {
                result.put("code",500);
                result.put("message", StateCodeConfig.when_500_message(JSONBody));
                logger.info("occupy return false");
                return result;
            }

        }catch (Exception e){
            e.printStackTrace();
            result.put("code",500);
            result.put("message", StateCodeConfig.when_500_message(JSONBody));
            logger.error("/playing/occupy api end with 500 , unknown error ! token :" + token);
            return result;
        }
    }

}
