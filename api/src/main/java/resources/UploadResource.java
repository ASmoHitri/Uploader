package resources;

import beans.SongFileBean;
import com.kumuluz.ee.logs.cdi.Log;
import entities.SongFile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Log
@ApplicationScoped
@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadResource {
    @Inject
    private SongFileBean songBean;

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadFile(@PathParam("id") Integer songId, InputStream inputStream) {
        SongFile songFile = songBean.getSong(songId);
        if(songFile != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        songFile = songBean.saveFile(inputStream, songId);
        if (songFile == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(songFile).build();
    }
}
