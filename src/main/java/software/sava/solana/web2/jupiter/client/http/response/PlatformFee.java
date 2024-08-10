package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;
import systems.comodal.jsoniter.ValueType;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record PlatformFee(long amount, int feeBps) {

  public static PlatformFee parse(final JsonIterator ji) {
    if (ji.whatIsNext() == ValueType.NULL) {
      ji.skip();
      return null;
    } else {
      return ji.testObject(new Builder(), PARSER).create();
    }
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("amount", buf, offset, len)) {
      builder.amount = ji.readLong();
    } else if (fieldEquals("feeBps", buf, offset, len)) {
      builder.feeBps = ji.readInt();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private long amount;
    private int feeBps;

    private Builder() {
    }

    private PlatformFee create() {
      return new PlatformFee(amount, feeBps);
    }
  }
}
