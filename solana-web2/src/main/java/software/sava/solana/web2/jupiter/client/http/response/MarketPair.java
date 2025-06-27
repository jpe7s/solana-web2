package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record MarketPair(String a, String b) {

  public static MarketPair parse(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("a", buf, offset, len)) {
      builder.a = ji.readString();
    } else if (fieldEquals("b", buf, offset, len)) {
      builder.b = ji.readString();
    } else {
      System.out.format("%nUnhandled MarketPair field %s: %s%n", new String(buf, offset, len), ji.currentBuffer());
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private String a;
    private String b;

    private Builder() {
    }

    private MarketPair create() {
      return new MarketPair(a, b);
    }
  }
}
