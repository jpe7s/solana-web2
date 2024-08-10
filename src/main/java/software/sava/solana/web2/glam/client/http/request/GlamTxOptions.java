package software.sava.solana.web2.glam.client.http.request;

public interface GlamTxOptions {

  static GlamTxOptions createComputeUnitLimitOptions(final long computeUnitLimit) {
    return new GlamTxOptionsRecord(computeUnitLimit, Long.MIN_VALUE, Long.MIN_VALUE);
  }

  static GlamTxOptions createComputeUnitLimitAndPriceOptions(final long computeUnitLimit,
                                                             final long computeUnitPriceMicroLamports) {
    return new GlamTxOptionsRecord(computeUnitLimit, computeUnitPriceMicroLamports, Long.MIN_VALUE);
  }

  static GlamTxOptions createComputeUnitLimitAndJitoTipOptions(final long computeUnitLimit,
                                                               final long jitoTipLamports) {
    return new GlamTxOptionsRecord(computeUnitLimit, Long.MIN_VALUE, jitoTipLamports);
  }

  static GlamTxOptions createComputeUnitPriceMicroLamportsOptions(final long computeUnitPriceMicroLamports) {
    return new GlamTxOptionsRecord(Long.MIN_VALUE, computeUnitPriceMicroLamports, Long.MIN_VALUE);
  }

  static GlamTxOptions createComputeUnitPriceMicroLamportsAndJitoTipOptions(final long computeUnitPriceMicroLamports,
                                                                            final long jitoTipLamports) {
    return new GlamTxOptionsRecord(Long.MIN_VALUE, computeUnitPriceMicroLamports, jitoTipLamports);
  }

  static GlamTxOptions createJitoTipOptions(final long jitoTipLamports) {
    return new GlamTxOptionsRecord(Long.MIN_VALUE, Long.MIN_VALUE, jitoTipLamports);
  }

  static GlamTxOptions createOptions(final long computeUnitLimit,
                                     final long computeUnitPriceMicroLamports,
                                     final long jitoTipLamports) {
    return new GlamTxOptionsRecord(computeUnitLimit, computeUnitPriceMicroLamports, jitoTipLamports);
  }

  String toJson();

  long computeUnitLimit();

  long computeUnitPriceMicroLamports();

  long jitoTipLamports();
}
