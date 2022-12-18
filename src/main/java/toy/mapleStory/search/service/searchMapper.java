package toy.mapleStory.search.service;

import org.apache.ibatis.annotations.Mapper;
import toy.mapleStory.search.vo.testVO;

import java.util.List;

@Mapper
public interface searchMapper {
    public List<testVO> totalList();
    public void insertList(testVO testVO);
}
