package com.bookrentalsystem.bks.controller.transaction;

import com.bookrentalsystem.bks.dto.transaction.FilterTransaction;
import com.bookrentalsystem.bks.dto.transaction.TransactionDto;
import com.bookrentalsystem.bks.service.TransactionService;
import com.bookrentalsystem.bks.utility.ConvertToLocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final ConvertToLocalDateTime convertToLocalDateTime;

    //transaction table view
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    @GetMapping("/table")
    public String transactionTable(Model model){
        model.addAttribute("filter",new FilterTransaction());
        return findPaginated(0,model);
    }

    //pagination method
    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;
        Page<TransactionDto> page = transactionService.getPaginatedTransaction(pageNo, pageSize);
        List<TransactionDto> transactionList = page.getContent();

        if(model.getAttribute("filter") == null){
            model.addAttribute("filter",new FilterTransaction());
        }


        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());

        if(model.getAttribute("transaction") == null){
            model.addAttribute("transaction", transactionList);
        }

        return "transaction/TransactionTable";
    }

    @RequestMapping("/download/excel")
    public ResponseEntity<Resource> downloadExcelFile() throws IOException {

        String fileName = "TransactionHistory.xlsx";
        ByteArrayInputStream actualData = transactionService.downloadHistoryInExcel();

        InputStreamResource isr = new InputStreamResource(actualData);

      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                      "attachment; filename="+ fileName)
              .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
              .body(isr);
    }

    @PostMapping("/page/filter/first/{pageNo}")
    public String findPaginatedForFilter(@ModelAttribute FilterTransaction filterTransaction,
                                         @PathVariable(value = "pageNo") int pageNo, Model model,
                                         @RequestParam(required = false) String fromDate,
                                         @RequestParam(required = false) String todate,
                                         RedirectAttributes redirectAttributes) {
            int pageSize = 5;

        /**
         *this is used to check if the from date is empty or not .
         * If empty then send
         */
            if(filterTransaction.getFrom().isEmpty()){
                String message = "Please select the date from where you want to find the transaction!!";
                redirectAttributes.addFlashAttribute("errorMsg",message);
                return "redirect:/transaction/table";
            }


                LocalDate from  = convertToLocalDateTime.convertToDate(filterTransaction.getFrom());//convert to localDate
                LocalDate toDate;

        /**
         *this is used to check if the to date is empty or not .
         * If empty then to date will be todays date
         */
                if(filterTransaction.getTo().isEmpty()){
                    toDate = convertToLocalDateTime.convertToDate();
                }else {
                    toDate  = convertToLocalDateTime.convertToDate(filterTransaction.getTo());
                }


                Page<TransactionDto> page = transactionService.findTransactionFromDate(pageNo,pageSize,from,toDate);
                List<TransactionDto> transactionList = page.getContent();


                model.addAttribute("filter",new FilterTransaction(filterTransaction.getFrom(),
                        convertToLocalDateTime.convertDateToString(toDate)));
                model.addAttribute("currentPage", pageNo);
                model.addAttribute("totalPages", page.getTotalPages());
                model.addAttribute("transaction", transactionList);
                model.addAttribute("fromDate",filterTransaction.getFrom());
                model.addAttribute("todate",convertToLocalDateTime.convertDateToString(toDate));

                return "transaction/FilterTransactionTable";

    }

    @GetMapping("/page/filter/{pageNo}")
    public String secondFilter( @PathVariable(value = "pageNo") int pageNo, Model model,
                                @RequestParam(required = false,name = "fromDate") String fromDate,
                                @RequestParam(required = false,name = "todate") String todate){

        int pageSize =5;

        LocalDate from  = convertToLocalDateTime.convertToDate(fromDate);
        LocalDate toDate  = convertToLocalDateTime.convertToDate(todate);
        Page<TransactionDto> page = transactionService.findTransactionFromDate(pageNo,pageSize,from,toDate);
        List<TransactionDto> transactionList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("transaction", transactionList);
        model.addAttribute("fromDate",fromDate);
        model.addAttribute("todate",todate);

        model.addAttribute("filter",new FilterTransaction(fromDate,todate));

        return "transaction/FilterTransactionTable";

    }
}
