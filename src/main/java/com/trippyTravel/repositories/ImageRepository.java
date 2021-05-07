package com.trippyTravel.repositories;

import com.trippyTravel.models.Image;
import com.trippyTravel.models.Trip;
import com.trippyTravel.models.User;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("select i from Image i, Trip t, Group g where i.trip=t AND t.group=g AND g.id=?1 order by i.id DESC")
    List<Image> getGroupImages(long id);


    @Query("select i from Image i, Trip t, Group g, GroupMember gm, User u where i.trip=t AND t.group=g AND gm.group=g AND gm.member=u AND u=?1 order by i.id DESC")
    List<Image> findImagesByUser(User user);

    List<Image> findImagesByTripOrderByIdDesc(Trip trip);


}
