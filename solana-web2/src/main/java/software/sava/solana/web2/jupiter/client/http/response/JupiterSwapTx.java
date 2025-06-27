package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.util.Base64;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterSwapTx(byte[] swapTransaction,
                            long lastValidBlockHeight,
                            String base64Encoded) {

  public static JupiterSwapTx parse(final JsonIterator ji) {
    final var parser = new Builder();
    ji.testObject(parser);
    return parser.create();
  }

  private static final class Builder implements FieldBufferPredicate {

    private long lastValidBlockHeight;
    private String swapTransaction;

    private Builder() {
    }

    private JupiterSwapTx create() {
      return new JupiterSwapTx(
          Base64.getDecoder().decode(swapTransaction),
          lastValidBlockHeight,
          swapTransaction
      );
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("swapTransaction", buf, offset, len)) {
        swapTransaction = ji.readString();
      } else if (fieldEquals("lastValidBlockHeight", buf, offset, len)) {
        lastValidBlockHeight = ji.readLong();
      } else {
        ji.skip();
      }
      return true;
    }
  }
}
