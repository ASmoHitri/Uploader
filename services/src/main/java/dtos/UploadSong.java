package dtos;

import javax.ws.rs.FormParam;

public class UploadSong {
    private Song songInfo;
    private byte[] file;

    public Song getSongInfo() {
        return songInfo;
    }

    @FormParam("songInfo")
    public void setSongInfo(Song songInfo) {
        this.songInfo = songInfo;
    }

    public byte[] getFile() {
        return file;
    }

    @FormParam("file")
    public void setFile(byte[] file) {
        this.file = file;
    }
}
