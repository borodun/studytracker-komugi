package ru.nsu.ccfit.komugi;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Path("/tokens")
public class TokenResource {

    static HashMap<Long, Token> activeTokens = new HashMap<>();
    static HashMap<String, User> activeUsers = new HashMap<>();

    private static Token findByUserId(Long id) {
        return activeTokens.get(id);
    }

    public static User findByToken(String token) {
        if (token == null) {
            return null;
        }
        User user = activeUsers.get(token);
        if (user == null) {
            return null;
        }
        updateToken(activeTokens.get(user.id));
        return user;
    }

    private boolean tokenExists(User user) {
        Token token = findByUserId(user.id);
        return token != null;
    }

    private static Token newToken(User user) {
        Token token = new Token();
        token.userId = user.id;

        String hashStr = user.login + user.email + LocalDateTime.now();

        byte[] bytesOfMessage = hashStr.getBytes();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert md != null;
        byte[] theMD5digest = md.digest(bytesOfMessage);

        token.token = theMD5digest.toString();
        token.expirationDate = LocalDateTime.now().plusHours(1);

        activeTokens.put(user.id, token);
        activeUsers.put(token.token, user);

        return token;
    }

    private static void deleteToken(Token token) {
        Long id = activeUsers.get(token.token).id;
        activeTokens.remove(id);
        activeUsers.remove(token.token);
    }

    private static Token updateToken(Token token) {
        LocalDateTime time = LocalDateTime.now();
        if (token.expirationDate.isBefore(time)) {
            token.expirationDate = LocalDateTime.now().plusHours(1);
        }
        return token;
    }

    public static Token getToken(User user) {
        Token token = findByUserId(user.id);
        if (token == null) {
            return newToken(user);
        } else {
            return updateToken(token);
        }
    }

    public static boolean validate(Long id, String token) {
        if (id == null || token == null) {
            return false;
        }
        Token tok = findByUserId(id);
        if (tok == null) {
            return false;
        }
        return Objects.equals(tok.token, token);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok().entity(activeTokens.values()).build();
    }
}
