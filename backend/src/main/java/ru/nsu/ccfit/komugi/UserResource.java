package ru.nsu.ccfit.komugi;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserResource {

    private static User findById(Long id) {
        return User.find("id = ?1", id).firstResult();
    }

    private static boolean idExists(Long id) {
        User user = findById(id);
        return user != null;
    }

    private static User findByLogin(String login) {
        return User.find("login = ?1", login).firstResult();
    }

    private static boolean loginExists(String login) {
        User user = findByLogin(login);
        return user != null;
    }

    private static User findByEmail(String email) {
        return User.find("email = ?1", email).firstResult();
    }

    private static boolean emailExists(String email) {
        User user = findByEmail(email);
        return user != null;
    }

    public static User getUserByData(String  token) {
        if (token == null) {
            return null;
        }
        return TokenResource.findByToken(token);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<User> users = User.listAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("id") Long id) {
        User user = findById(id);
        return Response.ok(user).build();
    }

    @PUT
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeById(User user) {
        if (!idExists(user.id)){
            return Response.ok("No user with id " + user.id).build();
        }

        if (user.login != null) {
            User.update("login = ?1 where id = ?2", user.login, user.id);
        }
        if (user.password != null) {
            User.update("password = ?1 where id = ?2", user.password, user.id);
        }
        if (user.email != null) {
            User.update("email = ?1 where id = ?2", user.email, user.id);
        }
        return Response.ok("User updated").build();
    }

    @DELETE
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@QueryParam("id") Long id) {
        boolean deleted = User.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not deleted").build();
        }
        return Response.ok("Deleted").build();
    }

    @GET
    @Path("/login/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByLogin(@PathParam("login") String login) {
        User user = findByLogin(login);
        return Response.ok(user).build();
    }

    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByEmail(@PathParam("email") String email) {
        User user = findByEmail(email);
        return Response.ok(user).build();
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(@QueryParam("login") String login, @QueryParam("pass") String pass) {
        User user = User.find("login = ?1 AND password = ?2", login, pass).firstResult();
        if (user != null) {
            String token = TokenResource.getToken(user).token;
            return Response.ok(token).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Wrong credentials").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response registerUser(User user) {
        System.out.println("Add user: " + user.login + ", " + user.password + ", " + user.email);

        if (loginExists(user.login)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Login already exists").build();
        } else if (emailExists(user.email)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email already exists").build();
        }

        user.persist();
        if (user.isPersistent()) {
            return Response.status(Response.Status.CREATED).entity("Created").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not created").build();
        }
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUser(User userData, @QueryParam("token") String token) {
        System.out.println("Change user: " + ", " + userData.login + ", " + userData.password + ", " + userData.email + ", " + token);

        if (!loginExists(userData.login)){
            return Response.ok("No user with login " + userData.login).build();
        }

        User usrToChange = getUserByData(token);
        if (usrToChange == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        if (userData.login != null) {
            User.update("login = ?1 where id = ?2", userData.login, usrToChange.id);
        }
        if (userData.password != null) {
            User.update("password = ?1 where id = ?2", userData.password, usrToChange.id);
        }
        if (userData.email != null) {
            User.update("email = ?1 where id = ?2", userData.email, usrToChange.id);
        }

        return Response.ok("User updated").build();
    }


    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteUser(@QueryParam("token") String token) {
        User usrToDelete = getUserByData(token);
        if (usrToDelete == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        SubjectResource.deleteByUserId(usrToDelete.id);
        TaskResource.deleteByUserId(usrToDelete.id);
        VisitedResource.deleteByUserId(usrToDelete.id);
        boolean deleted = User.deleteById(usrToDelete.id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not deleted").build();
        }
        return Response.ok("Deleted").build();
    }
}


