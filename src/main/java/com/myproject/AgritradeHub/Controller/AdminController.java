package com.myproject.AgritradeHub.Controller;

import java.awt.Dialog.ModalExclusionType;
import java.net.http.HttpClient.Redirect;
import java.util.List;

import org.aspectj.lang.reflect.CatchClauseSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.Model.Enquiry;
import com.myproject.AgritradeHub.Model.ProductCategory;
import com.myproject.AgritradeHub.Model.Users;
import com.myproject.AgritradeHub.Model.Users.userRole;
import com.myproject.AgritradeHub.Repository.EnquiryRepository;
import com.myproject.AgritradeHub.Repository.AddCategoryRepository;
import com.myproject.AgritradeHub.Repository.UserRepository;

import jakarta.persistence.metamodel.Attribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	@Autowired
	private EnquiryRepository enquiryRepo;

	@Autowired
	private HttpSession session;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AddCategoryRepository productCategoryRepo;
	@Autowired
	private AddCategoryRepository addCategoryRepo;

	@GetMapping("/AdminDashboard")
	public String ShowAdminDashboard() {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";
		}

		return "Admin/AdminDashboard";
	}

	@GetMapping("/ManageFarmer")
	public String ShowManageFarmer(Model model) {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";
		}

		List<Users> farmerList = userRepo.findAllByRole(userRole.FARMER);
		model.addAttribute("farmerList", farmerList);
		return "Admin/ManageFarmer";
	}

	@GetMapping("/DeleteFarmer")
	public String DeleteFarmer(@RequestParam("id") long id) {
		userRepo.deleteById(id);
		return "redirect:/Admin/ManageFarmer";
	}

	@GetMapping("/ManageEnquiry")
	public String ShowManageEnquiry(Model model) {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/adminLogin";

		}
		List<Enquiry> enquiryList = enquiryRepo.findAll();
		model.addAttribute("enquiryList", enquiryList);

		return "Admin/ManageEnquiry";
	}

	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		if (session.getAttribute("loggedInAdmin") == null) {
			return "redirect:/ChangePassword";
		}
		return "Admin/ChangePassword";
	}

	@PostMapping("/ChangePassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes) {
		try {
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass = request.getParameter("confirmPassword");

			if (!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password And confirm Password Are Not same!");
				return "redirect:/Admin/ChangePassword";
			}
			Users admin = (Users) session.getAttribute("loggedInAdmin");

			if (oldPass.equals(admin.getPassword())) {
				admin.setPassword(confirmPass);
				userRepo.save(admin);
				session.removeAttribute("loggedInAdmin");
				attributes.addFlashAttribute("msg", "Password Change Succsfully");
				return "redirect:/adminLogin";

			} else {
				attributes.addFlashAttribute("msg", "Invalid old Password!");
			}

			return "redirect:/Admin/ChangePassword";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error :" + e.getMessage());
			return "redirect:/Admin/ChangePassword";
		}
	}

	// logout
	@GetMapping("/logout")
	public String logout(RedirectAttributes attributes) {
		session.removeAttribute("loggedinAdmin");
		attributes.addFlashAttribute("msg", "Logged Out Successfully!");
		return "redirect:/AdminLogin";
	}

	/*
	 * @GetMapping("/ManageMerchants") public String ShowManageMerchants(Model
	 * model) { if (session.getAttribute("loggedInAdmin") == null) { return
	 * "redirect:/ManageMerchants"; }
	 * 
	 * List<Users> merchantList = userRepo.findAllByRole(userRole.MERCHANT);
	 * model.addAttribute("merchantList", merchantList); return
	 * "Admin/ManageMerchants";
	 * 
	 * }
	 */
	
		// Add category
		@GetMapping("/AddCategory")
		public String ShowAddCategory(Model model) {
			ProductCategory categoryDto = new ProductCategory();
			model.addAttribute("categoryDto", categoryDto);
			return "Admin/AddCategory";
		}
		@PostMapping("/AddCategory")
		public String AddCategory(@ModelAttribute("categoryDto") ProductCategory category, RedirectAttributes attributes) {
			try {
				addCategoryRepo.save(category);
				System.err.println("Category Saved Successfully");
				attributes.addFlashAttribute("msg", "Category Addd Successfully");
				
			} catch (Exception e) {
				return "redirect:/Admin/AddCategory";
			}
			return "redirect:/Admin/AddCategory";
		}
		
		
	
		// Manage Merchant
		@GetMapping("/ManageMerchant")
		public String ShowManageMerchants(Model model) {
			if (session.getAttribute("loggedInAdmin") == null) {
				return "redirct:/ManageMerchant";
			}

			List<Users> merchantList = userRepo.findAllByRole(userRole.MERCHANT);
			model.addAttribute("merchantList", merchantList);
			return "Admin/ManageMerchant";

		}
		
		
		
		
		
		
		//View product
		@GetMapping("/ViewOrder")
		public String ShowViewOrder()
		{
			return"Admin/ViewOrder";
		}
}