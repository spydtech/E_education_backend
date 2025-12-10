package com.Eonline.Education.modals;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class SaveEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@OneToOne
	@JoinColumn
	private Post post;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public SaveEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SaveEntity(int id, Post post) {
		super();
		this.id = id;
		this.post = post;
	}
	

}
