package com.myproject.AgritradeHub.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.Model.Enquiry;
import com.myproject.AgritradeHub.Model.Users;
import com.myproject.AgritradeHub.Model.Users.userRole;
import com.myproject.AgritradeHub.Model.Users.userStatus;
import com.myproject.AgritradeHub.Repository.EnquiryRepository;
import com.myproject.AgritradeHub.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
public class MainController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EnquiryRepository enquiryRepo;

	@GetMapping("/")
	public String ShowIndex() {
		return "index";
	}

	@GetMapping("/AboutUs")
	public String ShowAbout() {
		return "about";
	}

	@GetMapping("/FarmerRegistration")
	public String ShowRegister(Model model) {
		Users userDto = new Users();
		model.addAttribute("userDto", userDto);
		return "FarmerRegistration";
	}

	@PostMapping("/FarmerRegistration")
	public String Registration(@ModelAttribute("userDto") Users newUser, RedirectAttributes attributes) {
		try {
			newUser.setRole(userRole.FARMER);
			newUser.setStatus(userStatus.PENDING);
			newUser.setRegDate(LocalDateTime.now());
			userRepo.save(newUser);

			return "redirect:/FarmerRegistration";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getMessage());
			return "redirect:/FarmerRegistration";
		}
	}

	@GetMapping("/Contact")
	public String ShowContactUs(Model model) {
		Enquiry enquiry = new Enquiry();
		model.addAttribute("enquiry", enquiry);
		return "contact";
	}

	@PostMapping("/Contact")
	public String SubmitEnquiry(@ModelAttribute("enquiry") Enquiry enquiry) {
		try {

			enquiry.setEnquiryDate(LocalDateTime.now());
			enquiryRepo.save(enquiry);

			return "redirect:/Contact";
		} catch (Exception e) {
			return "redirect:/Contact";
		}
	}

	// Farmer login
	@GetMapping("/FarmerLogin")
	public String ShowFarmerLogin() {
		return "FarmerLogin";
	}

	@PostMapping("/FarmerLogin")
	public String farmerLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(email)) {

				attributes.addFlashAttribute("msg", "User dosn't Exist!");
				return "redirect:/FarmerLogin";
			}
			Users Farmer = userRepo.findByEmail(email);

			if (password.equals(Farmer.getPassword()) && Farmer.getRole().equals(userRole.FARMER)) {

				if (Farmer.getStatus().equals(userStatus.PENDING)) {
					attributes.addFlashAttribute("msg", "Registration pending, Wait for Admin Approval");
				} else if (Farmer.getStatus().equals(userStatus.DISABLED)) {
					attributes.addFlashAttribute("msg", "Login Disabled 🚫,Pleace Contact Administration");

				} else {

					session.setAttribute("loggedInFarmer", Farmer);
					return "redirect:/Farmer/FarmerDashboard";

				}
			} else {
				attributes.addFlashAttribute("msg", "Invalid User or Password");

			}

			return "redirect:/FarmerLogin";
		} catch (Exception e) {
			return "redirect:/FarmerLogin";
		}
	}

	// Admin login
	@GetMapping("/AdminLogin")
	public String ShowAdminLogin() {
		return "AdminLogin";

	}

	@PostMapping("/AdminLogin")
	public String AdminLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String username = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(username))

			{
				attributes.addFlashAttribute("msg", "User dosn't exists");
			}

			Users admin = userRepo.findByEmail(username);
			if (password.equals(admin.getPassword()) && admin.getRole().equals(userRole.ADMIN)) {
				session.setAttribute("loggedInAdmin", admin);
				return "redirect:/Admin/AdminDashboard";
			} else {

				attributes.addFlashAttribute("msg", "Invalid User or Worng Password!");
			}

			return "redirect:/AdminLogin";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getMessage());
			return "redirect:/AdminLogin";
		}
	}

	// merchant login
	@GetMapping("/MerchantLogin")
	public String ShowMerchantLogin() {
		return "/MerchantLogin";
	}

	@PostMapping("/MerchantLogin")
	public String MerchantLogin(HttpServletRequest request, RedirectAttributes attributes, HttpSession session) {
		try {
			String username = request.getParameter("email");
			String password = request.getParameter("password");

			if (!userRepo.existsByEmail(username))

			{
				attributes.addFlashAttribute("msg", "User dosn't exists");
			}

			Users merchant = userRepo.findByEmail(username);
			if (password.equals(merchant.getPassword()) && merchant.getRole().equals(userRole.MERCHANT)) {
				session.setAttribute("loggedInMerchant", merchant);
				return "redirect:/Merchant/MerchantDashboard";

			} else {

				attributes.addFlashAttribute("msg", "Invalid User or Worng Password!");
			}

			return "redirect:/MerchantLogin";
		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error : " + e.getMessage());
			return "redirect:/MerchantLogin";
		}

	}

	// merchant Registration
	@GetMapping("/merchantRegistration")
	public String showmerchantRegistration(Model model) {

		Users merchantDto = new Users();
		model.addAttribute("merchantDto", merchantDto);

		return "merchantRegistration";
	}

	@PostMapping("/merchantRegistration")
	public String merchantRegistration(@ModelAttribute("merchantDto") Users newUser, RedirectAttributes attributes) {
		try {
			newUser.setRole(userRole.MERCHANT);
			newUser.setStatus(userStatus.PENDING);
			newUser.setRegDate(LocalDateTime.now());
			userRepo.save(newUser);

			attributes.addFlashAttribute("msg", "Registration successfull");
			return "redirect:/merchantRegistration";
		} catch (Exception e) {
			attributes.addAttribute("msg", "Error:" + e.getMessage());
			return "redirect:/merchantRegistration";
		}
	}

	

}