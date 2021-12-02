package ru.nsu.ccfit.komugi;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("/visited")
public class VisitedResource {

    private static Visited findById(Long id) {
        return Visited.findById(id);
    }

    private static List<Visited> findByUserId(Long id) {
        return Visited.list("user_id = ?1", id);
    }

    public static void deleteByUserId(Long id) {
        List<Visited> visited = findByUserId(id);
        for (Visited visit : visited) {
            Visited.deleteById(visit.id);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Visited> visited = Visited.listAll();
        return Response.ok(visited).build();
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("id") Long id) {
        Visited visited = findById(id);
        return Response.ok(visited).build();
    }

    @GET
    @Path("/userid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUserId(@QueryParam("id") Long id) {
        List<Visited> visited = findByUserId(id);
        return Response.ok(visited).build();
    }

    @POST
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addById(Visited visited) {
        visited.persist();
        if (visited.isPersistent()) {
            return Response.ok(visited).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Not created").build();
    }

    @PUT
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeById(Visited visited) {
        if (findById(visited.id) == null) {
            return Response.ok("No user with id " + visited.id).build();
        }

        if (visited.userId != null) {
            Visited.update("user_id = ?1 where id = ?2", visited.userId, visited.id);
        }
        if (visited.subjectId != null) {
            Visited.update("subject_id = ?1 where id = ?2", visited.subjectId, visited.id);
        }
        if (visited.date != null) {
            Visited.update("date = ?1 where id = ?2", visited.date, visited.id);
        }
        Visited.update("visited = ?1 where id = ?2", visited.visited, visited.id);

        return Response.ok().entity("Visited changed").build();
    }

    @DELETE
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@QueryParam("id") Long id) {
        boolean deleted = Visited.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not deleted").build();
        }
        return Response.ok("Deleted").build();
    }

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUser(@QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        List<Visited> visited = findByUserId(user.id);
        if (visited.size() != 0) {
            return Response.ok(visited).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubject(Visited Visited, @QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Visited.userId = user.id;
        Visited.persist();
        if (Visited.isPersistent()) {
            return Response.status(Response.Status.CREATED).entity("Created").build();
        }

        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeSubject(Visited visitedData, @QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Visited visited = findById(visitedData.id);
        if (visited == null || !Objects.equals(visited.userId, user.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong data or credentials").build();
        }

        if (visited.subjectId != null) {
            Visited.update("subject_id = ?1 where id = ?2", visited.subjectId, visited.id);
        }
        if (visited.date != null) {
            Visited.update("date = ?1 where id = ?2", visited.date, visited.id);
        }
        Visited.update("visited = ?1 where id = ?2", visited.visited, visited.id);

        return Response.ok("Visited updated").build();
    }

    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubject(@QueryParam("token") String token, @QueryParam("id") Long id) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Visited visited = findById(id);
        if (visited == null || !Objects.equals(visited.userId, user.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong data or credentials").build();
        }

        boolean deleted = Visited.deleteById(visited.id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not deleted").build();
        }
        return Response.ok("Deleted").build();
    }
}
