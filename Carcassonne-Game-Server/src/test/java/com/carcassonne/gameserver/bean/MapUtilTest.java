package com.carcassonne.gameserver.bean;

import com.carcassonne.gameserver.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapUtilTest {
    Card card = new Card();
    Edge cityEdge = new Edge(1,"city",null);
    Edge roadEdge = new Edge(2,"road",null);
    Edge grassEdge = new Edge(4,"grass",null);


    @Test
    void judgeCardWhereToPut() {
        MapUtil mapUtil = new MapUtil();
        Point point = new Point(15,15);
        Card card = new Card();
        card.setBot(new Edge(1,"city",null));
        card.setLef(new Edge(2,"road",null));
        card.setRig(new Edge(3,"road","left"));
        card.setTop(new Edge(4,"grass",null));
        mapUtil.addCard(point,card);
        ArrayList<Point> points= mapUtil.judgeCardWhereToPut(card);
        for(Point point1:points){
            System.out.println(point1);
        }
    }
    @Test
    public void addEdgeMapTest(){
        ArrayList<Edge> oneCityBottom = new ArrayList<>(){{
            add(null);
            add(null);
            add(cityEdge);
            add(null);
        }};
        ArrayList<Edge> oneCityLeft = new ArrayList<>(){{
            add(null);
            add(null);
            add(null);
            add(cityEdge);
        }};
        ArrayList<Edge> oneCityTop = new ArrayList<>(){{
            add(cityEdge);
            add(null);
            add(null);
            add(null);
        }};
        ArrayList<Edge> twoCity = new ArrayList<>(){
            {
                add(cityEdge);
                add(null);
                add(cityEdge);
                add(null);
            }
        };

        ArrayList<Edge> threeCity = new ArrayList<>(){
            {
                add(cityEdge);
                add(cityEdge);
                add(cityEdge);
                add(null);
            }
        };

        /*

                    1514
                    1515
                    1516 1616
                    1517
         */
        Point point1515 = new Point(15,15);
        Point point1516 = new Point(15,16);
        Point point1616 = new Point(16,16);
        Point point1514 = new Point(15,14);
        Point point1517 = new Point(15,17);

        HashMap<Point,ArrayList<Edge>> edgeMap = new HashMap<>();
        edgeMap.put(point1514,oneCityBottom);
        edgeMap.put(point1515,twoCity);
        edgeMap.put(point1516,threeCity);
        edgeMap.put(point1517,oneCityTop);
        edgeMap.put(point1616,oneCityLeft);

        Block block = new Block(edgeMap,"city");
        block.Walk(point1616);
        block.caculate();
        System.out.println(block);
        Block block1 = new Block(edgeMap,"road");
        block1.Walk(point1616);
        block1.caculate();

        Block block2 = new Block(edgeMap,"city");
        block2.Walk(point1515);
        block2.caculate();

        Block block3 = new Block(edgeMap,"city");
        block3.Walk(point1514);
        block3.caculate();

        Block block4 = new Block(edgeMap,"city");
        block4.Walk(point1516);
        block4.caculate();

        Block block5 = new Block(edgeMap,"city");
        block5.Walk(point1517);
        block5.caculate();

        edgeMap.put(point1515,threeCity);

        Block block6 = new Block(edgeMap,"city");
        block6.Walk(point1616);
        block6.caculate();

        Block block7 = new Block(edgeMap,"city");
        block7.Walk(point1515);
        block7.caculate();

        assertEquals(5,block1.scoreAll);
        assertEquals(10,block2.scoreAll);
        assertEquals(10,block3.scoreAll);
        assertEquals(10,block4.scoreAll);
        assertEquals(10,block5.scoreAll);
        assertEquals(0,block6.scoreAll);
        assertEquals(0,block7.scoreAll);


        HashMap<Point,ArrayList<Edge>> edgeMap1 = new HashMap<>();

//        edgeMap1.put(point1515,oneCityBottom);
//        edgeMap1.put(point1516,oneCityTop);
        Block block8 = new Block(edgeMap1,"city");
        block8.addEdgeMap(point1515,cityEdge,2);
        block8.addEdgeMap(point1516,cityEdge,0);
        block8.Walk(point1516);
        block8.caculate();
        System.out.println(block8);

        Block block9 = new Block(edgeMap1,"city");
        block9.addEdgeMap(point1515,cityEdge,2);
        block9.addEdgeMap(point1516,cityEdge,0);
        block9.Walk(point1515);
        block9.caculate();
        System.out.println(block9);
    }
    @Test
    public void mergeBlockTest(){
        ArrayList<Edge> oneCityBottom = new ArrayList<>(){{
            add(null);
            add(null);
            add(cityEdge);
            add(null);
        }};
        ArrayList<Edge> oneCityLeft = new ArrayList<>(){{
            add(null);
            add(null);
            add(null);
            add(cityEdge);
        }};
        ArrayList<Edge> oneCityTop = new ArrayList<>(){{
            add(cityEdge);
            add(null);
            add(null);
            add(null);
        }};
        ArrayList<Edge> twoCity = new ArrayList<>(){
            {
                add(cityEdge);
                add(null);
                add(cityEdge);
                add(null);
            }
        };

        ArrayList<Edge> threeCity = new ArrayList<>(){
            {
                add(cityEdge);
                add(cityEdge);
                add(cityEdge);
                add(null);
            }
        };

        /*

                    1514
                    1515
                    1516 1616
                    1517
         */
        Point point1515 = new Point(15,15);
        Point point1516 = new Point(15,16);
        Point point1616 = new Point(16,16);
        Point point1514 = new Point(15,14);
        Point point1517 = new Point(15,17);

        HashMap<Point,ArrayList<Edge>> edgeMap1 = new HashMap<>();
        edgeMap1.put(point1514,oneCityBottom);
        edgeMap1.put(point1515,twoCity);
        edgeMap1.put(point1517,oneCityBottom);
        edgeMap1.put(point1516,threeCity);
        HashMap<Point,ArrayList<Edge>> edgeMap2 = new HashMap<>();
        edgeMap2.put(point1517,oneCityTop);
        edgeMap2.put(point1616,oneCityLeft);



        Block block1 = new Block(edgeMap1,"city");
        Block block2 = new Block(edgeMap2,"city");
        //定义玩家情况
        String ownerId1 = "Player1";
        String ownerId2 = "Player2";

        System.out.println("block1添加新玩家1,block2添加新玩家2\n");
        block1.record(ownerId1);
        block2.record(ownerId2);
        System.out.println("\n"+block1);
        System.out.println("\n"+block2);

        System.out.println("block2添加新玩家2\n");

        block2.record(ownerId2);
        System.out.println(block2);

        System.out.println("合并了\n");
        block1.mergeBlock(block2);
        System.out.println(block1);
        System.out.println("被合并的是了的康康");
        System.out.println(block2);

        System.out.println("开始记分了\n");
        block1.Walk(point1616);
        block1.caculate();

        System.out.println(block1);

        System.out.println("再加一个2，试一下会不会有两个人得分");
        block1.record(ownerId2);
        System.out.println(block2);
    }

}