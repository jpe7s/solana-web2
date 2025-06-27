package software.sava.solana.web2.birdeye.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.time.Instant;
import java.util.List;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record TokenList(Instant updatedAt,
                        List<Token> tokens,
                        int total) {

  public static TokenList parseTokens(final JsonIterator ji) {
    return ji.skipUntil("data").testObject(new Builder(), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("updateUnixTime", buf, offset, len)) {
      builder.updatedAt = Instant.ofEpochSecond(ji.readLong());
    } else if (fieldEquals("tokens", buf, offset, len)) {
      builder.tokens = Token.parseTokens(ji);
    } else if (fieldEquals("total", buf, offset, len)) {
      builder.total = ji.readInt();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private Instant updatedAt;
    private List<Token> tokens;
    private int total;

    private Builder() {
    }

    private TokenList create() {
      return new TokenList(updatedAt, tokens, total);
    }
  }
}
