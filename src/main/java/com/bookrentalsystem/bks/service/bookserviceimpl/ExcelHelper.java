package com.bookrentalsystem.bks.service.bookserviceimpl;

import com.bookrentalsystem.bks.dto.book.BookRequest;
import lombok.NonNull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    //check if the file is excel type or not
    public static boolean checkExcelFormat(MultipartFile multipartFile){
      String contentType =  multipartFile.getContentType();
      if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
          return true;
      }
          return false;
    }

    //convert excel to list of Book
    public static List<BookRequest> convertToListOfBookRequest(InputStream inputStream){
        List<BookRequest> list = new ArrayList<>();

        try {

           XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
           XSSFSheet sheet = workBook.getSheet("book");

           int rowNum = 0;
           //iterat through row
         Iterator<Row> iterator= sheet.iterator();

         while (iterator.hasNext()){
             Row row = iterator.next();
             //skip first row header row
             if(rowNum == 0){
                 rowNum++;
                 continue;
             }

             Iterator<Cell> cells =  row.iterator();

             int cid=0;

             BookRequest bookRequest = new BookRequest();

             while (cells.hasNext()){
                 Cell cell = cells.next();

                 switch (cid){
                     case 0:
                        bookRequest.setName(cell.getStringCellValue());
                        break;
                     case 1:
                         bookRequest.setPage((int)cell.getNumericCellValue());
                         break;
                     case 2:
                         bookRequest.setIsbn(cell.getStringCellValue());
                         break;
                     case 3:
                         bookRequest.setRating(cell.getNumericCellValue());
                         break;
                     case 4:
                         bookRequest.setStock((int)cell.getNumericCellValue());
                         break;
                     case 5:
                         bookRequest.setPublished_date(cell.getStringCellValue());
                         break;
                     case 6:
                         bookRequest.setImage_path(cell.getStringCellValue());
                         break;
                     case 7:
                         bookRequest.setCategoryId((long) cell.getNumericCellValue());
                         break;
                     case 8:
                         var stringCellValue = cell.getStringCellValue();
                         var split = stringCellValue.split(",");
                         List<Long> authorId = new ArrayList<>();
                         for (String value:split){
                             authorId.add(Long.valueOf(value));
                         }
                         bookRequest.setAuthorsId(authorId);
                         break;
                     default:

                 }
                 cid++;
             }
             list.add(bookRequest);
         }



        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


}
