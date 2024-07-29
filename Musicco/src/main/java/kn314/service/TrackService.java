package kn314.service;

import kn314.exception.TrackNotFoundException;
import kn314.model.Track;
import kn314.repository.TrackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TrackService {
    private final TrackRepository trackRepository;

    public Track createTrack(Track track){
        return trackRepository.save(track);
    }

    public List<Track> findAllTracks(){
        return trackRepository.findAll();
    }

    public Track updateTrack(Track updatedTrack){
        Optional<Track> existingTrack = trackRepository.findById(updatedTrack.getId());

        if (existingTrack.isPresent()){
            Track track = existingTrack.get();

            track.setTitle(updatedTrack.getTitle());
            track.setArtist(updatedTrack.getArtist());
            track.setGenre(updatedTrack.getGenre());
            track.setDuration(updatedTrack.getDuration());
            track.setDescription(updatedTrack.getDescription());
            track.setFilePath(updatedTrack.getFilePath());
            track.setCreated_at(updatedTrack.getCreated_at());

            Track savedTrack = trackRepository.save(track);

            return savedTrack;
        } else {
            throw new TrackNotFoundException("Track not found with id: " + updatedTrack.getId());
        }
    }

    public List<Track> findAllByTitle(String title){
        return trackRepository.findAllByTitle(title);
    }

    public List<Track> findAllByArtist(String artist){
        return trackRepository.findAllByArtist(artist);
    }

    public void deleteTrackById(long id){
        trackRepository.deleteTrackById(id);
    }

    public Track findById(long id){
        return trackRepository.findById(id)
                .orElseThrow(() -> new TrackNotFoundException("Track by id " + id + " was not found"));
    }
}
