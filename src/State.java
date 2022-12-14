import java.util.Objects;

public class State {
  private final String name;

  public State(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "State " + name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    State state = (State) o;
    return name.equals(state.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
