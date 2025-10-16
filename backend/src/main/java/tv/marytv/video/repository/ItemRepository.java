package tv.marytv.video.repository;

import tv.marytv.video.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.children WHERE i.parent IS NULL")
    Page<Item> findTopLevelItems(Pageable pageable);

    @Query("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.children WHERE i.parent IS NULL AND (:categoryId IS NULL OR i.category.id = :categoryId) AND (:eventId IS NULL OR i.event.id = :eventId) AND (:isNew IS NULL OR i.isNew = :isNew) AND (:isHeadline IS NULL OR i.isHeadline = :isHeadline)")
    Page<Item> findFilteredTopLevelItems(@Param("categoryId") Long categoryId, @Param("eventId") Long eventId, @Param("isNew") Boolean isNew, @Param("isHeadline") Boolean isHeadline, Pageable pageable);

    @Query("SELECT DISTINCT i FROM Item i LEFT JOIN FETCH i.children WHERE i.id = :id")
    Optional<Item> findByIdWithChildren(@Param("id") Long id);

    @Query("SELECT i FROM Item i WHERE i.parent.id = :parentId")
    List<Item> findChildrenByParentId(@Param("parentId") Long parentId);

    long countByParentId(Long parentId);
}