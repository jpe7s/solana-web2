package software.sava.solana.web2.jupiter.client.http.request;

import software.sava.core.accounts.PublicKey;
import software.sava.rpc.json.PublicKeyEncoding;
import software.sava.solana.web2.jupiter.client.http.response.SwapMode;
import systems.comodal.jsoniter.CharBufferFunction;
import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static software.sava.solana.web2.jupiter.client.http.response.SwapMode.ExactIn;
import static software.sava.solana.web2.jupiter.client.http.response.SwapMode.ExactOut;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;
import static systems.comodal.jsoniter.JsonIterator.fieldEqualsIgnoreCase;

record JupiterQuoteRequestRecord(SwapMode swapMode,
                                 PublicKey inputTokenMint,
                                 BigInteger amount,
                                 PublicKey outputTokenMint,
                                 int slippageBps,
                                 Collection<String> excludeDexes,
                                 Collection<String> allowDexes,
                                 boolean restrictIntermediateTokens,
                                 boolean onlyDirectRoutes,
                                 boolean asLegacyTransaction,
                                 int platformFeeBps,
                                 int maxAccounts,
                                 boolean autoSlippage,
                                 int maxAutoSlippageBps,
                                 int autoSlippageCollisionUsdValue) implements JupiterQuoteRequest {

  private static final CharBufferFunction<SwapMode> PARSE_MODE = (buf, offset, len) -> {
    if (fieldEqualsIgnoreCase("ExactIn", buf, offset, len)) {
      return ExactIn;
    } else if (fieldEqualsIgnoreCase("ExactOut", buf, offset, len)) {
      return ExactOut;
    } else {
      return null;
    }
  };

  static final class Parser implements FieldBufferPredicate {

    private final JupiterQuoteRequest.Builder builder;

    Parser(final JupiterQuoteRequest prototype) {
      this.builder = JupiterQuoteRequest.buildRequest(prototype);
    }

    JupiterQuoteRequest createRequest() {
      return builder.create();
    }

    private static List<String> readStringArray(final JsonIterator ji) {
      final var dexes = new ArrayList<String>();
      while (ji.readArray()) {
        dexes.add(ji.readString());
      }
      return List.copyOf(dexes);
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("amount", buf, offset, len)) {
        builder.amount(ji.readLong());
      } else if (fieldEquals("swapMode", buf, offset, len)) {
        builder.swapMode(ji.applyChars(PARSE_MODE));
      } else if (fieldEquals("inputTokenMint", buf, offset, len)) {
        builder.inputTokenMint(PublicKeyEncoding.parseBase58Encoded(ji));
      } else if (fieldEquals("outputTokenMint", buf, offset, len)) {
        builder.outputTokenMint(PublicKeyEncoding.parseBase58Encoded(ji));
      } else if (fieldEquals("slippageBps", buf, offset, len)) {
        builder.slippageBps(ji.readInt());
      } else if (fieldEquals("excludeDexes", buf, offset, len)) {
        builder.allowDexes(readStringArray(ji));
      } else if (fieldEquals("allowDexes", buf, offset, len)) {
        builder.allowDexes(readStringArray(ji));
      } else if (fieldEquals("restrictIntermediateTokens", buf, offset, len)) {
        builder.restrictIntermediateTokens(ji.readBoolean());
      } else if (fieldEquals("onlyDirectRoutes", buf, offset, len)) {
        builder.onlyDirectRoutes(ji.readBoolean());
      } else if (fieldEquals("asLegacyTransaction", buf, offset, len)) {
        builder.asLegacyTransaction(ji.readBoolean());
      } else if (fieldEquals("platformFeeBps", buf, offset, len)) {
        builder.platformFeeBps(ji.readInt());
      } else if (fieldEquals("maxAccounts", buf, offset, len)) {
        builder.maxAccounts(ji.readInt());
      } else if (fieldEquals("autoSlippage", buf, offset, len)) {
        builder.autoSlippage(ji.readBoolean());
      } else if (fieldEquals("maxAutoSlippageBps", buf, offset, len)) {
        builder.maxAutoSlippageBps(ji.readInt());
      } else if (fieldEquals("autoSlippageCollisionUsdValue", buf, offset, len)) {
        builder.autoSlippageCollisionUsdValue(ji.readInt());
      } else {
        ji.skip();
      }
      return true;
    }
  }

  static final class BuilderImpl implements Builder {

    private BigInteger amount;
    private SwapMode swapMode;
    private PublicKey inputTokenMint;
    private PublicKey outputTokenMint;
    private int slippageBps;
    private Collection<String> excludeDexes;
    private Collection<String> allowDexes;
    private boolean restrictIntermediateTokens;
    private boolean onlyDirectRoutes;
    private boolean asLegacyTransaction;
    private int platformFeeBps;
    private int maxAccounts;
    private boolean autoSlippage;
    private int maxAutoSlippageBps;
    private int autoSlippageCollisionUsdValue;

    BuilderImpl() {
    }

    BuilderImpl(final JupiterQuoteRequest prototype) {
      this.amount = prototype.amount();
      this.swapMode = prototype.swapMode();
      this.inputTokenMint = prototype.inputTokenMint();
      this.outputTokenMint = prototype.outputTokenMint();
      this.slippageBps = prototype.slippageBps();
      this.excludeDexes = prototype.excludeDexes();
      this.allowDexes = prototype.allowDexes();
      this.restrictIntermediateTokens = prototype.restrictIntermediateTokens();
      this.onlyDirectRoutes = prototype.onlyDirectRoutes();
      this.asLegacyTransaction = prototype.asLegacyTransaction();
      this.platformFeeBps = prototype.platformFeeBps();
      this.maxAccounts = prototype.maxAccounts();
      this.autoSlippage = prototype.autoSlippage();
      this.maxAutoSlippageBps = prototype.maxAutoSlippageBps();
      this.autoSlippageCollisionUsdValue = prototype.autoSlippageCollisionUsdValue();
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
          restrictIntermediateTokens,
          onlyDirectRoutes,
          asLegacyTransaction,
          platformFeeBps,
          maxAccounts,
          autoSlippage,
          maxAutoSlippageBps,
          autoSlippageCollisionUsdValue
      );
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
    public Builder restrictIntermediateTokens(final boolean restrictIntermediateTokens) {
      this.restrictIntermediateTokens = restrictIntermediateTokens;
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
    public boolean restrictIntermediateTokens() {
      return restrictIntermediateTokens;
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

    @Override
    public int autoSlippageCollisionUsdValue() {
      return autoSlippageCollisionUsdValue;
    }

    @Override
    public BuilderImpl autoSlippageCollisionUsdValue(final int autoSlippageCollisionUsdValue) {
      this.autoSlippageCollisionUsdValue = autoSlippageCollisionUsdValue;
      return this;
    }

    @Override
    public int maxAutoSlippageBps() {
      return maxAutoSlippageBps;
    }

    @Override
    public BuilderImpl maxAutoSlippageBps(final int maxAutoSlippageBps) {
      this.maxAutoSlippageBps = maxAutoSlippageBps;
      return this;
    }

    @Override
    public boolean autoSlippage() {
      return autoSlippage;
    }

    @Override
    public BuilderImpl autoSlippage(final boolean autoSlippage) {
      this.autoSlippage = autoSlippage;
      return this;
    }
  }
}
