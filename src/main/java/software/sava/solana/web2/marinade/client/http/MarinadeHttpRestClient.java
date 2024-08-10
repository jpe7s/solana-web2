package software.sava.solana.web2.marinade.client.http;

import software.sava.solana.web2.marinade.client.http.response.MarinadeAPY;
import software.sava.rpc.json.http.client.JsonHttpClient;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

final class MarinadeHttpRestClient extends JsonHttpClient implements MarinadeRestClient {

  static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(13);

  private static final Function<HttpResponse<byte[]>, BigDecimal> PRICE_PARSER = applyResponse(JsonIterator::readBigDecimalDropZeroes);
  private static final Function<HttpResponse<byte[]>, MarinadeAPY> APY_PARSER = applyResponse(MarinadeAPY::parse);

  private final URI priceSolURI;
  private final URI priceUsdURI;

  MarinadeHttpRestClient(final URI endpoint,
                         final HttpClient httpClient,
                         final Duration requestTimeout) {
    super(endpoint, httpClient, requestTimeout);
    this.priceSolURI = endpoint.resolve("/msol/price_sol");
    this.priceUsdURI = endpoint.resolve("/msol/price_usd");
  }

  @Override
  public CompletableFuture<MarinadeAPY> getApy(final int numWeeks) {
    return sendGetRequest(endpoint.resolve(String.format("/msol/apy/%dw", numWeeks)), APY_PARSER);
  }

  @Override
  public CompletableFuture<BigDecimal> getPriceSOL() {
    return sendGetRequest(priceSolURI, PRICE_PARSER);
  }

  @Override
  public CompletableFuture<BigDecimal> getPriceUSD() {
    return sendGetRequest(priceUsdURI, PRICE_PARSER);
  }
}
