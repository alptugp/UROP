import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Implement {
  static List<Router> routers = new ArrayList<>();
  static Map<RouterInterface, List<Router>> interfacesToRouters = new HashMap<>();

  public static void main(String[] args) {


    Router router = routers.get(0);
    TreeNode<Router, RouterInterface> root = new TreeNode<>(router);
    treeConstructor(root);
  }

  public static void treeConstructor(TreeNode<Router, RouterInterface> root) {
    Router router = root.getItem();
    for (RouterInterface routerInterface : router.getInterfaces()) {
      TreeNode<RouterInterface, Router> childInterface = new TreeNode<>(routerInterface);
      root.addChild(childInterface);
      interfacesToRouters.get(routerInterface).remove(router);
      for (Router nextRouter : interfacesToRouters.get(routerInterface)) {
        nextRouter.removeInterface(routerInterface);
        TreeNode<Router, RouterInterface> childRouter = new TreeNode<Router, RouterInterface>(nextRouter);
        childInterface.addChild(childRouter);
        treeConstructor(childRouter);
      }
    }
  }
}
