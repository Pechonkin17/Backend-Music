package kn314.model;

import kn314.model.enums.Genre;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    long id;

    @Column(name = "title", length = 25, nullable = false)
    String title;

    @Column(name = "artist", length = 25, nullable = false)
    String artist;

    @Column(name = "genre", columnDefinition = "ENUM('OTHER') DEFAULT 'OTHER'")
    Genre genre;

    @Column(name = "duration")
    int duration;

    @Column(name = "description")
    String description;

    @Column(name = "file_path")
    String filePath;

    @Column(name = "created_at")
    LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToMany(mappedBy = "tracks")
    List<Playlist> playlists;
}