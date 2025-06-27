package software.sava.solana.web2.sanctum.client.http;

import software.sava.core.accounts.PublicKey;
import software.sava.core.util.LamportDecimal;
import software.sava.solana.web2.sanctum.client.http.request.SanctumSwapRequest;
import software.sava.solana.web2.sanctum.client.http.request.SwapMode;
import software.sava.solana.web2.sanctum.client.http.response.SanctumQuote;
import software.sava.solana.web2.sanctum.client.http.response.StakePoolContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static software.sava.solana.web2.sanctum.client.http.SanctumHttpClient.DEFAULT_REQUEST_TIMEOUT;

public interface SanctumClient {

  String PUBLIC_ENDPOINT = "https://api.sanctum.so";
  String EXTRA_API_ENDPOINT = "https://sanctum-extra-api.ngrok.dev";

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return new SanctumHttpClient(
        apiEndpoint,
        extraApiEndpoint,
        httpClient,
        requestTimeout,
        extendRequest,
        applyResponse
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return new SanctumHttpClient(
        apiEndpoint,
        extraApiEndpoint,
        httpClient,
        requestTimeout,
        null,
        applyResponse
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest) {
    return new SanctumHttpClient(
        apiEndpoint,
        extraApiEndpoint,
        httpClient,
        requestTimeout,
        extendRequest,
        null
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final Duration requestTimeout) {
    return createClient(apiEndpoint, extraApiEndpoint, httpClient, requestTimeout, null, null);
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        httpClient,
        DEFAULT_REQUEST_TIMEOUT,
        extendRequest,
        applyResponse
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        httpClient,
        DEFAULT_REQUEST_TIMEOUT,
        null,
        applyResponse
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        httpClient,
        DEFAULT_REQUEST_TIMEOUT,
        extendRequest,
        null
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final HttpClient httpClient) {
    return createClient(apiEndpoint, extraApiEndpoint, httpClient, DEFAULT_REQUEST_TIMEOUT);
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        HttpClient.newHttpClient(),
        DEFAULT_REQUEST_TIMEOUT,
        extendRequest,
        applyResponse
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        HttpClient.newHttpClient(),
        DEFAULT_REQUEST_TIMEOUT,
        null,
        applyResponse
    );
  }

  static SanctumClient createClient(final URI apiEndpoint,
                                    final URI extraApiEndpoint,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        HttpClient.newHttpClient(),
        DEFAULT_REQUEST_TIMEOUT,
        extendRequest,
        null
    );
  }

  static SanctumClient createClient(final URI apiEndpoint, final URI extraApiEndpoint) {
    return createClient(
        apiEndpoint,
        extraApiEndpoint,
        HttpClient.newHttpClient(),
        DEFAULT_REQUEST_TIMEOUT,
        null,
        null
    );
  }

  static SanctumClient createClient(final HttpClient httpClient,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(
        URI.create(PUBLIC_ENDPOINT),
        URI.create(EXTRA_API_ENDPOINT),
        httpClient,
        extendRequest,
        applyResponse
    );
  }

  static SanctumClient createClient(final HttpClient httpClient,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(httpClient, null, applyResponse);
  }

  static SanctumClient createClient(final HttpClient httpClient,
                                    final UnaryOperator<HttpRequest.Builder> extendRequest) {
    return createClient(httpClient, extendRequest, null);
  }

  static SanctumClient createClient(final UnaryOperator<HttpRequest.Builder> extendRequest,
                                    final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient(
        URI.create(PUBLIC_ENDPOINT),
        URI.create(EXTRA_API_ENDPOINT),
        extendRequest,
        applyResponse
    );
  }

  static SanctumClient createClient(final UnaryOperator<HttpRequest.Builder> extendRequest) {
    return createClient(extendRequest, null);
  }

  static SanctumClient createClient(final Predicate<HttpResponse<byte[]>> applyResponse) {
    return createClient((UnaryOperator<HttpRequest.Builder>) null, applyResponse);
  }

  static SanctumClient createClient(final HttpClient httpClient) {
    return createClient(httpClient, null, null);
  }

  static SanctumClient createClient() {
    return createClient((UnaryOperator<HttpRequest.Builder>) null, null);
  }

  URI endpoint();

  CompletableFuture<Map<String, BigDecimal>> price(final Collection<String> tokenMint);

  default CompletableFuture<Map<String, BigDecimal>> tokenPrices(final Collection<PublicKey> tokenMint) {
    return price(tokenMint.stream().map(PublicKey::toBase58).collect(Collectors.toSet()));
  }

  CompletableFuture<SanctumQuote> quote(final String inputMint,
                                        final String outputMint,
                                        final BigInteger amount,
                                        final SwapMode swapMode);

  default CompletableFuture<SanctumQuote> quote(final String inputMint,
                                                final String outputMint,
                                                final BigInteger amount) {
    return quote(inputMint, outputMint, amount, SwapMode.ExactIn);
  }

  default CompletableFuture<SanctumQuote> quote(final String inputMint,
                                                final String outputMint,
                                                final BigDecimal amount,
                                                final SwapMode swapMode) {
    return quote(inputMint, outputMint, amount.movePointRight(LamportDecimal.LAMPORT_DIGITS).toBigInteger(), swapMode);
  }

  default CompletableFuture<SanctumQuote> quote(final String inputMint,
                                                final String outputMint,
                                                final BigDecimal amount) {
    return quote(inputMint, outputMint, amount, SwapMode.ExactIn);
  }

  CompletableFuture<byte[]> swap(final String postBody);

  default CompletableFuture<byte[]> swap(final SanctumSwapRequest sanctumSwapRequest) {
    return swap(sanctumSwapRequest.serialize());
  }

  CompletableFuture<Map<String, BigDecimal>> solValue(final Collection<String> tokenMints);

  CompletableFuture<List<StakePoolContext>> fetchSanctumLstList();
}
