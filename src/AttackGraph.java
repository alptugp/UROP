import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttackGraph {
  private List<State> eulerTour = new ArrayList<>();

  //Constructor for creating an attack graph from its eulerTour
  public AttackGraph(List<State> eulerTour) {
    this.eulerTour = eulerTour;
  }

  //Constructor for creating an attack graph from its tree structure
  public AttackGraph(Node<State> root) {
    generateEulerTour(root);
  }

  public void setEulerTour(List<State> eulerTour) {
    this.eulerTour = eulerTour;
  }

  //Method for generating the euler tour of an attack graph from its tree structure
  public void generateEulerTour(Node<State> r) {
    eulerTour.add(r.getItem());
    if (r.getChildren().isEmpty()) {
      return;
    }
    for (Node<State> child : r.getChildren()) {
      generateEulerTour(child);
      eulerTour.add(r.getItem());
    }
  }

  public List<State> getEulerTour() {
    return eulerTour;
  }

  @Override
  public String toString() {
    return "AttackGraph{" +
             eulerTour +
            '}';
  }
}
