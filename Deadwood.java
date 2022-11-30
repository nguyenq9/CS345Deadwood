import java.util.ArrayList;

public class Deadwood {
    private static SetUp setup = SetUp.setup;
    private static GameView gameView = GameView.gameView;

    private static ArrayList<PlayerController> players;
    private static PlayerController player;
    private static Set currSet;
    private static BoardController board;
    private static ArrayList<Scene> cardScenes;
    private static int maxDays;
    private static int activeScenes;
    private static int currDay;
    private static int playerCount;
    private static boolean hasMoved;
    private static boolean hasTaken;
    private static boolean hasWorked;
    private static boolean isWorking;
    private static boolean inOffice;
    private static boolean isTaking;
    private static boolean inSet;
    private static boolean isWrapped;
    private static boolean isMoving;
    private static boolean isUpgrading;
    private static int playerIndex = 0;

    public static void initializePlayers(int numPlayers) {
        playerCount = numPlayers;
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Blue");
        playerNames.add("Cyan");
        playerNames.add("Green");
        playerNames.add("Orange");
        playerNames.add("Pink");
        playerNames.add("Red");
        playerNames.add("Violet");
        playerNames.add("Yellow");
        players = setup.initializePlayers(playerCount, playerNames, board);
        player = players.get(playerIndex);
        resetBools();
    }

    public static void initialDisplay() { 
        GUIView.displayPlayerInfo(player.getPlayerName(), player.getPlayerRank(),
            player.getPlayerDollars(), player.getPlayerCredits(), player.getPlayerRehearsals());
        GUIView.displayCurrentDay(currDay);    
    }

    public static void main(String[] args) {
        // Handle Arguments
        double scale = 0.5;
        if (args.length > 0) {
            scale = Double.parseDouble(args[0]);
            if (scale < 0.35) {
                scale = 0.35;
            }
            if (scale > 3) {
                scale = 3;
            }
        }

        // Setup Game
        board = setup.initializeBoard();
        cardScenes = setup.initializeCards();
        setup.assignScenes(board, cardScenes);
        maxDays = setup.getMaxDays(playerCount);
        currDay = 1;
        activeScenes = 10;
        
        try {
            GUIView.launchApp(scale, board); // New GUI Implementation
        } catch (Exception e) {
            System.out.println("Error running application");
            e.printStackTrace();
        }
        
        System.exit(0); // Temporary
    }

    public static boolean verifyAction(ActionType action) {
        switch (action) {
            case MOVE:
                if (hasMoved) {
                    gameView.displayErrorMessage(ErrorType.ALREADY_MOVED);
                    return false;
                } else if (isWorking) {
                    gameView.displayErrorMessage(ErrorType.MOVE_WHILE_WORKING);
                    return false;
                } else if (hasWorked) {
                    gameView.displayErrorMessage(ErrorType.WORK_AND_MOVE);
                    return false;
                } else {
                    return true;
                }
            case TAKE:
                if (isWorking) {
                    gameView.displayErrorMessage(ErrorType.TAKE_WHILE_WORKING);
                    return false;
                } else if (!inSet){
                    return false;
                } else if (isWrapped) {
                    return false;
                } else {
                    return true;
                }
            case ACT:
                if (hasWorked) {
                    gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
                    return false;
                } else if (!isWorking) {
                    gameView.displayErrorMessage(ErrorType.ACT_WHILE_NOT_WORKING);
                    return false;
                } else if (hasTaken) {
                    gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
                    return false;
                } else {
                    return true;
                }
            case REHEARSE:
                if (hasWorked) {
                    gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
                    return true;
                } else if (!isWorking) {
                    gameView.displayErrorMessage(ErrorType.REHEARSE_WHILE_NOT_WORKING);
                    return false;
                } else if (hasTaken) {
                    gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
                    return false;
                } else {
                    return true;
                }
            case UPGRADE:
                if (inOffice) {
                    return true;
                } else {
                    gameView.displayErrorMessage(ErrorType.NOT_AT_OFFICE);
                    return false;
                }
            case UPGRADING:
                if (isUpgrading) {
                    return true;
                } else {
                    return false;
                }
            case ROLE:
                if (isTaking) {
                    return true;
                } else {
                    return false;
                }
            case LOCATION:
                if (isMoving) {
                    return true;
                } else {
                    return false;
                }
            case END:
                return true;
        }
        return false;
    }

    public static void resetBools() {
        hasMoved = false;
        hasTaken = false;
        hasWorked = false;
        isWorking = player.getPlayerIsWorking();
        inOffice = player.getPlayerLocation().getLocationName().toLowerCase().equals("office");
        currSet = getSet(player.getPlayerLocation());
        if (currSet != null) {
            inSet = true;
            isWrapped = currSet.getIsWrapped();
        }
        isTaking = false;
        isMoving = false;
        isUpgrading = false;
    }

    public static void moveButtonClicked() {
        if (verifyAction(ActionType.MOVE)) {
            isMoving = true;
            GUIView.highlightLocations(player.getPlayerLocation().getAdjacentLocations());
        }
    }

    public static void takeButtonClicked() {
        if (verifyAction(ActionType.TAKE) && !hasTaken) {
            isTaking = true;
            ArrayList<Role> roles = getAvailableRoles();
            if (roles != null) {
                for (Role i: roles) {
                    System.out.println(i.getRoleName());
                }
                GUIView.highlightRoles(roles);
            }
        }
    }

    public static void roleClicked(Role role) {
        if (verifyAction(ActionType.ROLE) && !hasTaken) {
            ArrayList<Role> roles = getAvailableRoles();
            if (roles.contains(role)) {
                player.setPlayerRole(role);
                player.setPlayerIsWorking(true);
                GUIView.movePlayerToRole(player.getPlayerName(),
                    player.getPlayerRole().getRoleName());
                GUIView.clearHighlightRoles(roles);
            }
            role.setIsTaken(true);
            hasTaken = true;
        }
    }

    public static void actButtonClicked() {
        if (verifyAction(ActionType.ACT) && !hasWorked) {
            // player rolls die
            // display success/failure
            // display role name and role line
            // decrement scene if last shot counter.
            hasWorked = true;
        }
    }

    public static void rehearseButtonClicked() {
        if (verifyAction(ActionType.REHEARSE) && !hasWorked) {
            hasWorked = true;
        }
    }

    public static void upgradeButtonClicked() {
        if (verifyAction(ActionType.UPGRADE)) {
            isUpgrading = true;
        }
    }

    public static void endTurnButtonClicked() {
        if (verifyAction(ActionType.END)) {
            playerIndex++;
            if (playerIndex >= playerCount) {
                playerIndex = 0;
            }
            player = players.get(playerIndex);
            GUIView.displayPlayerInfo(player.getPlayerName(), player.getPlayerRank(),
            player.getPlayerDollars(), player.getPlayerCredits(), player.getPlayerRehearsals());
            resetBools();
        }
    }

    public static void locationClicked(Location location) {
        if (verifyAction(ActionType.LOCATION) && !hasMoved) {
            if (player.getPlayerLocation().getAdjacentLocations().contains(location)) {
                GUIView.clearHightlightLocations(player
                .getPlayerLocation().getAdjacentLocations());
                Location prevLocation = player.getPlayerLocation();
                player.setPlayerLocation(location);
                GUIView.updatePlayerLocation(prevLocation);
                GUIView.updatePlayerLocation(location);
                currSet = getSet(location);
                if (location instanceof Set) {
                    inSet = true;
                }
                hasMoved = true;
            } else {
                return;
            }
        }
    }

    private static void upgradeChoiceClicked(int rank, String currency) {
        if (verifyAction(ActionType.UPGRADING)) {
            // do stuff
        }
    }

    public static Set getSet(Location location) {
        ArrayList<Set> boardSets = board.getBoardSets();
        Set currSet = null;
        for (int i = 0; i < boardSets.size(); i++) {
            if (boardSets.get(i).getLocationName().toLowerCase().equals(player.
                getPlayerLocation().getLocationName().toLowerCase())) {
                    currSet = boardSets.get(i);
            }
        }

        return currSet;
    }

    public static void decrementActiveScenes() {
        activeScenes--;
    }

    public static ArrayList<Role> getAvailableRoles() {
        ArrayList<Role> availableRoles = new ArrayList<Role>();
        if (currSet == null) {
            return availableRoles;
        }
        
        // Set roles
        for (int i = 0; i < currSet.getRoles().size(); i++) {
            if (currSet.getRoles().get(i).getRank() <= player.getPlayerRank() &&
                                        !currSet.getRoles().get(i).getIsTaken()) {
                availableRoles.add(currSet.getRoles().get(i));
            }
        }

        // Set scene roles
        Scene setScene = currSet.getScene();
        for (int i = 0; i < setScene.getRoles().size(); i++) {
            if ( setScene.getRoles().get(i).getRank() <= player.getPlayerRank() &&
                                        !setScene.getRoles().get(i).getIsTaken()) {
                availableRoles.add(setScene.getRoles().get(i));
            }
        }

        return availableRoles;
    }
}

// int playerIndex = 0;
// // main game loop
// for (int i = 0; i < maxDays; i++) {
//     currDay = i+1;
//     activeScenes = board.getBoardSets().size();
//     gameView.displayCurrentDay(currDay);

//     // this loop iterates when a day ends
//     while (activeScenes > 1) {
//         PlayerController player = players.get(playerIndex);
//         ActionType action;
//         gameView.displayTurn(player.getPlayerName());
//         boolean hasMoved = false;
//         boolean hasTaken = false;
//         boolean hasWorked = false;

//         // controls the players inputs and makes sure they are taking a valid turn
//         do {
//             boolean isWorking = player.getPlayerIsWorking();
//             action = GameView.getPlayerInput();
//             switch (action) {
//                 case HELP:
//                     gameView.displayHelp();
//                     break;
//                 case MOVE:
//                     if (hasMoved) {
//                         gameView.displayErrorMessage(ErrorType.ALREADY_MOVED);
//                     } else if (isWorking) {
//                         gameView.displayErrorMessage(ErrorType.MOVE_WHILE_WORKING);
//                         break;
//                     } else if (hasWorked) {
//                         gameView.displayErrorMessage(ErrorType.WORK_AND_MOVE);
//                     } else {
//                         hasMoved = player.move();
//                     }
//                     break;
//                 case TAKE:
//                     if (isWorking) {
//                         gameView.displayErrorMessage(ErrorType.TAKE_WHILE_WORKING);
//                     } else {
//                         hasTaken = player.take();
//                     }
//                     break;
//                 case ACT:
//                     if (hasWorked) {
//                         gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
//                     } else if (!isWorking) {
//                         gameView.displayErrorMessage(ErrorType.ACT_WHILE_NOT_WORKING);
//                     } else if (hasTaken) {
//                         gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
//                     } else {
//                         hasWorked = player.act();
//                     }
//                     break;
//                 case REHEARSE:
//                     if (hasWorked) {
//                         gameView.displayErrorMessage(ErrorType.ALREADY_WORKED);
//                     } else if (!isWorking) {
//                         gameView.displayErrorMessage(ErrorType.REHEARSE_WHILE_NOT_WORKING);
//                     } else if (hasTaken) {
//                         gameView.displayErrorMessage(ErrorType.TAKE_AND_WORK);
//                     } else {
//                         hasWorked = player.rehearse();
//                     }
//                     break;
//                 case UPGRADE:
//                     player.upgrade();
//                     break;
//                 case SCENE:
//                     gameView.displaySetInfo(player.getPlayerLocation().getLocationName(), board);
//                     break;
//                 case WHERE:
//                     gameView.displayCurrentLocation(players.get(playerIndex).getPlayerLocation().getLocationName());
//                     break;
//                 case WHO:
//                     gameView.displayCurrentDetails(player.getPlayerName(), player.getPlayerRank(), player.getPlayerDollars(), player.getPlayerCredits(),
//                     player.getPlayerRehearsals(), player.getPlayerLocation().getLocationName(), player.getPlayerRole());
//                     break;
//                 case DETAILS:
//                     for (int j = 0; j < players.size(); j++) {
//                         PlayerController p = players.get(j);
//                         gameView.displayDetails(p.getPlayerName(), p.getPlayerRank(), p.getPlayerDollars(), p.getPlayerCredits(),
//                             p.getPlayerRehearsals(), p.getPlayerLocation().getLocationName(), p.getPlayerRole());
//                     }
//                     break;
//                 case END:
//                     break;
//             }
//         } while (action != ActionType.END);

//         // next player turn
//         playerIndex++;
//         if (playerIndex >= playerCount) {
//             playerIndex = 0;
//         }
//     }
//     board.getBoardTrailer().resetPlayerLocations(players);
//     gameView.displayEndDay(currDay);
// }
// // display winners when the game is over
// ArrayList<String> winners = ScoreCalculator.getWinners(players);
// gameView.displayWinners(winners);