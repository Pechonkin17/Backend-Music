package kn314.service;

import kn314.exception.PlaylistNotFoundException;
import kn314.model.Playlist;
import kn314.model.Track;
import kn314.repository.PlaylistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public Playlist createPlaylist(Playlist playlist){
        return playlistRepository.save(playlist);
    }

    public List<Playlist> findAllPlaylists(){
        return playlistRepository.findAll();
    }

    public Playlist updatePlaylist(Playlist updatedPlaylist){
        Optional<Playlist> existingPlaylist = playlistRepository.findById(updatedPlaylist.getId());

        if (existingPlaylist.isPresent()){
            Playlist playlist = existingPlaylist.get();

            playlist.setTitle(updatedPlaylist.getTitle());
            playlist.setDescription(updatedPlaylist.getDescription());
            playlist.setCreated_at(updatedPlaylist.getCreated_at());

            Playlist savedPlaylist = playlistRepository.save(playlist);

            return savedPlaylist;
        } else {
            throw new PlaylistNotFoundException("Playlist not found with id: " + updatedPlaylist.getId());
        }
    }

    public List<Playlist> findByTitle(String title){
        return playlistRepository.findByTitle(title);
    }

    public void deletePlaylistById(long id){
        playlistRepository.deletePlaylistById(id);
    }

    public Playlist findById(long id){
        return playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist by id " + id + " was not found"));
    }

    public Playlist addTrackToPlaylist(Playlist playlist, Track track) {
        playlist.getTracks().add(track);
        return playlistRepository.save(playlist);
    }

    public Playlist removeTrackFromPlaylist(Playlist playlist, Track track) {
        playlist.getTracks().remove(track);
        return playlistRepository.save(playlist);
    }
}
