/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class VideoController {
	@Autowired
	private VideoRepository repository;

	public static final String TITLE_PARAMETER = "title";
	public static final String DURATION_PARAMETER = "duration";
	public static final String TOKEN_PATH = "/oauth/token";
	public static final String VIDEO_SVC_PATH = "/video";
	public static final String PARAM_ID = "/{id}";
	private static final String LIKE_PATH = PARAM_ID+"/like";
	private static final String UNLIKE_PATH = PARAM_ID+"/unlike";
	public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByName";
	public static final String VIDEO_DURATION_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByDurationLessThan";

	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo (@RequestBody Video video){
		repository.save(video);
		return video;
	}

	@RequestMapping(value=VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList(){
		Collection<Video> videos = new ArrayList<>();
		repository.findAll().forEach(videos::add);
		return videos;
	}

	@RequestMapping(value=VIDEO_SVC_PATH + LIKE_PATH, method = RequestMethod.POST)
	public void likeVideo(@PathVariable("id") long id){
		registerVideoInteraction(id, true);
	}

	@RequestMapping(value=VIDEO_SVC_PATH + UNLIKE_PATH, method = RequestMethod.POST)
	public void unlikeVideo(@PathVariable("id") long id){
		registerVideoInteraction(id, false);
	}

	@RequestMapping(value=VIDEO_SVC_PATH + PARAM_ID, method = RequestMethod.GET)
	public @ResponseBody Video getVideoById(@PathVariable("id") long id){
		return repository.findOne(id);
	}

	private void registerVideoInteraction(long id, boolean isLike) {
		Video video = repository.findOne(id);
		long likes = video.getLikes();

		if (isLike) likes++;
		else likes--;

		video.setLikes(likes);
		repository.save(video);
	}
}
