import java.util.ArrayList;
import java.util.List;

public class TreeNode<T, L> {
  private T item;
  private final List<TreeNode<L, T>> children;

  public TreeNode(T item) {
    this.item = item;
    this.children = new ArrayList<>();
  }

  public TreeNode(T item, List<TreeNode<L, T>> children) {
    this.item = item;
    this.children = children;
  }

  public void addChild(TreeNode<L, T> child) {
    children.add(child);
  }

  public void removeChild(TreeNode<L, T> child) {
    children.remove(child);
  }

  public T getItem() {
    return item;
  }

  public List<TreeNode<L, T>> getChildren() {
    return children;
  }
}
