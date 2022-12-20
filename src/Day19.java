import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day19 {

    public static void main(String[] args) {
        new Day19().doChallenge();
    }

    private void doChallenge() {
        part1();
        part2();
    }

    private void part1() {
        String input = getInput();
        String[] lines = input.split("\n");


        List<Integer> bestQualities = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Blueprint[] robotBluePrints = parseBluePrint(line);

            int geodesA = findGeodesProduced(1, robotBluePrints, new ArrayList<>(), new ArrayList<>(List.of(new OreRobot())), 0);
            int geodesB = findGeodesProduced(1, robotBluePrints, new ArrayList<>(), new ArrayList<>(List.of(new OreRobot())), 1);
            int geodesC = findGeodesProduced(1, robotBluePrints, new ArrayList<>(), new ArrayList<>(List.of(new OreRobot())), 2);
            int geodesD = findGeodesProduced(1, robotBluePrints, new ArrayList<>(), new ArrayList<>(List.of(new OreRobot())), 3);
            int mostGeodes = Math.max(geodesA, Math.max(geodesB, Math.max(geodesC, geodesD)));

            int blueprintNumber = i + 1;
            int quality = blueprintNumber * mostGeodes;
            bestQualities.add(quality);
            System.out.println("Blueprint: " + blueprintNumber + ", Number of Geodes: " + mostGeodes + ", Quality: " + quality);
        }

        System.out.println();
        System.out.println("Total = " + bestQualities.stream().mapToInt(i -> i).sum());
    }

    private int findGeodesProduced(int day, Blueprint[] robotBluePrints, List<Resource> resources, List<Robot> robots, int nextRobotToBuild) {
        if (day > 24) return -1;
        List<Robot> newRobots = new ArrayList<>();

        Blueprint blueprint = robotBluePrints[nextRobotToBuild];
        if (blueprint.canMake(resources)) {
            newRobots.add(blueprint.make(resources));
        }

        robots.forEach(r -> resources.add(r.produce()));
        robots.addAll(newRobots);

        if (day == 24) {
            return (int) resources.stream().filter(r -> r == Resource.GEODE).count();
        } else {
            day++;

            int timeToBuildD = getTimeToBuild(new ArrayList<>(resources), new ArrayList<>(robots), robotBluePrints[3]);
            int geodesD = findGeodesProduced(day + timeToBuildD, robotBluePrints, resources, robots, 3);

            int timeToBuildC = getTimeToBuild(new ArrayList<>(resources), new ArrayList<>(robots), robotBluePrints[2]);
            int geodesC = findGeodesProduced(day + timeToBuildC, robotBluePrints, resources, robots, 2);
            
            int timeToBuildB = getTimeToBuild(new ArrayList<>(resources), new ArrayList<>(robots), robotBluePrints[1]);
            int geodesB = findGeodesProduced(day + timeToBuildB, robotBluePrints, resources, robots, 1);

            int timeToBuildA = getTimeToBuild(new ArrayList<>(resources), new ArrayList<>(robots), robotBluePrints[0]);
            int geodesA = findGeodesProduced(day + timeToBuildA, robotBluePrints, resources, robots, 0);
            
            return Math.max(geodesA, Math.max(geodesB, Math.max(geodesC, geodesD)));
        }
    }

    private int getTimeToBuild(ArrayList<Resource> resources, ArrayList<Robot> robots, Blueprint robotBluePrint) {
        for (int i = 0; i < 24; i++) {
            if (robotBluePrint.canMake(resources)) {
                return i;
            } else {
                robots.forEach(r -> resources.add(r.produce()));
            }
        }
        return 26;
    }

    private Blueprint[] parseBluePrint(String line) {
        Blueprint[] robotBluePrints = new Blueprint[4];
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(line);
        m.find();

        m.find();
        int oreRobotAmount = Integer.parseInt(m.group(1));
        robotBluePrints[0] = new OreBlueprint(oreRobotAmount, 0, 0);

        m.find();
        int clayRobotAmount = Integer.parseInt(m.group(0));
        robotBluePrints[1] = new ClayBlueprint(clayRobotAmount, 0, 0);

        m.find();
        int oreAmount = Integer.parseInt(m.group(0));
        m.find();
        int clayAMount = Integer.parseInt(m.group(0));
        robotBluePrints[2] = new ObsidianBlueprint(oreAmount, clayAMount, 0);

        m.find();
        int geodeRobotOre = Integer.parseInt(m.group(0));
        m.find();
        int geodeRobotObsidian = Integer.parseInt(m.group(0));
        robotBluePrints[3] = new GeodeBlueprint(geodeRobotOre, 0, geodeRobotObsidian);
        return robotBluePrints;
    }


    void part2() {

    }

    abstract class Blueprint {

        int oreCost;
        int clayCost;
        int obsidianCost;

        Blueprint(int oreCost, int clayCost, int obsidianCost) {
            this.oreCost = oreCost;
            this.clayCost = clayCost;
            this.obsidianCost = obsidianCost;
        }

        boolean canMake(List<Resource> resources) {
            int ore = (int) resources.stream().filter(r -> r == Resource.ORE).count();
            int clay = (int) resources.stream().filter(r -> r == Resource.CLAY).count();
            int obsidian = (int) resources.stream().filter(r -> r == Resource.OBSIDIAN).count();

            return ore >= oreCost && clay >= clayCost && obsidian >= obsidianCost;
        }

        Robot make(List<Resource> resources) {
            int ore = oreCost;
            int clay = clayCost;
            int obsidian = obsidianCost;
            while (ore > 0) {
                ore--;
                resources.remove(Resource.ORE);
            }
            while (clay > 0) {
                clay--;
                resources.remove(Resource.CLAY);
            }
            while (obsidian > 0) {
                obsidian--;
                resources.remove(Resource.OBSIDIAN);
            }
            return getRobotToMake();
        }

        abstract Robot getRobotToMake();
    }

    class OreBlueprint extends Blueprint {

        OreBlueprint(int oreCost, int clayCost, int obsidianCost) {
            super(oreCost, clayCost, obsidianCost);
        }

        @Override
        Robot getRobotToMake() {
            return new OreRobot();
        }
    }

    class ClayBlueprint extends Blueprint {

        ClayBlueprint(int oreCost, int clayCost, int obsidianCost) {
            super(oreCost, clayCost, obsidianCost);
        }

        @Override
        Robot getRobotToMake() {
            return new ClayRobot();
        }
    }

    class ObsidianBlueprint extends Blueprint {
        ObsidianBlueprint(int oreCost, int clayCost, int obsidianCost) {
            super(oreCost, clayCost, obsidianCost);
        }

        @Override
        Robot getRobotToMake() {
            return new ObsidianRobot();
        }
    }

    class GeodeBlueprint extends Blueprint {
        GeodeBlueprint(int oreCost, int clayCost, int obsidianCost) {
            super(oreCost, clayCost, obsidianCost);
        }

        @Override
        Robot getRobotToMake() {
            return new GeodeRobot();
        }
    }

    interface Robot {
        Resource produce();
    }

    class OreRobot implements Robot {
        @Override
        public Resource produce() {
            return Resource.ORE;
        }
    }

    class ClayRobot implements Robot {
        @Override
        public Resource produce() {
            return Resource.CLAY;
        }
    }

    class ObsidianRobot implements Robot {
        @Override
        public Resource produce() {
            return Resource.OBSIDIAN;
        }
    }

    class GeodeRobot implements Robot {
        @Override
        public Resource produce() {
            return Resource.GEODE;
        }
    }


    enum Resource {
        ORE, CLAY, OBSIDIAN, GEODE
    }

    private static String getInput2() {
        return "Blueprint 1:" +
                "  Each ore robot costs 4 ore." +
                "  Each clay robot costs 2 ore." +
                "  Each obsidian robot costs 3 ore and 14 clay." +
                "  Each geode robot costs 2 ore and 7 obsidian." +
                "\n" +
                "Blueprint 2:" +
                "  Each ore robot costs 2 ore." +
                "  Each clay robot costs 3 ore." +
                "  Each obsidian robot costs 3 ore and 8 clay." +
                "  Each geode robot costs 3 ore and 12 obsidian.";
    }

    private static String getInput() {
        return """
                Blueprint 1: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 2 ore and 20 obsidian.
                Blueprint 2: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 2 ore and 8 obsidian.
                Blueprint 3: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 4 ore and 8 obsidian.
                Blueprint 4: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 15 clay. Each geode robot costs 2 ore and 13 obsidian.
                Blueprint 5: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 4 ore and 18 obsidian.
                Blueprint 6: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 2 ore and 13 obsidian.
                Blueprint 7: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 12 clay. Each geode robot costs 4 ore and 19 obsidian.
                Blueprint 8: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 2 ore and 13 obsidian.
                Blueprint 9: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 9 clay. Each geode robot costs 2 ore and 9 obsidian.
                Blueprint 10: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 12 clay. Each geode robot costs 2 ore and 10 obsidian.
                Blueprint 11: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 10 clay. Each geode robot costs 2 ore and 13 obsidian.
                Blueprint 12: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 3 ore and 16 obsidian.
                Blueprint 13: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 19 obsidian.
                Blueprint 14: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 4 ore and 16 obsidian.
                Blueprint 15: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 2 ore and 18 obsidian.
                Blueprint 16: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 10 clay. Each geode robot costs 4 ore and 10 obsidian.
                Blueprint 17: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 2 ore and 10 obsidian.
                Blueprint 18: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 3 ore and 14 obsidian.
                Blueprint 19: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 13 clay. Each geode robot costs 2 ore and 10 obsidian.
                Blueprint 20: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 2 ore and 10 obsidian.
                Blueprint 21: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 4 ore and 15 obsidian.
                Blueprint 22: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 2 ore and 15 obsidian.
                Blueprint 23: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 5 clay. Each geode robot costs 2 ore and 10 obsidian.
                Blueprint 24: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 4 ore and 8 obsidian.
                Blueprint 25: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 3 ore and 10 obsidian.
                Blueprint 26: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 2 ore and 20 obsidian.
                Blueprint 27: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 15 obsidian.
                Blueprint 28: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 4 ore and 20 obsidian.
                Blueprint 29: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 17 obsidian.
                Blueprint 30: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 9 clay. Each geode robot costs 3 ore and 9 obsidian.
                """;
    }
}
