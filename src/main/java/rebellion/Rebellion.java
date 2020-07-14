package rebellion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Rebellion {
	private static final String DEFAULT_RESOURCE_FILE = "resources/save/rebellion.yaml";

    private String rebellionName = "Kintargo";
	private int supporters = 0;
    private int treasury = 0;
    private int population = 0;
    private int notoriety;
    private int rebellionMaxLevel;
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

	@NotNull
	public static Rebellion getRebellionFromFile() {
		File rebellionFile = new File(DEFAULT_RESOURCE_FILE);
		if (rebellionFile.canRead()) {

			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			try {
				return mapper.readValue(rebellionFile, Rebellion.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Rebellion();
	}

	public static void writeOutRebellionToFile(Rebellion rebellion){
		new File("resources/save/").mkdir();
		File rebellionFile = new File(DEFAULT_RESOURCE_FILE);
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
		try {
			if(!rebellionFile.createNewFile()){
				rebellionFile.delete();
				rebellionFile.createNewFile();
			}
			mapper.writeValue(rebellionFile, rebellion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@JsonIgnore
    public String getSheet() {
        return "Rebellion Rank: " + getRebellionRank() + "\n" +
                "Rebellion Max Level: " + getRebellionMaxLevel() + "\n" +
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

    public void setRebellionName(String rebellionName){
        this.rebellionName = rebellionName;
    }

    @NotNull
    @JsonIgnore
    public String getRebellion() {
        return rebellionName;
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

    public int getRebellionMaxLevel() {
        return rebellionMaxLevel;
    }

    @JsonIgnore
    public int getRebellionRank() {
        int supporters = getSupporters();
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
        this.rebellionMaxLevel = maxRank;
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
}
