package ru.nsu.ccfit.komugi;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("/subjects")
public class SubjectResource {

    private static Subject findById(Long id) {
        return Subject.findById(id);
    }

    private static List<Subject> findByUserId(Long id) {
        return Subject.list("user_id = ?1", id);
    }

    public static void deleteByUserId(Long id) {
        List<Subject> subjects = findByUserId(id);
        for (Subject subject : subjects) {
            Subject.deleteById(subject.id);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Subject> subjects = Subject.listAll();
        return Response.ok(subjects).build();
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("id") Long id) {
        Subject subject = findById(id);
        return Response.ok(subject).build();
    }

    @GET
    @Path("/userid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUserId(@QueryParam("id") Long id) {
        List<Subject> subjects = findByUserId(id);
        return Response.ok(subjects).build();
    }

    @POST
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addById(Subject subject) {
        subject.persist();
        if (subject.isPersistent()) {
            return Response.ok(subject).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Not created").build();
    }

    @PUT
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeById(Subject subject) {
        if (findById(subject.id) == null) {
            return Response.ok("No subject with id " + subject.id).build();
        }

        if (subject.name != null) {
            Subject.update("name = ?1 where id = ?2", subject.name, subject.id);
        }
        if (subject.dayOfWeek != null) {
            Subject.update("day_of_week = ?1 where id = ?2", subject.dayOfWeek, subject.id);
        }
        if (subject.timeOfDay != null) {
            Subject.update("time_of_day = ?1 where id = ?2", subject.timeOfDay, subject.id);
        }
        if (subject.classroom != null) {
            Subject.update("classroom = ?1 where id = ?2", subject.classroom, subject.id);
        }
        if (subject.type != null) {
            Subject.update("type = ?1 where id = ?2", subject.type, subject.id);
        }
        if (subject.userId != null) {
            Subject.update("user_id = ?1 where id = ?2", subject.userId, subject.id);
        }

        return Response.ok().entity("Subject changed").build();
    }

    @DELETE
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@QueryParam("id") Long id) {
        boolean deleted = Subject.deleteById(id);
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

        List<Subject> subjects = findByUserId(user.id);
        if (subjects.size() != 0) {
            return Response.ok(subjects).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubject(Subject subject, @QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        subject.userId = user.id;
        subject.persist();
        if (subject.isPersistent()) {
            return Response.status(Response.Status.CREATED).entity("Created").build();
        }

        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeSubject(Subject subjectData, @QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Subject subject = findById(subjectData.id);
        if (subject == null || !Objects.equals(subject.userId, user.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong data or credentials").build();
        }

        if (subject.name != null) {
            Subject.update("name = ?1 where id = ?2", subjectData.name, subject.id);
        }
        if (subject.dayOfWeek != null) {
            Subject.update("day_of_week = ?1 where id = ?2", subjectData.dayOfWeek, subject.id);
        }
        if (subject.timeOfDay != null) {
            Subject.update("time_of_day = ?1 where id = ?2", subjectData.timeOfDay, subject.id);
        }
        if (subject.classroom != null) {
            Subject.update("classroom = ?1 where id = ?2", subjectData.classroom, subject.id);
        }
        if (subject.type != null) {
            Subject.update("type = ?1 where id = ?2", subjectData.type, subject.id);
        }

        return Response.ok("Subject updated").build();
    }

    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubject(@QueryParam("token") String token, @QueryParam("id") Long id) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Subject subject = findById(id);
        if (subject == null || !Objects.equals(subject.userId, user.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong data or credentials").build();
        }

        boolean deleted = Subject.deleteById(subject.id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not deleted").build();
        }
        return Response.ok("Deleted").build();
    }
}
