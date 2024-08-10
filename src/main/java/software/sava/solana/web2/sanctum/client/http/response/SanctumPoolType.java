package software.sava.solana.web2.sanctum.client.http.response;

import software.sava.core.accounts.PublicKey;

public enum SanctumPoolType {

  Spl("SPoo1Ku8WFXoNDMHPsrGSTSG1Y47rzgn41SLUNakuHy"),
  SanctumSpl("SP12tWFxD9oJsVWNavTTBZvMbA6gkAmxtVgxdqvyvhY"),
  SanctumSplMulti("SPMBzsVUuoHA4Jm6KunbsotaahvVikZs1JyTW6iJvbn"),
  ReservePool(),
  SPool("5ocnV1qiCgaQR8Jb8xWnVbApfaygJ8tNoZfgPwsgx9kx"),
  Lido(),
  Marinade();

  private final PublicKey programPublicKey;

  SanctumPoolType(final String programPublicKey) {
    this.programPublicKey = PublicKey.fromBase58Encoded(programPublicKey);
  }

  SanctumPoolType() {
    this.programPublicKey = null;
  }

  public PublicKey programPublicKey() {
    return programPublicKey;
  }
}
