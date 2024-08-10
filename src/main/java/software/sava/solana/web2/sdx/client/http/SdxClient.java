package software.sava.solana.web2.sdx.client.http;

import software.sava.solana.web2.sdx.client.http.request.Network;
import software.sava.solana.web2.sdx.client.http.response.OptionMarkets;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.web2.sdx.client.http.SdxHttpClient.DEFAULT_REQUEST_TIMEOUT;

public interface SdxClient {

  String DEFAULT_ENDPOINT = "https://bidasklive-d2jplirhdq-as.a.run.app";

  static SdxClient createClient(final URI endpoint,
                                final HttpClient httpClient,
                                final Duration requestTimeout) {
    return new SdxHttpClient(endpoint, httpClient, requestTimeout);
  }

  static SdxClient createClient(final HttpClient httpClient, final Duration requestTimeout) {
    return createClient(URI.create(DEFAULT_ENDPOINT), httpClient, requestTimeout);
  }

  static SdxClient createClient(final HttpClient httpClient) {
    return createClient(httpClient, DEFAULT_REQUEST_TIMEOUT);
  }

  static SdxClient createClient(final URI endpoint, final HttpClient httpClient) {
    return createClient(endpoint, httpClient, DEFAULT_REQUEST_TIMEOUT);
  }

  static SdxClient createClient(final URI endpoint) {
    return createClient(endpoint, HttpClient.newHttpClient());
  }

  static SdxClient createClient() {
    return createClient(URI.create(DEFAULT_ENDPOINT));
  }

  URI endpoint();
  
  default CompletableFuture<List<OptionMarkets>> bidAskLive(final Collection<String> assets) {
    return bidAskLive(assets, Network.mainnet_beta);
  }

  CompletableFuture<List<OptionMarkets>> bidAskLive(final Collection<String> assets, final Network network);

  default CompletableFuture<List<OptionMarkets>> bidAskLive(final String asset) {
    return bidAskLive(asset, Network.mainnet_beta);
  }

  CompletableFuture<List<OptionMarkets>> bidAskLive(final String asset, final Network network);
}
