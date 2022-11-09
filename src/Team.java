// a specific number of heroes can be a team
public class Team {
    private Hero[] heroes;
    private HeroFactory heroFactory;
    private int teamLevel;

    public Team(int num, Config config) {
        this.heroes = new Hero[num];
        this.heroFactory = new HeroFactory();
        for (int i = 0; i < heroes.length; i++) {
            heroes[i] = heroFactory.create(config);
        }
        this.teamLevel = 1;
    }

    public int getTeamSize() {
        return heroes.length;
    }

    public Hero[] getHeroes() {
        return heroes;
    }

    // the whole team moves together and everyone will go to next position
    public void teamMove(Position pos) {
        for (int i = 0; i < heroes.length; i++) {
            heroes[i].setPos(pos);
        }
    }

    //todo
    public Position getTeamPosition() {
        return heroes[0].pos;
    }

    public void updateTeamLevel() {
        int newLevel = this.teamLevel;
        for (Hero hero : heroes) {
            newLevel = Math.max(newLevel, hero.getLevel());
        }
        this.teamLevel = newLevel;
    }

    public void chooseTarget() {

    }

    public int getTeamLevel() {
        return teamLevel;
    }
}
