package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.FieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.Logger.Level.WARNING;
import static software.sava.rpc.json.PublicKeyEncoding.parseBase58Encoded;
import static software.sava.rpc.json.http.response.JsonUtil.parseEncodedData;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record MarketRecord(PublicKey pubkey,
                           long lamports,
                           byte[] data,
                           PublicKey owner,
                           boolean executable,
                           BigInteger rentEpoch,
                           int space,
                           String addressLookupTableAddress,
                           MarketPair vaultLpMint,
                           MarketPair vaultToken,
                           String serumAsks,
                           String serumBids,
                           String serumCoinVaultAccount,
                           String serumEventQueue,
                           String serumPcVaultAccount,
                           String serumVaultSigner,
                           long routingGroup,
                           long amp,
                           int decimalA,
                           int decimalB,
                           List<PublicKey> tokenMints) {

  static final System.Logger logger = System.getLogger(MarketRecord.class.getName());

  public static List<MarketRecord> parse(final JsonIterator ji) {
    final var records = new ArrayList<MarketRecord>(262_144);
    while (ji.readArray()) {
      final var parser = new Parser();
      ji.testObject(parser);
      final var record = parser.create();
      records.add(record);
    }
    return records;
  }

  private static final ContextFieldBufferPredicate<Parser> PARAMS_PARSER = (parser, buf, offset, len, ji) -> {
    if (fieldEquals("addressLookupTableAddress", buf, offset, len)) {
      parser.addressLookupTableAddress = ji.readString();
    } else if (fieldEquals("vaultLpMint", buf, offset, len)) {
      parser.vaultLpMint = MarketPair.parse(ji);
    } else if (fieldEquals("vaultToken", buf, offset, len)) {
      parser.vaultToken = MarketPair.parse(ji);
    } else if (fieldEquals("serumAsks", buf, offset, len)) {
      parser.serumAsks = ji.readString();
    } else if (fieldEquals("serumBids", buf, offset, len)) {
      parser.serumBids = ji.readString();
    } else if (fieldEquals("serumCoinVaultAccount", buf, offset, len)) {
      parser.serumCoinVaultAccount = ji.readString();
    } else if (fieldEquals("serumEventQueue", buf, offset, len)) {
      parser.serumEventQueue = ji.readString();
    } else if (fieldEquals("serumPcVaultAccount", buf, offset, len)) {
      parser.serumPcVaultAccount = ji.readString();
    } else if (fieldEquals("serumVaultSigner", buf, offset, len)) {
      parser.serumVaultSigner = ji.readString();
    } else if (fieldEquals("routingGroup", buf, offset, len)) {
      parser.routingGroup = ji.readLong();
    } else if (fieldEquals("amp", buf, offset, len)) {
      parser.amp = ji.readLong();
    } else if (fieldEquals("decimalA", buf, offset, len)) {
      parser.decimalA = ji.readInt();
    } else if (fieldEquals("decimalB", buf, offset, len)) {
      parser.decimalB = ji.readInt();
    } else if (fieldEquals("tokenMints", buf, offset, len)) {
      final var tokenMints = new ArrayList<PublicKey>();
      while (ji.readArray()) {
        final var tokenMint = parseBase58Encoded(ji);
        tokenMints.add(tokenMint);
      }
      parser.tokenMints = tokenMints;
    } else {
      final var field = new String(buf, offset, len);
      logger.log(WARNING, String.format("%nUnhandled MarketRecord.params field %s: %s%n", field, ji.currentBuffer()));
      ji.skip();
    }
    return true;
  };

  private static final class Parser implements FieldBufferPredicate {

    private PublicKey pubkey;
    private long lamports;
    private byte[] data;
    private PublicKey owner;
    private boolean executable;
    private BigInteger rentEpoch;
    private int space;
    private String addressLookupTableAddress;
    private MarketPair vaultLpMint;
    private MarketPair vaultToken;
    private String serumAsks;
    private String serumBids;
    private String serumCoinVaultAccount;
    private String serumEventQueue;
    private String serumPcVaultAccount;
    private String serumVaultSigner;
    private long routingGroup;
    private long amp;
    private int decimalA;
    private int decimalB;
    private List<PublicKey> tokenMints;

    private Parser() {
    }

    private MarketRecord create() {
      return new MarketRecord(
          pubkey,
          lamports,
          data,
          owner,
          executable,
          rentEpoch,
          space,
          addressLookupTableAddress,
          vaultLpMint,
          vaultToken,
          serumAsks,
          serumBids,
          serumCoinVaultAccount,
          serumEventQueue,
          serumPcVaultAccount,
          serumVaultSigner,
          routingGroup,
          amp,
          decimalA, decimalB,
          tokenMints
      );
    }

    @Override
    public boolean test(final char[] buf, final int offset, final int len, final JsonIterator ji) {
      if (fieldEquals("pubkey", buf, offset, len)) {
        pubkey = parseBase58Encoded(ji);
      } else if (fieldEquals("lamports", buf, offset, len)) {
        lamports = ji.readLong();
      } else if (fieldEquals("data", buf, offset, len)) {
        data = parseEncodedData(ji);
      } else if (fieldEquals("owner", buf, offset, len)) {
        owner = parseBase58Encoded(ji);
      } else if (fieldEquals("executable", buf, offset, len)) {
        executable = ji.readBoolean();
      } else if (fieldEquals("rentEpoch", buf, offset, len)) {
        rentEpoch = ji.readBigInteger();
      } else if (fieldEquals("space", buf, offset, len)) {
        space = ji.readInt();
      } else if (fieldEquals("params", buf, offset, len)) {
        ji.testObject(this, PARAMS_PARSER);
      } else {
        logger.log(WARNING, "Unhandled MarketRecord field: %s%n", new String(buf, offset, len));
        ji.skip();
      }
      return true;
    }
  }
}
