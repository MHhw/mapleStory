package toy.mapleStory.search.service;

import org.json.JSONObject;
import toy.mapleStory.search.vo.searchVO;

import java.util.List;

public interface searchInfoServiceI {
    public abstract List<JSONObject> infoList(String id) throws InterruptedException;
    public abstract List<JSONObject> subInfoList(List<JSONObject> characterInfo) throws InterruptedException;
    public abstract JSONObject itemInfoList(List<JSONObject> characterInfo) throws InterruptedException;
    public abstract String getStorageMoney(List<searchVO> characterInfo) throws InterruptedException;
    public abstract Long calcTotalMoney(List<JSONObject> characterInfo);

}
