package software.sava.solana.web2.jupiter.client.http.request;

import software.sava.core.accounts.PublicKey;

public record JupiterSwapRequest(PublicKey userPublicKey,
                                 boolean wrapAndUnwrapSol,
                                 boolean useSharedAccounts,
                                 PublicKey feeAccount,
                                 int computeUnitPriceMicroLamports,
                                 int prioritizationFeeLamports,
                                 int jitoFeeLamports,
                                 boolean asLegacyTransaction,
                                 boolean useTokenLedger,
                                 PublicKey destinationTokenAccount,
                                 boolean skipUserAccountsRpcCalls,
                                 int minBps,
                                 int maxBps) {

  public static Builder buildRequest() {
    return new Builder();
  }

  public StringBuilder preSerialize() {
    final var builder = new StringBuilder(1_024);
    builder.append("{\"userPublicKey\":\"").append(userPublicKey).append('"');
    if (!wrapAndUnwrapSol) {
      builder.append(",\"wrapAndUnwrapSol\":false");
    }
    if (!useSharedAccounts) {
      builder.append(",\"useSharedAccounts\":false");
    }
    if (feeAccount != null) {
      builder.append(",\"feeAccount\":\"").append(feeAccount).append('"');
    }
    if (computeUnitPriceMicroLamports > 0) {
      builder.append(",\"computeUnitPriceMicroLamports\":").append(computeUnitPriceMicroLamports);
    } else if (prioritizationFeeLamports > 0) {
      builder.append(",\"prioritizationFeeLamports\":").append(prioritizationFeeLamports);
    } else if (jitoFeeLamports > 0) {
      builder.append(",\"prioritizationFeeLamports\":{\"jitoTipLamports\": ").append(jitoFeeLamports).append('}');
    }
    if (asLegacyTransaction) {
      builder.append(",\"asLegacyTransaction\":true");
    }
    if (useTokenLedger) {
      builder.append(",\"useTokenLedger\":true");
    }
    if (destinationTokenAccount != null) {
      builder.append(",\"destinationTokenAccount\":\"").append(destinationTokenAccount).append('"');
    }
    if (skipUserAccountsRpcCalls) {
      builder.append(",\"skipUserAccountsRpcCalls\":true");
    }
    if (maxBps > 0) {
      builder.append(String.format("""
              ,"dynamicSlippage": {"minBps": %d, "maxBps": %d}""",
          minBps, maxBps
      ));
    }
    return builder.append(",\"quoteResponse\":");
  }

  public static final class Builder {

    private PublicKey userPublicKey;
    private boolean wrapAndUnwrapSol = true;
    private boolean useSharedAccounts = true;
    private PublicKey feeAccount;
    private int computeUnitPriceMicroLamports = Integer.MIN_VALUE;
    private int prioritizationFeeLamports = Integer.MIN_VALUE;
    private int jitoFeeLamports = Integer.MIN_VALUE;
    private boolean asLegacyTransaction;
    private boolean useTokenLedger;
    private PublicKey destinationTokenAccount;
    private boolean skipUserAccountsRpcCalls;
    private int minBps;
    private int maxBps;

    private Builder() {
    }

    public JupiterSwapRequest createRequest() {
      return new JupiterSwapRequest(
          userPublicKey,
          wrapAndUnwrapSol,
          useSharedAccounts,
          feeAccount,
          computeUnitPriceMicroLamports,
          prioritizationFeeLamports,
          jitoFeeLamports,
          asLegacyTransaction,
          useTokenLedger,
          destinationTokenAccount,
          skipUserAccountsRpcCalls,
          minBps,
          maxBps
      );
    }

    public Builder dynamicSlippage(final int minBps, final int maxBps) {
      this.minBps = minBps;
      this.maxBps = maxBps;
      return this;
    }

    public int minBps() {
      return this.minBps;
    }

    public int maxBps() {
      return this.maxBps;
    }

    public Builder userPublicKey(final PublicKey userPublicKey) {
      this.userPublicKey = userPublicKey;
      return this;
    }

    public Builder wrapAndUnwrapSol(final boolean wrapAndUnwrapSol) {
      this.wrapAndUnwrapSol = wrapAndUnwrapSol;
      return this;
    }

    public Builder useSharedAccounts(final boolean useSharedAccounts) {
      this.useSharedAccounts = useSharedAccounts;
      return this;
    }

    public Builder feeAccount(final PublicKey feeAccount) {
      this.feeAccount = feeAccount;
      return this;
    }

    public Builder computeUnitPriceMicroLamports(final int computeUnitPriceMicroLamports) {
      this.computeUnitPriceMicroLamports = computeUnitPriceMicroLamports;
      return this;
    }

    public Builder prioritizationFeeLamports(final int prioritizationFeeLamports) {
      this.prioritizationFeeLamports = prioritizationFeeLamports;
      return this;
    }

    public Builder jitoFeeLamports(final int jitoFeeLamports) {
      this.jitoFeeLamports = jitoFeeLamports;
      return this;
    }

    public Builder asLegacyTransaction(final boolean asLegacyTransaction) {
      this.asLegacyTransaction = asLegacyTransaction;
      return this;
    }

    public Builder useTokenLedger(final boolean useTokenLedger) {
      this.useTokenLedger = useTokenLedger;
      return this;
    }

    public Builder destinationTokenAccount(final PublicKey destinationTokenAccount) {
      this.destinationTokenAccount = destinationTokenAccount;
      return this;
    }

    public Builder skipUserAccountsRpcCalls(final boolean skipUserAccountsRpcCalls) {
      this.skipUserAccountsRpcCalls = skipUserAccountsRpcCalls;
      return this;
    }

    public PublicKey userPublicKey() {
      return userPublicKey;
    }

    public boolean wrapAndUnwrapSol() {
      return wrapAndUnwrapSol;
    }

    public boolean useSharedAccounts() {
      return useSharedAccounts;
    }

    public PublicKey feeAccount() {
      return feeAccount;
    }

    public int computeUnitPriceMicroLamports() {
      return computeUnitPriceMicroLamports;
    }

    public int prioritizationFeeLamports() {
      return prioritizationFeeLamports;
    }

    public int jitoFeeLamports() {
      return jitoFeeLamports;
    }

    public boolean asLegacyTransaction() {
      return asLegacyTransaction;
    }

    public boolean useTokenLedger() {
      return useTokenLedger;
    }

    public PublicKey destinationTokenAccount() {
      return destinationTokenAccount;
    }

    public boolean skipUserAccountsRpcCalls() {
      return skipUserAccountsRpcCalls;
    }
  }
}
