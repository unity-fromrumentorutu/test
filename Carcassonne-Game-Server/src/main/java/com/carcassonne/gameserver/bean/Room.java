package com.carcassonne.gameserver.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.manager.RoomManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 房间
 */
public class Room {
    public static String WAIT_START_STATE = "waitStartState";


    private Integer num;
    private String name;
    private String password;
    private String roomState;
    private RoomManager roomManager;
    private Chat chat;


    public Room(Integer num, String name, String password, String roomState) {
        this.num = num;
        this.name = name;
        this.password = password;
        this.roomState = roomState;
        this.roomManager = new RoomManager();
        this.chat = new Chat();
    }

    public JSONObject toJSONObject(){
        JSONObject room = new JSONObject();
        if(num != null) room.put("num",num);
        if(password != null) room.put("password",password);
        if(name != null) room.put("name",name);
        if(roomState != null) room.put("roomState",roomState);
        return room;
    }

    public void addMsg(String accountNum ,String content){
        chat.addMsg(accountNum,content);
    }

    public JSONArray getMsgListToJSONArray(){
        return chat.getMsgListToJSONObject();
    }

    public void createCardLibrary(ArrayList<Card> cardArrayList){
        ArrayList<Card> temp = new ArrayList<>();

        for (int i = 0 ; i < cardArrayList.size() ; i++){
            for ( int j = 0 ; j < cardArrayList.get(i).getCount() ; j++){
                temp.add(cardArrayList.get(i));
            }
        } System.out.println("@@@@@  牌库大小："+temp.size());
        Collections.shuffle(temp);
        Card[] cardLib = new Card[temp.size()];
        for (int i = 0 ; i < temp.size() ; i++){
            cardLib[i] = temp.get(i);
        }
        roomManager.setCardLibrary(cardLib);
    }

    @Override
    public String toString() {
        return "Room{" +
                "num=" + num +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", roomState='" + roomState ;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
