package software.sava.solana.web2.sdx.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record OptionMarkets(String assetName,
                            String vaultAddress,
                            Instant retrievedAt,
                            List<OptionMarket> markets) {

  private static OptionMarkets parseMarket(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  public static List<OptionMarkets> parseMarkets(final JsonIterator ji) {
    if (ji.skipUntil("underlyingAssets") == null) {
      return List.of();
    } else {
      final var markets = new ArrayList<OptionMarkets>();
      while (ji.readArray()) {
        markets.add(parseMarket(ji));
      }
      return markets;
    }
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("assetName", buf, offset, len)) {
      builder.assetName = ji.readString();
    } else if (fieldEquals("vaultAddress", buf, offset, len)) {
      builder.vaultAddress = ji.readString();
    } else if (fieldEquals("retrievedAt", buf, offset, len)) {
      builder.retrievedAt = Instant.ofEpochSecond(ji.readLong());
    } else if (fieldEquals("markets", buf, offset, len)) {
      builder.markets = OptionMarket.parseMarkets(ji);
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private String assetName;
    private String vaultAddress;
    private Instant retrievedAt;
    private List<OptionMarket> markets;

    private Builder() {
    }

    private OptionMarkets create() {
      return new OptionMarkets(assetName, vaultAddress, retrievedAt, markets);
    }
  }
}
