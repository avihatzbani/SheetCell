package basics.graph.GraphImpl;
import java.util.List;

public interface Graph<T> {
    List<T> topologicalSort();
}
