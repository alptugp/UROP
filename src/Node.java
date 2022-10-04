import java.util.ArrayList;
import java.util.List;

public class Node<T> {
  private T item;
  private final List<Node<T>> children;

  public Node(T item) {
    this.item = item;
    this.children = new ArrayList<>();
  }

  public Node(T item, List<Node<T>> children) {
    this.item = item;
    this.children = children;
  }

  public void addChild(Node<T> child) {
    children.add(child);
  }

  public void removeChild(Node<T> child) {
    children.remove(child);
  }

  public T getItem() {
    return item;
  }

  public List<Node<T>> getChildren() {
    return children;
  }
}
