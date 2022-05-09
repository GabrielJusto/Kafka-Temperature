package br.com.bonatto.repository;

import br.com.bonatto.modelo.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>
{
}
