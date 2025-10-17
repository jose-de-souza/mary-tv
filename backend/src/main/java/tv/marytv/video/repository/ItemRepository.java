package tv.marytv.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tv.marytv.video.entity.Category;
import tv.marytv.video.entity.Event;
import tv.marytv.video.entity.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.children LEFT JOIN FETCH i.parent WHERE i.parent IS NULL")
    List<Item> findAllWithChildren();

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.children WHERE i.id = :id")
    Optional<Item> findByIdWithChildren(@Param("id") Long id);

    List<Item> findByCategoryId(Long categoryId);

    List<Item> findByEventId(Long eventId);

    Optional<Item> findByVideoUrl(String videoUrl);

    @Query("SELECT i FROM Item i WHERE i.isNew = true")
    List<Item> findAllNewItems();

    @Query("SELECT i FROM Item i WHERE i.isHeadline = true")
    List<Item> findAllHeadlineItems();

    @Query("SELECT i FROM Item i LEFT JOIN i.category c LEFT JOIN i.event e WHERE i.title LIKE %:keyword% OR i.description LIKE %:keyword% OR c.name LIKE %:keyword% OR e.name LIKE %:keyword%")
    List<Item> searchByKeyword(@Param("keyword") String keyword);
}