package ru.nsu.ccfit.komugi;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

@Path("/tasks")
public class TaskResource {

    private static Task findById(Long id) {
        return Task.findById(id);
    }

    private static List<Task> findByUserId(Long id) {
        return Task.list("user_id = ?1", id);
    }

    public static void deleteByUserId(Long id) {
        List<Task> tasks = findByUserId(id);
        for (Task task : tasks) {
            Task.deleteById(task.id);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Task> tasks = Task.listAll();
        return Response.ok(tasks).build();
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@QueryParam("id") Long id) {
        Task task = findById(id);
        return Response.ok(task).build();
    }

    @GET
    @Path("/userid")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUserId(@QueryParam("id") Long id) {
        List<Task> tasks = findByUserId(id);
        return Response.ok(tasks).build();
    }

    @POST
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addById(Task task) {
        task.persist();
        if (task.isPersistent()) {
            return Response.ok(task).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Not created").build();
    }

    @PUT
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeById(Task task) {
        if (findById(task.id) == null) {
            return Response.ok("No user with id " + task.id).build();
        }

        if (task.name != null) {
            Task.update("name = ?1 where id = ?2", task.name, task.id);
        }
        if (task.description != null) {
            Task.update("description = ?1 where id = ?2", task.description, task.id);
        }
        if (task.userId != null) {
            Task.update("user_id = ?1 where id = ?2", task.userId, task.id);
        }
        if (task.subjectId != null) {
            Task.update("subject_id = ?1 where id = ?2", task.subjectId, task.id);
        }
        if (task.tag != null) {
            Task.update("tag = ?1 where id = ?2", task.tag, task.id);
        }

        return Response.ok().entity("Task changed").build();
    }

    @DELETE
    @Path("/id")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@QueryParam("id") Long id) {
        boolean deleted = Task.deleteById(id);
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

        List<Task> task = findByUserId(user.id);
        if (task.size() != 0) {
            return Response.ok(task).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSubject(Task Task, @QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Task.userId = user.id;
        Task.persist();
        if (Task.isPersistent()) {
            return Response.status(Response.Status.CREATED).entity("Created").build();
        }

        return Response.noContent().build();
    }

    @PUT
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeSubject(Task taskData, @QueryParam("token") String token) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Task task = findById(taskData.id);
        if (task == null || !Objects.equals(task.userId, user.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong data or credentials").build();
        }

        if (task.name != null) {
            Task.update("name = ?1 where id = ?2", taskData.name, task.id);
        }
        if (task.description != null) {
            Task.update("description = ?1 where id = ?2", taskData.description, task.id);
        }
        if (task.subjectId != null) {
            Task.update("subject_id = ?1 where id = ?2", taskData.subjectId, task.id);
        }
        if (task.tag != null) {
            Task.update("tag = ?1 where id = ?2", taskData.tag, task.id);
        }

        return Response.ok("Task updated").build();
    }

    @DELETE
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSubject(@QueryParam("token") String token, @QueryParam("id") Long id) {
        User user = UserResource.getUserByData(token);
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not authorized").build();
        }

        Task task = findById(id);
        if (task == null || !Objects.equals(task.userId, user.id)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Wrong data or credentials").build();
        }

        boolean deleted = Task.deleteById(task.id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Not deleted").build();
        }
        return Response.ok("Deleted").build();
    }
}
