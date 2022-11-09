import java.util.Scanner;

// main control logic
public class MonstersAndHeroes extends Game {
    private final int worldSize = 8;
    private Team team;
    private World world;
    private Config config;
    private Scanner sc;

    public MonstersAndHeroes() {
        this.sc = new Scanner(System.in);
        this.config = new Config();
        config.getCondfigs();
        this.world = new World(worldSize, worldSize, config);
    }

    @Override
    public void start() {
        System.out.println("Welcome to Monsters and Heroes game! ");
        System.out.println("Author: Taoyu Chen");
        System.out.println("\n\n");
        printCMD();

        System.out.println("Enter the number of heroes in your team [1-3]");
        System.out.println("Warriors are favored on strength and agility");
        System.out.println("Sorcerers are favored on dexterity and agility");
        System.out.println("Paladins are favored on strength and dexterity");
        int number = sc.nextInt();
        this.team = new Team(number, config);

        System.out.println("\nLoading your team into map");
        Position pos = world.initTeamPos(worldSize);
        team.teamMove(pos);
        world.printMap();
        // firstly we will be born on a market
        intoANewCell(pos, world.getMap());
        userInput();
    }

    public void userInput() {
        while (true) {
            System.out.println("\nEnter your command");
            System.out.println("If you need help enter H/h");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Quiting the game ...");
                System.exit(0);
            } else if (input.equalsIgnoreCase("i")) {
                for (Hero hero : team.getHeroes()) {
                    System.out.println("Hero " + hero.name);
                    hero.printHeroInfo();
                    System.out.println("");
                }
            } else if (input.equalsIgnoreCase("b")) {
                for (Hero hero : team.getHeroes()) {
                    System.out.println("Hero " + hero.name);
                    hero.bag.printItems();
                    System.out.println("");
                }
            } else if (input.equalsIgnoreCase("e")) {
                for (Hero hero : team.getHeroes()) {
                    System.out.println("Hero " + hero.name);
                    hero.equip();
                    System.out.println("");
                }
            } else if (input.equalsIgnoreCase("p")) {
                for (Hero hero : team.getHeroes()) {
                    System.out.println("Hero " + hero.name);
                    hero.drinkPotion();
                    System.out.println("");
                }
            } else if (input.equalsIgnoreCase("m")) {
                world.printMap();
            } else if (input.equalsIgnoreCase("h")) {
                printCMD();
            } else if (input.equalsIgnoreCase("w") || input.equalsIgnoreCase("a") || input.equalsIgnoreCase("s") || input.equalsIgnoreCase("d")) {
                movement(input);
            } else {
                System.out.println("Invalid input, try again");
            }
        }
    }

    public boolean movement(String direction) {
        Position oldPos = world.getTeamPos();
        int x = oldPos.getX();
        int y = oldPos.getY();
        if (direction.equalsIgnoreCase("w")) {
            x -= 1;
        }
        if (direction.equalsIgnoreCase("a")) {
            y -= 1;
        }
        if (direction.equalsIgnoreCase("s")) {
            x += 1;
        }
        if (direction.equalsIgnoreCase("d")) {
            y += 1;
        }

        // try to move to the new position
        // check legal
        Position newPos = new Position(x, y);
        Cell[][] map = world.getMap();
        if (x >= worldSize || y >= worldSize) {
            System.out.println("Your move out of Map, try again");
            return false;
        } else if (map[x][y] instanceof InaccessibleCell) {
            map[x][y].intoCell(team);
            return false;
        }
//        else if(map[x][y] instanceof MarketCell){
//            team.teamMove(newPos);
//            System.out.println("It is a market, do you want to enter?");
//            System.out.println("M/m to enter, else to quit and make next move");
//            String input = sc.nextLine();
//            if(input.equalsIgnoreCase("m")){
//                ((MarketCell) map[x][y]).generateRandomItems(config); // create selling list
//                map[x][y].intoCell(team);
//            }
//            return true;
//        }
//        else{
//            team.teamMove(newPos);
//            map[x][y].intoCell(team);
//            return true;
//        }
        else {
            intoANewCell(newPos, map);
            return true;
        }
    }

    public void intoANewCell(Position pos, Cell[][] map) {
        int x = pos.getX();
        ;
        int y = pos.getY();
        //go into the cell and do correspond thing
        if (map[x][y] instanceof MarketCell) {
            team.teamMove(pos);
            world.setTeamPos(pos);
            System.out.println("It is a market, do you want to enter?");
            System.out.println("M/m to enter, else to quit and make next move");
            sc = new Scanner(System.in);
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("m")) {
                ((MarketCell) map[x][y]).generateRandomItems(config); // create selling list
                map[x][y].intoCell(team);
            }
        } else {
            team.teamMove(pos);
            world.setTeamPos(pos);
            map[x][y].intoCell(team);
        }
    }


    // tell players how to move and some basic input
    public void printCMD() {
        System.out.println("W/w: move up");
        System.out.println("A/a: move left");
        System.out.println("S/s: move down");
        System.out.println("D/d: move right");
        System.out.println("");
        System.out.println("Q/q: quit game");
        System.out.println("I/i: show current team information");
        System.out.println("B/b: show current Bag information of every team member");
        System.out.println("E/e: change equipment");
        System.out.println("P/p: drink potions");
        System.out.println("M/m: show map");
        System.out.println("");
        System.out.println("H/h: show help");
    }

}
