package beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import dtos.Song;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@ApplicationScoped
public class SongFileBean {
    private static Logger LOG = LogManager.getLogger(SongFileBean.class.getName());

    @PersistenceContext(unitName = "uploader-jpa")
    private EntityManager entityManager;

    private Client httpClient = ClientBuilder.newClient();

    @Inject
    @DiscoverService("microservice-catalogs")
    private Optional<String> basePath;

    public JsonObject addSong(Song song) {
        if (basePath.isPresent()) {
            Response response = httpClient.target(basePath.get() + "/api/v1/songs").request().post(Entity.entity(song, MediaType.APPLICATION_JSON_TYPE));
            if (response.getStatus() != 201) {
                LOG.warn("Failed to add song. Got response with status " + response.getStatus());
                if (response.getStatus() == 400) {
                    return Json.createObjectBuilder().add("status", "400").build();
                }
                return null;
            }
            return (JsonObject) response.getEntity();
        }
        LOG.error("Could not get path for catalogs microservice. Could not add song.");
        return null;
    }
}
