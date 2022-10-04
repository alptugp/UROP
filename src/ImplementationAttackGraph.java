import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ImplementationAttackGraph {
  public static void main(String[] args) {
    // Constructing the first attack graph
    Node<State> condA = new Node<>(new State("conditionA"));
    Node<State> vulBorC = new Node<>(new State("vulnerabilityToBorC"));
    condA.addChild(vulBorC);
    Node<State> condB = new Node<>(new State("conditionB"));
    vulBorC.addChild(condB);
    Node<State> vulE = new Node<>(new State("vulnerabilityToE"));
    condB.addChild(vulE);
    Node<State> condE = new Node<>(new State("conditionE"));
    vulE.addChild(condE);
    Node<State> condC = new Node<>(new State("conditionC"));
    vulBorC.addChild(condC);
    Node<State> vulD = new Node<>(new State("vulnerabilityToD"));
    condA.addChild(vulD);
    Node<State> condD = new Node<>(new State("conditionD"));
    vulD.addChild(condD);
    AttackGraph attackGraph1 = new AttackGraph(condA);


    // Constructing the second attack graph
    Node<State> condF = new Node<>(new State("conditionF"));
    Node<State> vulToHorJ = new Node<>(new State("vulnerabilityToHorJ"));
    condF.addChild(vulToHorJ);
    Node<State> condH = new Node<>(new State("conditionH"));
    Node<State> condJ = new Node<>(new State("conditionJ"));
    vulToHorJ.addChild(condH);
    vulToHorJ.addChild(condJ);
    AttackGraph attackGraph2 = new AttackGraph(condF);


    // Connecting the two attack graphs (connecting vulnerabilityToE state of attackGraph1 with conditionF state of attackGraph2) where conditionA is the root of the attack graph (since it is the third argument to the method)
    AttackGraph connectedAttackGraph = connect(attackGraph1, new State ("vulnerabilityToE"), attackGraph2, new State("conditionF"), new State("conditionA"));
    System.out.println(connectedAttackGraph);

    // Unconnecting the attack graph from its edge that connects conditionF state with vulnerabilityToE state
    List<AttackGraph> unconnectedAttackGraphs = unconnect(connectedAttackGraph, new State("vulnerabilityToE"), new State("conditionF"));
    for (AttackGraph attackGraph : unconnectedAttackGraphs) {
      System.out.println(attackGraph);
    }
  }

  // Method for connecting the State u of graph1 with the state v of graph2
  public static AttackGraph connect(AttackGraph graph1, State u, AttackGraph graph2, State v, State newRoot) {
    List<State> eulerTour1 = new ArrayList<>(graph1.getEulerTour());
    assert eulerTour1.contains(u);
    List<State> eulerTour2 = new ArrayList<>(graph2.getEulerTour());
    assert eulerTour2.contains(v);
    eulerTour1 = reroute(u, eulerTour1);
    eulerTour2 = reroute(v, eulerTour2);

    List<State> newEulerTour = new ArrayList<>();
    newEulerTour.addAll(eulerTour1);
    newEulerTour.addAll(eulerTour2);
    newEulerTour.add(u);
    newEulerTour = reroute(newRoot, newEulerTour);
    return new AttackGraph(newEulerTour);
  }

  // Method for uncconnecting the State u of graph1 from the state v of graph2, which returns the list of resulting attack graphs after deleting an edge
  public static List<AttackGraph> unconnect(AttackGraph graph, State u, State v) {
    List<State> eulerTour = graph.getEulerTour();
    List<State> j = new ArrayList<>();
    List<State> k = new ArrayList<>();
    List<State> l = new ArrayList<>();
    int substart = 0;

    for (int i = 0; i < eulerTour.size(); i++) {
      if (eulerTour.get(i).equals(u) && eulerTour.get(i + 1).equals(v)) {
        j = new ArrayList<>(eulerTour.subList(0, i + 1));
        substart = i + 1;
      }

      if (eulerTour.get(i).equals(v) && eulerTour.get(i + 1).equals(u)) {
        k = new ArrayList<>(eulerTour.subList(substart, i + 1));
        l = new ArrayList<>(eulerTour.subList(i + 1, eulerTour.size()));
        break;
      }
    }

    j.remove(j.size() - 1);
    j.addAll(l);
    List<AttackGraph> result = new ArrayList<>();
    result.add(new AttackGraph(k));
    result.add(new AttackGraph(j));
    return result;
  }

  /* public static Node<State> traverseForState(State state, Node<State> root) {
    if (root.getItem().equals(state)) {
      return root;
    }

    for (Node<State> child : root.getChildren()) {
      traverseForState(state, child);
    }

    return null;
  }*/

  public static List<State> reroute(State newRoot, List<State> eulerTour) {
    // return the given euler tour if the newRoot is already the current root
    if (eulerTour.get(0).equals(newRoot)) {
      return eulerTour;
    }

    int i = 0;
    for (; i < eulerTour.size(); i++) {
      if (eulerTour.get(i).equals(newRoot)) {
        break;
      }
    }
    List<State> splittedTour1 = new ArrayList<>(eulerTour.subList(0, i));
    List<State> splittedTour2 = new ArrayList<>(eulerTour.subList(i, eulerTour.size()));
    assert !splittedTour1.isEmpty();
    splittedTour1 = splittedTour1.subList(1, splittedTour1.size());
    splittedTour1.add(newRoot);
    splittedTour2.addAll(splittedTour1);
    return splittedTour2;
  }

  // Method for querying whether state u is connected with state v in attack graph
  public static Boolean areConnected(AttackGraph attackGraph, State u, State v) {
    return attackGraph.getEulerTour().contains(u) && attackGraph.getEulerTour().contains(v);
  }


}
