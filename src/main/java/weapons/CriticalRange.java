package weapons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CriticalRange {
	private String textMultiplier;
	private List<Integer> multipliers;
	private int range;
	private boolean hasCritical = true;

	public CriticalRange(){

	}

	public CriticalRange(String criticalRange) {
		if(criticalRange == null || "—".equals(criticalRange)){
			hasCritical = false;
			return;
		}
		multipliers = new ArrayList<>();
		String[] token = criticalRange.split("/");

		range = 1;
		if(token.length == 2){
			String[] rangeTok = token[0].split("-");
			if(rangeTok.length == 2) {
				range += Integer.parseInt(rangeTok[1]) - Integer.parseInt(rangeTok[0]);
			}else {
				try {
					multipliers.add(Integer.parseInt(rangeTok[0].substring(1)));
				} catch (NumberFormatException e){
					textMultiplier = rangeTok[0];
				}
			}try{
			multipliers.add(Integer.parseInt(token[1].substring(1)));
		} catch (NumberFormatException e){
			textMultiplier = token[1];
		}
		}else{
			try{
			multipliers.add(Integer.parseInt(token[0].substring(1)));
	} catch (NumberFormatException e){
		textMultiplier = token[0];
	}
		}

	}

	public String toString(){
		if(hasCritical){
			return buildTextRange()+buildMultiplier();
		}else {
			return "-";
		}
	}

	private String buildMultiplier() {
		if(textMultiplier != null){
			return textMultiplier;
		}
		return multipliers.stream().map(multiplier -> "x" + multiplier).collect(Collectors.joining("/"));
	}

	private String buildTextRange() {
		if(range == 1){
			return "";
		}

		return (21 - range) + "-20/";
	}

	public List<Integer> getMultipliers() {
		return multipliers;
	}

	public void setMultipliers(List<Integer> multipliers) {
		this.multipliers = multipliers;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public boolean isHasCritical() {
		return hasCritical;
	}

	public void setHasCritical(boolean hasCritical) {
		this.hasCritical = hasCritical;
	}

	public String getTextMultiplier() {
		return textMultiplier;
	}

	public void setTextMultiplier(String textMultiplier) {
		this.textMultiplier = textMultiplier;
	}
}
