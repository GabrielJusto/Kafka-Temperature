package br.com.bonatto.station.controller;

import br.com.bonatto.station.form.StationForm;
import br.com.bonatto.station.model.Station;
import br.com.bonatto.broker.repository.PointRepository;
import br.com.bonatto.broker.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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

    }
}
