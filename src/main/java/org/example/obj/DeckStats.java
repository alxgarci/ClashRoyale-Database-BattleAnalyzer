package org.example.obj;

import java.util.ArrayList;

public class DeckStats {
    private ArrayList<String> deck;
    private int timesUsed;
    private int totalCrowns;
    private int totalTrophies;

    public DeckStats(ArrayList<String> deck, int timesUsed, int totalCrowns, int totalTrophies) {
        this.deck = deck;
        this.timesUsed = timesUsed;
        this.totalCrowns = totalCrowns;
        this.totalTrophies = totalTrophies;
    }

    public int getTotalTrophies() {
        return totalTrophies;
    }

    public void setTotalTrophies(int totalTrophies) {
        this.totalTrophies = totalTrophies;
    }

    public ArrayList<String> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<String> deck) {
        this.deck = deck;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public int getTotalCrowns() {
        return totalCrowns;
    }

    public void setTotalCrowns(int totalCrowns) {
        this.totalCrowns = totalCrowns;
    }

    @Override
    public String toString() {
        return "[DeckStats] DECK= " + getDeckToString() +
                "\n          [STATS] TIMES USED=" + timesUsed +
                ", CROWNS=" + totalCrowns +
                ", TROPHIES=" + totalTrophies;
    }

    public String getDeckToString() {
        StringBuilder res = new StringBuilder();
        int c = 0;
        for (String d: deck) {
            if (c == 0) {
                res.append(d);
            } else if (c == 3) {
                res.append("\n                  ").append(d);
            } else {
                res.append(" // ").append(d);
            }
            c++;
        }
        return res.toString();
    }
}
