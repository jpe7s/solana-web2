package software.sava.solana.web2.helius.client.http;

import software.sava.solana.web2.helius.client.http.response.PriorityFeesEstimates;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.web2.helius.client.http.HeliusJsonRpcClient.DEFAULT_REQUEST_TIMEOUT;
import static software.sava.rpc.json.http.request.Commitment.CONFIRMED;

public interface HeliusClient {

  static HeliusClient createHttpClient(final URI endpoint,
                                       final HttpClient httpClient,
                                       final Duration requestTimeout) {
    return new HeliusJsonRpcClient(endpoint, httpClient, requestTimeout, CONFIRMED);
  }

  static HeliusClient createHttpClient(final URI endpoint,
                                       final HttpClient httpClient) {
    return createHttpClient(endpoint, httpClient, DEFAULT_REQUEST_TIMEOUT);
  }

  static HeliusClient createHttpClient(final String endpoint, final HttpClient httpClient) {
    return createHttpClient(URI.create(endpoint), httpClient);
  }

  static HeliusClient createHttpClient(final URI endpoint) {
    return createHttpClient(endpoint, HttpClient.newHttpClient());
  }

  static HeliusClient createHttpClient(final String endpoint) {
    return createHttpClient(URI.create(endpoint));
  }

  URI endpoint();

  CompletableFuture<PriorityFeesEstimates> getPriorityFeeEstimate(final String params);

  CompletableFuture<PriorityFeesEstimates> getPriorityFeeEstimate(final List<String> accountKeys);

  CompletableFuture<PriorityFeesEstimates> getPriorityFeeEstimate(final List<String> accountKeys, final int lookBackSlots);

  CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction);

  CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction, final String transactionEncoding);

  CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction,
                                                                             final String transactionEncoding,
                                                                             final int lookBackSlots);

  CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction, final int lookBackSlots);

  CompletableFuture<BigDecimal> getRecommendedPriorityFeeEstimate(final String params);

  CompletableFuture<BigDecimal> getRecommendedPriorityFeeEstimate(final List<String> accountKeys);

  CompletableFuture<BigDecimal> getRecommendedTransactionPriorityFeeEstimate(final String transaction);

  CompletableFuture<BigDecimal> getRecommendedTransactionPriorityFeeEstimate(final String transaction, final String transactionEncoding);

}
