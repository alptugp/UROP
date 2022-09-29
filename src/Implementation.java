import java.util.*;

public class Implementation {
  static Map<RouterInterface, List<Router>> interfacesToRouters = new HashMap<>();
  static List<Rule<RouterInterface>> interfacePacketFilters = new ArrayList<>();

  public static void main(String[] args) {
    // CREATING THE FIRST NETWORK TOPOLOGY
    Host hostA = new Host("A");
    Host hostB = new Host("B");
    Host hostC = new Host("C");

    Host hostK = new Host("K");
    Host hostL = new Host("L");
    Host hostM = new Host("M");

    Host hostO = new Host("O");
    Host hostP = new Host("P");
    Host hostR = new Host("R");


    Router R1 = new Router(1);
    RouterInterface R1I1 = new RouterInterface(R1, 1, new ArrayList<>(List.of(hostA, hostB, hostC)));
    RouterInterface R1I2 = new RouterInterface(R1, 2, new ArrayList<>(List.of(hostO, hostP, hostR)));
    RouterInterface R1I3xR2I1 = new RouterInterface(R1, 3, new ArrayList<>());
    R1.setInterfaces(new ArrayList<>(List.of(R1I1, R1I2, R1I3xR2I1)));
    List<Rule<Host>> packetFilterR1 = new ArrayList<>(List.of(new Rule<>(hostA, hostO), new Rule<>(hostR, hostB)));
    R1.setPacketFilter(packetFilterR1);

    Router R2 = new Router(2);
    // RouterInterface R2I1 = new RouterInterface(R2, 1, new ArrayList<>());
    RouterInterface R2I2 = new RouterInterface(R2, 2, new ArrayList<>(List.of(hostK, hostL, hostM)));
    R2.setInterfaces(new ArrayList<>(List.of(R1I3xR2I1, R2I2)));
    List<Rule<Host>> packetFilterR2 = new ArrayList<>(List.of(new Rule<>(hostO, hostK)));
    R2.setPacketFilter(packetFilterR2);

    List<Router> routers = new ArrayList<>(List.of(R1, R2));
    Router router = routers.get(0);
    interfacesToRouters.put(R1I3xR2I1, new ArrayList<>(List.of(R1, R2)));
    TreeNode<Router, RouterInterface> root1 = new TreeNode<>(router);
    treeConstructor(root1);


    // CREATING THE SECOND NETWORK TOPOLOGY
    Host hostU = new Host("U");
    Host hostV = new Host("V");
    Host hostN = new Host("N");

    Host hostH = new Host("H");
    Host hostW = new Host("W");
    Host hostJ = new Host("J");

    Router R3 = new Router(3);
    RouterInterface R3I1 = new RouterInterface(R3, 1, new ArrayList<>(List.of(hostU, hostV, hostN)));
    RouterInterface R3I2 = new RouterInterface(R3, 2, new ArrayList<>(List.of(hostH, hostW, hostJ)));
    // RECURSIVE CONNECTION (Host M is being connected to R3I3, although it is already in R2I2)
    RouterInterface R3I3 = new RouterInterface(R3, 3, new ArrayList<>(List.of(hostM)));
    //
    R3.setInterfaces(new ArrayList<>(List.of(R3I1, R3I2, R3I3)));
    List<Rule<Host>> packetFilterR3 = new ArrayList<>(List.of());
    R3.setPacketFilter(packetFilterR3);
    interfacePacketFilters.add(new Rule<>(R3I1, R3I2));
    TreeNode<Router, RouterInterface> root2 = new TreeNode<>(R3);
    treeConstructor(root2);

    // CONNECTING THE TWO NETWORK TOPOLOGIES AND THEIR TREE STRUCTURES
    R3.addInterface(R1I3xR2I1);
    connect(root1, root2, R1I3xR2I1);



    /*
       List<Router> list = interfacesToRouters.getOrDefault(R1I3xR2I1, new ArrayList<>());
       list.add(R3);
       interfacesToRouters.put(R1I3xR2I1, list);
     */

    List<Rule<Host>> packetFilters = new ArrayList<>();
    packetFilters.addAll(R1.getPacketFilter());
    packetFilters.addAll(R2.getPacketFilter());
    packetFilters.addAll(R3.getPacketFilter());

    // QUERYING THE COMMUNICATION BETWEEN TWO HOSTS
    if (queryConnection(root1, packetFilters, hostA, hostJ)) {
      System.out.println("Can communicate");
    } else {
      System.out.println("Cannot communicate");
    }


    // QUERYING THE REACHABILITY FROM A ROUTER TO ANOTHER ROUTER
    System.out.println(reachability(R1, R2, packetFilters, root1));
  }










  public static void treeConstructor(TreeNode<Router, RouterInterface> root) {
    Router router = root.getItem();
    for (RouterInterface routerInterface : router.getInterfaces()) {
      TreeNode<RouterInterface, Router> childInterface = new TreeNode<>(routerInterface);
      root.addChild(childInterface);
      if (interfacesToRouters.get(routerInterface) != null) {
        interfacesToRouters.get(routerInterface).remove(router);
        for (Router nextRouter : interfacesToRouters.get(routerInterface)) {
          nextRouter.removeInterface(routerInterface);
          TreeNode<Router, RouterInterface> childRouter = new TreeNode<>(nextRouter);
          childInterface.addChild(childRouter);
          treeConstructor(childRouter);
        }
      }
    }
  }

  public static TreeNode<RouterInterface, Router> traverseForHost(TreeNode<Router, RouterInterface> root, Host targetHost) {
     for (TreeNode<RouterInterface, Router> childInterface : root.getChildren()) {
       if (childInterface.getItem().getHosts().contains(targetHost)) {
         return childInterface;
       }

       if (childInterface.getItem().getHosts().isEmpty()) {
         for (TreeNode<Router, RouterInterface> childRouter : childInterface.getChildren()) {
           TreeNode<RouterInterface, Router> subResult = traverseForHost(childRouter, targetHost);
           if (subResult != null) {
             return subResult;
           }
        }
      }
    }
    return null;
  }

  public static List<Rule<Host>> transitiveClosurePF(List<Rule<Host>> packetFilters) {
    Set<Rule<Host>> closure = new HashSet<>(packetFilters);
    while (true) {
      Set<Rule<Host>> newRules = new HashSet<>();
      int oldSize = closure.size();
      for (Rule<Host> rule1 : closure) {
        for (Rule<Host> rule2 : closure) {
          if (rule1.getDestination().equals(rule2.getSource()) && !closure.contains(new Rule<>(rule1.getSource(), rule2.getDestination()))) {
            newRules.add(new Rule<>(rule1.getSource(), rule2.getDestination()));
          }
        }
      }

      Set<Rule<Host>> resultingClosure = new HashSet<>();
      resultingClosure.addAll(closure);
      resultingClosure.addAll(newRules);

      int newSize = resultingClosure.size();
      if (oldSize == newSize) {
        break;
      }

      closure = resultingClosure;
    }

    return new ArrayList<>(closure);
  }

  public static void connect(TreeNode<Router, RouterInterface> root1, TreeNode<Router, RouterInterface> root2, RouterInterface connectionPoint) {
    TreeNode<RouterInterface, Router> targetInterfaceNode = traverseForInterface(root1, connectionPoint);
    assert targetInterfaceNode != null;
    targetInterfaceNode.addChild(root2);
  }

  public static TreeNode<RouterInterface, Router> traverseForInterface(TreeNode<Router, RouterInterface> root, RouterInterface targetInterface) {
    for (TreeNode<RouterInterface, Router> childInterface : root.getChildren()) {
      if (childInterface.getItem().equals(targetInterface)) {
        return childInterface;
      }

      for (TreeNode<Router, RouterInterface> childRouter : childInterface.getChildren()) {
        TreeNode<RouterInterface, Router> subResult = traverseForInterface(childRouter, targetInterface);
        if (subResult != null) {
          return subResult;
        }
      }
    }
    return null;
  }

  public static Boolean queryConnection(TreeNode<Router, RouterInterface> root,
                                        List<Rule<Host>> packetFilters, Host source, Host destination) {
    List<Rule<Host>> transitiveClosure = transitiveClosurePF(packetFilters);
    TreeNode<RouterInterface, Router> sourceNode = traverseForHost(root, source);
    TreeNode<RouterInterface, Router> destinationNode = traverseForHost(root, destination);

    assert sourceNode != null;
    RouterInterface interfaceOfSource = sourceNode.getItem();
    assert destinationNode != null;
    RouterInterface interfaceOfDestination = destinationNode.getItem();

    if (interfaceOfSource.equals(interfaceOfDestination)) {
      return true;
    } else if (interfacePacketFilters.contains(new Rule<>(interfaceOfSource, interfaceOfDestination))) {
      return true;
    } else return transitiveClosure.contains(new Rule<>(source, destination));
  }

  public static List<Host> reachability(Router router1, Router router2, List<Rule<Host>> packetFilters, TreeNode<Router, RouterInterface> root) {
    // RETURNS THE SUBSET OF PACKETS THAT THE NETWORK WILL CARRY FROM A ROUTER TO ANOTHER ROUTER
    List<Host> hostsOfRouter1 = new ArrayList<>();
    for (RouterInterface router1Interface : router1.getInterfaces()) {
      hostsOfRouter1.addAll(router1Interface.getHosts());
    }

    List<Host> hostsOfRouter2 = new ArrayList<>();
    for (RouterInterface router2Interface : router2.getInterfaces()) {
      hostsOfRouter2.addAll(router2Interface.getHosts());
    }

    List<Host> res = new ArrayList<>();
    for (Host hostOfRouter1 : hostsOfRouter1) {
      for (Host hostOfRouter2 : hostsOfRouter2) {
        if (queryConnection(root, packetFilters, hostOfRouter1, hostOfRouter2)) {
          res.add(hostOfRouter1);
          break;
        }
      }
    }

    return res;
  }
}
