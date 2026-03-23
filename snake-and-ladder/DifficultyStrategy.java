import java.util.List;

interface DifficultyStrategy {
    List<Snake> generateSnakes(int count, int boardSize);
    List<Ladder> generateLadders(int count, int boardSize, List<Snake> existingSnakes);
}
