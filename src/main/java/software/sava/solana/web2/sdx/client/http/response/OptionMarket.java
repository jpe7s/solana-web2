package software.sava.solana.web2.sdx.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record OptionMarket(String marketName,
                           ZonedDateTime settlementDateTime,
                           List<BigDecimal> strikes,
                           int seriesId,
                           OptionType optionType,
                           BigDecimal underlyingPrice,
                           BigDecimal markPriceStable,
                           BigDecimal markPriceUnderlying,
                           BigDecimal bidStable,
                           BigDecimal bidUnderlying,
                           BigDecimal askStable,
                           BigDecimal askUnderlying,
                           List<BigDecimal> bidIV,
                           List<BigDecimal> askIV,
                           List<BigDecimal> markPriceIV) {

  private static OptionMarket parseMarket(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  static List<OptionMarket> parseMarkets(final JsonIterator ji) {
    final var markets = new ArrayList<OptionMarket>();
    while (ji.readArray()) {
      markets.add(parseMarket(ji));
    }
    return markets;
  }

  private static List<BigDecimal> parseIV(final JsonIterator ji) {
    if (ji.readArray()) {
      final var iv = ji.readBigDecimalDropZeroes();
      if (ji.readArray()) {
        final var list = new ArrayList<BigDecimal>();
        list.add(iv);
        do {
          list.add(ji.readBigDecimalDropZeroes());
        } while (ji.readArray());
        return list;
      } else {
        return List.of(iv);
      }
    } else {
      return Builder.NO_IV;
    }
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("marketName", buf, offset, len)) {
      builder.marketName = ji.readString();
    } else if (fieldEquals("seriesId", buf, offset, len)) {
      builder.seriesId = ji.readInt();
    } else if (fieldEquals("optionType", buf, offset, len)) {
      builder.optionType = OptionType.valueOf(ji.readString());
    } else if (fieldEquals("underlyingPrice", buf, offset, len)) {
      builder.underlyingPrice = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("markPriceStable", buf, offset, len)) {
      builder.markPriceStable = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("markPriceUnderlying", buf, offset, len)) {
      builder.markPriceUnderlying = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("bidStable", buf, offset, len)) {
      builder.bidStable = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("bidUnderlying", buf, offset, len)) {
      builder.bidUnderlying = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("askStable", buf, offset, len)) {
      builder.askStable = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("askUnderlying", buf, offset, len)) {
      builder.askUnderlying = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("bidIV", buf, offset, len)) {
      builder.bidIV = parseIV(ji);
    } else if (fieldEquals("askIV", buf, offset, len)) {
      builder.askIV = parseIV(ji);
    } else if (fieldEquals("markPriceIV", buf, offset, len)) {
      builder.markPriceIV = parseIV(ji);
    } else {
      ji.skip();
    }
    return true;
  };


  private static final class Builder {

    private static final List<BigDecimal> NO_IV = List.of();

    private String marketName;
    private int seriesId;
    private OptionType optionType;
    private BigDecimal underlyingPrice;
    private BigDecimal markPriceStable;
    private BigDecimal markPriceUnderlying;
    private BigDecimal bidStable;
    private BigDecimal bidUnderlying;
    private BigDecimal askStable;
    private BigDecimal askUnderlying;
    private List<BigDecimal> bidIV;
    private List<BigDecimal> askIV;
    private List<BigDecimal> markPriceIV;

    private Builder() {
    }

    private static int toMonthOfYear(final String monthString) {
      return switch (monthString) {
        case "Jan" -> 1;
        case "Feb" -> 2;
        case "Mar" -> 3;
        case "Apr" -> 4;
        case "May" -> 5;
        case "Jun" -> 6;
        case "Jul" -> 7;
        case "Aug" -> 8;
        case "Sep" -> 9;
        case "Oct" -> 10;
        case "Nov" -> 11;
        case "Dec" -> 12;
        default -> throw new IllegalArgumentException(String.format("Unknown month [%s]", monthString));
      };
    }

    private static ZonedDateTime parseSettlementDate(final String name, final int from, int to) {
      final int year = 2000 + Integer.parseInt(name, to - 2, to, 10);
      to -= 2;
      final int month = toMonthOfYear(name.substring(to - 3, to));
      to -= 3;
      final int day = Integer.parseInt(name, from, to, 10);
      return ZonedDateTime.of(year, month, day, 8, 0, 0, 0, UTC);
    }

    private static List<BigDecimal> parseStrikes(final String marketName, final OptionType optionType, int from) {
      return switch (optionType) {
        case PUT, CALL ->
            List.of(new BigDecimal(marketName.toCharArray(), from, marketName.indexOf('-', from + 1) - from));
        case LONG_PUT_SPREAD, LONG_CALL_SPREAD -> {
          final char[] chars = marketName.toCharArray();
          int to = marketName.indexOf('/', from + 1);
          final var strikeA = new BigDecimal(chars, from, to - from);
          from = to + 2;
          final var strikeB = new BigDecimal(chars, from, marketName.indexOf('-', from + 1) - from);
          yield List.of(strikeA, strikeB);
        }
      };
    }

    private OptionMarket create() {
      int from = marketName.indexOf('-') + 1;
      int to = marketName.indexOf('-', from + 6);
      final var settlementDate = parseSettlementDate(marketName, from, to);
      to += 2;
      final var strikes = parseStrikes(marketName, optionType, to);
      return new OptionMarket(marketName, settlementDate, strikes, seriesId, optionType, underlyingPrice, markPriceStable, markPriceUnderlying, bidStable, bidUnderlying, askStable, askUnderlying, bidIV, askIV, markPriceIV);
    }
  }
}
