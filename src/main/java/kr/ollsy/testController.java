package kr.ollsy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testController {

    @GetMapping({"/test"})
    public String test() {
        return "test";
    }
}
