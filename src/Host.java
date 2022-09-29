import java.util.Objects;

public class Host {
  private final String name;

  public Host(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Host " + name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Host host = (Host) o;
    return name.equals(host.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
