package it.be.fido.admin.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
  private String message;
  private int statusCode;

  public MessageResponse(String message, int statusCode) {
    this.message = message;
    this.statusCode = statusCode;
  }
}
