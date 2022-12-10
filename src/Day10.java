public class Day10 {

    public static void main(String[] args) {
        new Day10().doChallenge();
    }

    private void doChallenge() {
        String input = getInput();
        String[] lines = input.split("\n");

        CPU cpu = new CPU();

        System.out.println("Part 2:");
        System.out.println();
        for (String line : lines) {
            if (line.startsWith("noop")) {
                cpu.noop();
            } else {
                String[] split = line.split(" ");
                cpu.addx(Long.parseLong(split[1]));
            }
        }
        System.out.println();
        System.out.println("-----");
        System.out.println();
        System.out.println("Part 1: " + cpu.sumSignalStrengths);
    }

    private class CPU {
        long x = 1;
        long cycles = 0;
        long sumSignalStrengths = 0;

        void addx(long v) {
            increaseCycle();
            increaseCycle();
            x += v;
        }

        void noop() {
            increaseCycle();
        }

        void increaseCycle() {
            cycles++;
            long pixelIndex = (cycles - 1) % 40;
            char c = (x == pixelIndex || x + 1 == pixelIndex || x - 1 == pixelIndex) ? '#' : '.';
            System.out.print(c);
            if (cycles % 40 == 0) {
                System.out.print("\n");
            }
            if ((cycles + 20) % 40 == 0) {
                sumSignalStrengths += x * cycles;
            }
        }
    }

    private static String getInput() {
        return """
                addx 2
                addx 4
                noop
                noop
                addx 17
                noop
                addx -11
                addx -1
                addx 4
                noop
                noop
                addx 6
                noop
                noop
                addx -14
                addx 19
                noop
                addx 4
                noop
                noop
                addx 1
                addx 4
                addx -20
                addx 21
                addx -38
                noop
                addx 7
                noop
                addx 3
                noop
                addx 22
                noop
                addx -17
                addx 2
                addx 3
                noop
                addx 2
                addx 3
                noop
                addx 2
                addx -8
                addx 9
                addx 2
                noop
                noop
                addx 7
                addx 2
                addx -27
                addx -10
                noop
                addx 37
                addx -34
                addx 30
                addx -29
                addx 9
                noop
                addx 2
                noop
                noop
                noop
                addx 5
                addx -4
                addx 9
                addx -2
                addx 7
                noop
                noop
                addx 1
                addx 4
                addx -1
                noop
                addx -19
                addx -17
                noop
                addx 1
                addx 4
                addx 3
                addx 11
                addx 17
                addx -23
                addx 2
                noop
                addx 3
                addx 2
                addx 3
                addx 4
                addx -22
                noop
                addx 27
                addx -32
                addx 14
                addx 21
                addx 2
                noop
                addx -37
                noop
                addx 31
                addx -26
                addx 5
                addx 2
                addx 3
                addx -2
                addx 2
                addx 5
                addx 2
                addx 3
                noop
                addx 2
                addx 9
                addx -8
                addx 2
                addx 11
                addx -4
                addx 2
                addx -15
                addx -22
                addx 1
                addx 5
                noop
                noop
                noop
                noop
                noop
                addx 4
                addx 19
                addx -15
                addx 1
                noop
                noop
                addx 6
                noop
                noop
                addx 5
                addx -1
                addx 5
                addx -14
                addx -13
                addx 30
                noop
                addx 3
                noop
                noop
                """;
    }
}
