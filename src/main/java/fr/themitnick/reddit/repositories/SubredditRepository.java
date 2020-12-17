package fr.themitnick.reddit.repositories;

import fr.themitnick.reddit.models.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<SubReddit, Long> {
}
