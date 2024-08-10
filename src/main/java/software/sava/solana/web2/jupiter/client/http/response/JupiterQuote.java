package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterQuote(PublicKey inputMint,
                           long inAmount,
                           PublicKey outputMint,
                           long outAmount,
                           long otherAmountThreshold,
                           SwapMode swapMode,
                           int slippageBps,
                           PlatformFee platformFee,
                           BigDecimal priceImpactPct,
                           List<JupiterRoute> routePlan,
                           long contextSlot,
                           double timeTaken,
                           String quoteResponseJson) {

  public static JupiterQuote parse(final String quoteResponseJson, final JsonIterator ji) {
    return ji.testObject(new Builder(quoteResponseJson), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("inputMint", buf, offset, len)) {
      builder.inputMint = PublicKey.parseBase58Encoded(ji);
    } else if (fieldEquals("inAmount", buf, offset, len)) {
      builder.inAmount = ji.readLong();
    } else if (fieldEquals("outputMint", buf, offset, len)) {
      builder.outputMint = PublicKey.parseBase58Encoded(ji);
    } else if (fieldEquals("outAmount", buf, offset, len)) {
      builder.outAmount = ji.readLong();
    } else if (fieldEquals("otherAmountThreshold", buf, offset, len)) {
      builder.otherAmountThreshold = ji.readLong();
    } else if (fieldEquals("swapMode", buf, offset, len)) {
      builder.swapMode = SwapMode.valueOf(ji.readString());
    } else if (fieldEquals("slippageBps", buf, offset, len)) {
      builder.slippageBps = ji.readInt();
    } else if (fieldEquals("platformFee", buf, offset, len)) {
      builder.platformFee = PlatformFee.parse(ji);
    } else if (fieldEquals("priceImpactPct", buf, offset, len)) {
      builder.priceImpactPct = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("routePlan", buf, offset, len)) {
      final var routePlan = new ArrayList<JupiterRoute>();
      while (ji.readArray()) {
        routePlan.add(JupiterRoute.parse(ji));
      }
      builder.routePlan = routePlan;
    } else if (fieldEquals("contextSlot", buf, offset, len)) {
      builder.contextSlot = ji.readLong();
    } else if (fieldEquals("timeTaken", buf, offset, len)) {
      builder.timeTaken = ji.readDouble();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private final String quoteResponseJson;
    private PublicKey inputMint;
    private long inAmount;
    private PublicKey outputMint;
    private long outAmount;
    private long otherAmountThreshold;
    private SwapMode swapMode;
    private int slippageBps;
    private PlatformFee platformFee;
    private BigDecimal priceImpactPct;
    private List<JupiterRoute> routePlan;
    private long contextSlot;
    private double timeTaken;

    private Builder(final String quoteResponseJson) {
      this.quoteResponseJson = quoteResponseJson;
    }

    private JupiterQuote create() {
      return new JupiterQuote(inputMint, inAmount, outputMint, outAmount, otherAmountThreshold, swapMode, slippageBps, platformFee, priceImpactPct, routePlan, contextSlot, timeTaken, quoteResponseJson);
    }
  }
}
