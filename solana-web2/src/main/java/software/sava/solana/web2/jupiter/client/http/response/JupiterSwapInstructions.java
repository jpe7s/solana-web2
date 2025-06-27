package software.sava.solana.web2.jupiter.client.http.response;

import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.lookup.AddressLookupTable;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.accounts.meta.LookupTableAccountMeta;
import software.sava.core.tx.Instruction;
import software.sava.core.tx.Transaction;
import systems.comodal.jsoniter.ContextFieldBufferPredicate;
import systems.comodal.jsoniter.JsonIterator;
import systems.comodal.jsoniter.ValueType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static software.sava.core.tx.Transaction.sortLegacyAccounts;
import static software.sava.rpc.json.PublicKeyEncoding.parseBase58Encoded;
import static systems.comodal.jsoniter.JsonIterator.fieldEquals;

public record JupiterSwapInstructions(Instruction tokenLedgerInstruction,
                                      List<Instruction> computeBudgetInstructions,
                                      List<Instruction> setupInstructions,
                                      Instruction swapInstruction,
                                      Instruction cleanupInstruction,
                                      List<Instruction> otherInstructions,
                                      List<PublicKey> addressLookupTableAddresses,
                                      long prioritizationFeeLamports) {

  public Map<PublicKey, AccountMeta> createAccountsMap() {
    final var feePayer = setupInstructions.getFirst().accounts().getFirst().publicKey();
    return AccountMeta.createAccountsMap(64, feePayer);
  }

  public int numInstructions() {
    return (tokenLedgerInstruction == null ? 0 : 1)
        + computeBudgetInstructions.size()
        + setupInstructions.size()
        + 1
        + (cleanupInstruction == null ? 0 : 1)
        + otherInstructions.size();
  }

  public int mergeAllAccounts(final Instruction[] instructions, final Map<PublicKey, AccountMeta> accounts) {
    int ix = 0;
    int serializedInstructionLength = 0;

    if (tokenLedgerInstruction != null) {
      serializedInstructionLength += tokenLedgerInstruction.mergeAccounts(accounts);
      instructions[ix] = tokenLedgerInstruction;
      ++ix;
    }

    for (final var instruction : computeBudgetInstructions) {
      serializedInstructionLength += instruction.mergeAccounts(accounts);
      instructions[ix] = instruction;
      ++ix;
    }

    for (final var instruction : setupInstructions) {
      serializedInstructionLength += instruction.mergeAccounts(accounts);
      instructions[ix] = instruction;
      ++ix;
    }

    serializedInstructionLength += swapInstruction.mergeAccounts(accounts);
    instructions[ix] = swapInstruction;

    if (cleanupInstruction != null) {
      serializedInstructionLength += cleanupInstruction.mergeAccounts(accounts);
      instructions[++ix] = cleanupInstruction;
    }

    for (final var instruction : otherInstructions) {
      serializedInstructionLength += instruction.mergeAccounts(accounts);
      instructions[++ix] = instruction;
    }

    return serializedInstructionLength;
  }

  public Transaction serializeTransaction() {
    final var accounts = createAccountsMap();
    final var instructions = new Instruction[numInstructions()];
    final int serializedInstructionLength = mergeAllAccounts(instructions, accounts);
    return Transaction.createTx(Arrays.asList(instructions), serializedInstructionLength, sortLegacyAccounts(accounts));
  }

  public Transaction serializeTransaction(final AddressLookupTable lookupTable) {
    final var accounts = createAccountsMap();
    final var instructions = new Instruction[numInstructions()];
    final int serializedInstructionLength = mergeAllAccounts(instructions, accounts);
    return Transaction.createTx(Arrays.asList(instructions), serializedInstructionLength, accounts, lookupTable);
  }

  public Transaction serializeTransaction(final LookupTableAccountMeta[] tableAccountMetas) {
    final var accounts = createAccountsMap();
    final var instructions = new Instruction[numInstructions()];
    final int serializedInstructionLength = mergeAllAccounts(instructions, accounts);
    return Transaction.createTx(Arrays.asList(instructions), serializedInstructionLength, accounts, tableAccountMetas);
  }

  private static final List<Instruction> NO_INSTRUCTIONS = List.of();

  public static JupiterSwapInstructions parseInstructions(final JsonIterator ji) {
    return ji.testObject(new Builder(), PARSER).create();
  }

  public static List<Instruction> parseInstructionsList(final JsonIterator ji) {
    if (ji.readArray()) {
      final var instructions = new ArrayList<Instruction>();
      do {
        instructions.add(parseInstruction(ji));
      } while (ji.readArray());
      return instructions;
    } else {
      return NO_INSTRUCTIONS;
    }
  }

  public static Instruction parseInstruction(final JsonIterator ji) {
    return ji.testObject(new InstructionBuilder(), INSTRUCTION_PARSER).create();
  }

  public static AccountMeta parseAccount(final JsonIterator ji) {
    return parseAccount(ji, new AccountBuilder());
  }

  private static AccountMeta parseAccount(final JsonIterator ji, final AccountBuilder builder) {
    return ji.testObject(builder, ACCOUNT_PARSER).create();
  }

  private static final ContextFieldBufferPredicate<InstructionBuilder> INSTRUCTION_PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("programId", buf, offset, len)) {
      builder.programId = parseBase58Encoded(ji);
    } else if (fieldEquals("accounts", buf, offset, len)) {
      final var accounts = new ArrayList<AccountMeta>();
      final var accountBuilder = new AccountBuilder();
      while (ji.readArray()) {
        accounts.add(parseAccount(ji, accountBuilder));
      }
      builder.accounts = accounts;
    } else if (fieldEquals("data", buf, offset, len)) {
      builder.data = ji.decodeBase64String();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final ContextFieldBufferPredicate<AccountBuilder> ACCOUNT_PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("pubkey", buf, offset, len)) {
      builder.pubKey = parseBase58Encoded(ji);
    } else if (fieldEquals("isSigner", buf, offset, len)) {
      builder.signer = ji.readBoolean();
    } else if (fieldEquals("isWritable", buf, offset, len)) {
      builder.writable = ji.readBoolean();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final ContextFieldBufferPredicate<Builder> PARSER = (builder, buf, offset, len, ji) -> {
    if (fieldEquals("tokenLedgerInstruction", buf, offset, len)) {
      if (ji.whatIsNext() == ValueType.OBJECT) {
        builder.tokenLedgerInstruction = parseInstruction(ji);
      } else {
        ji.skip();
      }
    } else if (fieldEquals("computeBudgetInstructions", buf, offset, len)) {
      builder.computeBudgetInstructions = parseInstructionsList(ji);
    } else if (fieldEquals("setupInstructions", buf, offset, len)) {
      builder.setupInstructions = parseInstructionsList(ji);
    } else if (fieldEquals("swapInstruction", buf, offset, len)) {
      builder.swapInstruction = parseInstruction(ji);
    } else if (fieldEquals("cleanupInstruction", buf, offset, len)) {
      if (ji.whatIsNext() == ValueType.OBJECT) {
        builder.cleanupInstruction = parseInstruction(ji);
      } else {
        ji.skip();
      }
    } else if (fieldEquals("otherInstructions", buf, offset, len)) {
      builder.otherInstructions = parseInstructionsList(ji);
    } else if (fieldEquals("addressLookupTableAddresses", buf, offset, len)) {
      final var addressLookupTableAddresses = new ArrayList<PublicKey>();
      while (ji.readArray()) {
        addressLookupTableAddresses.add(parseBase58Encoded(ji));
      }
      builder.addressLookupTableAddresses = addressLookupTableAddresses;
    } else if (fieldEquals("prioritizationFeeLamports", buf, offset, len)) {
      builder.prioritizationFeeLamports = ji.readLong();
    } else {
      ji.skip();
    }
    return true;
  };

  private static final class InstructionBuilder {

    private PublicKey programId;
    private List<AccountMeta> accounts;
    private byte[] data;

    private InstructionBuilder() {
    }

    private Instruction create() {
      return Instruction.createInstruction(programId, accounts, data);
    }
  }

  private static final class AccountBuilder {

    private PublicKey pubKey;
    private boolean signer;
    private boolean writable;

    private AccountBuilder() {
    }

    private AccountMeta create() {
      if (signer) {
        return writable
            ? AccountMeta.createWritableSigner(pubKey)
            : AccountMeta.createReadOnlySigner(pubKey);
      } else if (writable) {
        return AccountMeta.createWrite(pubKey);
      } else {
        return AccountMeta.createRead(pubKey);
      }
    }
  }

  static final class Builder {

    private Instruction tokenLedgerInstruction;
    private List<Instruction> computeBudgetInstructions;
    private List<Instruction> setupInstructions;
    private Instruction swapInstruction;
    private Instruction cleanupInstruction;
    private List<Instruction> otherInstructions;
    private List<PublicKey> addressLookupTableAddresses;
    private long prioritizationFeeLamports;

    private Builder() {
    }

    private JupiterSwapInstructions create() {
      return new JupiterSwapInstructions(
          tokenLedgerInstruction,
          computeBudgetInstructions,
          setupInstructions,
          swapInstruction,
          cleanupInstruction,
          otherInstructions,
          addressLookupTableAddresses,
          prioritizationFeeLamports
      );
    }
  }
}
