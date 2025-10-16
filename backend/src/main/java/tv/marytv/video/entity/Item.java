package tv.marytv.video.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "item_date")
    private Date itemDate;

    @Column(name = "is_new")
    private boolean isNew = true;

    @Column(name = "is_headline")
    private boolean isHeadline = false;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Item parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("item-children")
    @JsonIgnoreProperties("parent")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Item> children;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt = new Date();
}