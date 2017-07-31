package us.parshall.us.parshall.jwt.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/generatekeys")
public class GenerateKeysController {
    @RequestMapping(method= RequestMethod.GET)
    Map<String, String> generateKeys() throws NoSuchAlgorithmException {
        Map<String, String> keys = new HashMap<>();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();;
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        keys.put("public", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        keys.put("private", Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        return keys;
    }
}
