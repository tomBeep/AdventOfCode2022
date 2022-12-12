import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12 {

    public static void main(String[] args) {
        new Day12().doChallenge();
    }

    private void doChallenge() {
        String input = getInput();
        String[] lines = input.split("\n");
        int[][] heights = new int[lines.length][lines[0].length()];

        int startX = 0, startY = 0, endX = 0, endY = 0;
        List<Integer> startXPoints = new ArrayList<>();
        List<Integer> startYPoints = new ArrayList<>();

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'S') {
                    startX = x;
                    startY = y;
                    heights[y][x] = 'a';
                } else if (line.charAt(x) == 'E') {
                    endX = x;
                    endY = y;
                    heights[y][x] = 'z';
                } else {
                    int height = line.charAt(x);
                    heights[y][x] = height;
                }
                if (heights[y][x] == 'a') {
                    startYPoints.add(y);
                    startXPoints.add(x);
                }
            }
        }

        int part1Result = doPart1(heights, startX, startY, endX, endY);
        System.out.println("Part 1 shortest path: " + part1Result);
        int part2Result = doPart2(heights, startXPoints, startYPoints, endX, endY);
        System.out.println("Part 2 shortest path: " + part2Result);
    }

    private int doPart2(int[][] heights, List<Integer> startXPoints, List<Integer> startYPoints, int endX, int endY) {
        List<Integer> minPoints = new ArrayList();
        for (int i = 0; i < startXPoints.size(); i++) {
            int min = doPart1(heights, startXPoints.get(i), startYPoints.get(i), endX, endY);
            minPoints.add(min);
        }
        return minPoints.stream().mapToInt(i -> i).min().getAsInt();
    }

    private int doPart1(int[][] heights, int startX, int startY, int endX, int endY) {
        Node start = new Node(List.of(), startX, startY);
        List<Node> nodes = new ArrayList<>();
        List<Integer> pathLengths = new ArrayList<>();
        Map<Node, Integer> shortestToNode = new HashMap<>();
        nodes.add(start);
        while (!nodes.isEmpty()) {
            Node nodeToCheck = nodes.remove(nodes.size() - 1);
            if (nodeToCheck.x == endX && nodeToCheck.y == endY) {
                pathLengths.add(nodeToCheck.path.size());
            } else {
                int currentHeight = heights[nodeToCheck.y][nodeToCheck.x];
                Node right = new Node(nodeToCheck.getPathTo(), nodeToCheck.x + 1, nodeToCheck.y);
                Node left = new Node(nodeToCheck.getPathTo(), nodeToCheck.x - 1, nodeToCheck.y);
                Node up = new Node(nodeToCheck.getPathTo(), nodeToCheck.x, nodeToCheck.y - 1);
                Node down = new Node(nodeToCheck.getPathTo(), nodeToCheck.x, nodeToCheck.y + 1);
                for (Node n : List.of(right, left, up, down)) {
                    if (getNodeHeight(n, heights) <= currentHeight + 1) {
                        Integer pathPreviouslyFound = shortestToNode.get(n);
                        boolean isShorterPathToNode = pathPreviouslyFound != null && pathPreviouslyFound <= n.path.size();
                        if (!n.path.contains(n) && !isShorterPathToNode) {
                            nodes.add(n);
                            shortestToNode.put(n, n.path.size());
                        }
                    }
                }

                nodes.sort((n1, n2) -> {
                    // Heuristic to look for the path closest to the end.
                    int n1DistanceToEnd = Math.abs(n1.x - endX) + Math.abs(n1.y - endY);
                    int n2DistanceToEnd = Math.abs(n2.x - endX) + Math.abs(n2.y - endY);
                    return n1DistanceToEnd - n2DistanceToEnd;
                });
            }
        }
        return pathLengths.stream().mapToInt(i -> i).min().orElse(Integer.MAX_VALUE);
    }

    private int getNodeHeight(Node n1, int[][] heights) {
        return getNodeHeight(n1.x, n1.y, heights);
    }

    private int getNodeHeight(int x, int y, int[][] heights) {
        if (x < 0 || y < 0 || y >= heights.length || x >= heights[y].length) {
            return Integer.MAX_VALUE;
        }
        return heights[y][x];
    }

    private class Node {
        private List<Node> path;
        private int x, y;

        public Node(List<Node> path, int x, int y) {
            this.path = path;
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(x) + 10_000 * Integer.hashCode(y);
        }

        @Override
        public boolean equals(Object obj) {
            Node obj1 = (Node) obj;
            return obj1.y == this.y && obj1.x == this.x;
        }

        public List<Node> getPathTo() {
            ArrayList<Node> pathToThisNode = new ArrayList<>(path);
            pathToThisNode.add(this);
            return pathToThisNode;
        }
    }

    private static String getInput() {
        return """
                abacccaaaacccccccccccaaaaaacccccaaaaaaccccaaacccccccccccccccccccccccccccccccccccccccccccaaaaa
                abaaccaaaacccccccccccaaaaaaccccccaaaaaaaaaaaaaccccccccccccccccccccccccccccccccccccccccccaaaaa
                abaaccaaaacccccccccccaaaaacccccaaaaaaaaaaaaaaaccccccccccccccccccccccccccccccccccccccccccaaaaa
                abccccccccccccccccccccaaaaacccaaaaaaaaaaaaaaaacccccccccccccccccccccccccccaaaccccccccccccaaaaa
                abccccccccccccccccccccaacaacccaaaaaaaaccaaaaaccccccccccccccccccccccccccccaaaccccccccccccaccaa
                abcccccccccccccaacccaaaccccccaaaaaaaaaccaaaaaccccccccccccccccccccccccccccccacccccccccccccccca
                abcccccccccccaaaaaaccaaaccacccccaaaaaaacccccccccccccccccccccccccciiiicccccccddddddccccccccccc
                abcccccccccccaaaaaaccaaaaaaaccccaaaaaacccccaacccccccaaaccccccccciiiiiiiicccdddddddddacaaccccc
                abccccccccccccaaaaaaaaaaaaacccccaaaaaaacaaaacccccccaaaacccccccchhiiiiiiiiicddddddddddaaaccccc
                abcccccccccccaaaaaaaaaaaaaacccccccaaacccaaaaaacccccaaaaccccccchhhipppppiiiijjjjjjjddddaaccccc
                abcccccccccccaaaaaaaaaaaaaaccccccccccccccaaaaaccccccaaaccccccchhhpppppppiijjjjjjjjjddeeaccccc
                abcccccccccccccccccaaaaaaaacccccccccccccaaaaaccccccccccccccccchhppppppppppjjqqqjjjjjeeeaacccc
                abccccccccccccccccccaaaaaaaacccccccccccccccaacccccccccccccccchhhpppuuuupppqqqqqqqjjjeeeaacccc
                abcccccccccccccccccccaacccacccccccccccccccccccccccccccccccccchhhopuuuuuuppqqqqqqqjjjeeecccccc
                abacccccccccccccaaacaaaccccccccccccccccccccccccccccaaccccccchhhhoouuuuuuuqvvvvvqqqjkeeecccccc
                abaccccccccccccaaaaaacccccaaccccccccccccccccccccccaaaccccccchhhooouuuxxxuvvvvvvqqqkkeeecccccc
                abaccccccccccccaaaaaacccaaaaaaccccccccccccccccccaaaaaaaaccchhhhooouuxxxxuvyyyvvqqqkkeeecccccc
                abcccccccccccccaaaaacccaaaaaaaccccccccccccccccccaaaaaaaaccjjhooooouuxxxxyyyyyvvqqqkkeeecccccc
                abccccccccccccccaaaaaacaaaaaaaccccccccaaaccccccccaaaaaaccjjjooootuuuxxxxyyyyyvvqqkkkeeecccccc
                abccccccccccccccaaaaaaaaaaaaacccccccccaaaacccccccaaaaaacjjjooootttuxxxxxyyyyvvrrrkkkeeecccccc
                SbccccccccccccccccccaaaaaaaaacccccccccaaaacccccccaaaaaacjjjoootttxxxEzzzzyyvvvrrrkkkfffcccccc
                abcccccccccccaaacccccaaaaaaacaaaccccccaaaccccccccaaccaacjjjoootttxxxxxyyyyyyvvvrrkkkfffcccccc
                abcccccccccaaaaaacccaaaaaacccaaacacccaacccccccccccccccccjjjoootttxxxxyxyyyyyywvvrrkkkfffccccc
                abcccccccccaaaaaacccaaaaaaaaaaaaaaaccaaacaaacccccaacccccjjjnnnttttxxxxyyyyyyywwwrrkkkfffccccc
                abcaacacccccaaaaacccaaacaaaaaaaaaaaccaaaaaaacccccaacaaacjjjnnnntttttxxyywwwwwwwwrrrlkfffccccc
                abcaaaaccccaaaaacccccccccaacaaaaaaccccaaaaaacccccaaaaacccjjjnnnnnttttwwywwwwwwwrrrrllfffccccc
                abaaaaaccccaaaaaccccccaaaaaccaaaaacaaaaaaaaccccaaaaaaccccjjjjinnnntttwwwwwsssrrrrrllllffccccc
                abaaaaaaccccccccccccccaaaaacaaaaaacaaaaaaaaacccaaaaaaacccciiiiinnnntswwwwssssrrrrrlllfffccccc
                abacaaaaccccccccccccccaaaaaacaaccccaaaaaaaaaaccccaaaaaaccccciiiinnnssswwsssssllllllllfffccccc
                abccaaccccccccccccccccaaaaaaccccccccccaaacaaaccccaaccaacccccciiiinnsssssssmmllllllllfffaacccc
                abccccccccccccccccccccaaaaaaccccccccccaaaccccccccaaccccccccccciiinnmsssssmmmmlllllgggffaacccc
                abcccccccccccccccaccccccaaacccccccccccaaccccccccccccccccccccccciiimmmsssmmmmmgggggggggaaacccc
                abcccccccccaaaaaaaaccccccccccccccccccccccccccccaaaaaccccccccccciiimmmmmmmmmgggggggggaaacccccc
                abccccccccccaaaaaaccccccccccccccccccaacccccccccaaaaacccccccccccciiimmmmmmmhhggggcaaaaaaaccccc
                abccccccccccaaaaaacccccccccccccccccaacccccccccaaaaaacccccccccccciihhmmmmhhhhgccccccccaacccccc
                abccccaacaaaaaaaaaaccccccccccccccccaaaccccccccaaaaaaccccccccccccchhhhhhhhhhhaaccccccccccccccc
                abccccaaaaaaaaaaaaaaccccccccccaaccaaaaccccccccaaaaaacccaaacccccccchhhhhhhhaaaaccccccccccccccc
                abcccaaaaaaaaaaaaaaaccccccccaaaaaacaaaacacaccccaaaccccaaaacccccccccchhhhccccaaccccccccccaaaca
                abcccaaaaaacacaaacccccccccccaaaaaaaaaaaaaaacccccccccccaaaacccccccccccaaaccccccccccccccccaaaaa
                abcccccaaaacccaaaccccccccccaaaaaaaaaaaaaaaaccccccccccccaaacccccccccccaaacccccccccccccccccaaaa
                abcccccaacccccaacccccccccccaaaaaaaaaaaaaccccccccccccccccccccccccccccccccccccccccccccccccaaaaa
                """;
    }
}
