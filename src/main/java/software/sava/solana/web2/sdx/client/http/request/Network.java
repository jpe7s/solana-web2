package software.sava.solana.web2.sdx.client.http.request;

public enum Network {

  mainnet_beta,
  devnet;

  private final String param;

  Network() {
    this.param = this.name().replace('_', '-');
  }

  public String param() {
    return this.param;
  }
}
