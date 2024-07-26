package com.bookrentalsystem.bks.controller.login;

import com.bookrentalsystem.bks.dto.forgotpassword.ForgotPasswordDto;
import com.bookrentalsystem.bks.dto.login.ChangePasswordDto;
import com.bookrentalsystem.bks.dto.login.LoginUserDto;
import com.bookrentalsystem.bks.model.SendEmail;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.service.ForgotPasswordService;
import com.bookrentalsystem.bks.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordService forgotPasswordService;

    //login page view
    @GetMapping("/signIn")
    public String loginPage(Model model){
        model.addAttribute("changePassword",new ChangePasswordDto());
        return "/login/LoginPage";
    }

    //register new user
    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("user",new LoginUserDto());
        return "/login/RegistrationPage";
    }

    @PostMapping("/user/save")
    public String saveUser(@Valid @ModelAttribute("user") LoginUserDto loginUserDto,
                           BindingResult result,
                           Model model){
       String message = userService.findByEmail(loginUserDto.getEmail());

       //if the user having same email exist then send error message
       if(message != null){
           //this is used to send error to the field(object_name,field_name,message)
           FieldError fieldError =
                   new FieldError("loginUserDto","email",message);
           result.addError(fieldError);
       }

       //this check the validation of every field & if error occur it redirect to same form with error
       if(result.hasErrors()){
           model.addAttribute("user",loginUserDto);
           return "/login/RegistrationPage";
       }

        userService.saveUser(loginUserDto);
        return "redirect:/login/signup";
    }

    @GetMapping("/forgot/password")
    public String changePasswordEmailPage(Model model){
        model.addAttribute("change",new SendEmail());
        return "/login/EnterEmailPage";
    }

    //send email to get otp
    @PostMapping("/send/email")
    public String emailSend(@Valid @ModelAttribute("change") SendEmail email,
                            BindingResult result,
                            Model model) throws MessagingException {

        //check if email is valid or not
        if(result.hasErrors()){
            model.addAttribute("change",email);
            return "/login/EnterEmailPage";
        }
       Optional<User> singleUser = userService.findUsingEmail(email.getTo());
       if(singleUser.isPresent()){
           forgotPasswordService.sendEmail(email);
           ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
           forgotPasswordDto.setUserId(singleUser.get().getId());
           model.addAttribute("newCode",forgotPasswordDto);
           return "/login/CheckOtpPage";
       }
       return null;
    }

    //this api is used to check the otp code which we have enter & if the code is present then we get access to another page
    @PostMapping("/change")
    public String checkOtp(@ModelAttribute("newCode") ForgotPasswordDto forgotPasswordDto,Model model){

       String message = forgotPasswordService.checkCodeOtp(forgotPasswordDto.getCode());
       if(message!=null){
           User user = new User();
           user.setId(forgotPasswordDto.getUserId());
           model.addAttribute("user",user);
           return "/login/ChangePassword";
       }
       model.addAttribute("errorMsg","Please enter the same otp which we have send to you");
        return "login/CheckOtpPage";
    }

    //we change our password by using this api
    @PostMapping("/change/password")
    public String changePassword(@ModelAttribute("user") User user,Model model){
      User singleUser =  userService.findById(user.getId());

      if(user.getPassword().length() <= 3){
          model.addAttribute("user",user);
          model.addAttribute("errorMsg","Password should be at least 4 digit");
          return "/login/ChangePassword";
      }
      singleUser.setPassword(passwordEncoder.encode(user.getPassword()));
      userService.saveUserEntity(singleUser);

      model.addAttribute("message","Password changed successfully...");
      return "/login/LoginPage";
    }



}
