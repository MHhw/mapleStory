package toy.mapleStory.search.service;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface testServicei {
    public abstract void selectDBMethod();
    public abstract void insertDBMethod(String id);
    public abstract void excelDown(HttpServletResponse res);
    public JSONObject infoList() throws InterruptedException;
    public abstract void saveBinFile(JSONObject j) throws IOException;

}


