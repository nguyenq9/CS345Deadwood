import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser {

    private static ArrayList<Location> parseBoardNeighbors(Node parent) throws Exception {
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

    private static ArrayList<Location> parseSetNeighbors(Node set, ArrayList<Location> locations) throws Exception {
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

    private static ArrayList<Role> parseRoles(Node set, boolean onCard) throws Exception {
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

    private static ArrayList<Location> getNeighbors(NodeList attributes, ArrayList<Location> locations) throws Exception {
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

    private static int[] getArea(NodeList attributes) throws Exception {
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

    private static int[] parseUpgradeDollarCosts(NodeList attributes) throws Exception {
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

    private static int[] parseUpgradeCreditCosts(NodeList attributes) throws Exception {
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

    private static Trailer parseTrailer(Node location, ArrayList<Location> locations) throws Exception {
        NodeList attributes = location.getChildNodes();
        ArrayList<Location> adjacentLocations = getNeighbors(attributes, locations);
        int[] area = getArea(attributes);
        return new Trailer(adjacentLocations, area);
    }

    private static CastingOffice parseOffice(Node location, ArrayList<Location> locations) throws Exception {
        NodeList attributes = location.getChildNodes();
        ArrayList<Location> adjacentLocations = getNeighbors(attributes, locations);
        int[] area = getArea(attributes);
        int[] upgradeDollarCosts = parseUpgradeDollarCosts(attributes);
        int[] upgradeCreditCosts = parseUpgradeCreditCosts(attributes);
        return new CastingOffice(adjacentLocations, area, upgradeDollarCosts, upgradeCreditCosts);
    }

    public static Board parseBoard() throws Exception {
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
                        break;
                    }
                    case "parts": {
                        setRoles = parseRoles(attribute, false);
                        break;
                    }
                }
            }
            Set set = new Set(setName, setRoles, setMaxShots, setArea);
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


    public static ArrayList<Scene> parseCards() throws Exception{
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

    // public static void main(String[] args) throws Exception{
    //     Board board = parseBoard();
    //     ArrayList<Location> locations = board.getLocations();
    //     System.out.println("Number of Locations: " + locations.size());
    //     for (int i = 0; i < locations.size(); i++) {
    //         Location location = locations.get(i);
    //         String locationType = location.getLocationType();
    //         switch (locationType) {
    //             case "Set":
    //                 Set set = (Set) location;
    //                 System.out.println(set.getLocationName() + ": ");
    //                 System.out.println("Area: x:" + set.getLocationArea()[0] + " y:" + set.getLocationArea()[1] + " h:" + set.getLocationArea()[2] + " w:" + set.getLocationArea()[3]);
    //                 System.out.println(set.getMaxShotCounters() + " Shot Counters");
    //                 System.out.println("Locations:");
    //                 for (int j = 0; j < set.getAdjacentLocations().size(); j++) {
    //                     System.out.println("  " + set.getAdjacentLocations().get(j).getLocationName());
    //                 }
    //                 System.out.println("Roles:");
    //                 for (int j = 0; j < set.getRoles().size(); j++) {
    //                     System.out.println("  " + set.getRoles().get(j).getRoleName() + ": " + set.getRoles().get(j).getRoleLine());
    //                     System.out.println("  Area: x:" + set.getRoles().get(j).getArea()[0] + " y:" + set.getRoles().get(j).getArea()[1] + " h:" + set.getRoles().get(j).getArea()[2] + " w:" + set.getRoles().get(j).getArea()[3]);
    //                 }
    //         }
    //         System.out.println("");
    //     }

    //     ArrayList<Scene> cardScenes = parseCards();
    //     System.out.println("Number of Cards: " + cardScenes.size());
    //     for (int i = 0; i < cardScenes.size(); i++) {
    //         Scene scene = cardScenes.get(i);
    //         int budget = scene.getBudget();
    //         String sceneName = scene.getSceneName();
    //         int sceneNumber = scene.getSceneNumber();
    //         String sceneImg = scene.getSceneImg();
    //         String sceneDescription = scene.getSceneDescription();
    //         ArrayList<Role> sceneRoles = scene.getRoles();

    //         System.out.println(sceneName + " | " + sceneImg + " | " + budget);
    //         System.out.println("#: " + sceneNumber);
    //         System.out.println("Description: " + sceneDescription);
    //         System.out.println("Roles:");
    //         for (int j = 0; j < sceneRoles.size(); j++) {
    //             System.out.println("  " + sceneRoles.get(j).getRoleName() + ": " + sceneRoles.get(j).getRoleLine());
    //             System.out.println("  Area: x:" + sceneRoles.get(j).getArea()[0] + " y:" + sceneRoles.get(j).getArea()[1] + " h:" + sceneRoles.get(j).getArea()[2] + " w:" + sceneRoles.get(j).getArea()[3]);
    //         }
            
    //     }
    // }
}