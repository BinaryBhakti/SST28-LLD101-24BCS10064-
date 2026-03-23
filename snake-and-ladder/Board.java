import java.util.*;

class Board {
    private int size;
    private Map<Integer, Integer> snakeMap;
    private Map<Integer, Integer> ladderMap;

    public Board(int n, DifficultyStrategy difficulty) {
        this.size = n * n;
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();

        List<Snake> snakes = difficulty.generateSnakes(n, size);
        for (Snake s : snakes) {
            snakeMap.put(s.getHead(), s.getTail());
            System.out.println("Snake placed: " + s.getHead() + " -> " + s.getTail());
        }

        List<Ladder> ladders = difficulty.generateLadders(n, size, snakes);
        for (Ladder l : ladders) {
            ladderMap.put(l.getStart(), l.getEnd());
            System.out.println("Ladder placed: " + l.getStart() + " -> " + l.getEnd());
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
