package br.com.bonatto.ev_driver.controller;

import br.com.bonatto.ev_driver.form.ClientForm;
import br.com.bonatto.ev_driver.model.Client;
import br.com.bonatto.broker.repository.ClientRepository;
import br.com.bonatto.broker.repository.PointRepository;
import br.com.bonatto.ev_driver.producer.ClientRegister;
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
    private PointRepository pointRepository;

    @PostMapping
    public void newClient(@RequestBody ClientForm form, UriComponentsBuilder uriBuilder)
    {
        Client client = form.convert();
        pointRepository.save(client.getLocal());

        ClientRegister clientRegister = new ClientRegister(form);
        clientRegister.sendRegister();


    }

}
