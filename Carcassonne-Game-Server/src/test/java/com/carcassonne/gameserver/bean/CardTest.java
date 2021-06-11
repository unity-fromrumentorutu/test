package com.carcassonne.gameserver.bean;

import com.carcassonne.gameserver.util.CardLibraryUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testToString() {
    }

    @Test
    void getPictureUrl() {
    }

    @Test
    void setPictureUrl() {
    }

    @Test
    void getTopEdgeId() {
    }

    @Test
    void setTopEdgeId() {
    }

    @Test
    void getBotEdgeId() {
    }

    @Test
    void setBotEdgeId() {
    }

    @Test
    void getLefEdgeId() {
    }

    @Test
    void setLefEdgeId() {
    }

    @Test
    void getRigEdgeId() {
    }

    @Test
    void setRigEdgeId() {
    }

    @Test
    void getRotate() {
    }

    @Test
    void setRotate() {
    }

    @Test
    void setId() {
    }

    @Test
    void getId() {
    }

    @Test
    void testSetId() {
    }

    @Test
    void getTop() {
    }

    @Test
    void setTop() {
    }

    @Test
    void getBot() {
    }

    @Test
    void setBot() {
    }

    @Test
    void getLef() {
    }

    @Test
    void setLef() {
    }

    @Test
    void getRig() {
    }

    @Test
    void setRig() {
    }

    @Test
    void setTopRoadOrCity() {
    }

    @Test
    void setBotRoadOrCity() {
    }

    @Test
    void setLefRoadOrCity() {
    }

    @Test
    void setRigRoadOrCity() {
    }

    @Test
    void setEdges() {
    }

    @Test
    void rotate() {
        Card or = new Card();
        or.setBot(new Edge(1,"city","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.setLef(new Edge(2,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setRig(new Edge(3,"road","{\"top\":\"false\",\"rig\":\"true\",\"bot\":\"false\",\"lef\":\"true\"}"));
        or.setTop(new Edge(4,"glass","{\"top\":\"false\",\"rig\":\"false\",\"bot\":\"false\",\"lef\":\"false\"}"));
        or.rotate(1);
    }

    @Test
    void getLib(){
        ArrayList<Card> arrayList =CardLibraryUtil.getCardLibrary();
        System.out.println(arrayList);
    }

}