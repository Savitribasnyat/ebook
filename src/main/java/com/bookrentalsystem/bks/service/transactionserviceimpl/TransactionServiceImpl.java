package com.bookrentalsystem.bks.service.transactionserviceimpl;

import com.bookrentalsystem.bks.dto.transaction.TransactionDto;
import com.bookrentalsystem.bks.dto.transaction.rentBook.RentBookRequest;
import com.bookrentalsystem.bks.dto.transaction.rentBook.RentBookResponse;
import com.bookrentalsystem.bks.dto.transaction.returnBook.ReturnBookRequest;
import com.bookrentalsystem.bks.enums.BookRentStatus;
import com.bookrentalsystem.bks.exception.globalexception.CodeNotFoundException;
import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.Member;
import com.bookrentalsystem.bks.model.Transaction;
import com.bookrentalsystem.bks.repo.TransactionRepo;
import com.bookrentalsystem.bks.service.BookService;
import com.bookrentalsystem.bks.service.MemberService;
import com.bookrentalsystem.bks.service.TransactionService;
import com.bookrentalsystem.bks.utility.ConvertToLocalDateTime;
import com.bookrentalsystem.bks.utility.GenerateRandomNumber;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;
    private final ConvertToLocalDateTime localDateTime;
    private final BookService bookService;
    private final MemberService memberService;


    //method to save or rent the book
    public RentBookResponse rentABook(RentBookRequest rentBookRequest) {
        Transaction transaction = rentBookRequestToTransacrion(rentBookRequest);
        transactionRepo.save(transaction);
        return transactionToRentBookResponse(transaction);
    }

    //method to convert the rentBookRequest to transaction
    public Transaction rentBookRequestToTransacrion(RentBookRequest rentBookRequest) {
        Book singleBook = bookService.findBookByid(rentBookRequest.getBookId());
        singleBook.setStock(singleBook.getStock() - 1);
        bookService.saveBook(singleBook);
        return Transaction.builder()
                .code(GenerateRandomNumber.generateRandomNumber().toString())
                .from(localDateTime.convertToDate())
                .to(localDateTime.convertToDate().plusDays(rentBookRequest.getDays()))
                .status(BookRentStatus.RENT)
                .book(singleBook)
                .member(memberService.findMemberById(rentBookRequest.getMemberId()))
                .build();
    }

    //method to convert transaction to RentBookResponse
    public RentBookResponse transactionToRentBookResponse(Transaction transaction) {
        return RentBookResponse.builder()
                .id(transaction.getId())
                .code(transaction.getCode())
                .from(transaction.getFrom())
                .to(transaction.getTo())
                .status(transaction.getStatus())
                .book(transaction.getBook())
                .member(transaction.getMember())
                .build();
    }

    //method to find all the rent Book list
    @Override
    public List<RentBookResponse> allRentBooks() {
        List<Transaction> transactions = transactionRepo.findAll();
        return transactions.stream()
                .map(this::transactionToRentBookResponse).toList();
    }


    //this function is used to find the transaction from code
    @Override
    public Transaction findTransactionByCode(String code) {
        Optional<Transaction> singleTransaction = transactionRepo.findByCode(code);
        if (singleTransaction.isPresent()) {
            return singleTransaction.get();
        }
        throw new CodeNotFoundException("Transaction code does not exist");
    }

    //method to convert transactionToReturnresponse
    @Override
    public ReturnBookRequest transactionToReturnBook(Transaction transaction) {
        Member member = transaction.getMember();

        return ReturnBookRequest.builder()
                .code(transaction.getCode())
                .from(transaction.getFrom().toString())
                .member_name(member.getName())
                .build();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepo.save(transaction);
    }

    //convert transaction to transactionDto
    @Override
    public TransactionDto transactionToTransactionDto(Transaction transaction) {
        String memberName;
        String bookName;
        try {
            memberName = transaction.getMember().getName();
            bookName = transaction.getBook().getName();
        } catch (EntityNotFoundException e) {
            memberName = null;
            bookName = null;
        }

        return TransactionDto.builder()
                .id(transaction.getId())
                .book_name(bookName)
                .code(transaction.getCode())
                .status(transaction.getStatus())
                .returnDate(transaction.getReturnDate())
                .from(transaction.getFrom())
                .member_name(memberName)
                .build();
    }


    @Override
    public List<TransactionDto> allTransaction() {
        List<Transaction> transactions = transactionRepo.findAll();
        return transactions.stream()
                .map(this::transactionToTransactionDto).toList();
    }

    @Override
    public List<Transaction> allTransactionEntity() {
        List<Transaction> transactions = transactionRepo.findAll();
        return transactions
                .stream().filter(t -> t.getStatus().equals(BookRentStatus.RENT)).toList();
    }


    @Override
    public ByteArrayInputStream downloadHistoryInExcel() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            XSSFSheet sheet = workbook.createSheet("Transaction_History");

            List<Transaction> allTransactionHistory = transactionRepo.findAll();

            String[] header = {"ID", "CODE", "Date Of Rent", "Date of Return"
                    , "Status", "Return Date", "Book name", "Member name"};

            // Create a Font for styling header cells
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 13);
            headerFont.setColor(IndexedColors.BLUE.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Create cells
            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowNUm = 1;

            for (Transaction transaction : allTransactionHistory) {
                Row row = sheet.createRow(rowNUm++);
                createCell(transaction, row, workbook);
            }

            workbook.write(byteArrayOutputStream);
            byteArrayOutputStream.close();

        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }


    //pagination
    @Override
    public Page<TransactionDto> getPaginatedTransaction(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Transaction> transactions = transactionRepo.findAll(pageable);

        return transactions.map(this::transactionToTransactionDto);
    }

    private void createCell(Transaction transaction, Row row,
                            XSSFWorkbook workbook) // creating cells for each row
    {

        //this is the format for the date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM/dd/yyyy");

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink  in a format (HSSF, XSSF)*/
        workbook.getCreationHelper();

        Cell cell = row.createCell(0);
        cell.setCellValue(transaction.getId());

        cell = row.createCell(1);
        cell.setCellValue(transaction.getCode());

        cell = row.createCell(2);
        cell.setCellValue(transaction.getFrom().format(dateTimeFormatter));

        cell = row.createCell(3);
        cell.setCellValue(transaction.getTo().format(dateTimeFormatter));


        cell = row.createCell(4);
        cell.setCellValue(transaction.getStatus().toString());

        cell = row.createCell(5);
        if (transaction.getReturnDate() != null) {
            cell.setCellValue(transaction.getReturnDate().format(dateTimeFormatter));
        } else {
            cell.setCellValue("");
        }

        cell = row.createCell(6);
        cell.setCellValue(transaction.getBook().getName());

        cell = row.createCell(7);
        cell.setCellValue(transaction.getMember().getName());


    }

    @Override
    public Page<TransactionDto> findTransactionFromDate(Integer pageNo, Integer pageSize,
                                                        LocalDate from, LocalDate returnDate) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Transaction> allTransactions = transactionRepo.findByDate(from, returnDate, pageable);
        return allTransactions
                .map(this::transactionToTransactionDto);
    }

}
