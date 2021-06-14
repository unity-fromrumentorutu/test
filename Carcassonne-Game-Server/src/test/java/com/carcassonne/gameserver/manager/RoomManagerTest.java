package com.carcassonne.gameserver.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.bean.*;
import com.carcassonne.gameserver.util.CardLibraryUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RoomManagerTest {

    @Test
    void canPutCard() {
        Card[][] cards = new Card[31][31];
        Card or = new Card();
        or.setBot(new Edge(1,"City",null));
        or.setLef(new Edge(2,"Road",null));
        or.setRig(new Edge(3,"Road","left"));
        or.setTop(new Edge(4,"Grass",null));
        cards[15][15]=or;
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        Card nc = new Card();
        nc.setTop(new Edge(4,"Grass",null));
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
        or.setTop(new Edge(4,"Grass",null));
        cards[15][15]=or;
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表

        Card nc = new Card();
        nc.setBot(new Edge(1,"City",null));
        nc.setLef(new Edge(2,"Road",null));
        nc.setRig(new Edge(3,"Road","left"));
        nc.setTop(new Edge(4,"Grass",null));
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

        Card   or = new Card();
        or.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setBot(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
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


//      旋转测试
        /*
        Card nc1 = new Card();
        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"left\":\"false\"}"));
        nc1.setBot(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.rotate(0);
        System.out.println("》》》》》》》》》旋转0《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc1));
        System.out.println();
        nc1.rotate(1);
        System.out.println("》》》》》》》》》旋转1《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc1));
        System.out.println();
        nc1.rotate(1);
        System.out.println("》》》》》》》》》旋转2《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc1));
        System.out.println();
        nc1.rotate(1);
        System.out.println("》》》》》》》》》旋转3《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
        System.out.println(roomManager.getCanPutPositionList(nc1));
        System.out.println();
        */


        //错误日志2测试
//        Card nc1 = new Card();
//        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc1.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc1.setBot(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc1.rotate(1);
//        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
//        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
//        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
//        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
//        System.out.println("》》》》》》》》》可放坐标1《《《《《《《《《《");
//        System.out.println(roomManager.getCanPutPositionList(nc1));
//        roomManager.putCard(16,15,nc1);
//        System.out.println();
//
//        Card nc2 = new Card();
//        nc2.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc2.setRig(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"true\",\"lef\":\"false\"}"));
//        nc2.setBot(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"true\",\"lef\":\"false\"}"));
//        nc2.setLef(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc2.rotate(2);
//        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
//        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
//        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
//        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
//        System.out.println("》》》》》》》》》可放坐标2《《《《《《《《《《");
//        System.out.println(roomManager.getCanPutPositionList(nc2));
//        roomManager.putCard(16,16,nc2);
//        System.out.println();
//
//        Card nc3 = new Card();
//        nc3.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc3.setRig(new Edge(2,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc3.setBot(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"true\"}"));
//        nc3.setLef(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"true\",\"lef\":\"false\"}"));
//        nc3.rotate(1);
//        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
//        System.out.println(roomManager.getPuzzle().getHaveBePutCardsList());
//        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
//        System.out.println(roomManager.getPuzzle().getCanPutPositionList());
//        System.out.println("》》》》》》》》》可放坐标3《《《《《《《《《《");
//        System.out.println(roomManager.getCanPutPositionList(nc3));
//        roomManager.putCard(17,15,nc3);
//        System.out.println();
//
//        Card nc4 = new Card();
//        nc4.setTop(new Edge(1,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc4.setRig(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
//        nc4.setBot(new Edge(3,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        nc4.setLef(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
//        nc4.rotate(1);
//        System.out.println("》》》》》》》》》可放坐标4《《《《《《《《《《");
//        System.out.println(roomManager.getCanPutPositionList(nc4));
//        roomManager.putCard(16,17,nc4);
//
//        System.out.println();
//


    }
    @Test
    public void dongTest(){
        Card[][] cards = new Card[31][31];
        Card or = new Card();
        or.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setBot(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        cards[15][15]=or;
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);
        System.out.println("卡片边所占编号");
        System.out.println("上："+or.getTop().getType()+or.getTop().getCityorroad());
        System.out.println("右："+or.getRig().getType()+or.getRig().getCityorroad());
        System.out.println("下："+or.getBot().getType()+or.getBot().getCityorroad());
        System.out.println("左："+or.getLef().getType()+or.getLef().getCityorroad());
        HashMap<Integer, Block> unappropriatedCityBlock = roomManager.getUnappropriatedCityBlock();
        roomManager.appropriated(0,"murasame","city");

        testPut(roomManager,"0","grass","road","grass","road",16,15,
                0, "false","false","false","false","true","false");
        testPut(roomManager,"1.1","grass","grass","road","road",14,15,
                3, "false","false","false","false","false","true");

        //2.0出错了，我本地得到的可放位置是正确的，你放了一个不能放的地方，所以后面才错了
        //TODO 看看这个
        testPut(roomManager,"2.0","city","grass","grass","grass",17,15,
                2, "false","false","false","false","false","false");
        testPut(roomManager,"2.1","grass","road","grass","road",17,14,
                3, "false","false","false","false","true","false");
        testPut(roomManager,"3.0","city","grass","grass","city",13,15,
                3, "false","false","true","false","false","false");
        testPut(roomManager,"3.1","grass","road","grass","road",14,14,
                2, "false","false","false","false","true","false");

//        System.out.println("》》》》》》》》》回合4.0《《《《《《《《《《");
//        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
//        Card cardTBC = new Card();
//        cardTBC.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        cardTBC.setRig(new Edge(2,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        cardTBC.setBot(new Edge(3,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        cardTBC.setLef(new Edge(4,"grass","{\"top\":\"true\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        System.out.println(roomManager.getCanPutPositionList(cardTBC));
//        cardTBC.rotate(3);
//        roomManager.putCard(17,16,cardTBC);
//
//
//        System.out.println("》》》》》》》》》回合4.1《《《《《《《《《《");
//        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
//        Card cardLR3 = new Card();
//        cardLR3.setTop(new Edge(1,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        cardLR3.setRig(new Edge(2,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        cardLR3.setBot(new Edge(3,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        cardLR3.setLef(new Edge(4,"city","{\"top\":\"true\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
//        System.out.println(roomManager.getCanPutPositionList(cardLR3));
//        cardLR3.rotate(0);
//        roomManager.putCard(13,15,cardLR3);
    }
    @Test
    public void WalkTest(){
        Card[][] cards = new Card[31][31];

        Card or = new Card();
        or.setBot(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setRig(new Edge(3,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setTop(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);

        Card nc1 = new Card();
        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setLef(new Edge(2,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setBot(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        roomManager.putCard(14,15,nc1);


    }
    @Test
    public void startTest2(){
        Card[][] cards = new Card[31][31];

        Card or = new Card();
        or.setBot(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setRig(new Edge(3,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setTop(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);

        Card nc1 = new Card();
        nc1.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setLef(new Edge(2,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc1.setBot(new Edge(4,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        roomManager.putCard(15,17,nc1);

        System.out.println();

        Card nc2 = new Card();
        nc2.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"true\",\"lef\":\"false\"}"));
        nc2.setLef(new Edge(2,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        nc2.setBot(new Edge(4,"city","{\"top\":\"true\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));

        roomManager.putCard(15,16,nc2);
    }

    /**
     *
     * @param manager
     * @param huiheshu
     * @param TopType
     * @param RightType
     * @param BottomType
     * @param LeftType
     * @param rotate
     *testPut(roomManager,"0","grass","road","grass","road",16,15,0, "false","false","false","false","false","false");
     *
     */
    public void testPut(RoomManager manager,String huiheshu,String TopType,String RightType,String BottomType,String LeftType,int PointX,int PointY,
                        int rotate,String TopRight,String TopBottom,String TopLeft,String RightBottom,String RightLeft,String BottomLeft){
        System.out.println("》》》》》》》》》回合"+huiheshu+"《《《《《《《《《《");
        System.out.println("》》》》》》》》》已经放的《《《《《《《《《《");
        System.out.println(manager.getPuzzle().getHaveBePutCardsList());
        Card card = new Card();
        card.setTop(new Edge(1,TopType,"{\"top\":\"false\",\"rig\":\""+TopRight+"\",\"bot\":\""+TopBottom+"\",\"lef\":\""+TopLeft+"\"}"));
        card.setRig(new Edge(2,RightType,"{\"top\":\"false\",\"rig\":\"false\",\"bot\":\""+RightBottom+"\",\"lef\":\""+RightLeft+"\"}"));
        card.setBot(new Edge(3,BottomType,"{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\""+BottomLeft+"\"}"));
        card.setLef(new Edge(4,LeftType,"{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));

        card.rotate(rotate);
        System.out.println("这张卡是"+card);
        System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
        System.out.println(manager.getPuzzle().getCanPutPositionList());
        System.out.println("》》》》》》》》》可放坐标《《《《《《《《《《");
        System.out.println(manager.getCanPutPositionList(card));
        System.out.println("放在("+PointX+","+PointY+")");
        manager.putCard(PointX,PointY,card);
        System.out.println("卡片边所占编号");
        System.out.println("上："+card.getTop().getType()+card.getTop().getCityorroad());
        System.out.println("右："+card.getRig().getType()+card.getRig().getCityorroad());
        System.out.println("下："+card.getBot().getType()+card.getBot().getCityorroad());
        System.out.println("左："+card.getLef().getType()+card.getLef().getCityorroad());
    }
    @Test
    public void gameTest(){
        ArrayList<Card> cardsArray = CardLibraryUtil.getCardLibrary();

        Card[][] cards = new Card[31][31];
        Card or = new Card();
        or.setBot(new Edge(3,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setRig(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        Player player2 = new Player("player2");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);


        for(int i=0;i<10;i++){
            HashMap<Integer,ArrayList<Point>> hashMap = new HashMap<>();
            System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
            System.out.println(roomManager.getPuzzle().getCanPutPositionList());
            System.out.println("手牌:"+cardsArray.get(i).getTop().getType()+","+cardsArray.get(i).getRig().getType()+","+cardsArray.get(i).getBot().getType()+","+cardsArray.get(i).getLef().getType());

            hashMap = roomManager.getAllCanPutPositionList(cardsArray.get(i));
            System.out.println(hashMap);
            //suiji
            ArrayList<Point> pointArray = new ArrayList<>();

            for(int j=0;j<4;j++){
                for(int k=0;k<hashMap.get(j).size();k++){
                    Point point = hashMap.get(j).get(k);
                    point.setR(j);
                    pointArray.add(point);
                }
            }
            Collections.shuffle(pointArray);


            Point point = pointArray.get(0);
            Card card = cardsArray.get(i);
            card.rotate(point.getR());
            System.out.println("("+point.getX()+","+point.getY()+")"+","+point.getR());
            roomManager.putCard(point.getX(),point.getY(),card);
            System.out.println("当前卡牌:"+cardsArray.get(i).getTop().getType()+cardsArray.get(i).getTop().getCityorroad()+","+cardsArray.get(i).getRig().getType()+cardsArray.get(i).getRig().getCityorroad()+","+cardsArray.get(i).getBot().getType()+cardsArray.get(i).getBot().getCityorroad()+","+cardsArray.get(i).getLef().getType()+cardsArray.get(i).getLef().getCityorroad());

            System.out.println("放在了"+point+point.getR() );
        }
    }
    @Test
    public void gameTest2(){
        ArrayList<Card> cardsArray = CardLibraryUtil.getCardLibrary();

        Card[][] cards = new Card[31][31];
        Card or = new Card();
        or.setBot(new Edge(3,"grass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(4,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setRig(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setTop(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        Player player1 = new Player("murasame");
        Player player2 = new Player("player2");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        RoomManager roomManager=new RoomManager(cards,players);//卡片和玩家列表
        roomManager.putCard(15,15,or);


        for(int i=0;i<10;i++){
            HashMap<Integer,ArrayList<Point>> hashMap = new HashMap<>();
            System.out.println("》》》》》》》》》可放坐标周围一圈《《《《《《《《《《");
            System.out.println(roomManager.getPuzzle().getCanPutPositionList());
            System.out.println("手牌:"+cardsArray.get(i).getTop().getType()+","+cardsArray.get(i).getRig().getType()+","+cardsArray.get(i).getBot().getType()+","+cardsArray.get(i).getLef().getType());

            hashMap = roomManager.getAllCanPutPositionList(cardsArray.get(i));
            System.out.println(hashMap);
            ArrayList<Point> pointArray = new ArrayList<>();

            for(int j=0;j<4;j++){
                for(int k=0;k<hashMap.get(j).size();k++){
                    Point point = hashMap.get(j).get(k);
                    point.setR(j);
                    pointArray.add(point);
                }
            }
            Collections.shuffle(pointArray);


            Point point = pointArray.get(0);
            Card card = cardsArray.get(i);
            card.rotate(point.getR());
            System.out.println("("+point.getX()+","+point.getY()+")"+","+point.getR());
            roomManager.putCard(point.getX(),point.getY(),card);
            System.out.println("当前卡牌:"+cardsArray.get(i).getTop().getType()+cardsArray.get(i).getTop().getCityorroad()+","+cardsArray.get(i).getRig().getType()+cardsArray.get(i).getRig().getCityorroad()+","+cardsArray.get(i).getBot().getType()+cardsArray.get(i).getBot().getCityorroad()+","+cardsArray.get(i).getLef().getType()+cardsArray.get(i).getLef().getCityorroad());

            System.out.println("放在了"+point+point.getR() );
        }
    }
}