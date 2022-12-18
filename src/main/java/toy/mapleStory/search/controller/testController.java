package toy.mapleStory.search.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import toy.mapleStory.search.service.searchInfoService;
import toy.mapleStory.search.service.testService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class testController {

    @PostMapping(value="/searchInfoTest")
    public String searchInfoTest(Model model, @RequestParam("showId2") String id) {

        System.out.println("요청 도착");
        System.out.println(id);

        model.addAttribute("id", id);

        return "/searchInfoTest";
    }

    @ResponseBody
    @GetMapping(value="/callTest")
    public String CallTest(Model model, @RequestParam("id") String id) {
        String tmp = id + " 데이터입니다\n";

        try {
            // delay 5 seconds
            Thread.sleep(100);

            System.out.println("요청 도착!!");
            System.out.println(id);

            model.addAttribute("test", tmp);

        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println("id fin :"+ id);
        model.addAttribute("addTest",tmp);

        System.out.println("---");
        System.out.println("되라되라");

        return tmp;
    }

    @GetMapping(value="/callTest1")
    public String CallTest1(Model model, @RequestParam("id") String id) {
        String tmp = id + " 데이터입니다\n";

        try {
            // delay 5 seconds
            Thread.sleep(100);

            System.out.println("요청 도착!!");
            System.out.println(id);

            model.addAttribute("test", tmp);

        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println("id fin :"+ id);
        model.addAttribute("addTest",tmp);

        System.out.println("---");
        System.out.println("되라되라");

        return tmp;
    }

    @Autowired
    private testService info;
    private String message = "";

    @Autowired
    private searchInfoService searchService;

    //@ResponseBody
    @GetMapping(value="/fileTest")
    public void excelDown(Model model,
                          @RequestParam("abcid") String id,
                          HttpServletResponse res) throws IOException, InterruptedException {
        System.out.println(id);

//        List<searchVO> characterInfo = searchService.infoList("명회");
//        searchService.subInfoList(characterInfo);
//        Long totalMoney = searchService.calcTotalMoney(characterInfo);
//        List<Map> itemMapList = searchService.itemInfoList(characterInfo);
//        System.out.println(characterInfo);
//        System.out.println(totalMoney);
//        System.out.println(itemMapList);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString1 = mapper.writeValueAsString(characterInfo);
//        String jsonString2 = mapper.writeValueAsString(itemMapList);

        //info.saveBinFile(characterInfo, itemMapList, id);
        JSONObject j = new JSONObject();
        j = info.infoList();
        info.saveBinFile(j);

        System.out.println("FIN");
    }

    @ResponseBody
    @GetMapping(value="/dbTest")
    public void insertDBTest(@RequestParam("dbTest") String id){
        System.out.println(id);
        info.selectDBMethod();
        System.out.println("select fin");
        info.insertDBMethod(id);
        System.out.println("insert fin");
        info.selectDBMethod();
        System.out.println("select fin");

    }
}
