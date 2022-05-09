package br.com.bonatto.broker.repository;

import br.com.bonatto.station.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long>
{
}
