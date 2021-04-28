package com.trippyTravel.repositories;

import com.trippyTravel.models.Image;
import com.trippyTravel.models.Trip;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("select i from Image i, Trip t, Group g where i.trip=t AND t.group=g AND g.id=?1 ")
    List<Image> getGroupImages(long id);
}
