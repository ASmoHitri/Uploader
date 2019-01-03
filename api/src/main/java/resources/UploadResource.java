package resources;

import beans.SongFileBean;
import com.kumuluz.ee.logs.cdi.Log;
import dtos.Song;
import dtos.UploadSong;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.servlet.annotation.MultipartConfig;
import javax.ws.rs.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Log
@ApplicationScoped
@Path("/upload")
@Produces(value={MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
@Consumes(value={MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
public class UploadResource {
    @Inject
    private SongFileBean songBean;

    @POST
    public Response uploadFile(("song") String s, @FormParam("file") File file) {
        System.out.println(s);
//        if (uploadSong == null || uploadSong.getSongInfo() == null || uploadSong.getFile() == null) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//        JsonObject songObject = songBean.addSong(uploadSong.getSongInfo());
//        if (songObject == null) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//        if (songObject.getInt("status", -1) == 400) return Response.status(Response.Status.BAD_REQUEST).build();  // catalog's add song returned 400
//
//        Integer songId = songObject.getInt("id");
//        return Response.status(Response.Status.CREATED).entity(songObject).build();
        return Response.ok().build();
    }
}
