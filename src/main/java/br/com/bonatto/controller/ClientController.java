package br.com.bonatto.controller;

import br.com.bonatto.form.ClientForm;
import br.com.bonatto.form.StationForm;
import br.com.bonatto.modelo.Client;
import br.com.bonatto.modelo.Station;
import br.com.bonatto.repository.ClientRepository;
import br.com.bonatto.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public void newClient(@RequestBody ClientForm form, UriComponentsBuilder uriBuilder)
    {
        Client client = form.convert();
        System.out.println(form.getLat());
        pointRepository.save(client.getLocal());
        System.out.println(client.getLocal().getLat());
        System.out.println(client.getLocal().getId());

        clientRepository.save(client);


//        URI uri = uriBuilder.path("/gagaga/{id}").buildAndExpand(station.getId()).toUri();
//        return ResponseEntity.created(uri).body(new StationDto(station));

    }

}
