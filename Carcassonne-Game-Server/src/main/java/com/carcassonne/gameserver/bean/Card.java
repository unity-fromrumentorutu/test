package com.carcassonne.gameserver.bean;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Bean;

/**
 * 卡片类
 *
 */
public class Card {

    public static String PICTURE_PATH = "http://47.119.130.124/carcassonneImg/card/";

    private Integer id;  //卡片的id，对应数据库的id
    private String pictureUrl;//这张卡片图片资源的URL
    private String topEdgeId;
    private String botEdgeId;
    private String lefEdgeId;
    private String rigEdgeId;
    private int count;



    private Edge top; // 上边
    private Edge bot;// 下边
    private Edge lef;// 上边
    private Edge rig;// 上边
    private String rotate; //旋转情况

    public Card(Integer id, String pictureUrl, String topEdgeId, String botEdgeId, String lefEdgeId, String rigEdgeId) {
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.topEdgeId = topEdgeId;
        this.botEdgeId = botEdgeId;
        this.lefEdgeId = lefEdgeId;
        this.rigEdgeId = rigEdgeId;
    }

    public Card(){}

    public JSONObject toJsonString(){
        JSONObject card = new JSONObject();
        card.put("id",id);
        JSONObject top = new JSONObject();
        top.put("type",this.top.getType());
        top.put("position",this.top.getPosition());
        top.put("connect",this.top.getConnect());
        card.put("top",top);

        JSONObject bot = new JSONObject();
        bot.put("type",this.bot.getType());
        bot.put("position",this.bot.getPosition());
        bot.put("connect",this.bot.getConnect());
        card.put("bot",bot);

        JSONObject lef = new JSONObject();
        lef.put("type",this.lef.getType());
        lef.put("position",this.lef.getPosition());
        lef.put("connect",this.lef.getConnect());
        card.put("lef",lef);

        JSONObject rig = new JSONObject();
        rig.put("type",this.rig.getType());
        rig.put("position",this.rig.getPosition());
        rig.put("connect",this.rig.getConnect());
        card.put("rig",rig);

        return card;
    }


    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", topEdgeId='" + topEdgeId + '\'' +
                ", botEdgeId='" + botEdgeId + '\'' +
                ", lefEdgeId='" + lefEdgeId + '\'' +
                ", rigEdgeId='" + rigEdgeId + '\'' +
                ", count=" + count +
                ", top=" + top +
                ", bot=" + bot +
                ", lef=" + lef +
                ", rig=" + rig +
                ", rotate='" + rotate + '\'' +
                '}';
    }

    public void formatPictureURL(){
        pictureUrl = PICTURE_PATH + pictureUrl + ".jpg";
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTopEdgeId() {
        return topEdgeId;
    }

    public void setTopEdgeId(String topEdgeId) {
        this.topEdgeId = topEdgeId;
    }

    public String getBotEdgeId() {
        return botEdgeId;
    }

    public void setBotEdgeId(String botEdgeId) {
        this.botEdgeId = botEdgeId;
    }

    public String getLefEdgeId() {
        return lefEdgeId;
    }

    public void setLefEdgeId(String lefEdgeId) {
        this.lefEdgeId = lefEdgeId;
    }

    public String getRigEdgeId() {
        return rigEdgeId;
    }

    public void setRigEdgeId(String rigEdgeId) {
        this.rigEdgeId = rigEdgeId;
    }

    public String getRotate() {
        return rotate;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Edge getTop() {
        return top;
    }

    public void setTop(Edge top) {
        this.top = top;
    }

    public Edge getBot() {
        return bot;
    }

    public void setBot(Edge bot) {
        this.bot = bot;
    }

    public Edge getLef() {
        return lef;
    }

    public void setLef(Edge lef) {
        this.lef = lef;
    }

    public Edge getRig() {
        return rig;
    }

    public void setRig(Edge rig) {
        this.rig = rig;
    }

    public void setTopRoadOrCity(int i){
        this.top.setCityorroad(i);
    }

    public void setBotRoadOrCity(int i){
        this.bot.setCityorroad(i);
    }

    public void setLefRoadOrCity(int i){
        this.lef.setCityorroad(i);
    }

    public void setRigRoadOrCity(int i){
        this.rig.setCityorroad(i);
    }

    public void setEdges(Edge topEdge, Edge rightEdge,
                         Edge bottomEdge,Edge leftEdge){
        this.top = topEdge;
        this.lef = leftEdge;
        this.rig = rightEdge;
        this.bot = bottomEdge;
    }
    /**
     * 旋转函数
     */
    public void rotate(int count){
        Edge tmpEdge = new Edge(1,"City",null);
        String tmpEdgeId = "";
        for(int i=0;i<count%4;i++){
            tmpEdge = top;
            tmpEdgeId = topEdgeId;
            top = lef;
            topEdgeId = lefEdgeId;
            lef = bot;
            lefEdgeId = botEdgeId;
            bot = rig;
            botEdgeId = rigEdgeId;
            rig = tmpEdge;
            rigEdgeId = tmpEdgeId;
            JSONObject cardTopJson = JSONObject.parseObject(top.getConnect());
            String tmpConnect = cardTopJson.get("lef").toString();
            cardTopJson.put("lef",cardTopJson.get("bot"));
            cardTopJson.put("bot",cardTopJson.get("rig"));
            cardTopJson.put("rig",cardTopJson.get("top"));
            cardTopJson.put("top",tmpConnect);
            top.setConnect(cardTopJson.toString());

            JSONObject cardRigJson = JSONObject.parseObject(rig.getConnect());
            tmpConnect = cardRigJson.get("lef").toString();
            cardRigJson.put("lef",cardRigJson.get("bot"));
            cardRigJson.put("bot",cardRigJson.get("rig"));
            cardRigJson.put("rig",cardRigJson.get("top"));
            cardRigJson.put("top",tmpConnect);
            rig.setConnect(cardRigJson.toString());

            JSONObject cardBotJson = JSONObject.parseObject(bot.getConnect());
            tmpConnect = cardBotJson.get("lef").toString();
            cardBotJson.put("lef",cardBotJson.get("bot"));
            cardBotJson.put("bot",cardBotJson.get("rig"));
            cardBotJson.put("rig",cardBotJson.get("top"));
            cardBotJson.put("top",tmpConnect);
            bot.setConnect(cardBotJson.toString());

            JSONObject cardLefJson = JSONObject.parseObject(lef.getConnect());
            tmpConnect = cardLefJson.get("lef").toString();
            cardLefJson.put("lef",cardLefJson.get("bot"));
            cardLefJson.put("bot",cardLefJson.get("rig"));
            cardLefJson.put("rig",cardLefJson.get("top"));
            cardLefJson.put("top",tmpConnect);
            lef.setConnect(cardLefJson.toString());


//            System.out.println("top:"+top.getConnect());
//            System.out.println("rig:"+rig.getConnect());
//            System.out.println("bot:"+bot.getConnect());
//            System.out.println("lef:"+lef.getConnect());
        }
    }
}
