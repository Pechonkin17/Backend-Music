package kn314.repository;

import kn314.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByTitle(String title);
    void deletePlaylistById(long id);
    Optional<Playlist> findById(long id);
}
