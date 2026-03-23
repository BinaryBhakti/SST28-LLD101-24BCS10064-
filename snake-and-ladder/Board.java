import java.util.*;

class Board {
    private int size;
    private Map<Integer, Integer> snakeMap;
    private Map<Integer, Integer> ladderMap;

    public Board(int n) {
        this.size = n * n;
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();
        generateSnakes(n);
        generateLadders(n);
    }

    private Set<Integer> getOccupiedPositions() {
        Set<Integer> occupied = new HashSet<>();
        occupied.addAll(snakeMap.keySet());
        occupied.addAll(snakeMap.values());
        occupied.addAll(ladderMap.keySet());
        occupied.addAll(ladderMap.values());
        occupied.add(1);
        occupied.add(size);
        return occupied;
    }

    private void generateSnakes(int count) {
        Random random = new Random();
        int generated = 0;
        while (generated < count) {
            int head = random.nextInt(size - 2) + 2;
            int tail = random.nextInt(head - 1) + 1;
            Set<Integer> occupied = getOccupiedPositions();
            if (!occupied.contains(head) && !occupied.contains(tail)) {
                snakeMap.put(head, tail);
                generated++;
                System.out.println("Snake placed: " + head + " -> " + tail);
            }
        }
    }

    private void generateLadders(int count) {
        Random random = new Random();
        int generated = 0;
        while (generated < count) {
            int start = random.nextInt(size - 2) + 2;
            int end = random.nextInt(size - start) + start + 1;
            Set<Integer> occupied = getOccupiedPositions();
            if (!occupied.contains(start) && !occupied.contains(end)) {
                ladderMap.put(start, end);
                generated++;
                System.out.println("Ladder placed: " + start + " -> " + end);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int getFinalPosition(int position) {
        if (snakeMap.containsKey(position)) {
            System.out.println("  Bitten by snake! " + position + " -> " + snakeMap.get(position));
            return snakeMap.get(position);
        }
        if (ladderMap.containsKey(position)) {
            System.out.println("  Climbed ladder! " + position + " -> " + ladderMap.get(position));
            return ladderMap.get(position);
        }
        return position;
    }
}
