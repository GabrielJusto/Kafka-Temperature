package br.com.bonatto.broker.repository;

import br.com.bonatto.broker.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long>
{

}
