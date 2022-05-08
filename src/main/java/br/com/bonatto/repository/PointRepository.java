package br.com.bonatto.repository;

import br.com.bonatto.modelo.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long>
{

}
