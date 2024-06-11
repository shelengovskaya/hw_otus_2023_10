package ru.otus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientDTO;
import ru.otus.dto.mapper.ClientMapper;
import ru.otus.service.DBServiceClient;

@Controller
@RequestMapping("/clients")
public class ClientRestController {

    private final DBServiceClient dbServiceClient;
    private final ClientMapper clientMapper;

    public ClientRestController(DBServiceClient dbServiceClient, ClientMapper clientMapper) {
        this.dbServiceClient = dbServiceClient;
        this.clientMapper = clientMapper;
    }

    @GetMapping
    public void getClients(Model model) {
        model.addAttribute("clients",
                dbServiceClient.findAll().stream()
                        .map(clientMapper::toClientDTO).toList()
        );
    }

    @PostMapping
    public RedirectView saveClient(@ModelAttribute ClientDTO clientDTO) {
        dbServiceClient.saveClient(clientMapper.toClient(clientDTO));
        return new RedirectView("/clients");
    }
}
