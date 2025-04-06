module software.sava.solana_web2 {
  requires java.net.http;

  requires systems.comodal.json_iterator;

  requires software.sava.core;
  requires software.sava.rpc;

  exports software.sava.solana.web2.birdeye.client.http;
  exports software.sava.solana.web2.birdeye.client.http.response;
  exports software.sava.solana.web2.birdeye.client.http.request;

  exports software.sava.solana.web2.helius.client.http;
  exports software.sava.solana.web2.helius.client.http.response;
  exports software.sava.solana.web2.helius.client.http.request;

  exports software.sava.solana.web2.jito.client.http;
  exports software.sava.solana.web2.jito.client.http.response;

  exports software.sava.solana.web2.jupiter.client.http;
  exports software.sava.solana.web2.jupiter.client.http.response;
  exports software.sava.solana.web2.jupiter.client.http.request;
}
