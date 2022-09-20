import java.util.ArrayList;
import java.util.List;

public class RouterInterface {
  private final Router router;
  private Integer id;
  private final List<Host> hosts;

  public RouterInterface(Router router, Integer id) {
    this.router = router;
    this.id = id;
    this.hosts = new ArrayList<>();
  }

  public RouterInterface(Router router, Integer id, List<Host> hosts) {
    this.router = router;
    this.id = id;
    this.hosts = new ArrayList<>();
  }

  public void addHost(Host host) {
    hosts.add(host);
  }

  public void removeHost(Host host) {
    hosts.remove(host);
  }

}
