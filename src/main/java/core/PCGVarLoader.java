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
    private final String author;
    private String tabName;

    public Map<String, String> getVars() {
        return vars;
    }

    private Map<String, String> vars = new HashMap<>();

    public PCGVarLoader(File file, String author) {
        this.author = author;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            document.getDocumentElement().normalize();

            Element root = document.getDocumentElement();

            Element basic = (Element) root.getElementsByTagName("basics").item(0);

            Node bab = basic.getElementsByTagName("bab").item(0);
            tabName = basic.getElementsByTagName("name").item(0).getTextContent().split(" ")[0].toLowerCase();

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

                if("attack".equals(tokens[1])){
                    String key = tokens[0];
                    String value = "d20 + "+Integer.parseInt(combatManeuver.getTextContent());
                    vars.put(key, value);
                }
            }

            System.out.println(root);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

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