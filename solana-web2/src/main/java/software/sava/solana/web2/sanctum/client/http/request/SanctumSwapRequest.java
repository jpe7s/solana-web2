package software.sava.solana.web2.sanctum.client.http.request;

import software.sava.core.accounts.PublicKey;

import java.math.BigInteger;
import java.util.Objects;

public record SanctumSwapRequest(BigInteger amount,
                                 String dstLstAcc,
                                 PublicKey inputMint,
                                 SwapMode swapMode,
                                 PublicKey outputLstMint,
                                 int unitLimit,
                                 int unitPriceMicroLamports,
                                 int maxUnitPriceMicroLamports,
                                 BigInteger quotedAmount,
                                 PublicKey signer,
                                 PublicKey srcAcc,
                                 String swapSrc) {

  public static Builder buildRequest() {
    return new Builder();
  }

  public String serialize() {
    final var builder = new StringBuilder(1_024);
    builder.append('{');
    if (amount != null) {
      builder.append("\"amount\":\"").append(amount).append("\",");
    }
    if (dstLstAcc != null && !dstLstAcc.isBlank()) {
      builder.append("\"dstLstAcc\":\"").append(dstLstAcc).append("\",");
    }
    if (inputMint != null) {
      builder.append("\"input\":\"").append(inputMint).append("\",");
    }
    builder.append("\"mode\":\"").append(swapMode).append("\",");
    builder.append("\"outputLstMint\":\"").append(outputLstMint).append("\",");

    builder.append("\"priorityFee\":{");
    if (unitPriceMicroLamports >= 0) {
      builder.append("\"Manual\":{\"unit_limit\":").append(unitLimit).append(",\"unit_price_micro_lamports\":").append(unitPriceMicroLamports);
    } else if (maxUnitPriceMicroLamports >= 0) {
      if (unitLimit >= 0) {
        builder.append("\"Auto\":{\"max_unit_price_micro_lamports\":").append(maxUnitPriceMicroLamports).append(",\"unit_limit\":").append(unitLimit);
      } else {
        builder.append("\"FullAuto\":{\"max_unit_price_micro_lamports\":").append(maxUnitPriceMicroLamports);
      }
    } else {
      throw new IllegalArgumentException("either maxUnitPriceMicroLamports, or unitLimit and (unitPriceMicroLamports or maxUnitPriceMicroLamports) must be set.");
    }
    builder.append("}},");
    if (quotedAmount != null) {
      builder.append("\"quotedAmount\":\"").append(quotedAmount).append("\",");
    }
    builder.append("\"signer\":\"").append(signer).append("\",");
    if (srcAcc != null) {
      builder.append("\"srcAcc\":\"").append(srcAcc).append("\",");
    }
    builder.append("\"swapSrc\":\"").append(swapSrc).append("\"}");
    return builder.toString();
  }

  public static final class Builder {

    private BigInteger amount;
    private String dstLstAcc;
    private PublicKey inputMint;
    private SwapMode swapMode;
    private PublicKey outputLstMint;
    private int unitLimit = Integer.MIN_VALUE;
    private int unitPriceMicroLamports = Integer.MIN_VALUE;
    private int maxUnitPriceMicroLamports = Integer.MIN_VALUE;
    private BigInteger quotedAmount;
    private PublicKey signer;
    private PublicKey srcAcc;
    private String swapSrc;

    private Builder() {
    }

    public SanctumSwapRequest createRequest() {
      final var swapMode = Objects.requireNonNullElse(this.swapMode, SwapMode.ExactIn);
      return new SanctumSwapRequest(
          amount,
          dstLstAcc,
          inputMint,
          swapMode,
          outputLstMint,
          unitLimit,
          unitPriceMicroLamports,
          maxUnitPriceMicroLamports,
          quotedAmount,
          signer,
          srcAcc,
          swapSrc
      );
    }

    public Builder amount(final BigInteger amount) {
      this.amount = amount;
      return this;
    }

    public Builder dstLstAcc(final String dstLstAcc) {
      this.dstLstAcc = dstLstAcc;
      return this;
    }

    public Builder inputMint(final PublicKey inputMint) {
      this.inputMint = inputMint;
      return this;
    }

    public Builder swapMode(final SwapMode swapMode) {
      this.swapMode = swapMode;
      return this;
    }

    public Builder outputLstMint(final PublicKey outputLstMint) {
      this.outputLstMint = outputLstMint;
      return this;
    }

    public Builder unitLimit(final int unitLimit) {
      this.unitLimit = unitLimit;
      return this;
    }

    public Builder unitPriceMicroLamports(final int unitPriceMicroLamports) {
      this.unitPriceMicroLamports = unitPriceMicroLamports;
      return this;
    }

    public Builder maxUnitPriceMicroLamports(final int maxUnitPriceMicroLamports) {
      this.maxUnitPriceMicroLamports = maxUnitPriceMicroLamports;
      return this;
    }

    public Builder quotedAmount(final BigInteger quotedAmount) {
      this.quotedAmount = quotedAmount;
      return this;
    }

    public Builder signer(final PublicKey signer) {
      this.signer = signer;
      return this;
    }

    public Builder srcAcc(final PublicKey srcAcc) {
      this.srcAcc = srcAcc;
      return this;
    }

    public Builder swapSrc(final String swapSrc) {
      this.swapSrc = swapSrc;
      return this;
    }

    public BigInteger amount() {
      return amount;
    }

    public String dstLstAcc() {
      return dstLstAcc;
    }

    public PublicKey inputMint() {
      return inputMint;
    }

    public SwapMode swapMode() {
      return swapMode;
    }

    public PublicKey outputLstMint() {
      return outputLstMint;
    }

    public int unitLimit() {
      return unitLimit;
    }

    public int unitPriceMicroLamports() {
      return unitPriceMicroLamports;
    }

    public int maxUnitPriceMicroLamports() {
      return maxUnitPriceMicroLamports;
    }

    public BigInteger quotedAmount() {
      return quotedAmount;
    }

    public PublicKey signer() {
      return signer;
    }

    public PublicKey srcAcc() {
      return srcAcc;
    }

    public String swapSrc() {
      return swapSrc;
    }
  }
}
