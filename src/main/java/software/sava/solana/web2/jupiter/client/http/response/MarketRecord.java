package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
                           long routingGroup) {

  public static List<MarketRecord> parse(final JsonIterator ji) {
    final var records = new ArrayList<MarketRecord>(2_048);
    while (ji.readArray()) {
      final var record = ji.testObject(new Builder(), PARSER).create();
      records.add(record);
    }
    return records;
  }

  static void printValue(final JsonIterator ji, final String field) {
    var next = ji.whatIsNext();
    switch (next) {
      case INVALID -> {
        ji.skip();
        System.out.format("%nUnhandled MarketRecord.params field %s: %s%n", field, ji.currentBuffer());
      }
      case STRING -> System.out.format("""
          "%s":"%s"%n""", field, ji.readString()
      );
      case NUMBER -> System.out.format("""
          "%s":%d%n""", field, ji.readLong()
      );
      case NULL -> {
        System.out.format("""
            "%s":null%n""", field
        );
        ji.skip();
      }
      case BOOLEAN -> System.out.format("""
          "%s":%b%n""", field, ji.readBoolean()
      );
      case ARRAY -> {
        System.out.println("[");
        while (ji.readArray()) {
          printValue(ji, null);
        }
        System.out.println("]");
      }
      case OBJECT -> {
        System.out.println("{");
        for (String f; (f = ji.readObjField()) != null; ) {
          printValue(ji, f);
        }
        System.out.println("}");
      }
    }
  }

  private static final ContextFieldBufferPredicate<Builder> PARAMS_PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("addressLookupTableAddress", buf, offset, len)) {
      builder.addressLookupTableAddress = ji.readString();
    } else if (fieldEquals("vaultLpMint", buf, offset, len)) {
      builder.vaultLpMint = MarketPair.parse(ji);
    } else if (fieldEquals("vaultToken", buf, offset, len)) {
      builder.vaultToken = MarketPair.parse(ji);
    } else if (fieldEquals("serumAsks", buf, offset, len)) {
      builder.serumAsks = ji.readString();
    } else if (fieldEquals("serumBids", buf, offset, len)) {
      builder.serumBids = ji.readString();
    } else if (fieldEquals("serumCoinVaultAccount", buf, offset, len)) {
      builder.serumCoinVaultAccount = ji.readString();
    } else if (fieldEquals("serumEventQueue", buf, offset, len)) {
      builder.serumEventQueue = ji.readString();
    } else if (fieldEquals("serumPcVaultAccount", buf, offset, len)) {
      builder.serumPcVaultAccount = ji.readString();
    } else if (fieldEquals("serumVaultSigner", buf, offset, len)) {
      builder.serumVaultSigner = ji.readString();
    } else if (fieldEquals("routingGroup", buf, offset, len)) {
      builder.routingGroup = ji.readLong();
    } else {
      final var field = new String(buf, offset, len);
      printValue(ji, field);
    }
    return true;
  };

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("pubkey", buf, offset, len)) {
      builder.pubkey = parseBase58Encoded(ji);
    } else if (fieldEquals("lamports", buf, offset, len)) {
      builder.lamports = ji.readLong();
    } else if (fieldEquals("data", buf, offset, len)) {
      builder.data = parseEncodedData(ji);
    } else if (fieldEquals("owner", buf, offset, len)) {
      builder.owner = parseBase58Encoded(ji);
    } else if (fieldEquals("executable", buf, offset, len)) {
      builder.executable = ji.readBoolean();
    } else if (fieldEquals("rentEpoch", buf, offset, len)) {
      builder.rentEpoch = ji.readBigInteger();
    } else if (fieldEquals("space", buf, offset, len)) {
      builder.space = ji.readInt();
    } else if (fieldEquals("params", buf, offset, len)) {
      ji.testObject(builder, PARAMS_PARSER);
    } else {
      System.out.format("%nUnhandled MarketRecord field %s: %s%n", new String(buf, offset, len), ji.currentBuffer());
      ji.skip();
    }
    return true;
  };

  private static final class Builder {

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

    private Builder() {
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
          routingGroup
      );
    }
  }
}
