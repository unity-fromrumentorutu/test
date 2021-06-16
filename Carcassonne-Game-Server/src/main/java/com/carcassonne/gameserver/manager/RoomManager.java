package com.carcassonne.gameserver.manager;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carcassonne.gameserver.bean.*;
import com.carcassonne.gameserver.util.MapUtil;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 房间控制器
 */
public class RoomManager {

    static public int MAX_X = 30;
    static public int MAX_Y = 30;
    static public int MIN_X = 0;
    static public int MIN_Y = 0;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    private ArrayList<Player> players;

    private ArrayList<String> playerIds = new ArrayList<>();

    private Integer activePlayerNum;
    private Puzzle puzzle;
    private Card[] cardLibrary;
    private GameLog gameLog;
    private GameResult gameResult;

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

//    private ArrayList<ArrayList<Card>> city;
//    private ArrayList<ArrayList<Edge>> cityEdge;
//    private ArrayList<ArrayList<Card>> road;
//    private ArrayList<ArrayList<Edge>> roadEdge;

    private ArrayList<Block> cityBlock = new ArrayList<>();
    private ArrayList<Block> roadBlock = new ArrayList<>();
    private HashMap<String,Integer> playerScore = new HashMap<>();

    MapUtil mapUtil = new MapUtil();

    //进行时信息
    private Integer nowTurnNum = 0 ;
    private Integer nowPlayerNum = 0;
    private Integer nowLibNum = 0 ;
    private JSONObject lastPlayerOpInfo;


    public RoomManager(Card[][] cards , ArrayList<Player> nplayers){
        puzzle = new Puzzle();
//        puzzle.addHaveBePutCardsList(new Point(15,15));
        players = nplayers;
        lastPlayerOpInfo = new JSONObject();
        for( Player player:players){
            playerScore.put(player.getUserId(),0);
            playerIds.add(player.getUserId());
        }
    }

    public RoomManager(){
        players = new ArrayList<>();
        activePlayerNum = 0;
    }

    public Integer getScoreByAccountNum(String accountNum){
        return  playerScore.get(accountNum);
    }

    public Boolean playerActionPutCard(String accountNum, Integer putX, Integer putY, Integer rotation){
//        JSONArray jsonArray = new JSONArray();   //获取牌库用的
//        for (int i=0;i<cardLibrary.length;i++){
//            jsonArray.add(cardLibrary[i].toJsonString());
//        }
//        System.out.println(jsonArray);

        try {
            logger.info("====> 回合数为"+ nowTurnNum +", 玩家序号"+ nowPlayerNum +" 尝试放置坐标(X,Y,R)=("+putX+","+putY+","+rotation+") ，占领：否 ， 手牌URL："+ players.get(nowPlayerNum).getHand().getPictureUrl());
            logger.info("     手牌信息："+players.get(nowPlayerNum).getHand().toString());
            if(nowTurnNum == 0 ) nowTurnNum ++;
            if(players.get(nowPlayerNum).getAccountNum().equals(accountNum)){
                lastPlayerOpInfo.clear();
                lastPlayerOpInfo.put("lastPlayerHandCard",players.get(nowPlayerNum).getHand().getId());
                lastPlayerOpInfo.put("lastPlayerPutX",putX);
                lastPlayerOpInfo.put("lastPlayerPutY",putY);
                lastPlayerOpInfo.put("lastPlayerCardRotation",rotation);

                Card card = players.get(nowPlayerNum).getHand();
                card.rotate(rotation);
                putCard(putX,putY,card);

            }
            logger.info("~~~~ 本次放置成功！地图卡片数："+puzzle.showPuzzle().size()+"  详情="+puzzle.showPuzzle());
            return false;
        }catch (Exception e){
            logger.info("#### playerAction函数出错, 回合数" + nowTurnNum +" 玩家序号" + nowPlayerNum +"  错误信息："+e.getMessage());
            e.printStackTrace();

            return true;
        }

    }

    public Boolean playerActionOccupy(String accountNum,Integer occupyBlockNum,String blockType){

        try {
            if(occupyBlockNum != 999 && players.get(nowPlayerNum).getAccountNum().equals(accountNum)){

                JSONArray occupyArray = getNowPlayerCanOccupyBlock();
                for(int i=0;i<occupyArray.size();i++){
                    if(occupyArray.getJSONObject(i).getInteger("roundPlayerCanOccupyBlockId") == occupyBlockNum){
                        lastPlayerOpInfo.put("lastPlayerOccupyBlockInfo",occupyArray.getJSONObject(i));
                        break;
                    }
                }

                appropriated(occupyBlockNum,players.get(nowPlayerNum).getAccountNum(),blockType);
                lastPlayerOpInfo.put("lastPlayerOccupyBlockId",occupyBlockNum);

                    //切换玩家
                if(nowPlayerNum == players.size() -1){
                    nowTurnNum++;
                    nowPlayerNum = 0;
                    return deal();
                }else {
                    nowPlayerNum++;
                    return deal();
                }

            }
            else {
                if( players.get(nowPlayerNum).getAccountNum().equals(accountNum)){
                    lastPlayerOpInfo.put("lastPlayerOccupyBlockId",999);
                    if(nowPlayerNum == players.size() -1){
                        nowTurnNum++;
                        nowPlayerNum = 0;
                        return deal();
                    }else {
                        nowPlayerNum++;
                        return deal();
                    }
                }
                else return true;

            }

        }catch (Exception e){
            logger.info("！占领"+occupyBlockNum+"失败");
            System.out.println(e);
            return true;
        }
    }

    public JSONObject getLastPlayerOpInfo(){
        if(nowTurnNum == 0){
            lastPlayerOpInfo = new JSONObject();
            lastPlayerOpInfo.put("lastPlayerHandCard","null");
            lastPlayerOpInfo.put("lastPlayerPutX","null");
            lastPlayerOpInfo.put("lastPlayerPutY","null");
            lastPlayerOpInfo.put("lastPlayerCardRotation","null");
            return lastPlayerOpInfo;
        }
        else return lastPlayerOpInfo;
    }

    public void setLastPlayerOpInfo(JSONObject lastPlayerOpInfo) {
        this.lastPlayerOpInfo = lastPlayerOpInfo;
    }

    public Integer getNowLibNum() {
        return nowLibNum;
    }

    public void setNowLibNum(Integer nowLibNum) {
        this.nowLibNum = nowLibNum;
    }

    public String getPlayerAccountNum(){
        if( nowPlayerNum < players.size()) return players.get(nowPlayerNum).getAccountNum();
        else return "null";
    }


    public Boolean deal(){
        if(nowLibNum < cardLibrary.length){
            nowLibNum++;
            players.get(nowPlayerNum).setHand(cardLibrary[nowLibNum]);
            return false;
        }

        else{
            return true;
        }
    }

    public Integer getNowPlayerHeadCardId(){
        return players.get(nowPlayerNum).getHand().getId();
    }


    public String getMasterAccountNum(){
        String accountNum = null;
        if(players.get(0) != null) return  players.get(0).getAccountNum();
        return "null";
    }

    public JSONArray getNowPlayerCanPutPosition(){
        JSONArray res = new JSONArray();
        HashMap<Integer,ArrayList<Point>> allCanPutPositionList = getAllCanPutPositionList(players.get(nowPlayerNum).getHand());
        for (int i = 0 ; i < 4 ; i++){
            for (int j = 0 ; j < allCanPutPositionList.get(i).size() ; j++){
                JSONObject temp = new JSONObject();
                Point tempP = allCanPutPositionList.get(i).get(j);
                temp.put("roundPlayerCanPutPositionX",tempP.getX());
                temp.put("roundPlayerCanPutPositionY",tempP.getY());
                temp.put("roundPlayerCanPutPositionRotation",i);
                res.add(temp);
            }
        }

        //调试使用
        String pointsStr = "(X,Y,Rotation) =";
        for(int i=0 ;i<res.size();i++){
            pointsStr += ", ("+res.getJSONObject(i).get("roundPlayerCanPutPositionX")+","+res.getJSONObject(i).get("roundPlayerCanPutPositionY")+","+
                    res.getJSONObject(i).get("roundPlayerCanPutPositionRotation")+")";
        }
        logger.info("当前请求者"+players.get(nowPlayerNum).getAccountNum()+" ,能放的坐标数"+res.size()+" = " + pointsStr);
        return res;
    }

    public JSONArray getNowPlayerCanOccupyBlock(){
        JSONArray res = new JSONArray();
        HashMap<Integer, Block> roadBlock = getUnappropriatedRoadBlock();
        HashMap<Integer, Block> cityBlock = getUnappropriatedCityBlock();

        for(int i = 0; i< roadBlock.size(); i++){
            JSONObject temp = new JSONObject();
            temp.put("roundPlayerCanOccupyBlockId",i);
            temp.put("roundPlayerCanOccupyBlockType",roadBlock.get(i).getEdgeString());
            JSONArray roundPlayerCanOccupyPosition =new JSONArray();

            HashMap<Point, ArrayList<Edge>> map = roadBlock.get(i).getEdgeMap();
            for (Map.Entry<Point, ArrayList<Edge>> entry : map.entrySet()) {
                Point p =entry.getKey();
                ArrayList<Edge> arrayList=entry.getValue();
                for(int j = 0 ; j< 4 ; j++){
                    JSONObject cardInfo = new JSONObject();
                    if(arrayList.get(j) != null){
                        cardInfo.put("roundPlayerCanOccupyPositionX",p.getX());
                        cardInfo.put("roundPlayerCanOccupyPositionY",p.getY());
                        cardInfo.put("roundPlayerCanOccupyPositionEdge",arrayList.get(j).getPosition());
                    }
                    roundPlayerCanOccupyPosition.add(cardInfo);
                }
                temp.put("roundPlayerCanOccupyPosition",roundPlayerCanOccupyPosition);
            }
            res.add(temp);
        }

        for(int i = 0; i< cityBlock.size(); i++){
            JSONObject temp = new JSONObject();
            temp.put("roundPlayerCanOccupyBlockId",i);
            temp.put("roundPlayerCanOccupyBlockType",cityBlock.get(i).getEdgeString());
            JSONArray roundPlayerCanOccupyPosition =new JSONArray();

            HashMap<Point, ArrayList<Edge>> map = cityBlock.get(i).getEdgeMap();
            for (Map.Entry<Point, ArrayList<Edge>> entry : map.entrySet()) {
                Point p =entry.getKey();
                ArrayList<Edge> arrayList=entry.getValue();
                for(int j = 0 ; j< 4 ; j++){
                    JSONObject cardInfo = new JSONObject();
                    if(arrayList.get(j) != null){
                        cardInfo.put("roundPlayerCanOccupyPositionX",p.getX());
                        cardInfo.put("roundPlayerCanOccupyPositionY",p.getY());
                        cardInfo.put("roundPlayerCanOccupyPositionEdge",arrayList.get(j).getPosition());
                    }
                    roundPlayerCanOccupyPosition.add(cardInfo);
                }
                temp.put("roundPlayerCanOccupyPosition",roundPlayerCanOccupyPosition);
            }
            res.add(temp);
        }

        return res;
    }

    public JSONArray getPlayersInfo(){
        JSONArray array = new JSONArray();
        for (int i=0;i<players.size();i++){
            JSONObject object = new JSONObject();
            object.put("playerName",players.get(i).getNickname());
            object.put("playerAccountNum",players.get(i).getAccountNum());
            object.put("playerLevel",players.get(i).getLevel());
            object.put("playerSex",players.get(i).getSex());
            object.put("isReady",players.get(i).getReady().toString());
            array.add(object);
        }
        return array;
    }

    public Boolean addPlayer(Player player){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getAccountNum().equals(player.getAccountNum())){
                return false;
            }
        }
            players.add(player);
            activePlayerNum ++;
        return  true;
    }



    public String readyAndStartGame(String accountNum){
        int flag = 0;
        for (int i=0;i<players.size();i++){
            if(players.get(i).getAccountNum().equals(accountNum)){
                players.get(i).setReady(true);
            }
            if(players.get(i).getReady()==true) flag++;
        }
        if (flag==players.size() && flag > 1) {

            Card[][] cards = new Card[32][32];

            Card or = new Card();
            or.setBot(new Edge(99,"grass","{\"top\":\"false\",\"bot\":\"false\",\"lef\":\"false\",\"rig\":\"false\"}"));
            or.setLef(new Edge(99,"road","{\"top\":\"false\",\"bot\":\"false\",\"lef\":\"false\",\"rig\":\"true\"}"));
            or.setRig(new Edge(99,"road","{\"top\":\"false\",\"bot\":\"false\",\"lef\":\"true\",\"rig\":\"false\"}"));
            or.setTop(new Edge(99,"city","{\"top\":\"false\",\"bot\":\"false\",\"lef\":\"false\",\"rig\":\"false\"}"));
            puzzle = new Puzzle();
            putCard(15,15,or);

            updateCanPutPositionList(new Point(15,15));
            nowPlayerNum = 0;
            nowTurnNum = 0;
            deal();
            logger.info("游戏开始" + players );
            return "playing";
        }
        return "waiting";
    }


    public void deletePlayer(String accountNum){
        for(int i=0;i<players.size();i++){
            if(players.get(i).getAccountNum().equals(accountNum)){
                players.remove(i);
                break;
            }
        }
        activePlayerNum--;
    }


    //遍历更改Block中所有边的所属
    public void updateBlockAllEdgeOwn(Card[][] map,Block block,int own,int p,String type){
        ArrayList<Point> points = new ArrayList<>();
        points = block.getPoints();
//        System.out.println(points);

        for(Point point:points){
            if((map[point.getX()][point.getY()].getTop().getCityorroad()==p)&&(map[point.getX()][point.getY()].getTop().getType().equals(type))){
                map[point.getX()][point.getY()].setTopRoadOrCity(own);
//                System.out.println("上变了"+map[point.getX()][point.getY()].getTop().getCityorroad());
            }
            if((map[point.getX()][point.getY()].getRig().getCityorroad()==p)&&(map[point.getX()][point.getY()].getRig().getType().equals(type))){
                map[point.getX()][point.getY()].setRigRoadOrCity(own);
//                System.out.println("右变了"+map[point.getX()][point.getY()].getRig().getCityorroad());
            }
            if((map[point.getX()][point.getY()].getBot().getCityorroad()==p)&&(map[point.getX()][point.getY()].getBot().getType().equals(type))){
                map[point.getX()][point.getY()].setBotRoadOrCity(own);
//                System.out.println("下变了"+map[point.getX()][point.getY()].getBot().getCityorroad());
            }
            if((map[point.getX()][point.getY()].getLef().getCityorroad()==p)&&(map[point.getX()][point.getY()].getLef().getType().equals(type))){
                map[point.getX()][point.getY()].setLefRoadOrCity(own);
//                System.out.println("左变了"+map[point.getX()][point.getY()].getLef().getCityorroad());
            }
        }

    }

    //能否放置的判断
    /**
     * 参数：x y 对应地图的位置,card 为待放置的手牌
     * 函数执行后给出某一位置是否能放牌
     */
    public Boolean canPutCard(Integer x,Integer y,Card card){   //TODO 这里有问题
//        int n = 0;//有几个临边有放卡片
        boolean YN = true;//能否放
        Card[][] thiscard = puzzle.getmPuzzle();
        //左边没到左边界，左边有牌->左边牌的右边界判断能放？
        if((x-1)>=MIN_X){
            if(thiscard[x-1][y] != null){
//                n++;

                if( ! thiscard[x-1][y].getRig().getType().equals(card.getLef().getType())){

                    YN = false;
                }
            }
        }
        if((x+1)<=MAX_X){
            if(thiscard[x+1][y] != null){
//                n++;

                if( ! thiscard[x+1][y].getLef().getType() .equals(card.getRig().getType()) ){

                    YN = false;
                }
            }
        }
        if((y-1)>=MIN_Y){
            if(thiscard[x][y-1] != null){
//                n++;
                if(! thiscard[x][y-1].getBot().getType().equals(card.getTop().getType())){

                    YN = false;
                }
            }
        }
        if((y+1)<=MAX_Y){
            if(thiscard[x][y+1] != null){

//                n++;
                if(! thiscard[x][y+1].getTop().getType().equals(card.getBot().getType())){
                    YN = false;
                }
            }
        }
//        if(n==0){
//            YN = false;
//
//        }
        return YN;
    }

    //维护目前可放置卡片的坐标数组
    //到时候每次放牌操作都要调用
    public void updateCanPutPositionList(Point point){
        int x = point.getX();
        int y = point.getY();
        Point around[] = {new Point(x,y-1),new Point(x,y+1),new Point(x-1,y),new Point(x+1,y)};

        for(int i = 0;i < 4;i++){
            if((!puzzle.getCanPutPositionList().contains(around[i]))&&(!puzzle.getHaveBePutCardsList().contains(around[i]))){
                int ax=around[i].getX();
                int ay=around[i].getY();
                if(ax>=MIN_X&&ax<=MAX_X&&ay>=MIN_Y&&ay<=MAX_Y){
                    puzzle.addCanPutPositionList(around[i]);
                }
            }
        }
        puzzle.addHaveBePutCardsList(point);
    }


    //获取指定卡片的可放置坐标
    //返回坐标ArraryList
    public ArrayList<Point> getCanPutPositionList(Card card){
        ArrayList<Point> theCardCanPutPositionList = new ArrayList<Point>();
        for(Point point : puzzle.getCanPutPositionList()){
            if(canPutCard(point.getX(),point.getY(),card)){
                theCardCanPutPositionList.add(point);
            }
        }
        return theCardCanPutPositionList;
    }

    //返回旋转4个角度的可放牌坐标
    public HashMap<Integer,ArrayList<Point>> getAllCanPutPositionList(Card card){
        Card tmp = card;

        HashMap<Integer,ArrayList<Point>> allCanPutPositionList = new HashMap<>();
        allCanPutPositionList.put(0,getCanPutPositionList(tmp));//0°
        tmp.rotate(1);
        allCanPutPositionList.put(1,getCanPutPositionList(tmp));//90°
        tmp.rotate(1);
        allCanPutPositionList.put(2,getCanPutPositionList(tmp));//180°
        tmp.rotate(1);
        allCanPutPositionList.put(3,getCanPutPositionList(tmp));//270°
        tmp.rotate(1);
        return allCanPutPositionList;
    }


    //TODO 测试它，有问题qq找我 299108606
    public void putCard(int x,int y,Card card){
        Point point = new Point(x,y);
        Card[][] nmap = puzzle.getmPuzzle();
        nmap[x][y] = card;
        boolean isEmpty = true;
        if(x+1<=MAX_X){
            //不超过边界，右边有卡片
            //是城市的时候，卡片的边界赋值旁边卡片的相邻边界
            if(nmap[x+1][y]!=null){
                if(card.getRig().getType().equals("city")){
                    card.setRigRoadOrCity(nmap[x+1][y].getLef().getCityorroad());
                    cityBlock.get(nmap[x+1][y].getLef().getCityorroad()).addEdgeMap(point,card.getRig(),1);
//                    cityEdge.get(nmap[x+1][y].getLef().getCityorroad()).add(card.getRig());
                }else if(card.getRig().getType().equals("road")){
                    card.setRigRoadOrCity(nmap[x+1][y].getLef().getCityorroad());
                    roadBlock.get(nmap[x+1][y].getLef().getCityorroad()).addEdgeMap(point,card.getRig(),1);
//                    road.get(nmap[x+1][y].getLef().getCityorroad()).add(card);
//                    roadEdge.get(nmap[x+1][y].getLef().getCityorroad()).add(card.getRig());
                }
                isEmpty = false;
            }else{
                if(card.getRig().getType().equals("city")){
                    Block tmp = new Block("city",playerIds);
                    tmp.addEdgeMap(point,card.getRig(),1);
                    cityBlock.add(tmp);
                    card.setRigRoadOrCity(cityBlock.size()-1);
//                    ArrayList<Card> ncity1 = new ArrayList<Card>();
//                    ArrayList<Edge> ncitye1 = new ArrayList<Edge>();
//                    ncity1.add(card);
//                    ncitye1.add(card.getRig());
//                    city.add(ncity1);
//                    cityEdge.add(ncitye1);
//                    card.setRigRoadOrCity(city.size()-1);

                }else if(card.getRig().getType().equals("road")){
                    Block tmp = new Block("road",playerIds);
                    tmp.addEdgeMap(point,card.getRig(),1);
                    roadBlock.add(tmp);
                    card.setRigRoadOrCity(roadBlock.size()-1);
//                    System.out.println("右边"+(roadBlock.size()-1));
//                    ArrayList<Card> nroad1 = new ArrayList<Card>();
//                    ArrayList<Edge> nroade1 = new ArrayList<Edge>();
//                    nroad1.add(card);
//                    nroade1.add(card.getRig());
//                    road.add(nroad1);
//                    roadEdge.add(nroade1);
//                    card.setRigRoadOrCity(road.size()-1);
                }
            }
        }
        if(x-1>=MIN_X){
            if(nmap[x-1][y]!=null){

                if(card.getLef().getType().equals("city")){
                    card.setLefRoadOrCity(nmap[x-1][y].getRig().getCityorroad());
                    cityBlock.get(nmap[x-1][y].getRig().getCityorroad()).addEdgeMap(point,card.getLef(),3);
//                    city.get(nmap[x-1][y].getRig().getCityorroad()).add(card);
//                    cityEdge.get(nmap[x-1][y].getRig().getCityorroad()).add(card.getLef());
                }else if(card.getLef().getType().equals("road")){
                    card.setLefRoadOrCity(nmap[x-1][y].getRig().getCityorroad());
                    roadBlock.get(nmap[x-1][y].getRig().getCityorroad()).addEdgeMap(point,card.getLef(),3);
                }
                isEmpty = false;
            }else{
                if(card.getLef().getType().equals("city")){
                    Block tmp = new Block("city",playerIds);
                    tmp.addEdgeMap(point,card.getLef(),3);


                    cityBlock.add(tmp);
                    card.setLefRoadOrCity(cityBlock.size()-1);
//                    ArrayList<Card> ncity2 = new ArrayList<Card>();
//                    ArrayList<Edge> ncitye2 = new ArrayList<Edge>();
//                    ncity2.add(card);
//                    ncitye2.add(card.getLef());
//                    city.add(ncity2);
//                    cityEdge.add(ncitye2);
//                    card.setLefRoadOrCity(city.size()-1);
                }else if(card.getLef().getType().equals("road")){
                    Block tmp = new Block("road",playerIds);
                    tmp.addEdgeMap(point,card.getLef(),3);

//                    ArrayList<Point> aaaaa = tmp.getPoints();
//                    System.out.println(" 加入了 "+aaaaa);

                    roadBlock.add(tmp);
//
//                    System.out.println("现在"+roadBlock.get(roadBlock.size()-1).getPoints());
                    card.setLefRoadOrCity(roadBlock.size()-1);

//                    System.out.println("左边"+(roadBlock.size()-1));
//                    ArrayList<Card> nroad2 = new ArrayList<Card>();
//                    ArrayList<Edge> nroade2 = new ArrayList<Edge>();
//                    nroad2.add(card);
//                    nroade2.add(card.getLef());
//                    road.add(nroad2);
//                    roadEdge.add(nroade2);
//                    card.setLefRoadOrCity(road.size()-1);
                }
            }
        }
        if(y+1<=MAX_Y){
            if(nmap[x][y+1]!=null){

                if(card.getBot().getType().equals("city")){
                    card.setBotRoadOrCity(nmap[x][y+1].getTop().getCityorroad());
                    cityBlock.get(nmap[x][y+1].getTop().getCityorroad()).addEdgeMap(point,card.getBot(),2);
//                    city.get(nmap[x][y+1].getTop().getCityorroad()).add(card);
//                    cityEdge.get(nmap[x][y+1].getTop().getCityorroad()).add(card.getBot());
                }else if(card.getBot().getType().equals("road")){
                    card.setBotRoadOrCity(nmap[x][y+1].getTop().getCityorroad());
                    roadBlock.get(nmap[x][y+1].getTop().getCityorroad()).addEdgeMap(point,card.getBot(),2);
//                    road.get(nmap[x][y+1].getTop().getCityorroad()).add(card);
//                    roadEdge.get(nmap[x][y+1].getTop().getCityorroad()).add(card.getBot());
                }
                isEmpty = false;
            }else{
                if(card.getBot().getType().equals("city")){
                    Block tmp = new Block("city",playerIds);
                    tmp.addEdgeMap(point,card.getBot(),2);
                    cityBlock.add(tmp);
                    card.setBotRoadOrCity(cityBlock.size()-1);

//                    ArrayList<Card> ncity3 = new ArrayList<Card>();
//                    ArrayList<Edge> ncitye3 = new ArrayList<Edge>();
//                    ncity3.add(card);
//                    ncitye3.add(card.getBot());
//                    city.add(ncity3);
//                    cityEdge.add(ncitye3);
//                    card.setBotRoadOrCity(city.size()-1);
                }else if(card.getBot().getType().equals("road")){
                    Block tmp = new Block("road",playerIds);
                    tmp.addEdgeMap(point,card.getBot(),2);
                    roadBlock.add(tmp);
                    card.setBotRoadOrCity(roadBlock.size()-1);

//                    ArrayList<Card> nroad3 = new ArrayList<Card>();
//                    ArrayList<Edge> nroade3 = new ArrayList<Edge>();
//                    nroad3.add(card);
//                    nroade3.add(card.getBot());
//                    road.add(nroad3);
//                    roadEdge.add(nroade3);
//                    card.setBotRoadOrCity(road.size()-1);
                }
            }
        }

        if(y-1>=MIN_Y){

            if(nmap[x][y-1]!=null){

                if(card.getTop().getType().equals("city")){
                    card.setTopRoadOrCity(nmap[x][y-1].getBot().getCityorroad());
                    cityBlock.get(nmap[x][y-1].getBot().getCityorroad()).addEdgeMap(point,card.getTop(),0);
//                    city.get(nmap[x][y-1].getBot().getCityorroad()).add(card);
//                    cityEdge.get(nmap[x][y-1].getBot().getCityorroad()).add(card.getTop());
                }else if(card.getTop().getType().equals("road")){
                    card.setTopRoadOrCity(nmap[x][y-1].getBot().getCityorroad());
                    roadBlock.get(nmap[x][y-1].getBot().getCityorroad()).addEdgeMap(point,card.getTop(),0);
//                    road.get(nmap[x][y-1].getBot().getCityorroad()).add(card);
//                    roadEdge.get(nmap[x][y-1].getBot().getCityorroad()).add(card.getTop());
                }
                isEmpty = false;
            }else{

                if(card.getTop().getType().equals("city")){
                    Block tmp = new Block("city",playerIds);
                    tmp.addEdgeMap(point,card.getTop(),0);
                    cityBlock.add(tmp);
                    card.setTopRoadOrCity(cityBlock.size()-1);
//                    System.out.println("******>"+(cityBlock.size()-1));
//                    ArrayList<Card> ncity4 = new ArrayList<Card>();
//                    ArrayList<Edge> ncitye4 = new ArrayList<Edge>();
//                    ncity4.add(card);
//                    ncitye4.add(card.getTop());
//                    city.add(ncity4);
//                    cityEdge.add(ncitye4);
//                    card.setTopRoadOrCity(city.size()-1);
                }else if(card.getTop().getType().equals("road")){
                    Block tmp = new Block("road",playerIds);
                    tmp.addEdgeMap(point,card.getTop(),0);
                    roadBlock.add(tmp);
                    card.setTopRoadOrCity(roadBlock.size()-1);
//                    ArrayList<Card> nroad4 = new ArrayList<Card>();
//                    ArrayList<Edge> nroade4 = new ArrayList<Edge>();
//                    nroad4.add(card);
//                    nroade4.add(card.getTop());
//                    road.add(nroad4);
//                    roadEdge.add(nroade4);
//                    card.setTopRoadOrCity(road.size()-1);
                }
            }
        }

        //TOPEdge 的链接的遍历
        JSONObject cardTopJson = JSONObject.parseObject(card.getTop().getConnect());
        if (card.getTop().getType().equals("city")){
            if(cardTopJson.get("rig").equals("true")){
                //TODO 我人麻了
                if(card.getTop().getCityorroad()!=card.getRig().getCityorroad()){
                    int tmp = card.getRig().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,cityBlock.get(card.getRig().getCityorroad()),card.getTop().getCityorroad(),card.getRig().getCityorroad(),"city");
                    cityBlock.get(card.getTop().getCityorroad()).mergeBlock(cityBlock.get(tmp));

                }

//                city.get(card.getTop().getCityorroad()).addAll(city.get(card.getRig().getCityorroad()));
//                cityEdge.get(card.getTop().getCityorroad()).addAll(cityEdge.get(card.getRig().getCityorroad()));

            }else if(cardTopJson.get("bot").equals("true")){
                if(card.getTop().getCityorroad()!=card.getBot().getCityorroad()){
                    int tmp = card.getBot().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,cityBlock.get(card.getBot().getCityorroad()),card.getTop().getCityorroad(),card.getBot().getCityorroad(),"city");
                    cityBlock.get(card.getTop().getCityorroad()).mergeBlock(cityBlock.get(tmp));


                }

//                city.get(card.getTop().getCityorroad()).addAll(city.get(card.getBot().getCityorroad()));
//                cityEdge.get(card.getTop().getCityorroad()).addAll(cityEdge.get(card.getBot().getCityorroad()));
//                card.setBotRoadOrCity(card.getTop().getCityorroad());
            }else if(cardTopJson.get("lef").equals("true")){
                if(card.getTop().getCityorroad()!=card.getLef().getCityorroad()){
                    int tmp = card.getLef().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,cityBlock.get(card.getLef().getCityorroad()),card.getTop().getCityorroad(),card.getLef().getCityorroad(),"city");
                    cityBlock.get(card.getTop().getCityorroad()).mergeBlock(cityBlock.get(tmp));


                }

//                city.get(card.getTop().getCityorroad()).addAll(city.get(card.getLef().getCityorroad()));
//                cityEdge.get(card.getTop().getCityorroad()).addAll(cityEdge.get(card.getLef().getCityorroad()));
//                card.setLefRoadOrCity(card.getTop().getCityorroad());
            }
        }else if(card.getTop().getType().equals("road")){
            if(cardTopJson.get("rig").equals("true")){
                if(card.getTop().getCityorroad()!=card.getRig().getCityorroad()){
                    int tmp = card.getRig().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,roadBlock.get(card.getRig().getCityorroad()),card.getTop().getCityorroad(),card.getRig().getCityorroad(),"road");
                    roadBlock.get(card.getTop().getCityorroad()).mergeBlock(roadBlock.get(tmp));


                }

//                road.get(card.getTop().getCityorroad()).addAll(road.get(card.getRig().getCityorroad()));
//                roadEdge.get(card.getTop().getCityorroad()).addAll(roadEdge.get(card.getRig().getCityorroad()));
//                card.setRigRoadOrCity(card.getTop().getCityorroad());
            }else if(cardTopJson.get("bot").equals("true")){
                if(card.getTop().getCityorroad()!=card.getBot().getCityorroad()){
                    int tmp = card.getBot().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,roadBlock.get(card.getBot().getCityorroad()),card.getTop().getCityorroad(),card.getBot().getCityorroad(),"road");
                    roadBlock.get(card.getTop().getCityorroad()).mergeBlock(roadBlock.get(tmp));


                }

//                road.get(card.getTop().getCityorroad()).addAll(road.get(card.getBot().getCityorroad()));
//                roadEdge.get(card.getTop().getCityorroad()).addAll(roadEdge.get(card.getBot().getCityorroad()));
//                card.setBotRoadOrCity(card.getTop().getCityorroad());
            }else if(cardTopJson.get("lef").equals("true")){

                if(card.getTop().getCityorroad()!=card.getLef().getCityorroad()){
                    int tmp = card.getLef().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,roadBlock.get(card.getLef().getCityorroad()),card.getTop().getCityorroad(),card.getLef().getCityorroad(),"road");
                    roadBlock.get(card.getTop().getCityorroad()).mergeBlock(roadBlock.get(tmp));


                }

//                road.get(card.getTop().getCityorroad()).addAll(road.get(card.getLef().getCityorroad()));
//                roadEdge.get(card.getTop().getCityorroad()).addAll(roadEdge.get(card.getLef().getCityorroad()));
//                card.setLefRoadOrCity(card.getTop().getCityorroad());
            }
        }

        //RightEdge 的连接遍历
        JSONObject cardRightJson = JSONObject.parseObject(card.getRig().getConnect());
        if (card.getRig().getType().equals("city")){
             if(cardRightJson.get("bot").equals("true")){
                 if(card.getRig().getCityorroad()!=card.getBot().getCityorroad()){

                     int tmp = card.getBot().getCityorroad();
                     updateBlockAllEdgeOwn(nmap,cityBlock.get(card.getBot().getCityorroad()),card.getRig().getCityorroad(),card.getBot().getCityorroad(),"city");
                     cityBlock.get(card.getRig().getCityorroad()).mergeBlock(cityBlock.get(tmp));


                 }

//                city.get(card.getRig().getCityorroad()).addAll(city.get(card.getBot().getCityorroad()));
//                cityEdge.get(card.getRig().getCityorroad()).addAll(cityEdge.get(card.getBot().getCityorroad()));
//                card.setBotRoadOrCity(card.getRig().getCityorroad());
            }else if(cardRightJson.get("lef").equals("true")){

                 if(card.getRig().getCityorroad()!=card.getLef().getCityorroad()){
                     int tmp = card.getLef().getCityorroad();
                     updateBlockAllEdgeOwn(nmap,cityBlock.get(card.getLef().getCityorroad()),card.getRig().getCityorroad(),card.getLef().getCityorroad(),"city");
                     cityBlock.get(card.getRig().getCityorroad()).mergeBlock(cityBlock.get(tmp));


                 }

//                city.get(card.getRig().getCityorroad()).addAll(city.get(card.getLef().getCityorroad()));
//                cityEdge.get(card.getRig().getCityorroad()).addAll(cityEdge.get(card.getLef().getCityorroad()));
//                card.setLefRoadOrCity(card.getRig().getCityorroad());
            }
        }else if(card.getRig().getType().equals("road")){
            if(cardRightJson.get("bot").equals("true")){
                if(card.getRig().getCityorroad()!=card.getBot().getCityorroad()){
                    int tmp = card.getBot().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,roadBlock.get(card.getBot().getCityorroad()),card.getRig().getCityorroad(),card.getBot().getCityorroad(),"road");
                    roadBlock.get(card.getRig().getCityorroad()).mergeBlock(roadBlock.get(tmp));


                }

//                road.get(card.getRig().getCityorroad()).addAll(road.get(card.getBot().getCityorroad()));
//                roadEdge.get(card.getRig().getCityorroad()).addAll(roadEdge.get(card.getBot().getCityorroad()));
//                card.setBotRoadOrCity(card.getRig().getCityorroad());
            }else if(cardRightJson.get("lef").equals("true")){

                if(card.getRig().getCityorroad()!=card.getLef().getCityorroad()){
//                    System.out.println("SBPC"+roadBlock.get(card.getLef().getCityorroad()).getPoints());
                    int tmp = card.getLef().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,roadBlock.get(card.getLef().getCityorroad()),card.getRig().getCityorroad(),card.getLef().getCityorroad(),"road");
                    roadBlock.get(card.getRig().getCityorroad()).mergeBlock(roadBlock.get(tmp));
//                    System.out.println(card.getRig().getCityorroad()+"吃了"+card.getLef().getCityorroad());


                }

//                road.get(card.getRig().getCityorroad()).addAll(road.get(card.getLef().getCityorroad()));
//                roadEdge.get(card.getRig().getCityorroad()).addAll(roadEdge.get(card.getLef().getCityorroad()));
//                card.setLefRoadOrCity(card.getRig().getCityorroad());
            }
        }

        //BottomEdge链接的遍历
        JSONObject cardBottomJson = JSONObject.parseObject(card.getBot().getConnect());
        if (card.getBot().getType().equals("city")){
            if(cardBottomJson.get("lef").equals("true")){
                if(card.getBot().getCityorroad()!=card.getLef().getCityorroad()){
                    int tmp = card.getLef().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,cityBlock.get(card.getLef().getCityorroad()),card.getBot().getCityorroad(),card.getLef().getCityorroad(),"city");
                    cityBlock.get(card.getBot().getCityorroad()).mergeBlock(cityBlock.get(tmp));


                }

//                city.get(card.getBot().getCityorroad()).addAll(city.get(card.getLef().getCityorroad()));
//                cityEdge.get(card.getBot().getCityorroad()).addAll(cityEdge.get(card.getLef().getCityorroad()));
//                card.setLefRoadOrCity(card.getBot().getCityorroad());
            }
        }else if(card.getBot().getType().equals("road")){

            if(cardBottomJson.get("lef").equals("true")){
                if(card.getBot().getCityorroad()!=card.getLef().getCityorroad()){
                    int tmp = card.getLef().getCityorroad();
                    updateBlockAllEdgeOwn(nmap,roadBlock.get(card.getLef().getCityorroad()),card.getBot().getCityorroad(),card.getLef().getCityorroad(),"road");
                    roadBlock.get(card.getBot().getCityorroad()).mergeBlock(roadBlock.get(tmp));


                }

//                road.get(card.getBot().getCityorroad()).addAll(road.get(card.getLef().getCityorroad()));
//                roadEdge.get(card.getBot().getCityorroad()).addAll(roadEdge.get(card.getLef().getCityorroad()));
//                card.setLefRoadOrCity(card.getBot().getCityorroad());
            }
        }



        //TODO 在这要判断是否有城或者路完成得分，然后进行计分

        //这里暂时解决方法是把isFull的赋值为true了，不过还是会报错说因为那时候edgeMap还没全部好


        if(card.getTop().getCityorroad()!=-1){
            if (card.getTop().getType().equals("city")){

                cityBlock.get(card.getTop().getCityorroad()).start(point);
                Integer score = cityBlock.get(card.getTop().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = cityBlock.get(card.getTop().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }


            }else if(card.getTop().getType().equals("road")){

                roadBlock.get(card.getTop().getCityorroad()).start(point);
                Integer score = roadBlock.get(card.getTop().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = roadBlock.get(card.getTop().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }
        }
        if(card.getRig().getCityorroad()!=-1){
            if (card.getRig().getType().equals("city")){

                cityBlock.get(card.getRig().getCityorroad()).start(point);
                Integer score = cityBlock.get(card.getRig().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = cityBlock.get(card.getRig().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }else if(card.getRig().getType().equals("road")){

                roadBlock.get(card.getRig().getCityorroad()).start(point);
                Integer score = roadBlock.get(card.getRig().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = roadBlock.get(card.getRig().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }
        }
        if(card.getBot().getCityorroad()!=-1){
            if (card.getBot().getType().equals("city")){

                cityBlock.get(card.getBot().getCityorroad()).start(point);
                Integer score = cityBlock.get(card.getBot().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = cityBlock.get(card.getBot().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }else if(card.getBot().getType().equals("road")){

                roadBlock.get(card.getBot().getCityorroad()).start(point);
                Integer score = roadBlock.get(card.getBot().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = roadBlock.get(card.getBot().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }
        }
        if(card.getLef().getCityorroad()!=-1){
            if (card.getLef().getType().equals("city")){

                cityBlock.get(card.getLef().getCityorroad()).start(point);
                Integer score = cityBlock.get(card.getLef().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = cityBlock.get(card.getLef().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }else if(card.getLef().getType().equals("road")){

                roadBlock.get(card.getLef().getCityorroad()).start(point);
                Integer score = roadBlock.get(card.getLef().getCityorroad()).getScoreAll();
                if(score!=0){
                    ArrayList<String> playsID = roadBlock.get(card.getLef().getCityorroad()).getPlayerIdArray();
                    for(String ID :playsID){
                        playerScore.put(ID,playerScore.get(ID)+score);
                    }
                }
            }
        }

//        if(card.getTop().getCityorroad()!=-1){
//            System.out.println("Top");
//            System.out.println("city"+card.getTop().getCityorroad()+cityBlock.get(card.getTop().getCityorroad()));
//        }
//        if(card.getBot().getCityorroad()!=-1){
//            System.out.println("Bot");
//            System.out.println("road"+card.getTop().getCityorroad()+roadBlock.get(card.getBot().getCityorroad()));
//        }
//        System.out.println("》》》》》》》》"+point+"《《《《《《《《《");
//        System.out.println("Top:"+card.getTop().getType()+card.getTop().getCityorroad());
//        System.out.println("Right:"+card.getRig().getType()+card.getRig().getCityorroad());
//        System.out.println("Bottom:"+card.getBot().getType()+card.getBot().getCityorroad());
//        System.out.println("Left:"+card.getLef().getType()+card.getLef().getCityorroad());
//        System.out.println("城镇0得分情况："+cityBlock.get(0));
        updateCanPutPositionList(point);
//        nmap[x][y] = card;
        puzzle.setmPuzzle(nmap);

    }


    public HashMap<Integer, Block> getUnappropriatedCityBlock(){
        HashMap<Integer, Block> unappropriatedCityBlock = new HashMap<Integer, Block>();
//        ArrayList<Block> unappropriatedCityBlock = new ArrayList<>();
//        System.out.println("获取"+cityBlock);
        for(Integer i = 0;i<cityBlock.size();i++){
            if (cityBlock.get(i).scoreRecordIsempty()){
                unappropriatedCityBlock.put(i,cityBlock.get(i));
            }
        }
        return unappropriatedCityBlock;
    }

    public HashMap<Integer, Block> getUnappropriatedRoadBlock(){
        HashMap<Integer, Block> unappropriatedRoadBlock = new HashMap<Integer, Block>();
//        ArrayList<Block> unappropriatedBlock = new ArrayList<>();
//        System.out.println("获取"+roadBlock);
        for(Integer i = 0;i<roadBlock.size();i++){
            if (roadBlock.get(i).scoreRecordIsempty()){
                unappropriatedRoadBlock.put(i,roadBlock.get(i));
            }
        }
        return unappropriatedRoadBlock;
    }

    public void appropriated(int key,String ownerId,String type){
        if(type.equals("city")){
            cityBlock.get(key).record(ownerId);
        }else if (type.equals("road")){
            roadBlock.get(key).record(ownerId);
        }
    }

    @Override
    public String toString() {
        return "RoomManager{" +
                "players=" + players +
                ", activePlayerNum=" + activePlayerNum +
                ", puzzle=" + puzzle +
                ", cardLibrary=" + Arrays.toString(cardLibrary) +
                ", gameLog=" + gameLog +
                ", gameResult=" + gameResult +
                ", cityBlock=" + cityBlock +
                ", roadBlock=" + roadBlock +
                ", mapUtil=" + mapUtil +
                '}';
    }

    public Integer getActivePlayerNum() {
        return activePlayerNum;
    }

    public void setActivePlayerNum(Integer activePlayerNum) {
        this.activePlayerNum = activePlayerNum;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Card[] getCardLibrary() {
        return cardLibrary;
    }

    public void setCardLibrary(Card[] cardLibrary) {
        this.cardLibrary = cardLibrary;
    }

    public GameLog getGameLog() {
        return gameLog;
    }

    public void setGameLog(GameLog gameLog) {
        this.gameLog = gameLog;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public Integer getNowTurnNum() {
        return nowTurnNum;
    }

    public void setNowTurnNum(Integer nowTurnNum) {
        this.nowTurnNum = nowTurnNum;
    }

    public Integer getNowPlayerNum() {
        return nowPlayerNum;
    }

    public void setNowPlayerNum(Integer nowPlayerNum) {
        this.nowPlayerNum = nowPlayerNum;
    }

    public HashMap<String, Integer> getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(HashMap<String, Integer> playerScore) {
        this.playerScore = playerScore;
    }

    public JSONArray getPlayerScoreToJSONArray(){
        JSONArray res = new JSONArray();
        for (int i=0;i<players.size();i++) {
            JSONObject temp = new JSONObject();
            String accountNum = players.get(i).getAccountNum();
            Integer score = playerScore.get(accountNum);
            temp.put("playAccountNum",accountNum);
            temp.put("Score",score);
            temp.put("pieceRemainNum",8);
            res.add(temp);
        }
        return res;
    }

    public Integer getLibRemainNum(){
        return cardLibrary.length - nowLibNum;
    }


}
