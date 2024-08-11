package software.sava.solana.web2.birdeye.client.http.response;

import software.sava.core.accounts.PublicKey;
import software.sava.rpc.json.PublicKeyEncoding;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static software.sava.rpc.json.PublicKeyEncoding.parseBase58Encoded;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record Token(PublicKey address,
                    int decimals,
                    BigDecimal liquidity,
                    String logoURI,
                    BigDecimal mc,
                    String symbol,
                    BigDecimal v24hChangePercent,
                    BigDecimal v24hUSD,
                    String name,
                    Instant lastTrade) {

  public static List<Token> parseTokens(final JsonIterator ji) {
    final var tokens = new ArrayList<Token>(50);
    while (ji.readArray()) {
      final var token = ji.testObject(new Builder(), PARSER).create();
      tokens.add(token);
    }
    return tokens;
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("address", buf, offset, len)) {
      builder.address = parseBase58Encoded(ji);
    } else if (fieldEquals("decimals", buf, offset, len)) {
      builder.decimals = ji.readInt();
    } else if (fieldEquals("liquidity", buf, offset, len)) {
      builder.liquidity = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("logoURI", buf, offset, len)) {
      builder.logoURI = ji.readString();
    } else if (fieldEquals("mc", buf, offset, len)) {
      builder.mc = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("name", buf, offset, len)) {
      builder.name = ji.readString();
    } else if (fieldEquals("symbol", buf, offset, len)) {
      builder.symbol = ji.readString();
    } else if (fieldEquals("v24hChangePercent", buf, offset, len)) {
      builder.v24hChangePercent = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("v24hUSD", buf, offset, len)) {
      builder.v24hUSD = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("lastTradeUnixTime", buf, offset, len)) {
      builder.lastTrade = Instant.ofEpochSecond(ji.readLong());
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private PublicKey address;
    private int decimals;
    private BigDecimal liquidity;
    private String logoURI;
    private BigDecimal mc;
    private String symbol;
    private BigDecimal v24hChangePercent;
    private BigDecimal v24hUSD;
    private String name;
    private Instant lastTrade;

    private Builder() {
    }

    private Token create() {
      return new Token(address, decimals, liquidity, logoURI, mc, symbol, v24hChangePercent, v24hUSD, name, lastTrade);
    }
  }
}
