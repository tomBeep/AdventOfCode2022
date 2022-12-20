import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Day15 {

    public static void main(String[] args) {
        new Day15().doChallenge();
    }

    private void doChallenge() {
        part1();
        part2();
    }

    private void part1() {
        String input = getInput();
        String[] lines = input.split("\n");
        List<Point> sensors = getPoints(lines, 1, 3);
        List<Point> beacons = getPoints(lines, 5, 7);

        // Expand our starting and ending points the largest beacon distance away from the edge.
        int largestDistance = 0;
        int maxX = Integer.MIN_VALUE, minX = Integer.MAX_VALUE;
        for (int i = 0; i < sensors.size(); i++) {
            Point s = sensors.get(i);
            Point b = beacons.get(i);
            int distance = Math.abs(s.x - b.x) + Math.abs(s.y - b.y);
            largestDistance = Math.max(largestDistance, distance);
            maxX = Math.max(s.x, Math.max(b.x, maxX));
            minX = Math.min(s.x, Math.min(b.x, minX));
        }
        minX -= largestDistance;
        maxX += largestDistance;

        // loop through every item in row 2_000_000 checking it.
        long total = 0;
        int y = 2_000_000;
        for (int x = minX; x <= maxX; x++) {
            for (int i = 0; i < sensors.size(); i++) {
                int result = compareDistanceOfBeaconVsPoint(sensors, beacons, y, x, i);
                if (result <= 0) {
                    total++;
                    break;
                }
            }
        }

        System.out.println("Part 1 total is: " + (total - 1));// -1 because there is 1 beacon already on y=200_000
    }

    void part2() {
        String input = getInput();
        String[] lines = input.split("\n");
        List<Point> sensors = getPoints(lines, 1, 3);
        List<Point> beacons = getPoints(lines, 5, 7);

        int maxX = 4_000_000;
        int maxY = 4_000_000;
        int minX = 0;
        int minY = 0;
        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                boolean isOutsideSensorRange = true;
                long distanceToSkip = 0;
                for (int i = 0; i < sensors.size(); i++) {
                    int difference = compareDistanceOfBeaconVsPoint(sensors, beacons, y, x, i);
                    if (difference <= 0) {
                        // If the point is closer to the sensor than the beacon was, this can't be the point we're 
                        // looking for
                        isOutsideSensorRange = false;
                        // Work out how far 'right' we would need to move in order to get away from all sensors.
                        // This should save us checking every single point in a row.
                        distanceToSkip = Math.max(distanceToSkip, Math.abs(difference));
                    }
                }
                if (isOutsideSensorRange) {
                    System.out.println("Part 2 tuning frequency is: " + (4000000L * x + y));
                    return;
                } else {
                    // Skip along the row so we get out of range of all sensors
                    // Need to watch out for integer overflow.
                    long newX = (long) x + distanceToSkip;
                    x = (int) (newX > maxX ? maxX : newX);
                }
            }
        }
    }

    private List<Point> getPoints(String[] lines, int index1, int index2) {
        List<Point> beacons = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split("[=,:]");
            beacons.add(new Point(Integer.parseInt(split[index1]), Integer.parseInt(split[index2])));
        }
        return beacons;
    }
    
    /**
     * Compares distance of (Sensor <-> Beacon) to (Sensor <-> Point). If result is <0 then sensor is closer to beacon
     * than it is to point.
     */
    private int compareDistanceOfBeaconVsPoint(List<Point> sensors, List<Point> beacons, int pointY, int pointX, int indexToCheckAgainst) {
        Point s = sensors.get(indexToCheckAgainst);
        Point b = beacons.get(indexToCheckAgainst);
        int sensorToPointDistance = Math.abs(s.x - pointX) + Math.abs(s.y - pointY);
        int sensorToBeaconDistance = Math.abs(s.x - b.x) + Math.abs(s.y - b.y);
        return sensorToPointDistance - sensorToBeaconDistance;
    }

    private static String getInput() {
        return """
                Sensor at x=9450, y=2172986: closest beacon is at x=-657934, y=1258930
                Sensor at x=96708, y=1131866: closest beacon is at x=-657934, y=1258930
                Sensor at x=1318282, y=3917725: closest beacon is at x=-39403, y=3757521
                Sensor at x=3547602, y=1688021: closest beacon is at x=3396374, y=1626026
                Sensor at x=3452645, y=2433208: closest beacon is at x=3249864, y=2880665
                Sensor at x=46113, y=3689394: closest beacon is at x=-39403, y=3757521
                Sensor at x=2291648, y=2980268: closest beacon is at x=2307926, y=3005525
                Sensor at x=3127971, y=2022110: closest beacon is at x=3396374, y=1626026
                Sensor at x=2301436, y=2996160: closest beacon is at x=2307926, y=3005525
                Sensor at x=2989899, y=3239346: closest beacon is at x=3551638, y=3263197
                Sensor at x=544144, y=3031363: closest beacon is at x=-39403, y=3757521
                Sensor at x=3706626, y=767329: closest beacon is at x=3396374, y=1626026
                Sensor at x=2540401, y=2746490: closest beacon is at x=2342391, y=2905242
                Sensor at x=2308201, y=2997719: closest beacon is at x=2307926, y=3005525
                Sensor at x=782978, y=1855194: closest beacon is at x=1720998, y=2000000
                Sensor at x=2317632, y=2942537: closest beacon is at x=2342391, y=2905242
                Sensor at x=1902546, y=2461891: closest beacon is at x=1720998, y=2000000
                Sensor at x=3967424, y=1779674: closest beacon is at x=3396374, y=1626026
                Sensor at x=2970495, y=2586314: closest beacon is at x=3249864, y=2880665
                Sensor at x=3560435, y=3957350: closest beacon is at x=3551638, y=3263197
                Sensor at x=3932297, y=3578328: closest beacon is at x=3551638, y=3263197
                Sensor at x=2819004, y=1125748: closest beacon is at x=3396374, y=1626026
                Sensor at x=2793841, y=3805575: closest beacon is at x=3015097, y=4476783
                Sensor at x=3096324, y=109036: closest beacon is at x=3396374, y=1626026
                Sensor at x=3678551, y=3050855: closest beacon is at x=3551638, y=3263197
                Sensor at x=1699186, y=3276187: closest beacon is at x=2307926, y=3005525
                Sensor at x=3358443, y=3015038: closest beacon is at x=3249864, y=2880665
                Sensor at x=2309805, y=1755792: closest beacon is at x=1720998, y=2000000
                Sensor at x=2243001, y=2876549: closest beacon is at x=2342391, y=2905242
                Sensor at x=2561955, y=3362969: closest beacon is at x=2307926, y=3005525
                Sensor at x=2513546, y=2393940: closest beacon is at x=2638370, y=2329928
                Sensor at x=1393638, y=419289: closest beacon is at x=1720998, y=2000000
                Sensor at x=2696979, y=2263077: closest beacon is at x=2638370, y=2329928
                Sensor at x=3842041, y=2695378: closest beacon is at x=3249864, y=2880665
                """;
    }
}