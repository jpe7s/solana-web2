package software.sava.solana.web2.glam.client.http;

import software.sava.core.accounts.PublicKey;
import software.sava.solana.web2.glam.client.http.request.GlamTxOptions;
import software.sava.solana.web2.jupiter.client.http.response.JupiterQuote;
import software.sava.rpc.json.http.client.JsonHttpClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class GlamHttpRestClient extends JsonHttpClient implements GlamRestClient {

  static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(13);

  private static final Function<HttpResponse<byte[]>, byte[]> TX_PARSER = applyResponse(ji -> ji.skipUntil("tx").decodeBase64String());

  private final String bodyPrefix;
  private final URI jupiterSwapURI;

  GlamHttpRestClient(final PublicKey fundPublicKey,
                     final PublicKey signerPublicKey,
                     final URI endpoint,
                     final HttpClient httpClient,
                     final Duration requestTimeout) {
    super(endpoint, httpClient, requestTimeout);
    this.jupiterSwapURI = endpoint.resolve("/tx/jupiter/swap");
    this.bodyPrefix = String.format("""
        {"encoding":"base64","fund":"%s","signer":"%s\"""", fundPublicKey.toBase58(), signerPublicKey.toBase58());
  }

  private CompletableFuture<byte[]> jupiterSwap(final String body) {
    return sendPostRequest(jupiterSwapURI, TX_PARSER, body);
  }

  private static String stripJupiterQuoteRequest(final byte[] swapInstructions) {
    return new String(swapInstructions, 1, swapInstructions.length - 2);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwap(final JupiterQuote jupiterQuote, final byte[] swapInstructions) {
    final var body = String.format("""
        %s,"quoteResponse":%s,%s}""", bodyPrefix, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwap(final JupiterQuote jupiterQuote,
                                               final byte[] swapInstructions,
                                               final GlamTxOptions txOptions) {
    final var body = String.format("""
            %s%s,"quoteResponse":%s,%s}""",
        bodyPrefix, txOptions.toJson(), jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapWithComputeUnitPrice(final JupiterQuote jupiterQuote,
                                                                   final byte[] swapInstructions,
                                                                   final long computeUnitPriceMicroLamports) {
    final var body = String.format("""
            %s,"computeUnitPriceMicroLamports":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, computeUnitPriceMicroLamports, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapAndJitoTip(final JupiterQuote jupiterQuote,
                                                         final byte[] swapInstructions,
                                                         final long jitoTipLamports) {
    final var body = String.format("""
            %s,"jitoTipLamports":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, jitoTipLamports, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapWithComputeUnitPriceAndJitoTip(final JupiterQuote jupiterQuote,
                                                                             final byte[] swapInstructions,
                                                                             final long computeUnitPriceMicroLamports,
                                                                             final long jitoTipLamports) {
    final var body = String.format("""
            %s,"computeUnitPriceMicroLamports":%d,"jitoTipLamports":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, computeUnitPriceMicroLamports, jitoTipLamports, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimit(final JupiterQuote jupiterQuote,
                                                                   final byte[] swapInstructions,
                                                                   final long computeUnitLimit) {
    final var body = String.format("""
            %s,"computeUnitLimit":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, computeUnitLimit, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimitAndPrice(final JupiterQuote jupiterQuote,
                                                                           final byte[] swapInstructions,
                                                                           final long computeUnitLimit,
                                                                           final long computeUnitPriceMicroLamports) {
    final var body = String.format("""
            %s,"computeUnitLimit":%d,"computeUnitPriceMicroLamports":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, computeUnitLimit, computeUnitPriceMicroLamports, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimitAndJitoTip(final JupiterQuote jupiterQuote,
                                                                             final byte[] swapInstructions,
                                                                             final long computeUnitLimit,
                                                                             final long jitoTipLamports) {
    final var body = String.format("""
            %s,"computeUnitLimit":%d,"jitoTipLamports":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, computeUnitLimit, jitoTipLamports, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }

  @Override
  public CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimitAndPriceAndJitoTip(final JupiterQuote jupiterQuote,
                                                                                     final byte[] swapInstructions,
                                                                                     final long computeUnitLimit,
                                                                                     final long computeUnitPriceMicroLamports,
                                                                                     final long jitoTipLamports) {
    final var body = String.format("""
            %s,"computeUnitLimit":%d,"computeUnitPriceMicroLamports":%d,"jitoTipLamports":%d,"quoteResponse":%s,%s}""",
        bodyPrefix, computeUnitLimit, computeUnitPriceMicroLamports, jitoTipLamports, jupiterQuote.quoteResponseJson(), stripJupiterQuoteRequest(swapInstructions));
    return jupiterSwap(body);
  }
}
