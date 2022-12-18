package toy.mapleStory.search.service;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import toy.mapleStory.search.vo.searchVO;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class searchInfoService implements searchInfoServiceI{

    @Override
    public List<JSONObject> infoList(String id) throws InterruptedException {

        List<JSONObject> jsonList = new ArrayList<JSONObject>();

        String[] characterName = id.replace("입력받은 ID 리스트\r\n\r\n-", "")
                                    .replace("\r\n", "")
                                    .replace(" ", "")
                                    .split("-");

        for (String name : characterName) {
            JSONObject j = new JSONObject();

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

            jsonList.add(j);
        }

        System.out.println("---");
        System.out.println(Arrays.toString(characterName));
        System.out.println("---");

        return jsonList;
    }

    @Override
    public List<JSONObject> subInfoList(List<JSONObject> characterInfo) throws InterruptedException {

        //for (toy.mapleStory.search.vo.searchVO searchVO : characterInfo) {
        for(int x=0; x<characterInfo.size(); x++){
            JSONObject littleJson = characterInfo.get(x);

            if (littleJson.get("chk").equals("y")){
                final String Url = "https://maplestory.nexon.com" + littleJson.get("detailLink");
                Connection conn = Jsoup.connect(Url);

                // 메소, 정보 페이지 링크
                try {
                    Document document = conn.get();

                    if (document.getElementsByClass("private2").size() == 1) {
                        System.out.println("정보가 없습니다");
                        littleJson.put("chk", "n");
                        littleJson.put("mapleMoney", "-");
                        littleJson.put("inventoryLink", "-");
                        littleJson.put("storageLink", "-");
                        continue;
                    }

                    String mapleMoney = document.getElementsByClass("table_style01").get(0).select("td").get(4).text();
                    String sideLinkInventory = document.getElementsByClass("lnb_list").get(0).select("a").get(3).attr("href");
                    String sideLinkStorage = document.getElementsByClass("lnb_list").get(0).select("a").get(4).attr("href");

                    littleJson.put("mapleMoney", mapleMoney);
                    littleJson.put("inventoryLink", sideLinkInventory);
                    littleJson.put("storageLink", sideLinkStorage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return characterInfo;
    }

    @Override
    public JSONObject itemInfoList(List<JSONObject> characterInfo) throws InterruptedException {

        //List<JSONObject> jsonList = new ArrayList<JSONObject>();
        JSONObject jsonList = new JSONObject();

        // 캐릭터 인벤토리 조회
        for(int x=0; x<characterInfo.size(); x++) {
            JSONObject j = characterInfo.get(x);
            System.out.println(x + " : start");
            if(j.get("chk").equals("y")) {
                final String Url1 = "https://maplestory.nexon.com" + j.get("inventoryLink");
                Connection conn = Jsoup.connect(Url1);
                JSONObject allJson = new JSONObject();

                try {
                    Document document = conn.get();

                    Elements e1 = document.getElementsByClass("tab01_con_wrap"); // 장비
                    Elements e2 = document.getElementsByClass("tab02_con_wrap"); // 소비
                    Elements e3 = document.getElementsByClass("tab03_con_wrap"); // 기타
                    Elements e4 = document.getElementsByClass("tab04_con_wrap"); // 설치
                    Elements e5 = document.getElementsByClass("tab05_con_wrap"); // 캐시

                    JSONObject e1Img = new JSONObject();
                    JSONObject e2Img = new JSONObject();
                    JSONObject e3Img = new JSONObject();
                    JSONObject e4Img = new JSONObject();
                    JSONObject e5Img = new JSONObject();

                    JSONObject e1Item = new JSONObject();
                    JSONObject e2Item = new JSONObject();
                    JSONObject e3Item = new JSONObject();
                    JSONObject e4Item = new JSONObject();
                    JSONObject e5Item = new JSONObject();

                    int e1Size = e1.select("li").select("a").size();
                    int e2Size = e2.select("li").select("a").size();
                    int e3Size = e3.select("li").select("a").size();
                    int e4Size = e4.select("li").select("a").size();
                    int e5Size = e5.select("li").select("a").size();

                    /* 장비 list */
                    for(int e1Index=0; e1Index<e1Size/2;e1Index++) {
                        String idx = String.valueOf(e1Index);
                        String img = e1.select("li").select("a").get(calc(e1Index,2,'*')).select("img").attr("src");
                        String txt = e1.select("li").select("a").get(calc(calc(e1Index,2,'*'),1,'+')).text();

                        e1Img.put(idx, img);
                        e1Item.put(idx, txt);
                    }

                    /* 소비 list */
                    for(int e2Index=0; e2Index<e2Size/2;e2Index++) {
                        String idx = String.valueOf(e2Index);
                        String img = e2.select("li").select("a").get(calc(e2Index,2,'*')).select("img").attr("src");
                        String txt = e2.select("li").select("a").get(calc(calc(e2Index,2,'*'),1,'+')).text();

                        e2Img.put(idx, img);
                        e2Item.put(idx, txt);
                    }

                    /* 기타 list */
                    for(int e3Index=0; e3Index<e3Size/2;e3Index++) {
                        String idx = String.valueOf(e3Index);
                        String img = e3.select("li").select("a").get(calc(e3Index,2,'*')).select("img").attr("src");
                        String txt = e3.select("li").select("a").get(calc(calc(e3Index,2,'*'),1,'+')).text();

                        e3Img.put(idx, img);
                        e3Item.put(idx, txt);
                    }

                    /* 설치 list */
                    for(int e4Index=0; e4Index<e4Size/2;e4Index++) {
                        String idx = String.valueOf(e4Index);
                        String img = e4.select("li").select("a").get(calc(e4Index,2,'*')).select("img").attr("src");
                        String txt = e4.select("li").select("a").get(calc(calc(e4Index,2,'*'),1,'+')).text();

                        e4Img.put(idx, img);
                        e4Item.put(idx, txt);
                    }

                    /* 캐시 */
                    for(int e5Index=0; e5Index<e5Size/2;e5Index++) {
                        String idx = String.valueOf(e5Index);
                        String img = e5.select("li").select("a").get(calc(e5Index,2,'*')).select("img").attr("src");
                        String txt = e5.select("li").select("a").get(calc(calc(e5Index,2,'*'),1,'+')).text();

                        e5Img.put(idx, img);
                        e5Item.put(idx, txt);
                    }

//                    jsonList.add(e1Item);
//                    jsonList.add(e1Img);
//
//                    jsonList.add(e2Item);
//                    jsonList.add(e2Img);
//
//                    jsonList.add(e3Item);
//                    jsonList.add(e3Img);
//
//                    jsonList.add(e4Item);
//                    jsonList.add(e4Img);
//
//                    jsonList.add(e5Item);
//                    jsonList.add(e5Img);

                    allJson.put("Item1", e1Item);
                    allJson.put("Img1", e1Img);

                    allJson.put("Item2", e2Item);
                    allJson.put("Img2", e2Img);

                    allJson.put("Item3", e3Item);
                    allJson.put("Img3", e3Img);

                    allJson.put("Item4", e4Item);
                    allJson.put("Img4", e4Img);

                    allJson.put("Item5", e5Item);
                    allJson.put("Img5", e5Img);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("ㅇㅇㅇㅇㅇㅇㅇ");
                System.out.println(j.get("id").toString());
                System.out.println("ㅇㅇㅇㅇㅇㅇㅇ");
                jsonList.put(j.get("id").toString(),allJson);
            }
            System.out.println(x + " : fin\n");
            TimeUnit.MILLISECONDS.sleep(300);
        }

        return jsonList;
    }

    @Override
    public String getStorageMoney(List<searchVO> characterInfo) throws InterruptedException {

        String storageMoney = "-";

        for(int i=0; i<characterInfo.size(); i++) {

            if (!characterInfo.get(i).getStorageLink().equals("-")) {

                final String Url = "https://maplestory.nexon.com" + characterInfo.get(i).getStorageLink();
                Connection conn = Jsoup.connect(Url);

                // 창고 메소
                try {
                    Document document = conn.get();

                    storageMoney = document.getElementsByClass("money_txt").text();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("i :" + i);
                break;
            }

            TimeUnit.MILLISECONDS.sleep(100);
            System.out.println(i + "fin");
        }

        return storageMoney;
    }

    @Override
    public Long calcTotalMoney(List<JSONObject> characterInfo){
        Long totalMoney = 0L;

        System.out.println("돈");
        for(int x=0; x<characterInfo.size(); x++){
            JSONObject littleJson = characterInfo.get(x);
            if (littleJson.get("chk").equals("y")) {
                String tmp = littleJson.get("mapleMoney").toString().replace(",", "");
                System.out.println(littleJson.get("mapleMoney"));
                System.out.println(tmp);
                totalMoney += Long.parseLong(tmp);
            }
        }
        System.out.println(totalMoney);
        System.out.println("돈!");

        return totalMoney;
    }

    public static int calc(int x, int y, char opr) {

        if (opr == '+') {
            return x+y;
        }
        else if (opr == '-') {
            return x-y;
        }
        else if (opr == '*') {
            return x*y;
        }
        else if (opr == '/') {
            return x/y;
        }
        else {
            return 0;
        }

    }
}
