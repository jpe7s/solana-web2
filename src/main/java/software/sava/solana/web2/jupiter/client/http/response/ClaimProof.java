package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.util.ArrayList;
import java.util.List;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record ClaimProof(PublicKey mint,
                         PublicKey merkleTree,
                         long amount,
                         long lockedAmount,
                         List<byte[]> proof) {

  public static ClaimProof parseProof(final JsonIterator ji) {
    final var builder = new Parser();
    ji.testObject(builder);
    return builder.create();
  }

  private static final class Parser implements FieldBufferPredicate {

    private PublicKey mint;
    private PublicKey merkleTree;
    private long amount;
    private long lockedAmount;
    private List<byte[]> proof;

    private Parser() {
    }

    private ClaimProof create() {
      return new ClaimProof(mint, merkleTree, amount, lockedAmount, proof);
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("mint", buf, offset, len)) {
        mint = PublicKey.fromBase58Encoded(ji.readString());
      } else if (fieldEquals("merkle_tree", buf, offset, len)) {
        merkleTree = PublicKey.fromBase58Encoded(ji.readString());
      } else if (fieldEquals("amount", buf, offset, len)) {
        amount = ji.readLong();
      } else if (fieldEquals("locked_amount", buf, offset, len)) {
        lockedAmount = ji.readLong();
      } else if (fieldEquals("proof", buf, offset, len)) {
        final var proof = new ArrayList<byte[]>(16);
        while (ji.readArray()) {
          final byte[] item = new byte[32];
          for (int i = 0; ji.readArray(); i++) {
            item[i] = (byte) ji.readInt();
          }
          proof.add(item);
        }
        this.proof = proof;
      } else {
        ji.skip();
      }
      return true;
    }

  }
}
