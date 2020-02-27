package rebellion;

import org.jetbrains.annotations.NotNull;

public class Rebellion {
    private int supporters = 0;
    private int treasury = 0;
    private int population = 0;
    private String rebellionName = "Kintaro";
    private int notoriety;
    private int rebellionMaxLevel;
    private int members;

    public String getSheet() {
        return new StringBuilder()
                .append("Rebellion Rank: ").append(getRebellionRank()).append("\n")
                .append("Rebellion Max Level: ").append(getRebellionMaxLevel()).append("\n")
                .append("Rebellion Members: " + getMembers() + "\n")
                .append("Rebellion Supporters: " + getRebellionSupporters() + "\n")
                .append(getRebellion() + " Population: " + getPopulation() + "\n")
                .append("Rebellion Treasury: " + getTreasury() + "\n")
                .append("Rebellion Min Treasury: " + getMinTreasury() + "\n")
                .append("Notoriety: " + getNotoriety() + "\n")
                .append("Rebellion Actions: " + getActions() + "\n")
                .append("Max Teams: " + getMaxTeams() + "\n")
                .append("Teams Available TBD: " + getAvailableTeams() + "\n")
                .append("Available Actions TBD: " + getAvailableActions() + "\n")
                .append("Focused Check Bonus: " + getFocusCheckBonus() + "\n")
                .append("Secondary Check Bonus: " + getSecondaryCheckBonus() + "\n").toString();
    }

    public int getSecondaryCheckBonus() {
        int rank = getRebellionRank();

        return rank/3;
    }

    @NotNull
    public int getFocusCheckBonus() {
        int rank = getRebellionRank();

        return (rank/2)+2;
    }

    @NotNull
    public String getAvailableActions() {
        return "Change Officer Role, Dismiss Team, Guarantee Event, Lie Low, Recruit Supporters, Recruit Team, Special Action, Upgrade Team";
    }

    @NotNull
    public int getAvailableTeams() {
        //int rank = getRebellionRank();
        return 0;//(rank+11)/13+(rank+5)/12+(rank)/11+1;
    }

    @NotNull
    public int getMaxTeams() {
        int rank = getRebellionRank();
        return (rank+14)/17+(rank+5)/10+(rank+5)/13+(rank)/11+2;
    }

    @NotNull
    public int getActions() {
        int rank = getRebellionRank();
        return (rank+11)/13+(rank+5)/12+(rank)/11+1;
    }

    @NotNull
    public int getNotoriety() {
        return notoriety;
    }

    @NotNull
    public int getMinTreasury() {
        return getRebellionRank() * 10;
    }

    @NotNull
    public int getTreasury() {
        return treasury;
    }

    @NotNull
    public String getRebellion() {
        return rebellionName;
    }

    @NotNull
    public int getPopulation() {
        return population;
    }

    @NotNull
    public int getRebellionSupporters() {
        return supporters;
    }

    @NotNull
    public int getMembers() {
        return members;
    }

    public int getRebellionMaxLevel() {
        return rebellionMaxLevel;
    }

    public int getRebellionRank() {
        int supporters = getRebellionSupporters();
        return Math.min(_getRank(supporters),rebellionMaxLevel);
    }

    private int _getRank(int supporters) {
        if(supporters <= 9){
            return 1;
        } else if(supporters <= 14){
            return 2;
        } else if(supporters <= 19){
            return 3;
        } else if(supporters <= 29){
            return 4;
        } else if(supporters <= 39){
            return 5;
        } else if(supporters <= 54){
            return 6;
        } else if(supporters <= 74){
            return 7;
        } else if(supporters <= 104){
            return 8;
        } else if(supporters <= 159){
            return 9;
        } else if(supporters <= 234){
            return 10;
        } else if(supporters <= 329){
            return 11;
        } else if(supporters <= 474){
            return 12;
        } else if(supporters <= 664){
            return 13;
        } else if(supporters <= 954){
            return 14;
        } else if(supporters <= 1349){
            return 15;
        } else if(supporters <= 1899){
            return 16;
        } else if(supporters <= 2699){
            return 17;
        } else if(supporters <= 3849){
            return 18;
        } else if(supporters <= 5349){
            return 19;
        } else {
            return 20;
        }
    }

    public void setSupporters(int supporters) {
        this.supporters = supporters;
    }

    public void addSupporters(int supporters) {
        this.supporters += supporters;
    }

    public void addTreasury(int money) {
        treasury += money;
    }

    public void setTreasury(int money) {
        treasury = money;
    }

    public void setPopulation(int people) {
        population = people;
    }

    public void addPopulation(int people) {
        population += people;
    }

    public void setNotoriety(int notoriety) {
        this.notoriety = notoriety;
    }

    public void addNotoriety(int notoriety) {
        this.notoriety += notoriety;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public void addMembers(int members) {
        this.members += members;
    }

    public void setMaxRank(int maxRank) {
        this.rebellionMaxLevel = maxRank;
    }
}
