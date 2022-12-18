package toy.mapleStory.imageRead.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class imageReadController {

    @GetMapping(value = "/image")
    public String imgExcel(){
        String tmp = "";

        return tmp;
    }
}
