package rebellion;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Rebellion {
    private int supporters = 0;
    private int treasury = 0;
    private int population = 0;
    private String rebellionName = "Kintargo";
    private int notoriety;
    private int rebellionMaxLevel;
    private int members;
    private int dangerRating = 20;
    private Focus focus = Focus.NONE;

    private int demagogueConOrChaBonus;
    private int partisanStrOrWisBonus;
    private int recruiterLvlBonus;
    private int sentinelConOrChaBonus;
    private int sentinelStrOrWisBonus;
    private int sentinelDexOrIntBonus;
    private int spymasterDexOrIntBonus;
    private boolean hasStrategist;

    private int loyaltyOther;
    private int secrecyOther;
    private int securityOther;

    public String getSheet() {
        return "Rebellion Rank: " + getRebellionRank() + "\n" +
                "Rebellion Max Level: " + getRebellionMaxLevel() + "\n" +
                "Rebellion Members: " + getMembers() + "\n" +
                "Rebellion Supporters: " + getRebellionSupporters() + "\n" +
                getRebellion() + " Population: " + getPopulation() + "\n" +
                "Rebellion Treasury: " + getTreasury() + "\n" +
                "Rebellion Min Treasury: " + getMinTreasury() + "\n" +
                "Notoriety: " + getNotoriety() + "\n" +
                "Rebellion Actions: " + getActions() + "\n" +
                "Max Teams: " + getMaxTeams() + "\n" +
                "Teams Available TBD: " + getAvailableTeams() + "\n" +
                "Available Actions TBD: " + getAvailableActions() + "\n" +
                "Focus: " + getFocus() + "\n" +
                "Loyalty Bonus: " + getLoyaltyBonus() + "\n" +
                "Secrecy Bonus: " + getSecrecyBonus() + "\n" +
                "Security Bonus: " + getSecurityBonus() + "\n" +
                "Danger Rating: " + getDangerRating() + "\n";
    }




    public void setDemagogue(List<String> attributes){
        if(attributes.isEmpty()){
            demagogueConOrChaBonus = 0;
        }else if(attributes.size() == 1){
            demagogueConOrChaBonus = Integer.parseInt(attributes.get(0));
        }else if(attributes.size() == 2){
            demagogueConOrChaBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        }else if(attributes.size() == 6){
            demagogueConOrChaBonus = Math.max(Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(5)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,1,2,6");
        }
    }
    public void setPartisan(List<String> attributes){
        if(attributes.isEmpty()){
            partisanStrOrWisBonus = 0;
        }else if(attributes.size() == 1){
            partisanStrOrWisBonus = Integer.parseInt(attributes.get(0));
        }else if(attributes.size() == 2){
            partisanStrOrWisBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        }else if(attributes.size() == 6){
            demagogueConOrChaBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(4)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,1,2,6");
        }
    }
    public void setRecruiter(int level){
        recruiterLvlBonus = level;
    }
    public void setSentinal(List<String> attributes){
        if(attributes.isEmpty()){
            sentinelConOrChaBonus = 0;
            sentinelStrOrWisBonus = 0;
            sentinelDexOrIntBonus = 0;
        }else if(attributes.size() == 3){
            sentinelConOrChaBonus = Integer.parseInt(attributes.get(0));
            sentinelStrOrWisBonus = Integer.parseInt(attributes.get(1));
            sentinelDexOrIntBonus = Integer.parseInt(attributes.get(2));
        }else if(attributes.size() == 6){
            sentinelConOrChaBonus = Math.max(Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(5)));
            sentinelStrOrWisBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(4)));
            sentinelDexOrIntBonus = Math.max(Integer.parseInt(attributes.get(1)),Integer.parseInt(attributes.get(3)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,3,6");
        }
    }
    public void setSpyMaster(List<String> attributes){
        if(attributes.isEmpty()){
            spymasterDexOrIntBonus = 0;
        }else if(attributes.size() == 1){
            spymasterDexOrIntBonus = Integer.parseInt(attributes.get(0));
        }else if(attributes.size() == 2) {
            spymasterDexOrIntBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        }else if(attributes.size() == 6){
            spymasterDexOrIntBonus = Math.max(Integer.parseInt(attributes.get(1)),Integer.parseInt(attributes.get(3)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,1,2,6");
        }
    }
    public void setStrategist(List<String> attributes){
        if(attributes.isEmpty()){
            hasStrategist = false;
        }else{
            hasStrategist = true;
        }
    }

    public Focus getFocus() {
        return focus;
    }

    public void setFocus(Focus focus){
        this.focus = focus;
    }

    public void setDangerRating(int dangerRating) {
        this.dangerRating = dangerRating;
    }

    public void addDangerRating(int dangerRating) {
        this.dangerRating += dangerRating;
    }

    public int getDangerRating() {
        return dangerRating;
    }

    public int getLoyaltyBonus(){
        return getCheckBonus(Focus.LOYALTY) + getDemagogueConOrChaBonus() + getSentinelConOrChaBonus() + getLoyaltyOther();
    }

    public int getSecrecyBonus(){
        return getCheckBonus(Focus.SECRECY) + getSpymasterDexOrIntBonus() + getSentinelDexOrIntBonus() + getSecrecyOther();
    }

    public int getSecurityBonus(){
        return getCheckBonus(Focus.SECURITY) + getPartisanStrOrWisBonus() + getSentinelStrOrWisBonus() + getSecurityOther();
    }

    private int getCheckBonus(Focus focus) {
        int checkBonus;
        if (focus == this.focus) {
            checkBonus = getFocusCheckBonus();
        } else {
            checkBonus = getSecondaryCheckBonus();
        }
        return checkBonus;
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

    public void setRebellionName(String rebellionName){
        this.rebellionName = rebellionName;
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

    public void addLoyaltyOther(int loyaltyOther) {
        this.loyaltyOther += loyaltyOther;
    }

    public void setLoyaltyOther(int loyaltyOther) {
        this.loyaltyOther = loyaltyOther;
    }

    public void addSecrecyOther(int secrecyOther) {
        this.secrecyOther += secrecyOther;
    }

    public void setSecrecyOther(int secrecyOther) {
        this.secrecyOther = secrecyOther;
    }

    public void addSecurityOther(int securityOther) {
        this.securityOther += securityOther;
    }

    public void setSecurityOther(int securityOther) {
        this.securityOther = securityOther;
    }

    public int getDemagogueConOrChaBonus() {
        return demagogueConOrChaBonus;
    }

    public int getPartisanStrOrWisBonus() {
        return partisanStrOrWisBonus;
    }

    public int getRecruiterLvlBonus() {
        return recruiterLvlBonus;
    }

    public int getSentinelConOrChaBonus() {
        return sentinelConOrChaBonus;
    }

    public int getSentinelStrOrWisBonus() {
        return sentinelStrOrWisBonus;
    }

    public int getSentinelDexOrIntBonus() {
        return sentinelDexOrIntBonus;
    }

    public int getSpymasterDexOrIntBonus() {
        return spymasterDexOrIntBonus;
    }

    public boolean isHasStrategist() {
        return hasStrategist;
    }

    public int getLoyaltyOther() {
        return loyaltyOther;
    }

    public int getSecrecyOther() {
        return secrecyOther;
    }

    public int getSecurityOther() {
        return securityOther;
    }
}
