import java.awt.desktop.SystemSleepEvent;
import java.util.Random;
import java.util.Scanner;

// cell where heroes can fight with monsters
public class FightCell extends Cell {

    private Monster[] monsters;
    private Scanner sc;

    private Config config;

    private MonsterFactory monsterFactory;

    public FightCell(Position pos, Config config) {
        super(pos);
        this.sc = new Scanner(System.in);
        this.config = config;
        this.monsterFactory = new MonsterFactory();
    }

    @Override
    public void intoCell(Team team) {
        System.out.println("Now we roll a dice to see if there is any monster");
        Random rd = new Random();
        int num = rd.nextInt(10);
        // I set 70% will meet monsters
        if (num < 7) {
            System.out.println("Monsters here");
            System.out.println("Monsters Infos: ");
            int teamSize = team.getTeamSize();
            createMonsters(teamSize);
            // create monsters 1 by 1 according to hero's level
            for (int i = 0; i < teamSize; i++) {
                Hero hero = team.getHeroes()[i];
                int level = hero.getLevel();
                monsters[i] = monsterFactory.create(config, level);
                monsters[i].printMonsterInfo();
            }
            FightFlow(team);
        } else {
            System.out.println("No monsters here, you don't have to fight and can leave now");
        }
    }

    public Monster[] createMonsters(int number) {
        monsters = new Monster[number];
        return monsters;
    }

    public void printCMD() {
        System.out.println("B/b: show current Bag information hero");
        System.out.println("I/i: show current hero information");
        System.out.println("M/m: show monsters information");
        System.out.println("Q/q: quit the fight");
        System.out.println("skip: do nothing and let another hero plays");
        System.out.println("");
        System.out.println("A/a: choose target and attack");
        System.out.println("E/e: change equipment");
        System.out.println("P/p: drink potions");
        System.out.println("S/s: choose target and use spell");
        System.out.println("");
        System.out.println("H/h: show help\n");
    }

    // main fighting flow
    // each round monster will attack
    // each round heroes can 1) choose target and attack, 2) use spell, 3) use potion 4) equip weapon/armor
    // if all heroes dead(deadHeroes = teamSize), monsters win; vise vesa
    public void FightFlow(Team team) {
        // flag =1 heroes win; 2 monsters win
        int flag = 0;
        while(true){
            int deadHeroes = 0;
            int deadMonsters = 0;
            heroTurns(team);
            for(Monster monster:monsters){
                if(monster.hp<=0){
                    deadMonsters+=1;
                }
            }
            if(deadMonsters==monsters.length){
                flag=1;
                break;
            }
            monsterTurns(team);
            for(Hero hero:team.getHeroes()){
                if(hero.hp<=0){
                    deadHeroes+=1;
                }
            }
            if(deadHeroes==team.getTeamSize()){
                flag=2;
                break;
            }
        }
        if(flag==1){
            afterFight(team);
        }
        else if (flag==2){
            System.out.println("All heroes dead! Game over");
            System.exit(0);
        }
    }


    public void heroTurns(Team team) {
        for (Hero hero : team.getHeroes()) {
            if (hero.hp <= 0) {
                System.out.println(hero.getName() + " dead");
                continue;
            }
            System.out.println("\nNow is " + hero.getName() + "\'s turn");
            printCMD();
            while (true) {
                sc = new Scanner(System.in);
                String input = sc.nextLine();
                if (input.equalsIgnoreCase("b")) {
                    hero.bag.printItems();
                } else if (input.equalsIgnoreCase("i")) {
                    hero.printHeroInfo();
                } else if (input.equalsIgnoreCase("m")) {
                    for (Monster monster : monsters) {
                        monster.printMonsterInfo();
                    }
                } else if (input.equalsIgnoreCase("q")) {
                    return;
                }
                else if(input.equalsIgnoreCase("skip")){
                    break;
                }else if (input.equalsIgnoreCase("h")) {
                    printCMD();
                } else if (input.equalsIgnoreCase("a")) {
                    System.out.println("Which monster do you want to attack");
                    System.out.println("Enter a number, for top to bottom on Monster list, the first one is 0, second one is 1...");
                    int index = sc.nextInt();
                    hero.attack(monsters[index]);
                    break;
                } else if (input.equalsIgnoreCase("e")) {
                    hero.equip();
                    break;
                } else if (input.equalsIgnoreCase("p")) {
                    hero.drinkPotion();
                    break;
                } else if (input.equalsIgnoreCase("s")) {
                    System.out.println("Which monster do you want to attack");
                    System.out.println("Enter a number, for top to bottom on Monster list, the first one is 0, second one is 1...");
                    int index = sc.nextInt();
                    hero.useSpell(monsters[index]);
                    break;
                } else {
                    System.out.println("wrong input, try again");
                }
            }
        }
    }

    //todo
    // monster damage = value*0.1
    // hero dodge = value*0.002
    // hero defense = all * 0.1
    public void monsterTurns(Team team) {
        // random choose a target
        int size = team.getTeamSize();
        for (Monster monster : monsters) {
            if (monster.hp <= 0) {
                System.out.println(monster.name + " dead");
                continue;
            }
            Random rd = new Random();
            int num = rd.nextInt(size);
            Hero hero = team.getHeroes()[num];

            int originDamage = (int)(monster.getDamage()*0.1);
            int actualDamage;
            if(hero.armor!=null){
                actualDamage = originDamage - (int)(hero.defense*0.1+hero.armor.getReduction()*0.1);
            }
            else{
                actualDamage = originDamage;
            }

            double dodgeRate = hero.agility*0.002;
            double rate = Math.random();
            if(rate<dodgeRate){
                System.out.println("Monster "+monster.getName()+" misses attack on "+hero.getName());
                continue;
            }
            else{
                System.out.println("Monster "+monster.getName()+" attacks "+hero.getName()+" with "+actualDamage+" hp");
                hero.minusHp(actualDamage);
            }
        }
    }


    // after fight, if hero wins, give them gold and exp, and recover hp mp.
    // when hero gets exp, I check if he can level up
    // if lose quit the game
    public void afterFight(Team team) {
        for(Hero hero:team.getHeroes()){
            if(hero.hp>0){
                recover(hero);
                reward(hero);
                hero.levelUp();
            }
            else{
                revive(hero);
            }
        }
    }

    // can get exp = 2*number of monsters, gold = 100*monster level
    public void reward(Hero hero){
        hero.updateExp(monsters.length*2);
        for(Monster monster:monsters){
            int level = monster.getLevel();
            hero.updateGold(level*100);
        }
    }

    // one can recover some hp and mp after fight
    public void recover(Hero hero) {
        hero.hp = (int)(hero.hp*1.1);
        hero.mp = (int)(hero.mp*1.1);
    }

    // if one is dead but wins, he can revive but get nothing
    public void revive(Hero hero) {
        hero.hp = (int)(hero.level*50);
        hero.mp = (int)(hero.level*200);
    }

}
