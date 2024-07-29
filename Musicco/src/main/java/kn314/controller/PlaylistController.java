package kn314.controller;


import kn314.exception.PlaylistNotFoundException;
import kn314.model.Playlist;
import kn314.model.Track;
import kn314.service.PlaylistService;
import kn314.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("playlist")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final TrackService trackService;

    @GetMapping("/playlists")
    public ResponseEntity<List<Playlist>> getAllPlaylists(){
        List<Playlist> playlists = playlistService.findAllPlaylists();
        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }

    @PostMapping("/create_playlist")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist){
        Playlist newPlaylist = playlistService.createPlaylist(playlist);
        return new ResponseEntity<>(newPlaylist, HttpStatus.CREATED);
    }

    @PutMapping("/update_playlist")
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist updatedPlaylist){
        try {
            Playlist savedPlaylist = playlistService.updatePlaylist(updatedPlaylist);
            return new ResponseEntity<>(savedPlaylist, HttpStatus.OK);
        } catch (PlaylistNotFoundException playlistNotFoundException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete_playlist/{id}")
    public ResponseEntity<?> deletePlaylistById(@PathVariable("id") long id){
        playlistService.deletePlaylistById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add_track/{playlistId}/{trackId}")
    public ResponseEntity<Playlist> addTrackToPlaylist(
            @PathVariable("playlistId") long playlistId,
            @PathVariable("trackId") long trackId
    ) {
        Playlist playlist = playlistService.findById(playlistId);
        Track track = trackService.findById(trackId);

        playlistService.addTrackToPlaylist(playlist, track);

        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @DeleteMapping("/remove_track/{playlistId}/{trackId}")
    public ResponseEntity<Playlist> removeTrackFromPlaylist(
            @PathVariable("playlistId") long playlistId,
            @PathVariable("trackId") long trackId
    ) {
        Playlist playlist = playlistService.findById(playlistId);
        Track track = trackService.findById(trackId);

        playlistService.removeTrackFromPlaylist(playlist, track);

        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }
}
