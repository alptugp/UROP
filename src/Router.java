import java.util.ArrayList;
import java.util.List;

public class Router {
  private final List<RouterInterface> interfaces;
  private final List<Rule> packetFilter;
  private final Integer id;

  public Router(Integer id) {
    this.interfaces = new ArrayList<>();
    this.packetFilter = new ArrayList<>();
    this.id = id;
  }

  public Router(Integer id, List<RouterInterface> interfaces, List<Rule> packetFilter) {
    this.interfaces = interfaces;
    this.packetFilter = packetFilter;
    this.id = id;
  }

  public void addInterface(RouterInterface routerInterface) {
    interfaces.add(routerInterface);
  }

  public void removeInterface(RouterInterface routerInterface) {
    interfaces.remove(routerInterface);
  }

  public void addRule(Host source, Host destination) {
    packetFilter.add(new Rule(source, destination));
  }

  public void removeRule(Host source, Host destination) {
    packetFilter.remove(new Rule(source, destination));
  }

  public List<Rule> getPacketFilter() {
    return packetFilter;
  }

  public Integer getId() {
    return id;
  }

  public List<RouterInterface> getInterfaces() {
    return interfaces;
  }
}
