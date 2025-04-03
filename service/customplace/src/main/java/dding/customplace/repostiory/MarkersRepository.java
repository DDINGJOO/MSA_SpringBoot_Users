package dding.customplace.repostiory;


import dding.customplace.entity.Markers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkersRepository extends JpaRepository<Markers, String> {

    List<Markers> findAllByUserId(String userId);
}
