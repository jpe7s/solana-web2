package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static software.sava.rpc.json.PublicKeyEncoding.parseBase58Encoded;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterUltraOrder(SwapType swapType,
                                String requestId,
                                String quoteId,
                                long inAmount,
                                long outAmount,
                                long otherAmountThreshold,
                                SwapMode swapMode,
                                int slippageBps,
                                BigDecimal priceImpactPct,
                                List<JupiterRoute> routePlan,
                                PublicKey inputMint,
                                PublicKey outputMint,
                                int feeBps,
                                PublicKey feeMint,
                                PublicKey maker,
                                PublicKey taker,
                                boolean gasless,
                                byte[] transaction,
                                PrioritizationType prioritizationType,
                                long prioritizationFeeLamports,
                                DynamicSlippageReport dynamicSlippageReport,
                                long totalTime,
                                Instant expireAt) {

  public static JupiterUltraOrder parse(final JsonIterator ji) {
    final var parser = new Parser();
    ji.testObject(parser);
    return parser.create();
  }

  private static final class Parser implements FieldBufferPredicate {

    private SwapType swapType;
    private String requestId;
    private String quoteId;
    private long inAmount;
    private long outAmount;
    private long otherAmountThreshold;
    private SwapMode swapMode;
    private int slippageBps;
    private BigDecimal priceImpactPct;
    private List<JupiterRoute> routePlan;
    private PublicKey inputMint;
    private PublicKey outputMint;
    private int feeBps;
    private PublicKey feeMint;
    private PublicKey maker;
    private PublicKey taker;
    private boolean gasless;
    private byte[] transaction;
    private PrioritizationType prioritizationType;
    private long prioritizationFeeLamports;
    private DynamicSlippageReport dynamicSlippageReport;
    private long totalTime;
    private Instant expireAt;

    private Parser() {
    }

    private JupiterUltraOrder create() {
      return new JupiterUltraOrder(swapType, requestId, quoteId, inAmount, outAmount, otherAmountThreshold,
          swapMode, slippageBps, priceImpactPct, routePlan, inputMint, outputMint, feeBps, feeMint,
          maker, taker, gasless, transaction, prioritizationType, prioritizationFeeLamports,
          dynamicSlippageReport, totalTime, expireAt
      );
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("swapType", buf, offset, len)) {
        this.swapType = SwapType.valueOf(ji.readString());
      } else if (fieldEquals("requestId", buf, offset, len)) {
        this.requestId = ji.readString();
      } else if (fieldEquals("requestId", buf, offset, len)) {
        this.quoteId = ji.readString();
      } else if (fieldEquals("inAmount", buf, offset, len)) {
        this.inAmount = ji.readLong();
      } else if (fieldEquals("outAmount", buf, offset, len)) {
        this.outAmount = ji.readLong();
      } else if (fieldEquals("otherAmountThreshold", buf, offset, len)) {
        this.otherAmountThreshold = ji.readLong();
      } else if (fieldEquals("swapMode", buf, offset, len)) {
        this.swapMode = SwapMode.valueOf(ji.readString());
      } else if (fieldEquals("slippageBps", buf, offset, len)) {
        this.slippageBps = ji.readInt();
      } else if (fieldEquals("priceImpactPct", buf, offset, len)) {
        this.priceImpactPct = ji.readBigDecimal();
      } else if (fieldEquals("routePlan", buf, offset, len)) {
        final var routePlan = new ArrayList<JupiterRoute>();
        while (ji.readArray()) {
          routePlan.add(JupiterRoute.parse(ji));
        }
        this.routePlan = routePlan;
      } else if (fieldEquals("inputMint", buf, offset, len)) {
        this.inputMint = parseBase58Encoded(ji);
      } else if (fieldEquals("outputMint", buf, offset, len)) {
        this.outputMint = parseBase58Encoded(ji);
      } else if (fieldEquals("feeBps", buf, offset, len)) {
        this.feeBps = ji.readInt();
      } else if (fieldEquals("feeMint", buf, offset, len)) {
        this.feeMint = parseBase58Encoded(ji);
      } else if (fieldEquals("maker", buf, offset, len)) {
        this.maker = parseBase58Encoded(ji);
      } else if (fieldEquals("taker", buf, offset, len)) {
        this.taker = parseBase58Encoded(ji);
      } else if (fieldEquals("gasless", buf, offset, len)) {
        this.gasless = ji.readBoolean();
      } else if (fieldEquals("transaction", buf, offset, len)) {
        this.transaction = ji.readNull() ? null : ji.decodeBase64String();
      } else if (fieldEquals("prioritizationType", buf, offset, len)) {
        this.prioritizationType = PrioritizationType.valueOf(ji.readString());
      } else if (fieldEquals("prioritizationFeeLamports", buf, offset, len)) {
        this.prioritizationFeeLamports = ji.readLong();
      } else if (fieldEquals("dynamicSlippageReport", buf, offset, len)) {
        this.dynamicSlippageReport = DynamicSlippageReport.parse(ji);
      } else if (fieldEquals("totalTime", buf, offset, len)) {
        this.totalTime = ji.readLong();
      } else if (fieldEquals("expireAt", buf, offset, len)) {
        this.expireAt = Instant.parse(ji.readString());
      } else {
        // "mode": "ultra",
        // "router": "metis",
        ji.skip();
      }
      return true;
    }
  }
}
