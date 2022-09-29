import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouterInterface {
  private final Router router;
  private final Integer id;
  private List<Host> hosts;

  public RouterInterface(Router router, Integer id) {
    this.router = router;
    this.id = id;
    this.hosts = new ArrayList<>();
  }

  public RouterInterface(Router router, Integer id, List<Host> hosts) {
    this.router = router;
    this.id = id;
    this.hosts = hosts;
  }

  public void addHost(Host host) {
    hosts.add(host);
  }

  public void removeHost(Host host) {
    hosts.remove(host);
  }

  public Integer getId() {
    return id;
  }

  public List<Host> getHosts() {
    return hosts;
  }

  public Router getRouter() {
    return router;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RouterInterface that = (RouterInterface) o;
    return Objects.equals(router, that.router) && Objects.equals(id, that.id) && Objects.equals(hosts, that.hosts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(router, id, hosts);
  }
}
