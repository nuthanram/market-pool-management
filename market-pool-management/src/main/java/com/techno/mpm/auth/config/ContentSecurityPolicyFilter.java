package com.techno.mpm.auth.config;
import org.springframework.security.web.header.HeaderWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Base64;

public class ContentSecurityPolicyFilter implements HeaderWriter {

	
    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
        String nonce = generateNonce();
        response.setHeader("Content-Security-Policy", "script-src 'self' 'nonce-" + nonce + "'");
    }

    private String generateNonce() {
        SecureRandom random = new SecureRandom();
        byte[] nonceBytes = new byte[32];
        random.nextBytes(nonceBytes);
        return Base64.getEncoder().encodeToString(nonceBytes);
    }
}
