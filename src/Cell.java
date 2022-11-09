// basic unit on the map
public abstract class Cell {
    private Position pos;

    public Cell(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public abstract void intoCell(Team team);


}
