package toy.mapleStory.imageRead.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

// 구현 완료되면 service에 메소드 추가
// controller 단에서 데이터 받아오기
public class imageReadServiceTess {

    public static void main(String[] args){

        Tesseract  inst = new Tesseract();
        //inst.setLanguage("eng");
        inst.setDatapath("tessdata");
        inst.setOcrEngineMode(10);

        //inst.setTessVariable("tessedit_char_whitelist", ",0123456789");

        for(int i=10; i<80; i++){
            File  imageFile = new File("./imgtest/flag_"+(i)+".png");

            try{
                String result = inst.doOCR(imageFile);
                System.out.println("flag_"+ i + " -> " + result);
            }
            catch(TesseractException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
