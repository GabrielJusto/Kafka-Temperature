package br.com.bonatto.controller;

import br.com.bonatto.dto.StationDto;
import br.com.bonatto.form.StationForm;
import br.com.bonatto.modelo.Station;
import br.com.bonatto.repository.PointRepository;
import br.com.bonatto.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/stations")
public class StationController
{
    @Autowired
    private StationRepository repository;

    @Autowired
    private PointRepository pointRepository;

    @PostMapping
    public void newStation(@RequestBody StationForm form, UriComponentsBuilder uriBuilder)
    {
        Station station = form.convert();
        pointRepository.save(station.getLocal());
        repository.save(station);


//        URI uri = uriBuilder.path("/gagaga/{id}").buildAndExpand(station.getId()).toUri();
//        return ResponseEntity.created(uri).body(new StationDto(station));

    }
}
