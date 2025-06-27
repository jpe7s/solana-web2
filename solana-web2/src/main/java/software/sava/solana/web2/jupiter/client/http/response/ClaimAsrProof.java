package software.sava.solana.web2.jupiter.client.http.response;

import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.util.ArrayList;
import java.util.List;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record ClaimAsrProof(List<ClaimProof> claimProof, int voteCount, int voteCountExtra) {

  public static ClaimAsrProof parseProof(final JsonIterator ji) {
    final var builder = new Parser();
    ji.testObject(builder);
    return builder.create();
  }

  private static final class Parser implements FieldBufferPredicate {

    private List<ClaimProof> claims;
    private int voteCount;
    private int voteCountExtra;

    private Parser() {
    }

    private ClaimAsrProof create() {
      return new ClaimAsrProof(claims, voteCount, voteCountExtra);
    }


    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("claim", buf, offset, len)) {
        final var claims = new ArrayList<ClaimProof>();
        while (ji.readArray()) {
          claims.add(ClaimProof.parseProof(ji));
        }
        this.claims = claims;
      } else if (fieldEquals("voteCount", buf, offset, len)) {
        voteCount = ji.readInt();
      } else {
        voteCountExtra = ji.readInt();
      }
      return true;
    }
  }
}
