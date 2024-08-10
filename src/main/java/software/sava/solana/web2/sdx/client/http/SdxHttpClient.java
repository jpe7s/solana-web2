package software.sava.solana.web2.sdx.client.http;

import software.sava.rpc.json.http.client.JsonHttpClient;
import software.sava.solana.web2.sdx.client.http.request.Network;
import software.sava.solana.web2.sdx.client.http.response.OptionMarkets;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

final class SdxHttpClient extends JsonHttpClient implements SdxClient {

  static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(13);

  private static final Function<HttpResponse<byte[]>, List<OptionMarkets>> BID_ASK_LIVE = applyResponse(OptionMarkets::parseMarkets);

  SdxHttpClient(final URI endpoint,
                final HttpClient httpClient,
                final Duration requestTimeout) {
    super(endpoint, httpClient, requestTimeout);
  }

  @Override
  public CompletableFuture<List<OptionMarkets>> bidAskLive(final String asset, final Network network) {
    return sendGetRequest(endpoint.resolve(String.format("/?assets=%s&network=%s", asset, network.param())), BID_ASK_LIVE);
  }

  @Override
  public CompletableFuture<List<OptionMarkets>> bidAskLive(final Collection<String> assets, final Network network) {
    final var assetsParam = URLEncoder.encode(String.join(",", assets), StandardCharsets.US_ASCII);
    return bidAskLive(assetsParam, network);
  }
}
