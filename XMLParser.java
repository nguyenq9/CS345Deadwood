import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser {

    public static XMLParser xmlParser = new XMLParser();

    private XMLParser() {

    }

    private ArrayList<Location> parseSetNeighbors(Node set, ArrayList<Location> locations) throws Exception {
        ArrayList<Location> adjacentLocations = new ArrayList<Location>();
        NodeList neighbors = set.getChildNodes();
        for (int i = 0; i < neighbors.getLength(); i++) {
            Node neighbor = neighbors.item(i);
            if (neighbor.getNodeName().equals("neighbor")) {
                String locationName = neighbor.getAttributes().getNamedItem("name").getNodeValue();
                for (int j = 0; j < locations.size(); j++) {
                    if (locations.get(j).getLocationName().equals(locationName)) {
                        adjacentLocations.add(locations.get(j));
                    }
                }
            }
        }
        return adjacentLocations;
    }

    private ArrayList<Role> parseRoles(Node set, boolean onCard) throws Exception {
        ArrayList<Role> roles = new ArrayList<Role>();
        NodeList parts = set.getChildNodes();
        for (int i = 0; i < parts.getLength(); i++) {
            Node part = parts.item(i);
            if (part.getNodeName() == "part") {
                String roleName = part.getAttributes().getNamedItem("name").getNodeValue();
                int roleLevel = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
                String roleLine = "";
                int[] roleArea = new int[4];
                NodeList partChildren = part.getChildNodes();
                for (int j = 0; j < partChildren.getLength(); j++) {
                    Node partChild = partChildren.item(j);
                    String childName = partChild.getNodeName();
                    switch (childName) {
                        case "line":
                            roleLine = partChild.getTextContent();
                            break;
                        case "area":
                            roleArea[0] = Integer.parseInt(partChild.getAttributes().getNamedItem("x").getNodeValue());
                            roleArea[1] = Integer.parseInt(partChild.getAttributes().getNamedItem("y").getNodeValue());
                            roleArea[2] = Integer.parseInt(partChild.getAttributes().getNamedItem("h").getNodeValue());
                            roleArea[3] = Integer.parseInt(partChild.getAttributes().getNamedItem("w").getNodeValue());
                            break;
                    }
                }
                Role role = new Role(roleLevel, onCard, roleName, roleLine, roleArea);
                roles.add(role);
            }
        }
        return roles;
    }

    private ArrayList<Location> getNeighbors(NodeList attributes, ArrayList<Location> locations) throws Exception {
        ArrayList<Location> adjacentLocations = new ArrayList<Location>();
        for (int j = 0; j < attributes.getLength(); j++) {
            Node attribute = attributes.item(j);
            String attributeName = attribute.getNodeName();
            if (attributeName.equals("neighbors")) {
                adjacentLocations = parseSetNeighbors(attribute, locations);
            }
        }
        return adjacentLocations;
    }

    private int[] getArea(NodeList attributes) throws Exception {
        int[] area = new int[4];
        for (int j = 0; j < attributes.getLength(); j++) {
            Node attribute = attributes.item(j);
            String attributeName = attribute.getNodeName();
            if (attributeName.equals("area")) {
                area[0] = Integer.parseInt(attribute.getAttributes().getNamedItem("x").getNodeValue());
                area[1] = Integer.parseInt(attribute.getAttributes().getNamedItem("y").getNodeValue());
                area[2] = Integer.parseInt(attribute.getAttributes().getNamedItem("h").getNodeValue());
                area[3] = Integer.parseInt(attribute.getAttributes().getNamedItem("w").getNodeValue());
            }
        }
        return area;
    }

    private int getChildCount(Node parent) {
        int count = 0;
        NodeList children = parent.getChildNodes();
        for (int k = 0; k < children.getLength(); k++) {
            if (children.item(k).getNodeName() != "#text") {
                count++;
            }
        }
        return count;
    }

    private int[] parseUpgradeDollarCosts(NodeList attributes) throws Exception {
        int[] upgradeCosts = new int[5];
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String attributeName = attribute.getNodeName();
            if (attributeName.equals("upgrades")) {
                NodeList upgrades = attributes.item(i).getChildNodes();
                for (int j = 0; j < upgrades.getLength(); j++) {
                    Node upgrade = upgrades.item(j);
                    String upgradeName = upgrade.getNodeName();
                    if (upgradeName.equals("upgrade")) {
                        String currency = upgrade.getAttributes().getNamedItem("currency").getNodeValue();
                        if (currency.equals("dollar")) {
                            int rank = Integer.parseInt(upgrade.getAttributes().getNamedItem("level").getNodeValue());
                            int amount = Integer.parseInt(upgrade.getAttributes().getNamedItem("amt").getNodeValue());
                            upgradeCosts[rank - 2] = amount;
                        }
                    }
                }
                break;
            }
        }
        return upgradeCosts;
    }

    private int[] parseUpgradeCreditCosts(NodeList attributes) throws Exception {
        int[] upgradeCosts = new int[5];
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String attributeName = attribute.getNodeName();
            if (attributeName.equals("upgrades")) {
                NodeList upgrades = attributes.item(i).getChildNodes();
                for (int j = 0; j < upgrades.getLength(); j++) {
                    Node upgrade = upgrades.item(j);
                    String upgradeName = upgrade.getNodeName();
                    if (upgradeName.equals("upgrade")) {
                        String currency = upgrade.getAttributes().getNamedItem("currency").getNodeValue();
                        if (currency.equals("credit")) {
                            int rank = Integer.parseInt(upgrade.getAttributes().getNamedItem("level").getNodeValue());
                            int amount = Integer.parseInt(upgrade.getAttributes().getNamedItem("amt").getNodeValue());
                            upgradeCosts[rank - 2] = amount;
                        }
                    }
                }
                break;
            }
        }
        return upgradeCosts;
    }

    private Trailer parseTrailer(Node location, ArrayList<Location> locations) throws Exception {
        NodeList attributes = location.getChildNodes();
        ArrayList<Location> adjacentLocations = getNeighbors(attributes, locations);
        int[] area = getArea(attributes);
        return new Trailer(adjacentLocations, area);
    }

    private CastingOffice parseOffice(Node location, ArrayList<Location> locations) throws Exception {
        NodeList attributes = location.getChildNodes();
        ArrayList<Location> adjacentLocations = getNeighbors(attributes, locations);
        int[] area = getArea(attributes);
        int[] upgradeDollarCosts = parseUpgradeDollarCosts(attributes);
        int[] upgradeCreditCosts = parseUpgradeCreditCosts(attributes);
        return new CastingOffice(adjacentLocations, area, upgradeDollarCosts, upgradeCreditCosts);
    }

    private int[][] parseShotCounterArea(Node shotCounter) throws Exception{
        int[][] area = new int[3][4];
    
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                area[i][j] = 0;
            }
        }
        int index = 0;

        NodeList takes = shotCounter.getChildNodes();
        for (int i = 0; i < takes.getLength(); i++) {
            Node take = takes.item(i);
            if (take.getNodeName() == "take") {
                NodeList takeChildren = take.getChildNodes();
                for (int j = 0; j < takeChildren.getLength(); j++) {
                    Node takeChild = takeChildren.item(j);
                    String childName = takeChild.getNodeName();
                    switch (childName) {
                        case "area":
                            area[index][0] = Integer.parseInt(takeChild.getAttributes().getNamedItem("x").getNodeValue());
                            area[index][1] = Integer.parseInt(takeChild.getAttributes().getNamedItem("y").getNodeValue());
                            area[index][2] = Integer.parseInt(takeChild.getAttributes().getNamedItem("h").getNodeValue());
                            area[index][3] = Integer.parseInt(takeChild.getAttributes().getNamedItem("w").getNodeValue());
                            break;
                    }
                }
                index++;
            }
        }
        return area;
    }

    public Board parseBoard() throws Exception {
        ArrayList<Location> locations = new ArrayList<Location>();
        ArrayList<Set> sets = new ArrayList<Set>();

        DocumentBuilderFactory myDomFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder myBuilder = myDomFactory.newDocumentBuilder();
        Document myDoc = myBuilder.parse("board.xml");
        NodeList setNodes = myDoc.getElementsByTagName("set");

        for (int i = 0; i < setNodes.getLength(); i++) {
            
            Node setNode = setNodes.item(i);
            NodeList attributes = setNode.getChildNodes();

            String setName = setNode.getAttributes().getNamedItem("name").getNodeValue();
            ArrayList<Role> setRoles = new ArrayList<Role>();
            int setMaxShots = 0;
            int[] setArea = new int[4];
            int[][] shotCounterArea = new int[3][4];

            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                String attributeName = attribute.getNodeName();

                switch (attributeName) {
                    case "area": {
                        setArea[0] = Integer.parseInt(attribute.getAttributes().getNamedItem("x").getNodeValue());
                        setArea[1] = Integer.parseInt(attribute.getAttributes().getNamedItem("y").getNodeValue());
                        setArea[2] = Integer.parseInt(attribute.getAttributes().getNamedItem("h").getNodeValue());
                        setArea[3] = Integer.parseInt(attribute.getAttributes().getNamedItem("w").getNodeValue());
                        break;
                    }
                    case "takes": {
                        setMaxShots = getChildCount(attribute);
                        shotCounterArea = parseShotCounterArea(attribute);
                        break;
                    }
                    case "parts": {
                        setRoles = parseRoles(attribute, false);
                        break;
                    }
                }
            }
            Set set = new Set(setName, setRoles, setMaxShots, setArea, shotCounterArea);
            sets.add(set);
            locations.add(set);
        }

        Node trailerNode = myDoc.getElementsByTagName("trailer").item(0);
        Trailer trailer = parseTrailer(trailerNode, locations);
        locations.add(trailer);

        Node officeNode = myDoc.getElementsByTagName("office").item(0);
        CastingOffice office = parseOffice(officeNode, locations);
        locations.add(office);

        for (int i = 0; i < setNodes.getLength(); i++) {
            Node set = setNodes.item(i);
            NodeList attributes = set.getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                String attributeName = attribute.getNodeName();

                if (attributeName.equals("neighbors")) {
                    locations.get(i).setAdjacentLocations(parseSetNeighbors(attribute, locations));
                }
            }
        }

        Board board = new Board(locations, sets, trailer, office);

        return board;
    }

    public ArrayList<Scene> parseCards() throws Exception{
        ArrayList<Scene> cardScenes = new ArrayList<Scene>();
        DocumentBuilderFactory myDomFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder myBuilder = myDomFactory.newDocumentBuilder();
        Document myDoc = myBuilder.parse("cards.xml");

        NodeList cards = myDoc.getElementsByTagName("card");
        
        for (int i = 0; i < cards.getLength(); i++) {
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            String cardImg = card.getAttributes().getNamedItem("img").getNodeValue();
            int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
            ArrayList<Role> cardRoles = parseRoles(card, true);
            int cardNumber;
            String cardDescription;

            NodeList attributes = card.getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node attribute = attributes.item(j);
                if (attribute.getNodeName().equals("scene")) {
                    cardNumber = Integer.parseInt(attribute.getAttributes().getNamedItem("number").getNodeValue());
                    cardDescription = attribute.getTextContent();
                    
                    Scene scene = new Scene(budget, cardRoles, cardNumber, cardImg, cardName, cardDescription);
                    cardScenes.add(scene);
                }
            }
         }
        return cardScenes;
    }
}