package com.carcassonne.gameserver.bean;


import java.security.PublicKey;
import java.util.Objects;

/**
 * 玩家
 */
public class Player extends User{
    private String userId;
    private Boolean isReady;
    private Card hand;
    private Integer score;
    private Boolean isStart;
    private String gameState;
    private Integer inRoomNum;
    private Integer chessNum;

    public Player(Boolean isReady, Card hand, Integer score, Boolean isStart, String gameState,Integer inRoomNum,User user) {
        super(user.getAccountNum(),user.getPassword(),user.getSex(),user.getLevel(),user.getNickname(),user.getHeadPictureURL());
        this.isReady = isReady;
        this.hand = hand;
        this.score = score;
        this.isStart = isStart;
        this.gameState = gameState;
        this.inRoomNum = inRoomNum;
    }
    public Player(String nid){
        userId=nid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return userId.equals(player.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public String getUserId() {
        return userId;
    }

    public Integer getInRoomNum() {
        return inRoomNum;
    }

    public void setInRoomNum(Integer inRoomNum) {
        this.inRoomNum = inRoomNum;
    }

    public Boolean getReady() {
        return isReady;
    }

    public void setReady(Boolean ready) {
        isReady = ready;
    }

    public Card getHand() {
        return hand;
    }

    public void setHand(Card hand) {
        this.hand = hand;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }
}
