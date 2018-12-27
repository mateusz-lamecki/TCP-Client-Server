package com.skdwa.tcp.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Post {
	private String topic;
	private String content;
}
