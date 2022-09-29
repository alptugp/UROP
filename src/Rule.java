import java.util.Objects;

public class Rule<T> {
  private T source;
  private T destination;

  public Rule(T source, T destination) {
    this.source = source;
    this.destination = destination;
  }

  public T getSource() {
    return source;
  }

  public T getDestination() {
    return destination;
  }

  public void setSource(T source) {
    this.source = source;
  }

  public void setDestination(T destination) {
    this.destination = destination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rule<?> rule = (Rule<?>) o;
    return Objects.equals(source, rule.source) && Objects.equals(destination, rule.destination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, destination);
  }
}
