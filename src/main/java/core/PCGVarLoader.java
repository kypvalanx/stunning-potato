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

            NodeList abilities = ((Element) root.getElementsByTagName("abilities").item(0)).getElementsByTagName("ability");

            for(int i = 0; i < abilities.getLength(); i++){
                Element ability = (Element)abilities.item(i);

                String key = ability.getElementsByTagName("short").item(0).getTextContent().toLowerCase();
                String value = ""+Integer.parseInt(ability.getElementsByTagName("modifier").item(0).getTextContent());
                vars.put(key, value);
            }


            Node initiative = ((Element) root.getElementsByTagName("initiative").item(0)).getElementsByTagName("total").item(0);

            vars.put("init", "d20 + "+Integer.parseInt(initiative.getTextContent()));


            NodeList skills = ((Element) root.getElementsByTagName("skills").item(0)).getElementsByTagName("skill");
            for(int i = 0; i < skills.getLength(); i++){
                Element skill = (Element)skills.item(i);

                String key = skill.getElementsByTagName("name").item(0).getTextContent().toLowerCase();
                String value = "d20 + "+Integer.parseInt(skill.getElementsByTagName("skill_mod").item(0).getTextContent());
                vars.put(key, value);
            }

            NodeList savingThrows = ((Element) root.getElementsByTagName("saving_throws").item(0)).getElementsByTagName("saving_throw");

            for(int i = 0; i < savingThrows.getLength(); i++){
                Element savingThrow = (Element)savingThrows.item(i);

                String key = savingThrow.getElementsByTagName("short").item(0).getTextContent().toLowerCase();
                String value = "d20 + "+Integer.parseInt(savingThrow.getElementsByTagName("total").item(0).getTextContent());
                vars.put(key, value);
            }

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

            String unarmedName = "unarmed";
            String unarmedCrit = getElementByTagPath(root, "weapons", "unarmed", "critical").getTextContent().toLowerCase();
            String unarmedReach = getElementByTagPath(root, "weapons", "unarmed",  "reach").getTextContent().toLowerCase();
            String unarmedDamage = getElementByTagPath(root, "weapons", "unarmed", "damage").getTextContent().toLowerCase();
            String unarmedAttack = getElementByTagPath(root, "weapons", "unarmed",  "total").getTextContent().toLowerCase();

            String unarmedValue = "'" + unarmedName + " : " + unarmedCrit + " : "+ unarmedReach + " and 'attack and c20 + " + unarmedDamage + " and 'damage: and " + unarmedAttack;
            vars.put(unarmedName, unarmedValue);


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

                String value = "'" + name + " : " + range + "/" + multiplier + "x : "+ reach + " and 'attack and "+rangeSize(range)+"c20 + " + attack + " and 'damage: and " + damage;
                vars.put(name, value);


            }

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