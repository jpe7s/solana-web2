package software.sava.solana.web2.marinade.client.http;

import software.sava.solana.web2.marinade.client.http.response.MarinadeAPY;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.web2.marinade.client.http.MarinadeHttpRestClient.DEFAULT_REQUEST_TIMEOUT;

public interface MarinadeRestClient {

  String PUBLIC_ENDPOINT = "https://api.marinade.finance/";

  static MarinadeRestClient createClient(final URI endpoint,
                                         final HttpClient httpClient,
                                         final Duration requestTimeout) {
    return new MarinadeHttpRestClient(endpoint, httpClient, requestTimeout);
  }

  static MarinadeRestClient createClient(final URI endpoint, final HttpClient httpClient) {
    return new MarinadeHttpRestClient(endpoint, httpClient, DEFAULT_REQUEST_TIMEOUT);
  }

  static MarinadeRestClient createClient() {
    return createClient(URI.create(PUBLIC_ENDPOINT));
  }

  static MarinadeRestClient createClient(final URI endpoint) {
    return createClient(endpoint, HttpClient.newHttpClient());
  }

  static MarinadeRestClient createClient(final HttpClient httpClient) {
    return createClient(URI.create(PUBLIC_ENDPOINT), httpClient);
  }

  URI endpoint();

  CompletableFuture<MarinadeAPY> getApy(final int numWeeks);

  CompletableFuture<BigDecimal> getPriceSOL();

  CompletableFuture<BigDecimal> getPriceUSD();
}
