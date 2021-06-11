package com.carcassonne.gameserver.bean;

/**
 * 边类
 */
public class Edge {
    public final static String CITY = "city";
    public final static String ROAD = "road";
    public final static String GRASS = "grass";

    private int id;
    private String type;
    private String position;
    private int cityorroad;
    private String connect;
    private String playerAccount;

    private String ownerId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }

    public String getPlayerAccount() {
        return playerAccount;
    }

    public int getCityorroad() {
        return cityorroad;
    }

    public void setCityorroad(int cityorroad) {
        this.cityorroad = cityorroad;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Edge(int id, String type, String connect) {
        this.id = id;
        this.type = type;
        this.connect = connect;
        this.cityorroad = -1;
    }



    public void setPlayerAccount(String ownerId){
        this.playerAccount = ownerId;
    }


    @Override
    public String toString() {
        return "Edge{" +
                "type='" + type + '\'' +
                ", position='" + position + '\'' +
                ", connect='" + connect + '\'' +
                '}';
    }
}
