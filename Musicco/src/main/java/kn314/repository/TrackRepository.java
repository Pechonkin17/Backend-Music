package kn314.repository;

import kn314.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findAllByTitle(String title);
    List<Track> findAllByArtist(String artist);
    void deleteTrackById(long id);
    Optional<Track> findById(long id);
}
