package software.sava.solana.web2.jupiter.client.http;

import software.sava.core.accounts.PublicKey;
import software.sava.solana.web2.jupiter.client.http.request.JupiterQuoteRequest;
import software.sava.solana.web2.jupiter.client.http.request.JupiterTokenTag;
import software.sava.solana.web2.jupiter.client.http.response.JupiterQuote;
import software.sava.solana.web2.jupiter.client.http.response.JupiterSwapTx;
import software.sava.solana.web2.jupiter.client.http.response.MarketRecord;
import software.sava.solana.web2.jupiter.client.http.response.TokenContext;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static software.sava.solana.web2.jupiter.client.http.JupiterHttpClient.DEFAULT_REQUEST_TIMEOUT;

public interface JupiterClient {

  String PUBLIC_QUOTE_ENDPOINT = "https://quote-api.jup.ag";
  String PUBLIC_TOKEN_LIST_ENDPOINT = "https://tokens.jup.ag";

  static JupiterClient createClient(final URI quoteEndpoint,
                                    final URI tokensEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return new JupiterHttpClient(quoteEndpoint, tokensEndpoint, httpClient, requestTimeout, applyResponse);
  }

  static JupiterClient createClient(final URI quoteEndpoint,
                                    final URI tokensEndpoint,
                                    final HttpClient httpClient,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(quoteEndpoint, tokensEndpoint, httpClient, DEFAULT_REQUEST_TIMEOUT, applyResponse);
  }

  static JupiterClient createClient(final HttpClient httpClient,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(URI.create(PUBLIC_QUOTE_ENDPOINT), URI.create(PUBLIC_TOKEN_LIST_ENDPOINT), httpClient, applyResponse);
  }

  static JupiterClient createClient(final URI quoteEndpoint,
                                    final URI tokensEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout) {
    return new JupiterHttpClient(quoteEndpoint, tokensEndpoint, httpClient, requestTimeout, null);
  }

  static JupiterClient createClient(final URI quoteEndpoint,
                                    final URI tokensEndpoint,
                                    final HttpClient httpClient) {
    return createClient(quoteEndpoint, tokensEndpoint, httpClient, DEFAULT_REQUEST_TIMEOUT, null);
  }

  static JupiterClient createClient(final HttpClient httpClient) {
    return createClient(URI.create(PUBLIC_QUOTE_ENDPOINT), URI.create(PUBLIC_TOKEN_LIST_ENDPOINT), httpClient, DEFAULT_REQUEST_TIMEOUT, null);
  }

  URI endpoint();

  CompletableFuture<TokenContext> token(final PublicKey mint);

  static Map<String, TokenContext> reMapBySymbol(final Map<PublicKey, TokenContext> byMintAddress) {
    final var bySymbol = HashMap.<String, TokenContext>newHashMap(byMintAddress.size());
    for (final var tokenContext : byMintAddress.values()) {
      final var previous = bySymbol.put(tokenContext.symbol(), tokenContext);
      if (previous != null) {
        throw new IllegalStateException(String.format("Duplicate token symbol %s:%n  %s%n  %s", tokenContext.symbol(), previous, tokenContext));
      }
    }
    return bySymbol;
  }

  CompletableFuture<Map<PublicKey, TokenContext>> allTokens();

  CompletableFuture<Map<PublicKey, TokenContext>> tokenMap(final JupiterTokenTag tag);

  CompletableFuture<Map<PublicKey, TokenContext>> tokenMap(final Collection<JupiterTokenTag> tags);

  default CompletableFuture<Map<PublicKey, TokenContext>> verifiedTokenMap() {
    return tokenMap(JupiterTokenTag.verified);
  }

  CompletableFuture<Map<PublicKey, TokenContext>> tokensWithLiquidMarkets();

  default CompletableFuture<Map<PublicKey, TokenContext>> liquidStakingTokens() {
    return tokenMap(JupiterTokenTag.lst);
  }

  CompletableFuture<Map<String, PublicKey>> getDexLabelToProgramIdMap();

  CompletableFuture<JupiterSwapTx> swap(final StringBuilder jsonBodyBuilder, final JupiterQuote jupiterQuote);

  CompletableFuture<JupiterSwapTx> swap(final StringBuilder jsonBodyBuilder, final byte[] quoteResponseJson);

  CompletableFuture<JupiterSwapTx> swap(final String jsonBodyPrefix, final JupiterQuote jupiterQuote);

  CompletableFuture<JupiterSwapTx> swap(final String jsonBodyPrefix, final byte[] quoteResponseJson);

  CompletableFuture<byte[]> swapInstructions(final StringBuilder jsonBodyBuilder, final JupiterQuote jupiterQuote);

  CompletableFuture<byte[]> swapInstructions(final StringBuilder jsonBodyBuilder, final byte[] quoteResponseJson);

  CompletableFuture<byte[]> swapInstructions(final String jsonBodyPrefix, final JupiterQuote jupiterQuote);

  CompletableFuture<byte[]> swapInstructions(final String jsonBodyPrefix, final byte[] quoteResponseJson);


  default CompletableFuture<JupiterQuote> getQuote(final JupiterQuoteRequest quoteRequest) {
    return getQuote(quoteRequest.amount(), quoteRequest);
  }

  CompletableFuture<JupiterQuote> getQuote(final BigInteger amount, final JupiterQuoteRequest quoteRequest);

  CompletableFuture<JupiterQuote> getQuote(final BigInteger amount, final String query);

  CompletableFuture<JupiterQuote> getQuote(final String query);

  CompletableFuture<JupiterSwapTx> swap(final StringBuilder jsonBodyBuilder,
                                        final JupiterQuote jupiterQuote,
                                        final Duration requestTimeout);

  CompletableFuture<JupiterSwapTx> swap(final StringBuilder jsonBodyBuilder,
                                        final byte[] quoteResponseJson,
                                        final Duration requestTimeout);

  CompletableFuture<JupiterSwapTx> swap(final String jsonBodyPrefix,
                                        final JupiterQuote jupiterQuote,
                                        final Duration requestTimeout);

  CompletableFuture<JupiterSwapTx> swap(final String jsonBodyPrefix,
                                        final byte[] quoteResponseJson,
                                        final Duration requestTimeout);

  CompletableFuture<byte[]> swapInstructions(final StringBuilder jsonBodyBuilder,
                                             final JupiterQuote jupiterQuote,
                                             final Duration requestTimeout);

  CompletableFuture<byte[]> swapInstructions(final StringBuilder jsonBodyBuilder,
                                             final byte[] quoteResponseJson,
                                             final Duration requestTimeout);

  CompletableFuture<byte[]> swapInstructions(final String jsonBodyPrefix,
                                             final JupiterQuote jupiterQuote,
                                             final Duration requestTimeout);

  CompletableFuture<byte[]> swapInstructions(final String jsonBodyPrefix,
                                             final byte[] quoteResponseJson,
                                             final Duration requestTimeout);

  default CompletableFuture<JupiterQuote> getQuote(final JupiterQuoteRequest quoteRequest,
                                                   final Duration requestTimeout) {
    return getQuote(quoteRequest.amount(), quoteRequest, requestTimeout);
  }

  CompletableFuture<JupiterQuote> getQuote(final BigInteger amount,
                                           final JupiterQuoteRequest quoteRequest,
                                           final Duration requestTimeout);

  CompletableFuture<JupiterQuote> getQuote(final BigInteger amount,
                                           final String query,
                                           final Duration requestTimeout);

  CompletableFuture<JupiterQuote> getQuote(final String query,
                                           final Duration requestTimeout);

  CompletableFuture<List<MarketRecord>> getMarketCache();
}
