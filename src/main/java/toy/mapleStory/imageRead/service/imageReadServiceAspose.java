package toy.mapleStory.imageRead.service;

import com.aspose.ocr.AsposeOCR;

import java.io.File;
import java.io.IOException;

public class imageReadServiceAspose {
    public static void main(String[] args) {
        //API 인스턴스 생성
        AsposeOCR api = new AsposeOCR("123456789");

        // 이미지 경로
        for (int i = 10; i < 50; i++) {
            File imageFile = new File("./imgtest/flag_" + (i) + ".png");

            // 파일의 전체 경로로 페이지 인식
            try {
                //String result = api.RecognizePage(imageFile);
                String result = api.RecognizePage("./imgtest/flag_" + (i) + ".png");

                System.out.println("flag_"+i+" -> Result: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
