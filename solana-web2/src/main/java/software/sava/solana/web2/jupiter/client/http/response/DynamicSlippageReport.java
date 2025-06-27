package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record DynamicSlippageReport(int slippageBps,
                                    String otherAmount,
                                    String simulatedIncurredSlippageBps,
                                    String amplificationRatio,
                                    String categoryName,
                                    int heuristicMaxSlippageBps,
                                    int rtseSlippageBps,
                                    int failedTxnEstSlippage,
                                    int emaEstSlippage,
                                    String useIncurredSlippageForQuoting) {

  public static DynamicSlippageReport parse(final JsonIterator ji) {
    final var parser = new Parser();
    ji.testObject(parser);
    return parser.create();
  }

  private static final class Parser implements FieldBufferPredicate {

    private int slippageBps;
    private String otherAmount;
    private String simulatedIncurredSlippageBps;
    private String amplificationRatio;
    private String categoryName;
    private int heuristicMaxSlippageBps;
    private int rtseSlippageBps;
    private int failedTxnEstSlippage;
    private int emaEstSlippage;
    private String useIncurredSlippageForQuoting;

    private Parser() {
    }

    private DynamicSlippageReport create() {
      return new DynamicSlippageReport(slippageBps, otherAmount, simulatedIncurredSlippageBps,
          amplificationRatio, categoryName, heuristicMaxSlippageBps, rtseSlippageBps,
          failedTxnEstSlippage, emaEstSlippage, useIncurredSlippageForQuoting
      );
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("slippageBps", buf, offset, len)) {
        this.slippageBps = ji.readInt();
      } else if (fieldEquals("otherAmount", buf, offset, len)) {
        this.otherAmount = ji.readNull() ? null : ji.readString();
      } else if (fieldEquals("simulatedIncurredSlippageBps", buf, offset, len)) {
        this.simulatedIncurredSlippageBps = ji.readNull() ? null : ji.readString();
      } else if (fieldEquals("amplificationRatio", buf, offset, len)) {
        this.amplificationRatio = ji.readNull() ? null : ji.readString();
      } else if (fieldEquals("categoryName", buf, offset, len)) {
        this.categoryName = ji.readString();
      } else if (fieldEquals("heuristicMaxSlippageBps", buf, offset, len)) {
        this.heuristicMaxSlippageBps = ji.readInt();
      } else if (fieldEquals("rtseSlippageBps", buf, offset, len)) {
        this.rtseSlippageBps = ji.readInt();
      } else if (fieldEquals("failedTxnEstSlippage", buf, offset, len)) {
        this.failedTxnEstSlippage = ji.readInt();
      } else if (fieldEquals("emaEstSlippage", buf, offset, len)) {
        this.emaEstSlippage = ji.readInt();
      } else if (fieldEquals("useIncurredSlippageForQuoting", buf, offset, len)) {
        this.useIncurredSlippageForQuoting = ji.readNull() ? null : ji.readString();
      } else {
        ji.skip();
      }
      return true;
    }
  }
}
