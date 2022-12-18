package toy.mapleStory.search.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import toy.mapleStory.search.vo.testVO;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class testService implements testServicei {

    @Autowired
    searchMapper searchMapper;

    @Override
    public void selectDBMethod(){
        System.out.println("select db");

        List<testVO> testTmp = searchMapper.totalList();
        System.out.println("--");
        for(int abc=0; abc<testTmp.size(); abc++){
            System.out.println(testTmp.get(abc).getIdx());
            System.out.println(testTmp.get(abc).getName());
            System.out.println(testTmp.get(abc).getUpdateDate());
            System.out.println(testTmp.get(abc).getFileLocation());
        }
        System.out.println("\nfin");

    }

    @Override
    public void insertDBMethod(String id){
        System.out.println("insert db");
        System.out.println(id);
        String[] idList = id.split(",");

        for (int x = 0; x < idList.length; x++) {
            testVO insertDBString = new testVO();

            Date now = new Date(); // 현재 날짜/시간
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); // 포맷팅 정의
            String formatNow = formatter.format(now); // 포맷팅 적용

            insertDBString.setName(idList[x]);
            insertDBString.setUpdateDate(formatNow);
            insertDBString.setFileLocation("/date/" + idList[x] + ".bin");

            searchMapper.insertList(insertDBString);
        }
        System.out.println("fin");
    }

    private String message = "";

    @Override
    public void excelDown(HttpServletResponse res){
        String fileName = "oo.xlsx";

        try {
            message = "ok";

            FileInputStream file = new FileInputStream("./excel/cellTest.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            int rowNo = 0;
            int cellIndex = 0;

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println(rows);

            res.setContentType("ms-vns/excel");
            // 엑셀 파일명 설정
            res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            workbook.write(res.getOutputStream());
            workbook.close();
        }
        catch (Exception e){
            System.out.println("err" + e.getMessage());
            message = "error";
        }

        //return message;
    }

    @Override
    public void saveBinFile(JSONObject j) throws IOException {
        System.out.println("save bin file");

        try{
            System.out.println("check1");
            String fileName = "data/명회.bin";
            String testString = "test 제발되라";

            File jf = new File(fileName);

            BufferedWriter writer = new BufferedWriter(new FileWriter(jf));
            writer.write(j.toString());
            writer.close();

        }
        catch (IOException e){

        }

        System.out.println("파일 출력 완료!");
    }

    @Override
    public JSONObject infoList() throws InterruptedException{
        JSONObject j = new JSONObject();
        String[] id = {"명회"};

        for (String name : id) {

            final String Url = "https://maplestory.nexon.com/Ranking/World/Total?c=" + name;
            Connection conn = Jsoup.connect(Url);

            // 이름, 직업, 레벨, 경험치, 인기도, 길드
            try {
                Document document = conn.get();
                Elements ele = document.getElementsByClass("search_com_chk");

                String txt = ele.text();

                if (txt.length() > 0) {
                    String img = ele.select("img").get(3).attr("src");
                    String job = txt.substring(txt.indexOf(name) + name.length() + 1, txt.indexOf("Lv"));
                    String lv = txt.substring(txt.indexOf("Lv") + 3).split(" ")[0];

                    String[] var = txt.substring(txt.indexOf("Lv")).split(" ");
                    String exp = var[1];
                    String famous = var[2];
                    String guild = (var.length == 3) ? "-" : var[3];

                    String link = ele.select("a").attr("href");

                    j.put("chk", "y");
                    j.put("img", img);
                    j.put("id", name);
                    j.put("job", job);
                    j.put("lv", lv);
                    j.put("exp", exp);
                    j.put("famous", famous);
                    j.put("guild", guild);
                    j.put("detailLink", link);
                } else {
                    j.put("chk", "n");
                    j.put("img", "-");
                    j.put("id", name);
                    j.put("job", "-");
                    j.put("lv", "-");
                    j.put("exp", "-");
                    j.put("famous", "-");
                    j.put("guild", "-");
                    j.put("detailLink", "-");
                }

                j.put("mapleMoney", "-");
                j.put("inventoryLink", "-");
                j.put("storageLink", "-");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(j);
        System.out.println("파일 출력 완료!");
        return j;
    }
}
