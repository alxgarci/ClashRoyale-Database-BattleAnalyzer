package org.example.obj;

import org.example.App;

import java.util.ArrayList;

public class MatchHistory {
    private String opponentName;
    private int crowns;
    private int trophiesChange;

    private ArrayList<String> deck;
    private ArrayList<String> opponentDeck;
    private int id;

    @Override
    public String toString() {
        return "[MATCH ITEM]" +
                "Opponent='" + opponentName + '\'' +
                ", Crowns=" + crowns +
                ", Trophies=" + trophiesChange +
                ", Your Deck=" + getDeckToString(deck) +
                ", Opponent Deck=" + getDeckToString(opponentDeck);
    }

    public String toStringWrite() {
        return App.FILE_LINE_IDENTIFIER + opponentName + App.ARRAY_SPLITTER + crowns +
                App.ARRAY_SPLITTER + trophiesChange +
                App.ARRAY_SPLITTER + getDeckToString(deck) +
                App.ARRAY_SPLITTER + getDeckToString(opponentDeck);
    }

    private String getDeckToString(ArrayList<String> arrayList) {
        StringBuilder res = new StringBuilder();
        int c = 0;
        for (String d: arrayList) {
            if (c == 0) {
                res.append(d);
            } else {
                res.append(App.ARRAY_SPLITTER).append(d);
            }
            c++;
        }
        return res.toString();
    }

    public MatchHistory(String opponentName, int crowns, int trophiesChange, ArrayList<String> deck, ArrayList<String> opponentDeck) {
        this.opponentName = opponentName;
        this.crowns = crowns;
        this.trophiesChange = trophiesChange;
        this.deck = deck;
        this.opponentDeck = opponentDeck;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public int getCrowns() {
        return crowns;
    }

    public void setCrowns(int crowns) {
        this.crowns = crowns;
    }

    public int getTrophiesChange() {
        return trophiesChange;
    }

    public void setTrophiesChange(int trophiesChange) {
        this.trophiesChange = trophiesChange;
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<String> deck) {
        this.deck = deck;
    }

    public ArrayList<String> getOpponentDeck() {
        return opponentDeck;
    }

    public void setOpponentDeck(ArrayList<String> opponentDeck) {
        this.opponentDeck = opponentDeck;
    }
}
