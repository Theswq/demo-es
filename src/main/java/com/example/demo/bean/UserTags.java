package com.example.demo.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.example.demo.converter.TagTypeConverter;
import lombok.Data;

import java.util.List;

@Data
public class UserTags {
    @ExcelProperty("tagId")
    private String tagId;
    @ExcelProperty("parentId")
    private String parentId;
    @ExcelProperty("parentName")
    private String parentName;
    @ExcelProperty("tag")
    private String tag;
    @ExcelProperty("tagDescription")
    private String tagDescription;
    @ExcelProperty(value = "tagType" ,converter = TagTypeConverter.class)
    private String tagType;
    @ExcelProperty("tagsNumber")
    private int tagsNumber;
    @ExcelProperty("userNumber")
    private int userNumber;
    @ExcelProperty("tagParent")
    private String tagParent;
    @ExcelProperty("sortNumber")
    private int sortNumber;

}
