package br.com.bonatto.controller;

import br.com.bonatto.form.ClientForm;
import br.com.bonatto.form.StationForm;
import br.com.bonatto.modelo.Client;
import br.com.bonatto.modelo.Station;
import br.com.bonatto.repository.ClientRepository;
import br.com.bonatto.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/clients")
public class ClientController
{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PointRepository pointRepository;

    public void newClient(@RequestBody ClientForm form, UriComponentsBuilder uriBuilder)
    {
        Client client = form.convert();
        pointRepository.save(client.getLocal());
        clientRepository.save(client);


//        URI uri = uriBuilder.path("/gagaga/{id}").buildAndExpand(station.getId()).toUri();
//        return ResponseEntity.created(uri).body(new StationDto(station));

    }

}
