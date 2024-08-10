package software.sava.solana.web2.jupiter.client.http.request;

import software.sava.core.accounts.PublicKey;
import software.sava.solana.web2.jupiter.client.http.response.SwapMode;

import java.math.BigInteger;
import java.util.Collection;

record JupiterQuoteRequestRecord(SwapMode swapMode,
                                 PublicKey inputTokenMint,
                                 BigInteger amount,
                                 PublicKey outputTokenMint,
                                 int slippageBps,
                                 Collection<String> excludeDexes,
                                 Collection<String> allowDexes,
                                 boolean onlyDirectRoutes,
                                 boolean asLegacyTransaction,
                                 int platformFeeBps,
                                 int maxAccounts) implements JupiterQuoteRequest {

  static final class JupiterQuoteRequestBuilder implements Builder {

    private BigInteger amount;
    private SwapMode swapMode;
    private PublicKey inputTokenMint;
    private PublicKey outputTokenMint;
    private int slippageBps;
    private Collection<String> excludeDexes;
    private Collection<String> allowDexes;
    private boolean onlyDirectRoutes;
    private boolean asLegacyTransaction;
    private int platformFeeBps;
    private int maxAccounts;

    JupiterQuoteRequestBuilder() {
    }

    @Override
    public JupiterQuoteRequest create() {
      return new JupiterQuoteRequestRecord(swapMode,
          inputTokenMint,
          amount,
          outputTokenMint,
          slippageBps,
          excludeDexes,
          allowDexes,
          onlyDirectRoutes,
          asLegacyTransaction,
          platformFeeBps,
          maxAccounts);
    }

    @Override
    public Builder amount(final BigInteger amount) {
      this.amount = amount;
      return this;
    }

    @Override
    public Builder swapMode(final SwapMode swapMode) {
      this.swapMode = swapMode;
      return this;
    }

    @Override
    public Builder inputTokenMint(final PublicKey inputTokenMint) {
      this.inputTokenMint = inputTokenMint;
      return this;
    }

    @Override
    public Builder outputTokenMint(final PublicKey outputTokenMint) {
      this.outputTokenMint = outputTokenMint;
      return this;
    }

    @Override
    public Builder slippageBps(final int slippageBps) {
      this.slippageBps = slippageBps;
      return this;
    }

    @Override
    public Builder excludeDexes(final Collection<String> excludeDexes) {
      this.excludeDexes = excludeDexes;
      return this;
    }

    @Override
    public Builder allowDexes(final Collection<String> allowDexes) {
      this.allowDexes = allowDexes;
      return this;
    }

    @Override
    public Builder onlyDirectRoutes(final boolean onlyDirectRoutes) {
      this.onlyDirectRoutes = onlyDirectRoutes;
      return this;
    }

    @Override
    public Builder asLegacyTransaction(final boolean asLegacyTransaction) {
      this.asLegacyTransaction = asLegacyTransaction;
      return this;
    }

    @Override
    public Builder platformFeeBps(final int platformFeeBps) {
      this.platformFeeBps = platformFeeBps;
      return this;
    }

    @Override
    public Builder maxAccounts(final int maxAccounts) {
      this.maxAccounts = maxAccounts;
      return this;
    }

    @Override
    public BigInteger amount() {
      return amount;
    }

    @Override
    public SwapMode swapMode() {
      return swapMode;
    }

    @Override
    public PublicKey inputTokenMint() {
      return inputTokenMint;
    }

    @Override
    public PublicKey outputTokenMint() {
      return outputTokenMint;
    }

    @Override
    public int slippageBps() {
      return slippageBps;
    }

    @Override
    public Collection<String> excludeDexes() {
      return excludeDexes;
    }

    @Override
    public Collection<String> allowDexes() {
      return allowDexes;
    }

    @Override
    public boolean onlyDirectRoutes() {
      return onlyDirectRoutes;
    }

    @Override
    public boolean asLegacyTransaction() {
      return asLegacyTransaction;
    }

    @Override
    public int platformFeeBps() {
      return platformFeeBps;
    }

    @Override
    public int maxAccounts() {
      return maxAccounts;
    }
  }
}
