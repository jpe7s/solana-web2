package software.sava.solana.web2.birdeye.client.http;

import software.sava.rpc.json.http.client.JsonHttpClient;
import software.sava.solana.web2.birdeye.client.http.request.SortType;
import software.sava.solana.web2.birdeye.client.http.request.TokenListSortBy;
import software.sava.solana.web2.birdeye.client.http.response.TokenList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static java.lang.String.format;
import static java.net.http.HttpResponse.BodyHandlers.ofByteArray;

final class BirdeyeHttpClient extends JsonHttpClient implements BirdeyeClient {

  public static final String DEFAULT_CHAIN = "solana";
  static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(13);
  private static final Function<HttpResponse<byte[]>, TokenList> TOKEN_LIST_PARSER = applyResponse(TokenList::parseTokens);

  private final String apiKey;
  private final String chain;

  BirdeyeHttpClient(final URI quoteApiEndpoint,
                    final HttpClient httpClient,
                    final Duration requestTimeout,
                    final String apiKey,
                    final String chain) {
    super(quoteApiEndpoint, httpClient, requestTimeout);
    this.apiKey = apiKey;
    this.chain = chain;
  }

  private void setHeaders(final HttpRequest.Builder request) {
    request.setHeader("X-API-KEY", apiKey);
    request.setHeader("x-chain", chain);
  }

  @Override
  public CompletableFuture<TokenList> tokenList(final TokenListSortBy sortBy, final SortType sortType, final int offset, final int limit) {
    final var request = newGetRequest(endpoint.resolve(format(
        "/public/tokenlist?sort_by=%s&sort_type=%s&offset=%d&limit=%d",
        sortBy, sortType, offset, limit)));
    setHeaders(request);
    return httpClient.sendAsync(request.build(), ofByteArray()).thenApply(TOKEN_LIST_PARSER);
  }
}
