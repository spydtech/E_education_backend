package com.Eonline.Education.modals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 5000, nullable = false)
    private String content;

    @Column(name = "posted_by", nullable = false)
    private String postedBY;

    // Single image (for backward compatibility)
    @Lob
    @Column(name = "img", columnDefinition = "LONGBLOB")
    private byte[] img;

    // Single video (for backward compatibility)
    @Lob
    @Column(name = "video", columnDefinition = "LONGBLOB")
    private byte[] video;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @ElementCollection
    @CollectionTable(name = "post_images", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private List<byte[]> images = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "post_videos", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "video_data", columnDefinition = "LONGBLOB")
    private List<byte[]> videos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor for text-only posts
    public Post(String name, String content, String postedBY) {
        this.name = name;
        this.content = content;
        this.postedBY = postedBY;
        this.createdAt = LocalDateTime.now();
    }

    // Pre-update hook
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}