package software.sava.solana.web2.birdeye.client.http;

import software.sava.solana.web2.birdeye.client.http.request.SortType;
import software.sava.solana.web2.birdeye.client.http.request.TokenListSortBy;
import software.sava.solana.web2.birdeye.client.http.response.TokenList;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.web2.birdeye.client.http.BirdeyeHttpClient.DEFAULT_CHAIN;
import static software.sava.solana.web2.birdeye.client.http.BirdeyeHttpClient.DEFAULT_REQUEST_TIMEOUT;

public interface BirdeyeClient {

  String PUBLIC_ENDPOINT = "https://public-api.birdeye.so";

  static BirdeyeClient createClient(final URI quoteApiEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout,
                                    final String apiKey,
                                    final String chain) {
    return new BirdeyeHttpClient(quoteApiEndpoint, httpClient, requestTimeout, apiKey, chain);
  }

  static BirdeyeClient createClient(final URI quoteApiEndpoint,
                                    final HttpClient httpClient,
                                    final String apiKey,
                                    final String chain) {
    return createClient(quoteApiEndpoint, httpClient, DEFAULT_REQUEST_TIMEOUT, apiKey, chain);
  }

  static BirdeyeClient createClient(final URI quoteApiEndpoint, final String apiKey, final String chain) {
    return createClient(quoteApiEndpoint, HttpClient.newHttpClient(), apiKey, chain);
  }

  static BirdeyeClient createClient(final String apiKey, final String chain) {
    return createClient(URI.create(PUBLIC_ENDPOINT), apiKey, chain);
  }

  static BirdeyeClient createClient(final HttpClient httpClient, final String apiKey, final String chain) {
    return createClient(URI.create(PUBLIC_ENDPOINT), httpClient, apiKey, chain);
  }

  static BirdeyeClient createClient(final URI quoteApiEndpoint, final HttpClient httpClient, final String apiKey) {
    return createClient(quoteApiEndpoint, httpClient, apiKey, DEFAULT_CHAIN);
  }

  static BirdeyeClient createClient(final URI quoteApiEndpoint, final String apiKey) {
    return createClient(quoteApiEndpoint, apiKey, DEFAULT_CHAIN);
  }

  static BirdeyeClient createClient(final String apiKey) {
    return createClient(apiKey, DEFAULT_CHAIN);
  }

  static BirdeyeClient createClient(final HttpClient httpClient, final String apiKey) {
    return createClient(httpClient, apiKey, DEFAULT_CHAIN);
  }

  URI endpoint();

  default CompletableFuture<TokenList> tokenList() {
    return tokenList(TokenListSortBy.v24hUSD);
  }

  default CompletableFuture<TokenList> tokenList(final int limit) {
    return tokenList(TokenListSortBy.v24hUSD, SortType.desc, 0, limit);
  }

  default CompletableFuture<TokenList> tokenList(final TokenListSortBy sortBy) {
    return tokenList(sortBy, SortType.desc);
  }

  default CompletableFuture<TokenList> tokenList(final TokenListSortBy sortBy, final SortType sortType) {
    return tokenList(sortBy, sortType, 0, 50);
  }

  CompletableFuture<TokenList> tokenList(final TokenListSortBy sortBy, final SortType sortType, final int offset, final int limit);
}
