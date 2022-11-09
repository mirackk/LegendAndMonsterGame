//heroes can not move to here
public class InaccessibleCell extends Cell {
    public InaccessibleCell(Position pos) {
        super(pos);
    }

    @Override
    public void intoCell(Team team) {
        System.out.println("This is an Inaccessible place");
        System.out.println("You can not move to here");
    }
}
