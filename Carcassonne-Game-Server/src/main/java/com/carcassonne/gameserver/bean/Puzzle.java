package com.carcassonne.gameserver.bean;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 地图（拼图）
 */
public class Puzzle {
    private Card[][] mPuzzle;

    private ArrayList<Point> haveBePutCardsList;

    private ArrayList<Point> canPutPositionList;

    public Puzzle(){
        this.haveBePutCardsList = new ArrayList<Point>();
        this.canPutPositionList = new ArrayList<Point>();
    }

    public Puzzle(Card[][] mPuzzle) {
        this.mPuzzle = mPuzzle;
        this.haveBePutCardsList = new ArrayList<Point>();
        this.canPutPositionList = new ArrayList<Point>();
    }

    public Card[][] getmPuzzle() {
        return mPuzzle;
    }

    public void setmPuzzle(Card[][] mPuzzle) {
        this.mPuzzle = mPuzzle;
    }

    public ArrayList<Point> getHaveBePutCardsList() {
        return haveBePutCardsList;
    }

    public void setHaveBePutCardsList(ArrayList<Point> haveBePutCardsList) {
        this.haveBePutCardsList = haveBePutCardsList;
    }

    public ArrayList<Point> getCanPutPositionList() {
        return canPutPositionList;
    }

    public void setCanPutPositionList(ArrayList<Point> canPutPositionList) {
        this.canPutPositionList = canPutPositionList;
    }

    public void addHaveBePutCardsList(Point point){
        Iterator<Point> it = canPutPositionList.iterator();
        while(it.hasNext()){
            Point point1 = it.next();
            if(point.equals(point1)){
                it.remove();
            }
        }
//        for (Point point1 : canPutPositionList){
//            if(point1.equals(point)){
//                canPutPositionList.remove(point1);
//            }
//        }
        this.haveBePutCardsList.add(point);
    }

    public void addCanPutPositionList(Point point){
        this.canPutPositionList.add(point);
    }

}
