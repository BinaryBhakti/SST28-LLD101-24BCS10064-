import java.util.*;

class EasyDifficulty implements DifficultyStrategy {

    @Override
    public List<Snake> generateSnakes(int count, int boardSize) {
        Random random = new Random();
        List<Snake> snakes = new ArrayList<>();
        Set<Integer> occupied = new HashSet<>(Arrays.asList(1, boardSize));
        int maxDrop = Math.max(boardSize / 4, 2);

        while (snakes.size() < count) {
            int head = random.nextInt(boardSize - 2) + 2;
            int drop = random.nextInt(Math.min(maxDrop, head - 1)) + 1;
            int tail = head - drop;
            if (tail < 1 || occupied.contains(head) || occupied.contains(tail)) continue;
            snakes.add(new Snake(head, tail));
            occupied.add(head);
            occupied.add(tail);
        }
        return snakes;
    }

    @Override
    public List<Ladder> generateLadders(int count, int boardSize, List<Snake> existingSnakes) {
        Random random = new Random();
        List<Ladder> ladders = new ArrayList<>();
        Set<Integer> occupied = new HashSet<>(Arrays.asList(1, boardSize));
        for (Snake s : existingSnakes) {
            occupied.add(s.getHead());
            occupied.add(s.getTail());
        }
        int minClimb = Math.max(boardSize / 4, 2);

        while (ladders.size() < count) {
            int start = random.nextInt(boardSize - 2) + 2;
            int maxEnd = boardSize - 1;
            if (maxEnd - start < minClimb) continue;
            int climb = random.nextInt(maxEnd - start - minClimb + 1) + minClimb;
            int end = start + climb;
            if (occupied.contains(start) || occupied.contains(end)) continue;
            ladders.add(new Ladder(start, end));
            occupied.add(start);
            occupied.add(end);
        }
        return ladders;
    }
}
