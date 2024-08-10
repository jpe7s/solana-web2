package software.sava.solana.web2.jito.client.http.response;

import software.sava.rpc.json.http.request.Commitment;
import software.sava.rpc.json.http.response.Context;
import software.sava.rpc.json.http.response.RootBuilder;
import software.sava.rpc.json.http.response.TxInstructionError;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;
import systems.comodal.jsoniter.ValueType;

import java.util.*;

import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record BundleStatus(Context context,
                           String bundleId,
                           List<String> signatures,
                           long slot,
                           TxInstructionError error,
                           Commitment confirmationStatus,
                           Map<String, String> unhandledFields) {

  public static Map<String, BundleStatus> parseStatuses(final JsonIterator ji, final Context context) {
    final var statuses = new HashMap<String, BundleStatus>();
    while (ji.readArray()) {
      if (ji.whatIsNext() == ValueType.OBJECT) {
        final var status = ji.testObject(new Builder(context), PARSER).create();
        statuses.put(status.bundleId, status);
      } else {
        ji.skip();
      }
    }
    return statuses;
  }

  public static BundleStatus parseStatus(final JsonIterator ji, final Context context) {
    return ji.readArray() && ji.whatIsNext() == ValueType.OBJECT
        ? ji.testObject(new Builder(context), PARSER).create()
        : null;
  }

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("bundle_id", buf, offset, len)) {
      builder.bundleId = ji.readString();
    } else if (fieldEquals("transactions", buf, offset, len)) {
      if (ji.readArray()) {
        builder.signatures = new ArrayList<>();
        do {
          builder.signatures.add(ji.readString());
        } while (ji.readArray());
      } else {
        builder.signatures = List.of();
      }
    } else if (fieldEquals("slot", buf, offset, len)) {
      builder.slot = ji.readLong();
    } else if (fieldEquals("err", buf, offset, len)) {
      builder.error = TxInstructionError.parseError(ji);
    } else if (fieldEquals("confirmationStatus", buf, offset, len) || fieldEquals("confirmation_status", buf, offset, len)) {
      builder.confirmationStatus(ji.readString());
    } else {
      final var field = new String(buf, offset, len);
      builder.recordUnhandledField(field, ji);
    }
    return true;
  };

  private static final Map<String, String> ALL_FIELDS_HANDLED = Map.of();

  private static final class Builder extends RootBuilder {

    private String bundleId;
    private List<String> signatures;
    private long slot;
    private TxInstructionError error;
    private Commitment confirmationStatus;
    private Map<String, String> unhandledFields;

    private Builder(final Context context) {
      super(context);
    }

    private BundleStatus create() {
      return new BundleStatus(
          context,
          bundleId,
          signatures,
          slot,
          error,
          confirmationStatus,
          Objects.requireNonNullElse(unhandledFields, ALL_FIELDS_HANDLED));
    }

    private void recordUnhandledField(final String field, final JsonIterator ji) {
      final var value = switch (ji.whatIsNext()) {
        case STRING -> ji.readString();
        case NUMBER -> ji.readNumberAsString();
        case BOOLEAN -> Boolean.toString(ji.readBoolean());
        case INVALID -> {
          ji.skip();
          yield "?";
        }
        case NULL -> {
          ji.skip();
          yield "null";
        }
        case ARRAY -> {
          ji.skip();
          yield "[<?>]";
        }
        case OBJECT -> {
          ji.skip();
          yield "{<?>}";
        }
      };
      if (this.unhandledFields == null) {
        this.unhandledFields = new HashMap<>();
      }
      this.unhandledFields.put(field, value);
      System.err.format("%nUnhandled TxStatus field [%s]=[%s]%n", field, value);
    }

    private void confirmationStatus(final String confirmationStatus) {
      this.confirmationStatus = confirmationStatus == null || confirmationStatus.isBlank()
          ? null
          : Commitment.valueOf(confirmationStatus.toUpperCase(Locale.ENGLISH));
    }
  }
}