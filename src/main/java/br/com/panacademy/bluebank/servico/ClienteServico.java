package br.com.panacademy.bluebank.servico;

import br.com.panacademy.bluebank.dto.cliente.AtualizarClienteDTO;
import br.com.panacademy.bluebank.dto.cliente.AtualizarCredenciaisClienteDTO;
import br.com.panacademy.bluebank.dto.cliente.ClienteDTO;
import br.com.panacademy.bluebank.excecao.RecursoNaoEncontradoException;
import br.com.panacademy.bluebank.modelo.Cliente;
import br.com.panacademy.bluebank.modelo.Conta;
import br.com.panacademy.bluebank.repositorio.ClienteRepositorio;
import br.com.panacademy.bluebank.repositorio.ContaRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServico {

    private final ClienteRepositorio clienteRepositorio;
    private final ContaRepositorio contaRepositorio;

    public ClienteServico(ClienteRepositorio clienteRepositorio, ContaRepositorio contaRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
        this.contaRepositorio = contaRepositorio;
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        List<Cliente> listaClientes = clienteRepositorio.findAll();
        return listaClientes.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO filtrarPorId(Long id) {
        Cliente cliente = clienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: "+id));
        return new ClienteDTO(cliente);
    }

    @Transactional(readOnly = true)
    Cliente filtrarClientePorContaId(Long contaId){
        return clienteRepositorio.findByClienteByContaId(contaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente de conta id: "+ contaId +" não encontrado."));
    }

    @Transactional
    public ClienteDTO salvarCliente(Cliente cliente) {
        if(cliente.getConta() == null){
            Conta conta = new Conta();
            conta.setSaldo(0.0);
            Conta contaSalva = contaRepositorio.save(conta);
            cliente.setConta(contaSalva);
        }

        ClienteDTO clienteDTO = new ClienteDTO();
        BeanUtils.copyProperties(cliente, clienteDTO);

        clienteRepositorio.save(cliente);
        return new ClienteDTO(cliente);
    }

    public void deletarCliente(Long id) {
        try {
            clienteRepositorio.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoNaoEncontradoException("Cliente não encontrado " + id);
        }
    }

    @Transactional
    public AtualizarCredenciaisClienteDTO atualizarCredenciaisCliente(Long id, AtualizarCredenciaisClienteDTO dto) {
        Cliente entidade = clienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente de id: " + id + " não encontrado"));

        BeanUtils.copyProperties(dto, entidade);

        entidade = clienteRepositorio.save(entidade);
        return new AtualizarCredenciaisClienteDTO(entidade);
    }


    @Transactional
    public AtualizarClienteDTO atualizarCliente(Long id, AtualizarClienteDTO dto) {
        Cliente entidade = clienteRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));

        BeanUtils.copyProperties(dto, entidade);

        entidade = clienteRepositorio.save(entidade);
        return new AtualizarClienteDTO(entidade);
    }
}