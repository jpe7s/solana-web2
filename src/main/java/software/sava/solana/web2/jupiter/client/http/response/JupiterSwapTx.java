package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.util.Base64;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterSwapTx(byte[] swapTransaction,
                            long lastValidBlockHeight,
                            String base64Encoded) {

  public static JupiterSwapTx parse(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("swapTransaction", buf, offset, len)) {
      builder.swapTransaction = ji.readString();
    } else if (fieldEquals("lastValidBlockHeight", buf, offset, len)) {
      builder.lastValidBlockHeight = ji.readLong();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private long lastValidBlockHeight;
    private String swapTransaction;

    private Builder() {
    }

    private JupiterSwapTx create() {
      return new JupiterSwapTx(Base64.getDecoder().decode(swapTransaction), lastValidBlockHeight, swapTransaction);
    }
  }
}
