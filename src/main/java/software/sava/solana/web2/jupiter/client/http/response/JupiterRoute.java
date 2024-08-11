package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import static software.sava.rpc.json.PublicKeyEncoding.parseBase58Encoded;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterRoute(PublicKey ammKey,
                           String label,
                           PublicKey inputMint,
                           PublicKey outputMint,
                           long inAmount,
                           long outAmount,
                           long feeAmount,
                           PublicKey feeMint,
                           int percent) {

  public static JupiterRoute parse(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  private static final ContextFieldBufferPredicate<Builder> SWAP_INFO_PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("ammKey", buf, offset, len)) {
      builder.ammKey = parseBase58Encoded(ji);
    } else if (fieldEquals("label", buf, offset, len)) {
      builder.label = ji.readString();
    } else if (fieldEquals("inputMint", buf, offset, len)) {
      builder.inputMint = parseBase58Encoded(ji);
    } else if (fieldEquals("outputMint", buf, offset, len)) {
      builder.outputMint = parseBase58Encoded(ji);
    } else if (fieldEquals("inAmount", buf, offset, len)) {
      builder.inAmount = ji.readLong();
    } else if (fieldEquals("outAmount", buf, offset, len)) {
      builder.outAmount = ji.readLong();
    } else if (fieldEquals("feeAmount", buf, offset, len)) {
      builder.feeAmount = ji.readLong();
    } else if (fieldEquals("feeMint", buf, offset, len)) {
      builder.feeMint = parseBase58Encoded(ji);
    } else {
      ji.skip();
    }
    return true;
  };

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("swapInfo", buf, offset, len)) {
      ji.testObject(builder, SWAP_INFO_PARSER);
    } else if (fieldEquals("percent", buf, offset, len)) {
      builder.percent = ji.readInt();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

    private PublicKey ammKey;
    private String label;
    private PublicKey inputMint;
    private PublicKey outputMint;
    private long inAmount;
    private long outAmount;
    private long feeAmount;
    private PublicKey feeMint;
    private int percent;

    private Builder() {
    }

    private JupiterRoute create() {
      return new JupiterRoute(ammKey, label, inputMint, outputMint, inAmount, outAmount, feeAmount, feeMint, percent);
    }
  }
}
