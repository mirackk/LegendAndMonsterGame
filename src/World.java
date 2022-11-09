import java.util.Random;

// the whole map where user can take actions
public class World {
    private Cell[][] map;
    private int rowSize;
    private int colSize;

    private Config config;

    private Position teamPos;

    public World(int rowSize, int colSize, Config config) {
        this.map = new Cell[rowSize][colSize];
        this.colSize = colSize;
        this.rowSize = rowSize;
        this.config = config;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                map[i][j] = createCell(i, j);
            }
        }
    }

    // setting inaccessible rate 0.15
    // setting marketplace rate 0.35
    // setting fight place rate 0.50
    public Cell createCell(int row, int col) {
        double rate = Math.random();
        Position pos = new Position(row, col);
        if (rate < 0.15) {
            Cell cell = new InaccessibleCell(pos);
            return cell;
        } else if (rate >= 0.15 && rate < 0.5) {
            Cell cell = new MarketCell(pos);
            return cell;
        } else {
            Cell cell = new FightCell(pos, config);
            return cell;
        }
    }


    public Cell[][] getMap() {
        return map;
    }

    public Position getTeamPos() {
        return teamPos;
    }

    public void setTeamPos(Position teamPos) {
        this.teamPos = teamPos;
    }

    public Position initTeamPos(int size) {
        while (true) {
            Random rand = new Random();
            int row = rand.nextInt(size);
            int col = rand.nextInt(size);
            if (map[row][col] instanceof MarketCell) {
                Position pos = new Position(row, col);
                this.teamPos = pos;
                return pos;
            }
        }
    }

    // print map ,M means marker, F means fight, empty means Inaccessible
    public void printMap() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                System.out.print("+-----");
            }
            System.out.println("+");

            for (int j = 0; j < colSize; j++) {
                System.out.print("|");
                if (i == teamPos.getX() && j == teamPos.getY()) {
                    System.out.print("  T  ");
                } else if (map[i][j] instanceof MarketCell) {
                    System.out.print("  M  ");
                } else if (map[i][j] instanceof FightCell) {
                    System.out.print("  F  ");
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println("|");

            for (int j = 0; j < colSize; j++) {
                System.out.print("+-----");
            }
            System.out.println("+");
        }
        System.out.println("T as your current place, M as Market, F as FightPlace, Empty as Inaccessible place");
    }


}
