package tv.marytv.video.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String iconUrl;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "item_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemDate;

    @Column(name = "is_new")
    private boolean isNew = false;

    @Column(name = "is_headline")
    private boolean isHeadline = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType = ContentType.STANDALONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Item parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> children = List.of();

    public enum ContentType {
        SERIES_HEADER, STANDALONE, EPISODE
    }
}