package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface VideoRepository extends CrudRepository<Video, Long> {

    Collection<Video> findByName(String title);

    Collection<Video> findByDurationLessThan(long duration);
}
