package software.sava.solana.web2.jito.client.http;

import software.sava.solana.web2.jito.client.http.response.BundleStatus;
import software.sava.solana.web2.jito.client.http.response.SendTxResult;
import software.sava.rpc.json.http.request.Commitment;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface JitoClient {

  String DEFAULT_URL = "https://mainnet.block-engine.jito.wtf/";
  String AMSTERDAM = "https://amsterdam.mainnet.block-engine.jito.wtf/";
  String FRANKFURT = "https://frankfurt.mainnet.block-engine.jito.wtf/";
  String NEW_YORK = "https://ny.mainnet.block-engine.jito.wtf/";
  String TOKYO = "https://tokyo.mainnet.block-engine.jito.wtf/";

  static JitoClient createHttpClient(final URI endpoint,
                                     final HttpClient httpClient,
                                     final Duration requestTimeout,
                                     final Commitment defaultCommitment,
                                     final String apiAuthKey) {
    return new JitoJsonRpcClient(endpoint, httpClient, requestTimeout, defaultCommitment, apiAuthKey);
  }

  static JitoClient createHttpClient(final URI endpoint,
                                     final HttpClient httpClient,
                                     final Duration requestTimeout,
                                     final Commitment defaultCommitment) {
    return createHttpClient(endpoint, httpClient, requestTimeout, defaultCommitment, null);
  }

  URI endpoint();

  CompletableFuture<List<String>> getTipAccounts();

  CompletableFuture<BundleStatus> getBundleStatus(final String bundleId);

  CompletableFuture<Map<String, BundleStatus>> getBundleStatuses(final Collection<String> bundleIds);

  CompletableFuture<SendTxResult> sendBundleOnly(final Commitment preflightCommitment,
                                                 final String base64SignedTx,
                                                 final int maxRetries);

  default CompletableFuture<SendTxResult> sendBundleOnly(final String base64SignedTx, final int maxRetries) {
    return sendBundleOnly(Commitment.PROCESSED, base64SignedTx, maxRetries);
  }

  default CompletableFuture<SendTxResult> sendBundleOnly(final String base64SignedTx) {
    return sendBundleOnly(Commitment.PROCESSED, base64SignedTx, 1);
  }

  CompletableFuture<SendTxResult> sendTransactionSkipPreflight(final Commitment preflightCommitment,
                                                               final String base64SignedTx,
                                                               final int maxRetries);

  default CompletableFuture<SendTxResult> sendTransactionSkipPreflight(final String base64SignedTx, final int maxRetries) {
    return sendTransactionSkipPreflight(Commitment.PROCESSED, base64SignedTx, maxRetries);
  }

  default CompletableFuture<SendTxResult> sendTransactionSkipPreflight(final String base64SignedTx) {
    return sendTransactionSkipPreflight(Commitment.PROCESSED, base64SignedTx, 1);
  }

  CompletableFuture<SendTxResult> sendTransaction(final Commitment preflightCommitment,
                                                  final String base64SignedTx,
                                                  final int maxRetries);

  default CompletableFuture<SendTxResult> sendTransaction(final String base64SignedTx, final int maxRetries) {
    return sendTransaction(Commitment.CONFIRMED, base64SignedTx, maxRetries);
  }

  default CompletableFuture<SendTxResult> sendTransaction(final String base64SignedTx) {
    return sendTransaction(Commitment.CONFIRMED, base64SignedTx, 1);
  }

  CompletableFuture<String> sendBundle(final String base58SignedTransaction);

  CompletableFuture<String> sendBundle(final Collection<String> base58SignedTransactions);

  CompletableFuture<String> sendBundle(final String[] base58SignedTransactions);

  CompletableFuture<String> sendBundle(final byte[] signedTransaction);

  CompletableFuture<String> sendBundle(final byte[][] signedTransactions);

  CompletableFuture<String> sendBundleBytes(final Collection<byte[]> signedTransactions);
}
