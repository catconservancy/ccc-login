package org.rmcc.ccc.controller;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.rmcc.ccc.annotations.Loggable;
import org.rmcc.ccc.model.User;
import org.rmcc.ccc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserRepository userRepository;
    private JavaMailSender javaMailSender;
	
	@Autowired
	public UserController(UserRepository userRepository,
			JavaMailSender javaMailSender) {
		this.userRepository = userRepository;
		this.javaMailSender = javaMailSender;
	}	

	@RequestMapping(method = RequestMethod.GET)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
	
	@Loggable
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public User createUser(@Valid @RequestBody User user, final BindingResult bindingResult) throws MethodArgumentNotValidException {
	    if (bindingResult.hasErrors()) {
	        throw new MethodArgumentNotValidException(null, bindingResult);
	    }
	    // TODO: validate pw matches confirm pw
	    //       validate email is unique
	    user.setActive(false);
	    sendEmail(user);
	    return userRepository.save(user);
	}
	

	private void sendEmail(User user) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo("aaron.g.jones@gmail.com");
            helper.setReplyTo("aaron.g.jones@gmail.com");
            helper.setFrom("aaron.g.jones@gmail.com");
            helper.setSubject("CCC User Created");
            helper.setText("A new user has registered and requested access to CCC Databse: " + user.getFullName() + ": " + user.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {}
        javaMailSender.send(mail);
    }
}
