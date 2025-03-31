package br.appComercioSA.appcomerciosa.repository;

import br.appComercioSA.appcomerciosa.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    @Query("SELECT c FROM Cliente c WHERE c.cpf = :cpf AND c.senha = :senha")
    Cliente findByCpfAndSenha(@Param("cpf") String cpf, @Param("senha") String senha);
    
    Cliente findByCpf(String cpf);
}