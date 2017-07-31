package us.parshall.us.parshall.jwt.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/generatejwt")
public class GenerateJwtController {
    private static final Logger logger = LoggerFactory.getLogger(GenerateJwtController.class);

    @RequestMapping(method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> generateJwt(RequestEntity<JwtRequest> jwtRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, String> response = new HashMap<>();

        JwtRequest jwtR = jwtRequest.getBody();
        logger.info(jwtR.toString());
        byte[] publicKeyBytes = Base64.decodeBase64(jwtR.getPublicKey());
        byte[] privateKeyBytes = Base64.decodeBase64(jwtR.getPrivateKey());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        Algorithm algorithmRS = Algorithm.RSA256(publicKey, privateKey);

        Calendar calExpires = Calendar.getInstance();
        calExpires.add(Calendar.MINUTE, jwtR.getNotAfterMinutes());

        Calendar calNotBefore = Calendar.getInstance();
        calNotBefore.add(Calendar.MINUTE, jwtR.getNotBeforeMinutes());

        String jwt = JWT.create().withExpiresAt(calExpires.getTime())
                .withNotBefore(calNotBefore.getTime())
                .withClaim("username", jwtR.getUsername()).sign(algorithmRS);
        response.put("jwt", jwt);
        logger.info(jwt);
        return response;
    }
}
