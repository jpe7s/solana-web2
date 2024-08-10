package software.sava.solana.web2.marinade.client.http.response;

import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record MarinadeAPY(BigDecimal apy,
                          ZonedDateTime startTime,
                          BigDecimal startPrice,
                          ZonedDateTime endTime,
                          BigDecimal endPrice) {

  public static MarinadeAPY parse(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("value", buf, offset, len)) {
      builder.apy = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("end_time", buf, offset, len)) {
      builder.endTime = ZonedDateTime.parse(ji.readString());
    } else if (fieldEquals("end_price", buf, offset, len)) {
      builder.endPrice = ji.readBigDecimalDropZeroes();
    } else if (fieldEquals("start_time", buf, offset, len)) {
      builder.startTime = ZonedDateTime.parse(ji.readString());
    } else if (fieldEquals("start_price", buf, offset, len)) {
      builder.startPrice = ji.readBigDecimalDropZeroes();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private BigDecimal apy;
    private ZonedDateTime startTime;
    private BigDecimal startPrice;
    private ZonedDateTime endTime;
    private BigDecimal endPrice;

    private Builder() {
    }

    private MarinadeAPY create() {
      return new MarinadeAPY(apy, startTime, startPrice, endTime, endPrice);
    }
  }
}