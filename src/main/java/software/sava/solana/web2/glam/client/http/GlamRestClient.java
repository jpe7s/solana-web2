package software.sava.solana.web2.glam.client.http;

import software.sava.core.accounts.PublicKey;
import software.sava.solana.web2.glam.client.http.request.GlamTxOptions;
import software.sava.solana.web2.jupiter.client.http.response.JupiterQuote;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.web2.glam.client.http.GlamHttpRestClient.DEFAULT_REQUEST_TIMEOUT;

public interface GlamRestClient {

  String PUBLIC_ENDPOINT = "https://api.glam.systems";

  static GlamRestClient createClient(final PublicKey fundPublicKey,
                                     final PublicKey signerPublicKey,
                                     final URI apiEndpoint,
                                     final HttpClient httpClient,
                                     final Duration requestTimeout) {
    return new GlamHttpRestClient(fundPublicKey, signerPublicKey, apiEndpoint, httpClient, requestTimeout);
  }

  static GlamRestClient createClient(final PublicKey fundPublicKey,
                                     final PublicKey signerPublicKey,
                                     final URI apiEndpoint,
                                     final HttpClient httpClient) {
    return createClient(fundPublicKey, signerPublicKey, apiEndpoint, httpClient, DEFAULT_REQUEST_TIMEOUT);
  }

  static GlamRestClient createClient(final PublicKey fundPublicKey,
                                     final PublicKey signerPublicKey,
                                     final URI apiEndpoint) {
    return createClient(fundPublicKey, signerPublicKey, apiEndpoint, HttpClient.newHttpClient());
  }

  static GlamRestClient createClient(final PublicKey fundPublicKey,
                                     final PublicKey signerPublicKey,
                                     final HttpClient httpClient) {
    return createClient(fundPublicKey, signerPublicKey, URI.create(PUBLIC_ENDPOINT), httpClient);
  }

  static GlamRestClient createClient(final PublicKey fundPublicKey, final PublicKey signerPublicKey) {
    return createClient(fundPublicKey, signerPublicKey, URI.create(PUBLIC_ENDPOINT));
  }

  CompletableFuture<byte[]> jupiterSwap(final JupiterQuote jupiterQuote, final byte[] swapInstructions);

  CompletableFuture<byte[]> jupiterSwap(final JupiterQuote jupiterQuote,
                                        final byte[] swapInstructions,
                                        final GlamTxOptions txOptions);

  CompletableFuture<byte[]> jupiterSwapWithComputeUnitPrice(final JupiterQuote jupiterQuote,
                                                            final byte[] swapInstructions,
                                                            final long computeUnitPriceMicroLamports);

  CompletableFuture<byte[]> jupiterSwapAndJitoTip(final JupiterQuote jupiterQuote,
                                                  final byte[] swapInstructions,
                                                  final long jitoTipLamports);

  CompletableFuture<byte[]> jupiterSwapWithComputeUnitPriceAndJitoTip(final JupiterQuote jupiterQuote,
                                                                      final byte[] swapInstructions,
                                                                      final long computeUnitPriceMicroLamports,
                                                                      final long jitoTipLamports);

  CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimit(final JupiterQuote jupiterQuote,
                                                            final byte[] swapInstructions,
                                                            final long computeUnitLimit);

  CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimitAndPrice(final JupiterQuote jupiterQuote,
                                                                    final byte[] swapInstructions,
                                                                    final long computeUnitLimit,
                                                                    final long computeUnitPriceMicroLamports);

  CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimitAndJitoTip(final JupiterQuote jupiterQuote,
                                                                      final byte[] swapInstructions,
                                                                      final long computeUnitPriceMicroLamports,
                                                                      final long jitoTipLamports);

  CompletableFuture<byte[]> jupiterSwapWithComputeUnitLimitAndPriceAndJitoTip(final JupiterQuote jupiterQuote,
                                                                              final byte[] swapInstructions,
                                                                              final long computeUnitLimit,
                                                                              final long computeUnitPriceMicroLamports,
                                                                              final long jitoTipLamports);
}
