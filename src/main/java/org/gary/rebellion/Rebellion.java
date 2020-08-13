package org.gary.rebellion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.jetbrains.annotations.NotNull;


public class Rebellion {

	private long id=-1;
    private String name = "Kintargo";
	private int supporters = 0;
    private int treasury = 0;
    private int population = 0;
    private int notoriety;
    private int maxLevel;
    private int members;
    private int dangerRating = 20;
    private Focus focus = Focus.NONE;
    private int demagogueLoyaltyBonus;
    private int partisanSecurityBonus;
    private int recruiterLvlBonus;
    private int sentinelLoyaltyBonus;
    private int sentinelSecurityBonus;
    private int sentinelSecrecyBonus;
    private int spymasterSecrecyBonus;
    private boolean hasStrategist;

    public Rebellion(ResultSet resultSet) {
try {
    int i = 1;
    id = resultSet.getInt(i++);
    name = resultSet.getString(i++);
    supporters = resultSet.getInt(i++);
            treasury = resultSet.getInt(i++);
            population = resultSet.getInt(i++);
            notoriety = resultSet.getInt(i++);
            maxLevel = resultSet.getInt(i++);
            members = resultSet.getInt(i++);
            dangerRating = resultSet.getInt(i++);
            focus = Focus.valueOf(resultSet.getString(i++));
            demagogueLoyaltyBonus = resultSet.getInt(i++);
            partisanSecurityBonus = resultSet.getInt(i++);
            recruiterLvlBonus = resultSet.getInt(i++);
            sentinelLoyaltyBonus = resultSet.getInt(i++);
            sentinelSecurityBonus = resultSet.getInt(i++);
            sentinelSecrecyBonus = resultSet.getInt(i++);
            spymasterSecrecyBonus = resultSet.getInt(i++);
            hasStrategist = resultSet.getBoolean(i++);

} catch (SQLException e){
    e.printStackTrace();
}
    }

    public Rebellion() {

    }

    @JsonIgnore
    public String getSheet() {
        return "Rebellion Rank: " + getRebellionRank() + "\n" +
                "Rebellion Max Level: " + getMaxLevel() + "\n" +
                "Rebellion Members: " + getMembers() + "\n" +
                "Rebellion Supporters: " + getSupporters() + "\n" +
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
            demagogueLoyaltyBonus = 0;
        }else if(attributes.size() == 1){
            demagogueLoyaltyBonus = Integer.parseInt(attributes.get(0));
        }else if(attributes.size() == 2){
            demagogueLoyaltyBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        }else if(attributes.size() == 6){
            demagogueLoyaltyBonus = Math.max(Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(5)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,1,2,6");
        }
    }
    public void setPartisan(List<String> attributes){
        if(attributes.isEmpty()){
            partisanSecurityBonus = 0;
        }else if(attributes.size() == 1){
            partisanSecurityBonus = Integer.parseInt(attributes.get(0));
        }else if(attributes.size() == 2){
            partisanSecurityBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        }else if(attributes.size() == 6){
            demagogueLoyaltyBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(4)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,1,2,6");
        }
    }
    public void setRecruiter(int level){
        recruiterLvlBonus = level;
    }

    public void setSentinal(List<String> attributes){
        if(attributes.isEmpty()){
            sentinelLoyaltyBonus = 0;
            sentinelSecurityBonus = 0;
            sentinelSecrecyBonus = 0;
        }else if(attributes.size() == 3){
            sentinelLoyaltyBonus = Integer.parseInt(attributes.get(0));
            sentinelSecurityBonus = Integer.parseInt(attributes.get(1));
            sentinelSecrecyBonus = Integer.parseInt(attributes.get(2));
        }else if(attributes.size() == 6){
            sentinelLoyaltyBonus = Math.max(Integer.parseInt(attributes.get(2)),Integer.parseInt(attributes.get(5)));
            sentinelSecurityBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(4)));
            sentinelSecrecyBonus = Math.max(Integer.parseInt(attributes.get(1)),Integer.parseInt(attributes.get(3)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,3,6");
        }
    }

    public void setSpyMaster(List<String> attributes){
        if(attributes.isEmpty()){
            spymasterSecrecyBonus = 0;
        }else if(attributes.size() == 1){
            spymasterSecrecyBonus = Integer.parseInt(attributes.get(0));
        }else if(attributes.size() == 2) {
            spymasterSecrecyBonus = Math.max(Integer.parseInt(attributes.get(0)),Integer.parseInt(attributes.get(1)));
        }else if(attributes.size() == 6){
            spymasterSecrecyBonus = Math.max(Integer.parseInt(attributes.get(1)),Integer.parseInt(attributes.get(3)));
        }else{
            throw new IllegalArgumentException("Illegal list length: 0,1,2,6");
        }
    }
    public void setStrategist(List<String> attributes){
        hasStrategist = !attributes.isEmpty();
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

    @JsonIgnore
    public int getLoyaltyBonus(){
        return getCheckBonus(Focus.LOYALTY) + getDemagogueLoyaltyBonus() + getSentinelLoyaltyBonus();
    }

    @JsonIgnore
    public int getSecrecyBonus(){
        return getCheckBonus(Focus.SECRECY) + getSpymasterSecrecyBonus() + getSentinelSecrecyBonus();
    }

    @JsonIgnore
    public int getSecurityBonus(){
        return getCheckBonus(Focus.SECURITY) + getPartisanSecurityBonus() + getSentinelSecurityBonus();
    }

    private int getCheckBonus(Focus focus) {
        return focus == this.focus ? getFocusCheckBonus() : getSecondaryCheckBonus();
    }

    @JsonIgnore
    public int getSecondaryCheckBonus() {
        int rank = getRebellionRank();

        return rank/3;
    }

    @JsonIgnore
    public int getFocusCheckBonus() {
        int rank = getRebellionRank();

        return (rank/2)+2;
    }

    @NotNull
    @JsonIgnore
    public String getAvailableActions() {
        return "Change Officer Role, Dismiss Team, Guarantee Event, Lie Low, Recruit Supporters, Recruit Team, Special Action, Upgrade Team";
    }

    @JsonIgnore
    public int getAvailableTeams() {
        //int rank = getRebellionRank();
        return 0;//(rank+11)/13+(rank+5)/12+(rank)/11+1;
    }

    @JsonIgnore
    public int getMaxTeams() {
        int rank = getRebellionRank();
        return (rank+14)/17+(rank+5)/10+(rank+5)/13+(rank)/11+2;
    }

    @JsonIgnore
    public int getActions() {
        int rank = getRebellionRank();
        return (rank+11)/13+(rank+5)/12+(rank)/11+1;
    }

    public int getNotoriety() {
        return notoriety;
    }

    @JsonIgnore
    public int getMinTreasury() {
        return getRebellionRank() * 10;
    }

    public int getTreasury() {
        return treasury;
    }

    public void setName(String name){
        this.name = name;
    }

    @NotNull
    @JsonIgnore
    public String getRebellion() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public int getSupporters() {
        return supporters;
    }

    public int getMembers() {
        return members;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @JsonIgnore
    public int getRebellionRank() {
        int supporters = getSupporters();
        return Math.min(_getRank(supporters), maxLevel);
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

    public void addPopulation(int people) {
        population += people;
    }

    public void addNotoriety(int notoriety) {
        this.notoriety += notoriety;
    }

    public void addMembers(int members) {
        this.members += members;
    }

    public void setMaxRank(int maxRank) {
        this.maxLevel = maxRank;
    }

    public int getDemagogueLoyaltyBonus() {
        return demagogueLoyaltyBonus;
    }

    public int getPartisanSecurityBonus() {
        return partisanSecurityBonus;
    }

    public int getRecruiterLvlBonus() {
        return recruiterLvlBonus;
    }

    public int getSentinelLoyaltyBonus() {
        return sentinelLoyaltyBonus;
    }

    public int getSentinelSecurityBonus() {
        return sentinelSecurityBonus;
    }

    public int getSentinelSecrecyBonus() {
        return sentinelSecrecyBonus;
    }

    public int getSpymasterSecrecyBonus() {
        return spymasterSecrecyBonus;
    }

    public boolean isHasStrategist() {
        return hasStrategist;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void prepare(PreparedStatement statement) throws SQLException {
        int i = 1;
        statement.setString(i++, name);
        statement.setInt(i++, supporters);
        statement.setInt(i++, treasury);
        statement.setInt(i++, population);
        statement.setInt(i++, notoriety);
        statement.setInt(i++, maxLevel);
        statement.setInt(i++, members);
        statement.setInt(i++, dangerRating);
        statement.setString(i++, focus.name());
        statement.setInt(i++, demagogueLoyaltyBonus);
        statement.setInt(i++, partisanSecurityBonus);
        statement.setInt(i++, recruiterLvlBonus);
        statement.setInt(i++, sentinelLoyaltyBonus);
        statement.setInt(i++, sentinelSecurityBonus);
        statement.setInt(i++, sentinelSecrecyBonus);
        statement.setInt(i++, spymasterSecrecyBonus);
        statement.setBoolean(i++, hasStrategist);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rebellion rebellion = (Rebellion) o;
        return id == rebellion.id &&
                supporters == rebellion.supporters &&
                treasury == rebellion.treasury &&
                population == rebellion.population &&
                notoriety == rebellion.notoriety &&
                maxLevel == rebellion.maxLevel &&
                members == rebellion.members &&
                dangerRating == rebellion.dangerRating &&
                demagogueLoyaltyBonus == rebellion.demagogueLoyaltyBonus &&
                partisanSecurityBonus == rebellion.partisanSecurityBonus &&
                recruiterLvlBonus == rebellion.recruiterLvlBonus &&
                sentinelLoyaltyBonus == rebellion.sentinelLoyaltyBonus &&
                sentinelSecurityBonus == rebellion.sentinelSecurityBonus &&
                sentinelSecrecyBonus == rebellion.sentinelSecrecyBonus &&
                spymasterSecrecyBonus == rebellion.spymasterSecrecyBonus &&
                hasStrategist == rebellion.hasStrategist &&
                Objects.equal(name, rebellion.name) &&
                focus == rebellion.focus;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, supporters, treasury, population, notoriety, maxLevel, members, dangerRating, focus, demagogueLoyaltyBonus, partisanSecurityBonus, recruiterLvlBonus, sentinelLoyaltyBonus, sentinelSecurityBonus, sentinelSecrecyBonus, spymasterSecrecyBonus, hasStrategist);
    }

    @Override
    public String toString() {
        return "Rebellion{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", supporters=" + supporters +
                ", treasury=" + treasury +
                ", population=" + population +
                ", notoriety=" + notoriety +
                ", maxLevel=" + maxLevel +
                ", members=" + members +
                ", dangerRating=" + dangerRating +
                ", focus=" + focus +
                ", demagogueLoyaltyBonus=" + demagogueLoyaltyBonus +
                ", partisanSecurityBonus=" + partisanSecurityBonus +
                ", recruiterLvlBonus=" + recruiterLvlBonus +
                ", sentinelLoyaltyBonus=" + sentinelLoyaltyBonus +
                ", sentinelSecurityBonus=" + sentinelSecurityBonus +
                ", sentinelSecrecyBonus=" + sentinelSecrecyBonus +
                ", spymasterSecrecyBonus=" + spymasterSecrecyBonus +
                ", hasStrategist=" + hasStrategist +
                '}';
    }
}
