package br.com.panacademy.bluebank.repositorio;

import br.com.panacademy.bluebank.modelo.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepositorio extends JpaRepository<Transacao, Long> {


}
