public class Rule {
  private Host source;
  private Host destination;

  public Rule(Host source, Host destination) {
    this.source = source;
    this.destination = destination;
  }

  public Host getSource() {
    return source;
  }

  public Host getDestination() {
    return destination;
  }

  public void setSource(Host source) {
    this.source = source;
  }

  public void setDestination(Host destination) {
    this.destination = destination;
  }
}
