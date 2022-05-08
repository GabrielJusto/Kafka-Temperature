package br.com.bonatto.repository;

import br.com.bonatto.dto.StationDto;
import br.com.bonatto.modelo.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long>
{
}
