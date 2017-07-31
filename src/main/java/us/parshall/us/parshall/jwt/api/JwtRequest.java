package us.parshall.us.parshall.jwt.api;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class JwtRequest implements Serializable {
    private String publicKey;
    private String privateKey;
    private String username;
    private int notBeforeMinutes;
    private int notAfterMinutes;
}
