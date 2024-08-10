package software.sava.solana.web2.glam.client.http.request;

record GlamTxOptionsRecord(long computeUnitLimit,
                           long computeUnitPriceMicroLamports,
                           long jitoTipLamports) implements GlamTxOptions {

  @Override
  public String toJson() {
    if (computeUnitLimit > 0) {
      if (computeUnitPriceMicroLamports > 0) {
        return jitoTipLamports > 0
            ? String.format("""
                ,"computeUnitLimit":%d,"computeUnitPriceMicroLamports":%d,"jitoTipLamports":%d""",
            computeUnitLimit, computeUnitPriceMicroLamports, jitoTipLamports)
            : String.format("""
            ,"computeUnitLimit":%d,"computeUnitPriceMicroLamports":%d""", computeUnitLimit, computeUnitPriceMicroLamports);
      } else {
        return jitoTipLamports > 0
            ? String.format("""
            ,"computeUnitLimit":%d,"jitoTipLamports":%d""", computeUnitLimit, jitoTipLamports)
            : String.format("""
            ,"computeUnitLimit":%d""", computeUnitLimit);
      }
    } else if (computeUnitPriceMicroLamports > 0) {
      return jitoTipLamports > 0
          ? String.format("""
          ,"computeUnitPriceMicroLamports":%d,"jitoTipLamports":%d""", computeUnitPriceMicroLamports, jitoTipLamports)
          : String.format("""
          ,"computeUnitPriceMicroLamports":%d""", computeUnitPriceMicroLamports);
    } else {
      return jitoTipLamports > 0
          ? String.format("""
          ,"jitoTipLamports":%d""", jitoTipLamports)
          : "";
    }
  }
}
