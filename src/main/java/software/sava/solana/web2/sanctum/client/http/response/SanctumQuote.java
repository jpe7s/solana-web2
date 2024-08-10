package software.sava.solana.web2.sanctum.client.http.response;

import software.sava.core.accounts.PublicKey;
import software.sava.core.util.LamportDecimal;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record SanctumQuote(BigDecimal fee,
                           PublicKey feeMint,
                           BigDecimal feePct,
                           BigDecimal inAmount,
                           BigDecimal outAmount,
                           String swapSource) {

  public static SanctumQuote parse(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("feeAmount", buf, offset, len)) {
      builder.fee = ji.readBigDecimal().movePointLeft(LamportDecimal.LAMPORT_DIGITS).stripTrailingZeros();
    } else if (fieldEquals("feeMint", buf, offset, len)) {
      builder.feeMint = PublicKey.parseBase58Encoded(ji);
    } else if (fieldEquals("feePct", buf, offset, len)) {
      builder.feePct = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("inAmount", buf, offset, len)) {
      builder.inAmount = ji.readBigDecimal().movePointLeft(LamportDecimal.LAMPORT_DIGITS).stripTrailingZeros();
    } else if (fieldEquals("outAmount", buf, offset, len)) {
      builder.outAmount = ji.readBigDecimal().movePointLeft(LamportDecimal.LAMPORT_DIGITS).stripTrailingZeros();
    } else if (fieldEquals("swapSrc", buf, offset, len)) {
      builder.swapSource = ji.readString();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private BigDecimal fee;
    private PublicKey feeMint;
    private BigDecimal feePct;
    private BigDecimal inAmount;
    private BigDecimal outAmount;
    private String swapSource;

    private Builder() {
    }

    private SanctumQuote create() {
      return new SanctumQuote(fee, feeMint, feePct, inAmount, outAmount, swapSource);
    }
  }
}
