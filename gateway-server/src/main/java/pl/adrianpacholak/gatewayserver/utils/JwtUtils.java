package pl.adrianpacholak.gatewayserver.utils;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    public static final String AUTH_HEADER = "Authorization";
    public static final String USERNAME_CLAIM = "preferred_username";

    public String getUsername(HttpHeaders headers) {
        String authToken = headers.get(AUTH_HEADER)
                .get(0)
                .replace("Bearer ", "");
        JSONObject jsonObject = decodeJwt(authToken);
        return jsonObject.getString(USERNAME_CLAIM);
    }

    private JSONObject decodeJwt(String token) {
        String[] splitString = token.split("\\.");
        String base64EncodedBody = splitString[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        return new JSONObject(body);
    }
}
