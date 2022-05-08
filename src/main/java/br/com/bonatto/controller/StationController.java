package br.com.bonatto.controller;

import br.com.bonatto.dto.StationDto;
import br.com.bonatto.form.StationForm;
import br.com.bonatto.modelo.Station;
import br.com.bonatto.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/station")
public class StationController
{
    @Autowired
    private StationRepository repository;


    @PostMapping
    public ResponseEntity<StationDto> newStation(@RequestBody @Validated StationForm form, UriComponentsBuilder uriBuilder)
    {
        Station station = form.convert();
        repository.save(station);

        System.out.println("Cheguei aqui!!");


        return null;

    }
}
