package br.appComercioSA.appcomerciosa.repository;

import br.appComercioSA.appcomerciosa.model.Contato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
    List<Contato> findByClienteId(Long clienteId);
    void deleteByClienteId(Long clienteId);
}