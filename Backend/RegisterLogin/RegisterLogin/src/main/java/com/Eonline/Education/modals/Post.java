package com.Eonline.Education.modals;

import java.time.LocalDateTime;
import java.util.List;

import java.sql.Blob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String name;
	@Column(length=5000)
	private String content;
	private String postedBY;
//    @Lob
//    private Blob img;
	//private String img;
	@Lob
	private byte[] img;
	private LocalDateTime dateTime;
	private int likeCount;
	private int viewCount;
	private List<String> tags;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
//	public String getImg() {
//		return img;
//	}
//	public void setImg(String img) {
//		this.img = img;
//	}
	
	
	public String getPostedBY() {
		return postedBY;
	}
	public void setPostedBY(String postedBY) {
		this.postedBY = postedBY;
	}
	
//	public Blob getImg() {
//		return img;
//	}
//	public void setImg(Blob img) {
//		this.img = img;
//	

	
	public int getLikeCount() {
		return likeCount;
	}
	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	

}
