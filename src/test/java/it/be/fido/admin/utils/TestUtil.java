package it.be.fido.admin.utils;

import it.be.fido.admin.common.payload.response.MessageResponse;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {
    public static MessageResponse getMessageREsponse(String message, int statusCode) {
        return new MessageResponse(message, statusCode);
    }

}
