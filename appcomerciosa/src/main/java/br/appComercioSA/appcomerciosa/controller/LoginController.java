package br.appComercioSA.appcomerciosa.controller;

import br.appComercioSA.appcomerciosa.model.Cliente;
import br.appComercioSA.appcomerciosa.model.Contato;
import br.appComercioSA.appcomerciosa.repository.ClienteRepository;
import br.appComercioSA.appcomerciosa.repository.ContatoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        String mensagemSucesso = (String) session.getAttribute("mensagemSucesso");
        model.addAttribute("mensagemSucesso", mensagemSucesso);
        session.removeAttribute("mensagemSucesso");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "login";
    }

    @PostMapping("/logar")
    public String loginCliente(@ModelAttribute Cliente cliente, Model model, HttpSession session) {
        Cliente clienteLogado = clienteRepository.findByCpfAndSenha(cliente.getCpf(), cliente.getSenha());

        if (clienteLogado != null) {
            session.setAttribute("clienteLogado", clienteLogado);
            System.out.println("Cliente armazenado na sessão: " + clienteLogado.getNome()); // Log para depuração
            return "redirect:/";
        }

        model.addAttribute("erro", "CPF ou senha inválidos");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastroCliente(@Valid @ModelAttribute Cliente cliente,
                                  BindingResult result,
                                  @RequestParam String tipoContato,
                                  @RequestParam String valorContato,
                                  @RequestParam String observacaoContato,
                                  Model model) {

        if (result.hasErrors()) {
            return "cadastro";
        }

        if (clienteRepository.findByCpf(cliente.getCpf()) != null) {
            model.addAttribute("erro", "CPF já cadastrado");
            return "cadastro";
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);

        Contato contato = new Contato();
        contato.setTipo(tipoContato);
        contato.setValor(valorContato);
        contato.setObservacao(observacaoContato);
        contato.setCliente(clienteSalvo);

        contatoRepository.save(contato);

        return "redirect:/login";
    }
}