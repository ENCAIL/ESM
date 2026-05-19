package kr.hs.dgsw.esm.domain.category.repository;

import kr.hs.dgsw.esm.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
