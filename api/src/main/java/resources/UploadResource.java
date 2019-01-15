package resources;

import beans.SongFileBean;
import com.kumuluz.ee.logs.cdi.Log;
import configs.AppConfigs;
import entities.SongFile;
import helpers.FileHelpers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.OutputStream;

@Log
@ApplicationScoped
@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadResource {
    @Inject
    private SongFileBean songBean;

    @Inject
    private AppConfigs appConfigs;

    @GET
    @Path("{id}")
    public Response getSong(@PathParam("id") Integer songId) {
        SongFile songFile = songBean.getSong(songId);
        if (songFile == null) {
            return  Response.status(Response.Status.NOT_FOUND).build();
        }
        if (appConfigs.getOs().equals("linux")) {
            songFile.setFilePath(FileHelpers.removeRootDirName(songFile.getFilePath()));
        }
        return Response.ok(songFile).build();
    }

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

    @DELETE
    @Path("{id}")
    public Response removeFile(@PathParam("id") Integer songId) {
        SongFile songFile = songBean.getSong(songId);
        if(songFile == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Boolean removed = songBean.removeSong(songFile);
        if (!removed) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

//    @GET
//    @Path("download/{id}")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response getSongFile(@PathParam("id") Integer songId) {
//        SongFile songFile = songBean.getSong(songId);
//        if (songFile == null) {
//            return  Response.status(Response.Status.NOT_FOUND).build();
//        }
//        String path = songFile.getFilePath() + songFile.getFileName();
////        OutputStream outputStream = songBean.getSongFile(path);
//        byte[] out = songBean.getSongFile(path);
//        System.out.println("length " + out.length);
//        return Response.ok(out).build();
//    }
}
