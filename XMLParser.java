import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser {

    public static ArrayList<Location> parseBoardNeighbors(Node parent) throws Exception {
        ArrayList<Location> neighbors = new ArrayList<Location>();
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node c = children.item(i);
            String nodeName = c.getNodeName();

            switch(nodeName) {
                case "neighbor": {
                    String name = c.getAttributes().getNamedItem("name").getNodeValue();
                    System.out.println("   " + name);
                }
            }
        }

        return neighbors;
    }

    public static ArrayList<Role> getSetRoles(Node set) {
        ArrayList<Role> roles = new ArrayList<Role>();
        NodeList parts = set.getChildNodes();
        for (int i = 0; i < parts.getLength(); i++) {
            Node part = parts.item(i);
            if (part.getNodeName() == "part") {
                String roleName = part.getAttributes().getNamedItem("name").getNodeValue();
                int roleLevel = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
                String roleLine = "";
                NodeList partChildren = part.getChildNodes();
                for (int j = 0; j < partChildren.getLength(); j++) {
                    if (partChildren.item(j).getNodeName() == "line") {
                        roleLine = partChildren.item(j).getTextContent();
                        break;
                    }
                }
                Role role = new Role(roleLevel, false, roleName, roleLine);
                roles.add(role);
            }
        }
        return roles;
    }

    public static void parseBoardHelper(Node parent, String typer) throws Exception {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node c = children.item(i);
            String nodeName = c.getNodeName();

            if (!nodeName.equals("#text")) {
                String name = c.getAttributes().getNamedItem(typer).getNodeValue();
                System.out.println("  " + name);
            }
        }
    }

    private static int getChildCount(Node parent) {
        int count = 0;
        NodeList children = parent.getChildNodes();
        for (int k = 0; k < children.getLength(); k++) {
            if (children.item(k).getNodeName() != "#text") {
                count++;
            }
        }
        return count;
    }

    public static ArrayList<Location> parseBoard() throws Exception {
        ArrayList<Location> locations = new ArrayList<Location>();

        DocumentBuilderFactory myDomFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder myBuilder = myDomFactory.newDocumentBuilder();
        Document myDoc = myBuilder.parse("board.xml");

        NodeList sets = myDoc.getElementsByTagName("set");

        for (int i = 0; i < sets.getLength(); i++) {
            System.out.println("Printing Set " + i);
            Node set = sets.item(i);

            NodeList attributes = set.getChildNodes();
            
            String setName = set.getAttributes().getNamedItem("name").getNodeValue();
            System.out.println("Set: " + setName);

            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                String attributeName = attribute.getNodeName();

                switch (attributeName) {
                    case "neighbors": {
                        System.out.println("Neighbors: ");
                        parseBoardHelper(attribute, "name");
                        break;
                    }
                    case "area": {
                        String x = attribute.getAttributes().getNamedItem("x").getNodeValue();
                        String y = attribute.getAttributes().getNamedItem("y").getNodeValue();
                        String h = attribute.getAttributes().getNamedItem("h").getNodeValue();
                        String w = attribute.getAttributes().getNamedItem("w").getNodeValue();
                        System.out.println("Area: x=" + x + ", y=" + y + ", h=" + h + ", w=" + w);
                        break;
                    }
                    case "takes": {
                        int num_takes = getChildCount(attribute);
                        System.out.println(num_takes + " Takes");
                        break;
                    }
                    case "parts": {
                        System.out.println("Parts:");
                        parseBoardHelper(attribute, "name");
                        break;
                    }
                }
            }
        }

        return locations;
    }



    public static ArrayList<Scene> parseCards() {
        return null;
    }

    public static void main(String[] args) throws Exception{
        parseBoard();
    }
}