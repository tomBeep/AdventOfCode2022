import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day11 {

    public static void main(String[] args) {
        new Day11().doChallenge();
    }

    private void doChallenge() {
        part1();
        part2();
    }

    private List<Monkey> parseMonkeys() {
        String input = getInput();
        Scanner sc = new Scanner(input);
        List<Monkey> monkeys = new ArrayList<>();
        while (sc.hasNextLine()) {
            monkeys.add(new Monkey(sc));
        }
        return monkeys;
    }

    private void part1() {
        List<Monkey> monkeys = parseMonkeys();
        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeys) {
                for (long item : monkey.items) {
                    monkey.totalInspections++;
                    long worryLevel = monkey.operation.apply(item);
                    worryLevel /= 3;
                    int indexOfNewMonkey = monkey.test.apply(worryLevel) ? monkey.monkeyToThrowIfTrue : monkey.monkeyToThrowIfFalse;
                    monkeys.get(indexOfNewMonkey).items.add(worryLevel);
                }
                monkey.items.clear();
            }
        }

        long topTwoMultiplied = monkeys.stream()
                .mapToLong(m -> m.totalInspections)
                .sorted()
                .skip(monkeys.size() - 2)
                .reduce((total, next) -> total * next)
                .getAsLong();
        System.out.println("Part 1: " + topTwoMultiplied);
    }

    private void part2() {
        List<Monkey> monkeys = parseMonkeys();
        long divisor = 11L * 17 * 5 * 13 * 19 * 2 * 3 * 7;

        for (int i = 0; i < 10_000; i++) {
            for (Monkey monkey : monkeys) {
                for (long item : monkey.items) {
                    monkey.totalInspections++;
                    long worryLevel = monkey.operation.apply(item);
                    worryLevel = worryLevel % divisor;
                    int indexOfNewMonkey = monkey.test.apply(worryLevel) ? monkey.monkeyToThrowIfTrue : monkey.monkeyToThrowIfFalse;
                    monkeys.get(indexOfNewMonkey).items.add(worryLevel);
                }
                monkey.items.clear();
            }
        }

        long topTwoMultiplied = monkeys.stream()
                .mapToLong(m -> m.totalInspections)
                .sorted()
                .skip(monkeys.size() - 2)
                .reduce((total, next) -> total * next)
                .getAsLong();
        System.out.println("Part 2: " + topTwoMultiplied);
    }

    private class Monkey {
        List<Long> items = new ArrayList<>();
        Function<Long, Long> operation;
        Function<Long, Boolean> test;
        int monkeyToThrowIfTrue;
        int monkeyToThrowIfFalse;
        long totalInspections = 0;

        public Monkey(Scanner sc) {
            sc.nextLine();// Monkey name, who cares
            String startingItems = sc.nextLine();
            String operation = sc.nextLine();
            String test = sc.nextLine();
            String ifTrue = sc.nextLine();
            String ifFalse = sc.nextLine();
            if (sc.hasNextLine()) sc.nextLine();

            String[] itemSplit = startingItems.split(" ");
            Pattern itemMatcher = Pattern.compile("(\\d+),?");
            for (String item : itemSplit) {
                Matcher m = itemMatcher.matcher(item);
                if (m.matches()) {
                    this.items.add(Long.parseLong(m.group(1)));
                }
            }

            Pattern operationMatcher = Pattern.compile("\\s*Operation: new = (old|\\d+) ([*+]) (old|\\d+)");
            Matcher m = operationMatcher.matcher(operation);
            m.matches();
            if (m.group(2).equals("*")) {
                if (m.group(3).equals("old")) {
                    this.operation = integer -> integer * integer;
                } else {
                    long multiplier = Long.parseLong(m.group(3));
                    this.operation = integer -> integer * multiplier;
                }
            } else {
                if (m.group(3).equals("old")) {
                    this.operation = integer -> integer + integer;
                } else {
                    long multiplier = Long.parseLong(m.group(3));
                    this.operation = integer -> integer + multiplier;
                }
            }

            Pattern testMatcher = Pattern.compile(".*?(\\d*)");
            m = testMatcher.matcher(test);
            m.matches();
            int divisbleBy = Integer.parseInt(m.group(1));
            this.test = (input) -> input % divisbleBy == 0;

            m = testMatcher.matcher(ifTrue);
            m.matches();
            monkeyToThrowIfTrue = Integer.parseInt(m.group(1));

            m = testMatcher.matcher(ifFalse);
            m.matches();
            monkeyToThrowIfFalse = Integer.parseInt(m.group(1));
        }
    }

    private static String getInput() {
        return """
                Monkey 0:
                  Starting items: 98, 97, 98, 55, 56, 72
                  Operation: new = old * 13
                  Test: divisible by 11
                    If true: throw to monkey 4
                    If false: throw to monkey 7
                                
                Monkey 1:
                  Starting items: 73, 99, 55, 54, 88, 50, 55
                  Operation: new = old + 4
                  Test: divisible by 17
                    If true: throw to monkey 2
                    If false: throw to monkey 6
                                
                Monkey 2:
                  Starting items: 67, 98
                  Operation: new = old * 11
                  Test: divisible by 5
                    If true: throw to monkey 6
                    If false: throw to monkey 5
                                
                Monkey 3:
                  Starting items: 82, 91, 92, 53, 99
                  Operation: new = old + 8
                  Test: divisible by 13
                    If true: throw to monkey 1
                    If false: throw to monkey 2
                                
                Monkey 4:
                  Starting items: 52, 62, 94, 96, 52, 87, 53, 60
                  Operation: new = old * old
                  Test: divisible by 19
                    If true: throw to monkey 3
                    If false: throw to monkey 1
                                
                Monkey 5:
                  Starting items: 94, 80, 84, 79
                  Operation: new = old + 5
                  Test: divisible by 2
                    If true: throw to monkey 7
                    If false: throw to monkey 0
                                
                Monkey 6:
                  Starting items: 89
                  Operation: new = old + 1
                  Test: divisible by 3
                    If true: throw to monkey 0
                    If false: throw to monkey 5
                                
                Monkey 7:
                  Starting items: 70, 59, 63
                  Operation: new = old + 3
                  Test: divisible by 7
                    If true: throw to monkey 4
                    If false: throw to monkey 3
                """;
    }
}
