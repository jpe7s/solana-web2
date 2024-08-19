package software.sava.solana.web2.helius.client.http;

import software.sava.rpc.json.http.client.JsonRpcHttpClient;
import software.sava.rpc.json.http.request.Commitment;
import software.sava.solana.web2.helius.client.http.request.Encoding;
import software.sava.solana.web2.helius.client.http.response.PriorityFeesEstimates;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import static software.sava.solana.web2.helius.client.http.request.PriorityFeeRequest.serializeParams;
import static software.sava.solana.web2.helius.client.http.request.PriorityFeeRequest.serializeRecommendedParams;

final class HeliusJsonRpcClient extends JsonRpcHttpClient implements HeliusClient {

  static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(13);
  private static final Function<HttpResponse<byte[]>, PriorityFeesEstimates> PRIORITY_FEES = applyResponseResult(PriorityFeesEstimates::parseLevels);
  private static final Function<HttpResponse<byte[]>, BigDecimal> PRIORITY_FEE = applyResponseResult(ji -> ji.skipUntil("priorityFeeEstimate").readBigDecimalDropZeroes());

  private final AtomicLong id;
  private final Commitment defaultCommitment;

  HeliusJsonRpcClient(final URI endpoint,
                      final HttpClient httpClient,
                      final Duration requestTimeout,
                      final Commitment defaultCommitment) {
    super(endpoint, httpClient, requestTimeout);
    this.id = new AtomicLong(System.currentTimeMillis());
    this.defaultCommitment = defaultCommitment;
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getPriorityFeeEstimate(final String params) {
    return sendPostRequest(PRIORITY_FEES, String.format("""
            {"jsonrpc":"2.0","id":%d,"method":"getPriorityFeeEstimate","params":[{%s}]}""",
        id.incrementAndGet(), params));
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getPriorityFeeEstimate(final List<String> accountKeys) {
    return getPriorityFeeEstimate(serializeParams(accountKeys));
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getPriorityFeeEstimate(final List<String> accountKeys,
                                                                         final int lookBackSlots) {
    return getPriorityFeeEstimate(serializeParams(accountKeys, lookBackSlots));
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction) {
    return getPriorityFeeEstimate(serializeParams(transaction));
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction,
                                                                                    final Encoding transactionEncoding) {
    return getPriorityFeeEstimate(serializeParams(transaction, transactionEncoding));
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction,
                                                                                    final Encoding transactionEncoding,
                                                                                    final int lookBackSlots) {
    return getPriorityFeeEstimate(serializeParams(transaction, transactionEncoding, lookBackSlots));
  }

  @Override
  public CompletableFuture<PriorityFeesEstimates> getTransactionPriorityFeeEstimate(final String transaction,
                                                                                    final int lookBackSlots) {
    return getPriorityFeeEstimate(serializeParams(transaction, lookBackSlots));
  }

  @Override
  public CompletableFuture<BigDecimal> getRecommendedPriorityFeeEstimate(final String params) {
    final var body = String.format("""
            {"jsonrpc":"2.0","id":%d,"method":"getPriorityFeeEstimate","params":[{%s}]}""",
        id.incrementAndGet(), params);
    return sendPostRequest(PRIORITY_FEE, body);
  }

  @Override
  public CompletableFuture<BigDecimal> getRecommendedPriorityFeeEstimate(final List<String> accountKeys) {
    return getRecommendedPriorityFeeEstimate(serializeRecommendedParams(accountKeys));
  }

  @Override
  public CompletableFuture<BigDecimal> getRecommendedTransactionPriorityFeeEstimate(final String transaction) {
    return getRecommendedPriorityFeeEstimate(serializeRecommendedParams(transaction));
  }

  @Override
  public CompletableFuture<BigDecimal> getRecommendedTransactionPriorityFeeEstimate(final String transaction,
                                                                                    final Encoding transactionEncoding) {
    return getRecommendedPriorityFeeEstimate(serializeRecommendedParams(transaction, transactionEncoding));
  }
}
