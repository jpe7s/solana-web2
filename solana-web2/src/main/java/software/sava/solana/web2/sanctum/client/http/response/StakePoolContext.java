package software.sava.solana.web2.sanctum.client.http.response;

import software.sava.core.accounts.PublicKey;

import java.util.ArrayList;
import java.util.List;

import static software.sava.core.accounts.PublicKey.fromBase58Encoded;

public record StakePoolContext(String name,
                               String symbol,
                               PublicKey mintPublicKey,
                               int decimals,
                               PublicKey tokenProgram,
                               String logoURI,
                               SanctumPoolType poolType,
                               PublicKey poolPublicKey,
                               PublicKey validatorListPublicKey,
                               PublicKey voteAccount) {

  public static List<StakePoolContext> parse(final String body) {
    final var pools = new ArrayList<StakePoolContext>(256);
    int from = body.indexOf("]]\n") + 1;
    for (Builder builder; from < body.length(); ) {
      builder = new Builder();
      from = builder.parse(body, from);
      pools.add(builder.createPoolContext());
    }
    return pools;
  }

  private static final class Builder {

    private String name;
    private String symbol;
    private PublicKey mintPublicKey;
    private int decimals;
    private PublicKey tokenProgram;
    private String logoURI;
    private SanctumPoolType poolType;
    private PublicKey poolPublicKey;
    private PublicKey validatorListPublicKey;
    private PublicKey voteAccount;

    private Builder() {

    }

    public StakePoolContext createPoolContext() {
      return new StakePoolContext(
          name,
          symbol,
          mintPublicKey,
          decimals,
          tokenProgram,
          logoURI,
          poolType,
          poolPublicKey,
          validatorListPublicKey,
          voteAccount
      );
    }

    public int parse(final String body, int from) {
      for (int to; from < body.length(); ++from) {
        if (!Character.isAlphabetic(body.charAt(from))) {
          to = body.indexOf('\n', from + 1);
          if (body.charAt(from + 1) == '[') {
            return to + 1;
          }
        } else {
          to = body.indexOf(' ', from);
          final var field = body.substring(from, to);
          from = body.indexOf(' ', to + 1) + 1;
          to = body.indexOf('\n', from + 1);
          if (to < 0) {
            to = body.length();
          }
          switch (field) {
            case "name" -> this.name = body.substring(from + 1, to - 1);
            case "symbol" -> this.symbol = body.substring(from + 1, to - 1);
            case "mint" -> this.mintPublicKey = fromBase58Encoded(body.substring(from + 1, to - 1));
            case "decimals" -> this.decimals = Integer.parseInt(body.substring(from, to));
            case "token_program" -> this.tokenProgram = fromBase58Encoded(body.substring(from + 1, to - 1));
            case "logo_uri" -> this.logoURI = body.substring(from + 1, to - 1);
            case "program" -> this.poolType = SanctumPoolType.valueOf(body.substring(from + 1, to - 1));
            case "pool" -> this.poolPublicKey = fromBase58Encoded(body.substring(from + 1, to - 1));
            case "validator_list" -> this.validatorListPublicKey = fromBase58Encoded(body.substring(from + 1, to - 1));
            case "vote_account" -> this.voteAccount = fromBase58Encoded(body.substring(from + 1, to - 1));
          }
        }
        from = to;
      }
      return from;
    }
  }
}
