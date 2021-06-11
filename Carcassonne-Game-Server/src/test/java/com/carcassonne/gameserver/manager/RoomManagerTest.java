package com.carcassonne.gameserver.manager;

import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.bean.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RoomManagerTest {

    @Test
    void canPutCard() {
        Card[][] cards = new Card[31][31];
        Card or = new Card();
        or.setBot(new Edge(1,"City",null));
        or.setLef(new Edge(2,"Road",null));
        or.setRig(new Edge(3,"Road","left"));
        or.setTop(new Edge(4,"Glass",null));
        cards[15][15]=or;
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        Card nc = new Card();
        nc.setTop(new Edge(4,"Glass",null));
        nc.setRig(new Edge(3,"Road","left"));
        nc.setBot(new Edge(1,"City",null));
        nc.setLef(new Edge(2,"Road",null));
        assertEquals(true,roomManager.canPutCard(14,15,nc));
        assertEquals(true,roomManager.canPutCard(16,15,nc));
        assertEquals(false,roomManager.canPutCard(15,16,nc));
        assertEquals(false,roomManager.canPutCard(15,14,nc));
        assertEquals(false,roomManager.canPutCard(16,16,nc));
    }
    @Test
    void getCanPutPositionList(){
        Card[][] cards = new Card[31][31];
        Card or = new Card();
        or.setBot(new Edge(1,"City",null));
        or.setLef(new Edge(2,"Road",null));
        or.setRig(new Edge(3,"Road","left"));
        or.setTop(new Edge(4,"Glass",null));
        cards[15][15]=or;
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表

        Card nc = new Card();
        nc.setBot(new Edge(1,"City",null));
        nc.setLef(new Edge(2,"Road",null));
        nc.setRig(new Edge(3,"Road","left"));
        nc.setTop(new Edge(4,"Glass",null));
        ArrayList<Point> canPutPositionList = roomManager.getCanPutPositionList(nc);
        System.out.println(canPutPositionList.toString());
    }
    @Test
    void atest(){
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(3);
        a.add(2);
        for(int i:a){
            System.out.println(i);
        }
        a.remove(1);
        for(int i:a){
            System.out.println(i);
        }
    }
    @Test
    void suibian(){
        String s = "{\"name\":\"cmy\"}";

        JSONObject jsonObject=JSONObject.parseObject(s);
        System.out.println(jsonObject.get("name"));
    }
    @Test
    void qiuqiunidongqilai(){

        Card[][] cards = new Card[31][31];

        Card or = new Card();
        or.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setBot(new Edge(4,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表

        roomManager.putCard(15,15,or);
        HashMap<Integer, Block> unappropriatedCityBlock = roomManager.getUnappropriatedCityBlock();

//        System.out.println("**********");
//        System.out.println(unappropriatedCityBlock);//看了一下key是0
//        System.out.println("**********");

        System.out.println();

        roomManager.appropriated(0,"murasame","city");

//      开始放牌

        Card nc1 = new Card();
        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setBot(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"true\",\"lef\":\"false\"}"));
        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"true\",\"lef\":\"false\"}"));
        nc1.setLef(new Edge(2,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.rotate(0);
        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标1《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc1));
        roomManager.putCard(14,15,nc1);
        System.out.println();

        Card nc2 = new Card();
        nc2.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setBot(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.rotate(1);
        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标2《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc2));
        roomManager.putCard(16,15,nc2);
        System.out.println();

        Card nc3 = new Card();
        nc3.setTop(new Edge(1,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc3.setLef(new Edge(2,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc3.setRig(new Edge(3,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc3.setBot(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc3.rotate(0);
        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标3《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc3));

        System.out.println("(16.14)"+roomManager.canPutCard(16,14,nc3));
        System.out.println("(17.15)"+roomManager.canPutCard(17,15,nc3));
        roomManager.putCard(15,16,nc3);
        System.out.println();

        Card nc4 = new Card();
        nc4.setTop(new Edge(1,"city","{\"top\":\"true\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"true\"}"));
        nc4.setRig(new Edge(2,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc4.setBot(new Edge(3,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc4.setLef(new Edge(4,"city","{\"top\":\"true\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"true\"}"));
        nc4.rotate(1);
        System.out.println("》》》》》》》》》可放坐标4《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc4));
//        roomManager.putCard(13,15,nc4);

        System.out.println();

        System.out.println();
    }
    @Test
    public void WalkTest(){
        Card[][] cards = new Card[31][31];

        Card or = new Card();
        or.setBot(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setRig(new Edge(3,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setTop(new Edge(4,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);

        Card nc1 = new Card();
        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setLef(new Edge(2,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setBot(new Edge(4,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        roomManager.putCard(14,15,nc1);


    }
    @Test
    public void startTest2(){
        Card[][] cards = new Card[31][31];

        Card or = new Card();
        or.setBot(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setRig(new Edge(3,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setTop(new Edge(4,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);

        Card nc1 = new Card();
        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setLef(new Edge(2,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setBot(new Edge(4,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        roomManager.putCard(15,17,nc1);

        System.out.println();

        Card nc2 = new Card();
        nc2.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"true\",\"lef\":\"false\"}"));
        nc2.setLef(new Edge(2,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setBot(new Edge(4,"city","{\"top\":\"true\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));

        roomManager.putCard(15,16,nc2);
    }
}