package kn314.controller;

import kn314.exception.TrackNotFoundException;
import kn314.model.Track;
import kn314.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("track")
public class TrackController {
    private final TrackService trackService;

    @GetMapping("/tracks")
    public ResponseEntity<List<Track>> getAllTracks(){
        List<Track> tracks = trackService.findAllTracks();
        return new ResponseEntity<>(tracks, HttpStatus.OK);
    }

    @PostMapping("/create_track")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Track> createTrack(@RequestBody Track track){
        Track newTrack = trackService.createTrack(track);
        return new ResponseEntity<>(newTrack, HttpStatus.CREATED);
    }

    @PutMapping("/update_track")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Track> updateTrack(@RequestBody Track updatedTrack){
        try {
            Track savedTrack = trackService.updateTrack(updatedTrack);
            return new ResponseEntity<>(savedTrack, HttpStatus.OK);
        } catch (TrackNotFoundException trackNotFoundException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete_track/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> deleteTrackById(@PathVariable("id") long id){
        trackService.deleteTrackById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
