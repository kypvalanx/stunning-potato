package core;

import static core.Variables.VAR_SET;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PCGVarLoader {
    private String tabName;

    public Map<String, String> getVars() {
        return vars;
    }

    private Map<String, String> vars = new HashMap<>();

    public PCGVarLoader(File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();

            Element basic = (Element) root.getElementsByTagName("basics").item(0);

            Node bab = basic.getElementsByTagName("bab").item(0);
            tabName = basic.getElementsByTagName("name").item(0).getTextContent().toLowerCase();

            vars.put("bab", ""+Integer.parseInt(bab.getTextContent()));

            extractSkills(root, "abilities", "ability", "short", "", "modifier");


            extractInititive(root);


            extractSkills(root, "skills", "skill", "name", "d20 + ", "skill_mod");

            extractSkills(root, "saving_throws", "saving_throw", "short", "d20 + ", "total");

            extractCombatManeuvers(root);

            extractUnarmedStrike(root);


            extractWeapons(root);

            vars.put("range", "bab + dex");
            vars.put("melee", "bab + str");

            NodeList classes = ((Element)((Element)root.getElementsByTagName("basics").item(0)).getElementsByTagName("classes").item(0)).getElementsByTagName("class");

            for(int i = 0; i < classes.getLength(); i++){
                Element item = (Element)classes.item(i);
                String className = getElementByTagPath(item, "name").getTextContent().toLowerCase();
                String classLevel = getElementByTagPath(item, "level").getTextContent().toLowerCase();
                vars.put(className + " level", classLevel);
            }

            vars.put("level", getElementByTagPath(root, "basics", "classes", "levels_total").getTextContent().toLowerCase());


            System.out.println(root);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    private void extractWeapons(Element root) {
        NodeList weapons = ((Element)root.getElementsByTagName("weapons").item(0)).getElementsByTagName("weapon");

        for(int i = 0; i < weapons.getLength(); i++){
            Node item = weapons.item(i);

            if(!(item instanceof Element)){
                continue;
            }

            Element weapon = (Element) item;

            String name = cleanVarKey(getElementByTagPath(weapon, "common", "name", "output").getTextContent().toLowerCase());
            String range = getElementByTagPath(weapon, "common", "critical", "range").getTextContent().toLowerCase();
            String multiplier = getElementByTagPath(weapon, "common", "critical", "multiplier").getTextContent().toLowerCase();
            String reach = getElementByTagPath(weapon, "common", "reach").getTextContent().toLowerCase();
            String damage = getElementByTagPath(weapon, "common", "damage").getTextContent().toLowerCase();
            String attack = getElementByTagPath(weapon, "common", "to_hit", "total_hit").getTextContent().toLowerCase();

            NodeList specificHandedWeapons = getElementByTagPath(weapon, "melee").getChildNodes();
            for(int j = 0; j < specificHandedWeapons.getLength(); j++){

                Node rawNode = specificHandedWeapons.item(j);
                if(!(rawNode instanceof Element)){
                    continue;
                }

                Element specificHandedWeapon = ((Element) rawNode);
                String suffix = specificHandedWeapon.getTagName();

                Element damageNode = getElementByTagPath(specificHandedWeapon, "damage");
                Element toHitNode = getElementByTagPath(specificHandedWeapon, "to_hit");
                if(damageNode!=null && toHitNode!=null) {
                    String specificDamage = damageNode.getTextContent().toLowerCase();
                    String specificAttack = toHitNode.getTextContent().toLowerCase();

                    if(!"N/A".equals(specificAttack) && !"N/A".equals(specificDamage)){
                        createWeaponRoll(name, "_"+suffix, range, multiplier, reach, specificDamage, specificAttack);
                    }
                }
            }

            createWeaponRoll(name, "", range, multiplier, reach, damage, attack);


        }
    }

    private void createWeaponRoll(String name, String suffix, String range, String multiplier, String reach, String damage, String attack) {
        String value = "'" + name + " : " + range + "/" + multiplier + "x : "+ reach + " and 'attack: and "+rangeSize(range)+"c20 " + attack + " and 'damage: and " + damage;
        vars.put(name + suffix, value);
    }

    private void extractUnarmedStrike(Element root) {
        String unarmedName = "unarmed";
        String unarmedCrit = getElementByTagPath(root, "weapons", "unarmed", "critical").getTextContent().toLowerCase();
        String unarmedReach = getElementByTagPath(root, "weapons", "unarmed",  "reach").getTextContent().toLowerCase();
        String unarmedDamage = getElementByTagPath(root, "weapons", "unarmed", "damage").getTextContent().toLowerCase();
        String unarmedAttack = getElementByTagPath(root, "weapons", "unarmed",  "total").getTextContent().toLowerCase();

        String unarmedValue = "'" + unarmedName + " : " + unarmedCrit + " : "+ unarmedReach + " and 'attack and c20 + " + unarmedDamage + " and 'damage: and " + unarmedAttack;
        vars.put(unarmedName, unarmedValue);
    }

    private void extractCombatManeuvers(Element root) {
        NodeList combatManeuvers = ((Element) root.getElementsByTagName("attack").item(0)).getElementsByTagName("cmb");

        for(int i = 0; i < combatManeuvers.getLength(); i++){
            Element combatManeuver = (Element)combatManeuvers.item(i);

            String[] tokens = combatManeuver.getTagName().split("_");

            if(tokens.length>1 && "attack".equals(tokens[1])){
                String key = tokens[0];
                String value = "d20 + "+Integer.parseInt(combatManeuver.getTextContent());
                vars.put(key, value);
            }
        }
    }

    private void extractSkills(Element root, String skills2, String skill2, String name2, String s, String skill_mod) {
        NodeList skills = ((Element) root.getElementsByTagName(skills2).item(0)).getElementsByTagName(skill2);
        for (int i = 0; i < skills.getLength(); i++) {
            Element skill = (Element) skills.item(i);

            String key = skill.getElementsByTagName(name2).item(0).getTextContent().toLowerCase();
            String value = s + Integer.parseInt(skill.getElementsByTagName(skill_mod).item(0).getTextContent());
            vars.put(key, value);
        }
    }

    private void extractInititive(Element root) {
        Node initiative = ((Element) root.getElementsByTagName("initiative").item(0)).getElementsByTagName("total").item(0);

        vars.put("init", "d20 + "+Integer.parseInt(initiative.getTextContent()));
        vars.put("initiative", "d20 + "+Integer.parseInt(initiative.getTextContent()));
    }

    private int rangeSize(String range) {
        return 21-Integer.parseInt(range.split("-")[0]);
    }

    private String cleanVarKey(String dirtyKey) {
        return dirtyKey.replace("*", "");
    }

    private Element getElementByTagPath(Element element, String ... path) {
        Element cursor = element;
        for(String tag : path){
            if(cursor == null){
                break;
            }
            NodeList nodeList = cursor.getChildNodes();
            cursor = null;
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node instanceof Element && tag.equals(((Element)node).getTagName())){
                    cursor = (Element) node;
                    break;
                }
            }
        }
        return cursor;
    }

    public String add() {
        if (tabName == null) {
            throw new IllegalStateException("pcgen file missing TABNAME");
        }
        Variables.putP(VAR_SET, tabName);

        for (Map.Entry<String, String> vp : vars.entrySet()) {
            Variables.putP(vp.getKey(), vp.getValue());
        }


        return tabName;
    }
}