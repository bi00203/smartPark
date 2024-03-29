package com.example.smartparkpj.mapper;

import com.example.smartparkpj.domain.InquiryVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface InquiryMapper {

    List<InquiryVO> selectAll(int mno);

    InquiryVO selectOne(int ino);

    InquiryVO selectGetRead(int ino, int mno);

    List<Date> selectFormattedDate();

    void addInquiry(InquiryVO inquiryVO);

    void updateInquiry(InquiryVO inquiryVO);

    void adminAnswer(InquiryVO inquiryVO);

    void deleteInquiry(int ino);

    List<InquiryVO> adminInquiryList();

    void adminInquiryDelete(int ino);




}
