package com.carcassonne.gameserver.bean;

import com.carcassonne.gameserver.util.MapUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

class MapUtilTest {
    Card card = new Card();
    Edge cityEdge = new Edge(1,"City",null);
    Edge roadEdge = new Edge(2,"Road",null);
    Edge grassEdge = new Edge(4,"Grass",null);


    @Test
    void judgeCardWhereToPut() {
        MapUtil mapUtil = new MapUtil();
        Point point = new Point(15,15);
        Card card = new Card();
        card.setBot(new Edge(1,"City",null));
        card.setLef(new Edge(2,"Road",null));
        card.setRig(new Edge(3,"Road","left"));
        card.setTop(new Edge(4,"Grass",null));
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
                    1517
                    1516 1616
                    1515
                    1514
         */
        Point point1515 = new Point(15,15);
        Point point1516 = new Point(15,16);
        Point point1616 = new Point(16,16);
        Point point1514 = new Point(15,14);
        Point point1517 = new Point(15,17);

        HashMap<Point,ArrayList<Edge>> edgeMap = new HashMap<>();
        edgeMap.put(point1514,oneCityTop);
        edgeMap.put(point1515,twoCity);
        edgeMap.put(point1516,threeCity);
        edgeMap.put(point1517,oneCityBottom);
        edgeMap.put(point1616,oneCityLeft);

        Block block = new Block(edgeMap,"City");
        block.Walk(point1616,point1516);

    }
}