
package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterExecuteOrder(Status status,
                                  String signature,
                                  BigInteger slot,
                                  String error,
                                  long code,
                                  long totalInputAmount,
                                  long totalOutputAmount,
                                  long inputAmountResult,
                                  long outputAmountResult,
                                  List<SwapEvent> swapEvents,
                                  byte[] responseJson) {

  public static JupiterExecuteOrder parse(final byte[] responseJson, final JsonIterator ji) {
    final var parser = new Parser(responseJson);
    ji.testObject(parser);
    return parser.create();
  }

  public enum Status {
    Success, Failed
  }

  private static final class Parser implements FieldBufferPredicate {

    private final byte[] responseJson;
    private Status status;
    private String signature;
    private BigInteger slot;
    private String error;
    private long code;
    private long totalInputAmount;
    private long totalOutputAmount;
    private long inputAmountResult;
    private long outputAmountResult;
    private List<SwapEvent> swapEvents;

    private Parser(final byte[] responseJson) {
      this.responseJson = responseJson;
    }

    private JupiterExecuteOrder create() {
      return new JupiterExecuteOrder(status, signature, slot, error, code,
          totalInputAmount, totalOutputAmount, inputAmountResult, outputAmountResult,
          swapEvents, responseJson
      );
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("status", buf, offset, len)) {
        status = Status.valueOf(ji.readString());
      } else if (fieldEquals("signature", buf, offset, len)) {
        signature = ji.readString();
      } else if (fieldEquals("slot", buf, offset, len)) {
        slot = ji.readBigInteger();
      } else if (fieldEquals("error", buf, offset, len)) {
        error = ji.readString();
      } else if (fieldEquals("code", buf, offset, len)) {
        code = ji.readLong();
      } else if (fieldEquals("totalInputAmount", buf, offset, len)) {
        totalInputAmount = ji.readLong();
      } else if (fieldEquals("totalOutputAmount", buf, offset, len)) {
        totalOutputAmount = ji.readLong();
      } else if (fieldEquals("inputAmountResult", buf, offset, len)) {
        inputAmountResult = ji.readLong();
      } else if (fieldEquals("outputAmountResult", buf, offset, len)) {
        outputAmountResult = ji.readLong();
      } else if (fieldEquals("swapEvents", buf, offset, len)) {
        final var swapEvents = new ArrayList<SwapEvent>();
        while (ji.readArray()) {
          swapEvents.add(SwapEvent.parse(ji));
        }
        this.swapEvents = swapEvents;
      } else {
        ji.skip();
      }
      return true;
    }
  }
}
