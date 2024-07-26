package com.bookrentalsystem.bks.controller.member;

import com.bookrentalsystem.bks.dto.member.MemberRequest;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.service.MemberService;
import com.bookrentalsystem.bks.service.TransactionService;
import com.bookrentalsystem.bks.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TransactionService transactionService;
    private final UserService userService;

    //member list
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/table")
    public String memberTable(Model model){
        List<User> users =userService.findAllUser();
//        List<MemberResponse> memberResponses = memberService.allMemberResponseDTo(members);
        model.addAttribute("member",users);
        return "customer/CustomerTable";
    }

    //add member form
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/form")
    public String memberForm(Model model){
        if(model.getAttribute("member") == null)
            model.addAttribute("member",new MemberRequest());
        return "customer/CustomerForm";
    }

    //save the member - validation
    @PostMapping("/save")
    public String saveMember(@Valid @ModelAttribute("member") User user,
                             BindingResult result,Model model,RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            model.addAttribute("member",user);
            return "customer/CustomerForm";
        }

        String message= userService.saveUserEntity(user);

        if(message!=null){
            redirectAttributes.addFlashAttribute("message","Member updated successfully");
           return  "redirect:/member/table";
        }
        return "message";
    }

    @RequestMapping("/update/{id}")
    public String memberUpdate(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("member",user);
        return "/customer/CustomerUpdate";
    }

    //delete the member - soft delete
    @RequestMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,RedirectAttributes redirectAttributes){
//       List<Transaction> transaction =  transactionService.allTransactionEntity();
//       List<Transaction> filterTransaction =transaction.stream().filter(t -> t.getMember().getId().equals(id))
//                       .filter(t -> t.getStatus().equals(BookRentStatus.RENT)).toList();
//
//       if(filterTransaction.isEmpty()){
//           memberService.deleteMemberById(id);
//       }else {
//           throw new MemberCanNotBeDeletedException("Cannot delete this member");
//       }
        userService.deleteById(id);

        redirectAttributes.addFlashAttribute("message","Member Deleted Successfully!!");
        return "redirect:/member/table?success";
    }
}
