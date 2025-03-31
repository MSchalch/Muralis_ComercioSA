package br.appComercioSA.appcomerciosa.controller;

import br.appComercioSA.appcomerciosa.model.Cliente;
import br.appComercioSA.appcomerciosa.model.Contato;
import br.appComercioSA.appcomerciosa.repository.ClienteRepository;
import br.appComercioSA.appcomerciosa.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    private static final Logger logger = Logger.getLogger(ClienteController.class.getName());

    @GetMapping("/informacoes")
    public String informacoesCliente(HttpSession session, Model model) {
        Cliente clienteLogado = (Cliente) session.getAttribute("clienteLogado");
        
        if (clienteLogado == null) {
            logger.warning("Cliente não encontrado na sessão. Redirecionando para login.");
            return "redirect:/login";
        }
        
        logger.info("Cliente encontrado na sessão: " + clienteLogado.getNome());

        List<Contato> contatos = contatoRepository.findByClienteId(clienteLogado.getId());
        model.addAttribute("cliente", clienteLogado);
        model.addAttribute("contatos", contatos);
        
        return "informacoes_cliente";
    }

    @GetMapping("/editar")
    public String editarCliente(HttpSession session, Model model) {
        Cliente clienteLogado = (Cliente) session.getAttribute("clienteLogado");
        if (clienteLogado == null) {
            logger.warning("Tentativa de edição sem cliente logado. Redirecionando para login.");
            return "redirect:/login";
        }

        List<Contato> contatos = contatoRepository.findByClienteId(clienteLogado.getId());
        model.addAttribute("cliente", clienteLogado);
        model.addAttribute("contatos", contatos);
        
        return "editar_cliente";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, HttpSession session) {
        Cliente clienteLogado = (Cliente) session.getAttribute("clienteLogado");
        if (clienteLogado == null) {
            logger.warning("Tentativa de salvar dados sem cliente logado. Redirecionando para login.");
            return "redirect:/login";
        }
        
        cliente.setId(clienteLogado.getId());
        cliente.setSenha(clienteLogado.getSenha()); // Mantém a senha existente
        clienteRepository.save(cliente);
        session.setAttribute("clienteLogado", cliente);
        
        return "redirect:/cliente/informacoes";
    }

    @PostMapping("/deletar")
    public String deletarCliente(HttpSession session) {
        Cliente clienteLogado = (Cliente) session.getAttribute("clienteLogado");
        if (clienteLogado == null) {
            logger.warning("Tentativa de exclusão sem cliente logado. Redirecionando para login.");
            return "redirect:/login";
        }
        
        contatoRepository.deleteByClienteId(clienteLogado.getId());
        clienteRepository.deleteById(clienteLogado.getId());
        session.invalidate();
        
        return "redirect:/login";
    }
}

