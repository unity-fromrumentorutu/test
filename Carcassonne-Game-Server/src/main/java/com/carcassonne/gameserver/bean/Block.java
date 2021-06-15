package com.carcassonne.gameserver.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Block {

    String edgeString;
    public HashSet<Point> pointSet = new HashSet<>();
    HashMap<Point, Card> cardMap = new HashMap<>();//好像没用了
    HashMap<Point, ArrayList<Edge>> edgeMap = new HashMap<>();
    HashMap<String, Integer> scoreRecord = new HashMap<>();//玩家和玩家对应个数
    ArrayList<String> playerIdArray = new ArrayList<>();//最后得分玩家
    public boolean isFull = true;
    int scorePerCard = 0;
    int scoreAll = 0;

    public Block(String edgeString,ArrayList<String> nPlayerIdArray){
        this.edgeString = edgeString;
    }
    public Block(String edgeType, HashMap<Point, Card> cardMap) {
        this.edgeString = edgeType;
        this.cardMap = cardMap;

        switch (edgeType) {
            case "city":
                edgeString = "city";
                break;
            case "road":
                edgeString = "road";
                break;
            case "grass":
                edgeString = "grass";
                break;
            default:
                break;
        }
    }

    Block(HashMap<Point,ArrayList<Edge>> edgeMap,String edgeString){
        this.edgeMap= edgeMap;
        this.edgeString = edgeString;
    }

    public HashMap<Point, ArrayList<Edge>> getEdgeMap() {
        return edgeMap;
    }

    public void setEdgeMap(HashMap<Point, ArrayList<Edge>> edgeMap) {
        this.edgeMap = edgeMap;
    }

    /**
     *
     * @return Block的全部信息了可能
     */
    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append("EdgeMap:\n");

        for(Point point: edgeMap.keySet()){
            stringbuilder.append(point+" ");
            for(int i=0;i<4;i++)
                stringbuilder.append(edgeMap.get(point).get(i));

            stringbuilder.append("\n");
        }
        //玩家集合
        stringbuilder.append("玩家情况\n");
        for(String str:scoreRecord.keySet()){
            stringbuilder.append(str+" "+scoreRecord.get(str)+"\n");
        }
        //得分
        stringbuilder.append("总得分\n"+"是否完整"+isFull+"\n"+scorePerCard+"*"+pointSet.size()+"="+scoreAll+"\n");
        //得分玩家
        stringbuilder.append("得分玩家有：\n");
        int i = 1;
        for(String playerId : playerIdArray){
            stringbuilder.append("玩家"+i+" : "+playerId+"\n");
            i++;
        }
        return stringbuilder.toString();
    }

    public String getEdgeString() {
        return edgeString;
    }

    public void setEdgeString(String edgeString) {
        this.edgeString = edgeString;
    }

    public HashSet<Point> getPointSet() {
        return pointSet;
    }

    public void setPointSet(HashSet<Point> pointSet) {
        this.pointSet = pointSet;
    }

    public void addEdgeMap(Point point, Edge edge, int index){
            ArrayList<Edge> edgeArray =
                    edgeMap.containsKey(point)? edgeMap.get(point):new ArrayList<Edge>(){
                        {
                            add(null);
                            add(null);
                            add(null);
                            add(null);
                        }
                    };
            edgeArray.set(index,edge);
            edgeMap.put(point, edgeArray);
    }
    public ArrayList<Point> getPoints(){
        ArrayList<Point> points = new ArrayList<Point>();
        for(Point point : edgeMap.keySet()){
            points.add(point);
        }

        return points;
    }
    //合并情况不考虑得分

    /**
     * 合并block
     * @param block
     * 被合并的block会清空
     */
    public void mergeBlock(Block block){
        if(edgeString.equals(block.edgeString)){
            pointSet.addAll(block.pointSet);
            for(Point point : block.edgeMap.keySet()){
                ArrayList<Edge> edgeList = new ArrayList<Edge>(){
                            {
                                add(null);
                                add(null);
                                add(null);
                                add(null);
                            }
                        };
                if(block.edgeMap.containsKey(point))
                edgeList = copy(block.edgeMap.get(point));

                ArrayList<Edge> edgeList1 = new ArrayList<Edge>(){
                            {
                                add(null);
                                add(null);
                                add(null);
                                add(null);
                            }
                        };
                if(edgeMap.containsKey(point))
                    edgeList1 = copy(edgeMap.get(point));
                for(int i=0;i<4;i++){
                    if(edgeList.get(i)!=null){
                        edgeList1.set(i,edgeList.get(i));
                    }
                }
                edgeMap.put(point,edgeList1);
            }
            for(String ownerId:block.scoreRecord.keySet()){
                if(scoreRecord.containsKey(ownerId)){
                    scoreRecord.put(ownerId,scoreRecord.get(ownerId)+block.scoreRecord.get(ownerId));
                }
                else {
                    scoreRecord.put(ownerId,block.scoreRecord.get(ownerId));
                }
            }
            block.scoreRecord.clear();
            block.edgeMap.clear();
            block.pointSet.clear();
            block.isFull = true;
        }
    }
    public ArrayList<Edge> copy(ArrayList<Edge> edgeArrayList){
        ArrayList<Edge> edgeList1 = new ArrayList<Edge>(){
            {
                add(null);
                add(null);
                add(null);
                add(null);
            }
        };
        for(int i=0;i<4;i++){
            edgeList1.set(i,edgeArrayList.get(i));
        }
        return edgeList1;
    }
    /**
     * 要先调用Walk函数
     * 城市：2分/块 道路：1分/块
     * scoreAll为总得分，如果为0说明区块不完整不得分
     * PlayerIdArray为可以得分的玩家ID数组
     */
    public void caculate() {
        playerIdArray.clear();
        if(isFull){
            if (edgeString.equals("city")) {
                scorePerCard = 2;
            } else if (edgeString.equals("road")) {
                scorePerCard = 1;
            }
            scoreAll = isFull ? scorePerCard * pointSet.size() : 0;
            //判断有几个人可以得分
            int max = 0;
            for (Integer value : scoreRecord.values()) {
                if (value > max) max = value;
            }
            for (String ownerId : scoreRecord.keySet()) {
                if (scoreRecord.get(ownerId).equals(max)) {
                    playerIdArray.add(ownerId);
                }
            }
        }

    }

    /**
     * 将ownerId添加进scoreRecord键值（值为玩家对应个数）对中
     * @param ownerId
     */
    public void record(String ownerId) {
        if (scoreRecord.containsKey(ownerId)) {
            scoreRecord.put(ownerId, scoreRecord.get(ownerId) + 1);
        } else {
            scoreRecord.put(ownerId, 1);
        }
    }

    public void start(Point point){
        isFull = true;
        Walk(point);
        caculate();
    }

    /**
     * 记分功能先调用这个Walk（递归函数）,当这个区块不完整则isFull为false，然后调用caculate()函数
     * 这个函数主要是为了获取isFull的值，通过pointSet的个数来获取得分块
     * @param nextPoint 刚放进去的卡片的坐标
     */
    public void Walk(Point nextPoint) {
        if(!edgeMap.keySet().contains(nextPoint)){
//            System.out.println(nextPoint+"不存在");
            return;
        }
        if(pointSet.contains(nextPoint)){
//            System.out.println(nextPoint+"走过了");
            return;
        }

        pointSet.add(nextPoint);
        ArrayList<Edge> edgeArrayList = edgeMap.get(nextPoint);



        int x_nextPoint = nextPoint.getX();
        int y_nextPoint = nextPoint.getY();

        int mapNoNullCount = 0 ;
        int edgeNoNullCount = 0 ;
        for (int i = 0;i<4;i++) {

            switch (i) {
                case 0:
                    Point point =  new Point(x_nextPoint, y_nextPoint - 1);
                    Walk(point);
                    mapNoNullCount += edgeMap.keySet().contains(point)? 1:0;
                    edgeNoNullCount += edgeArrayList.get(i)==null?0:1;
                    break;
                case 1:
                    Point point1= new Point(x_nextPoint + 1, y_nextPoint);
                    Walk(point1);
                    mapNoNullCount += edgeMap.keySet().contains(point1)? 1:0;
                    edgeNoNullCount += edgeArrayList.get(i)==null?0:1;
                    break;
                case 2:
                    Point point2= new Point(x_nextPoint , y_nextPoint+1);
                    Walk(point2);
                    mapNoNullCount += edgeMap.keySet().contains(point2)? 1:0;
                    edgeNoNullCount += edgeArrayList.get(i)==null?0:1;
                    break;
                case 3:
                    Point point3= new Point(x_nextPoint-1 , y_nextPoint);
                    Walk(point3);
                    mapNoNullCount += edgeMap.keySet().contains(point3)? 1:0;
                    edgeNoNullCount += edgeArrayList.get(i)==null?0:1;
                    break;
                default:
                    break;
            }
        }
        if(mapNoNullCount!=edgeNoNullCount){
            isFull=false;
//            System.out.println("Block不完整不能得分哦现在");
//            System.out.println(nextPoint+"应该有"+edgeNoNullCount+"条边可以出去，但是旁边只有"+mapNoNullCount+"个卡牌");
//            System.out.println(this);
        }
    }

    public void addPlayerAccount(ArrayList<Edge> edgeArrayList){
        for(int i = 0;i<4;i++){
            if(edgeArrayList.get(i)!=null){
                Edge edge = edgeArrayList.get(i);
                if(edge.getPlayerAccount()!=null){
                    record(edge.getPlayerAccount());
                }
            }
        }
    }

    public ArrayList<Integer> searchCardEdge(Card card) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (card.getTop().getType().equals(edgeString)) arrayList.add(0);
        if (card.getRig().getType().equals(edgeString)) arrayList.add(1);
        if (card.getBot().getType().equals(edgeString)) arrayList.add(2);
        if (card.getLef().getType().equals(edgeString)) arrayList.add(3);
        return arrayList;
    }
    public Boolean scoreRecordIsempty(){
        return scoreRecord.isEmpty();
    }

    public int getScoreAll() {
        return scoreAll;
    }

    public ArrayList<String> getPlayerIdArray() {
        return playerIdArray;
    }
    public void addPlayerId(String id){
        playerIdArray.add(id);
    }
    public boolean isContainsPosition(int x,int y,int e){
        Point point = new Point(x,y);
        ArrayList<Edge> edgeArrayList = edgeMap.get(point);
        if(edgeArrayList.get(e)==null) return false;
        else return true;
    }
}
