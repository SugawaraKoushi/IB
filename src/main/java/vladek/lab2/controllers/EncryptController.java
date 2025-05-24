package vladek.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vladek.lab2.services.IEncryptService;

@RestController
@RequestMapping("api/lab1/encrypt")
public class EncryptController {
    @Autowired
    private IEncryptService encryptService;
}
