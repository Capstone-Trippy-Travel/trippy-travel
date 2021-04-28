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

//    @Query("select i from images i, Trip p, Group g where  ")
//    List<Image> getTripImages();
}
