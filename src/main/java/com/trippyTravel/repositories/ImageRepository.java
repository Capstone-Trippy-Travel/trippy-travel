package com.trippyTravel.repositories;

import com.trippyTravel.models.Image;
import com.trippyTravel.models.Trip;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

}
