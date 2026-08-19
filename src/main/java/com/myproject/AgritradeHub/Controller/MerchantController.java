package com.myproject.AgritradeHub.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myproject.AgritradeHub.API.PaymentService;
import com.myproject.AgritradeHub.Model.Orders;
import com.myproject.AgritradeHub.Model.Payment;
import com.myproject.AgritradeHub.Model.ProductCategory;
import com.myproject.AgritradeHub.Model.Products;
import com.myproject.AgritradeHub.Model.Products.ProductStatus;
import com.myproject.AgritradeHub.Model.Users;
import com.myproject.AgritradeHub.Model.Orders.OrderStatus;
import com.myproject.AgritradeHub.Repository.AddCategoryRepository;
import com.myproject.AgritradeHub.Repository.OrderRepository;
import com.myproject.AgritradeHub.Repository.PaymentRepository;
import com.myproject.AgritradeHub.Repository.ProductRepository;
import com.myproject.AgritradeHub.Repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.Product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Merchant")
public class MerchantController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private HttpSession session;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentRepository paymentRepo;

	@Autowired
	private AddCategoryRepository categoryRepo;

	@Autowired
	private ProductRepository productRepo;

	@GetMapping("/MerchantDashboard")
	public String ShowMerchantDashboard(Model model,@RequestParam(value = "category",required = false)String category){
			
		if(session.getAttribute("loggedInMerchant")==null) {

	
		return "redirect:/MerchantLogin";
	}
		if (category !=null && !category.isEmpty()) {
			List<Products> productList = productRepo.findAllByCategoryAndStatus(category, ProductStatus.AVAILABLE);
			model.addAttribute("product",productList);
			
		}
		else {
			List<Products> productlList = productRepo.findAll();
			model.addAttribute("productList",productlList);
			
		}
		List<ProductCategory> categories = categoryRepo.findAll();
		model.addAttribute("categories",categories);

	return"Merchant/MerchantDashboard";

	}

	@GetMapping("/BuyProduct")
	public String ShowBuyProduct(@RequestParam("id") long id, Model model, RedirectAttributes attributes) {
		if (session.getAttribute("loggedInMerchant") == null) {
			attributes.addFlashAttribute("error", "Session Expired ⚠️");
			return "redirect:/MerchantLogin";
		}

		Products product = productRepo.findById(id).orElseThrow();
		model.addAttribute("razorpayKeyId", "rzp_live_Io1s9ctQtD0G1b");

		model.addAttribute("product", product);
		return "Merchant/BuyProduct";
	}

	@GetMapping("/create-order")
	@ResponseBody
	public Map<String, Object> createRazorpayOrder(@RequestParam long productId, @RequestParam int quantity) {
		Map<String, Object> data = new HashMap<>();
		try {
			Products product = productRepo.findById(productId).orElseThrow();
			// int amount =
			// product.getPricePerUnit().multiply(BigDecimal.valueOf(quantity)).intValue();
			int amount = (int) (product.getPricePerUnit() * quantity);
			com.razorpay.Order razorOrder = paymentService.createRazorpayOrder(amount);

			data.put("orderId", razorOrder.get("id"));
			data.put("razorpayKeyId", "rzp_live_Io1s9ctQtD0G1b");
			data.put("amount", amount * 100); // paise
			data.put("currency", "INR");
		} catch (Exception e) {
			data.put("error", e.getMessage());
		}
		return data;
	}

	@PostMapping("/verify_payment")
	public String verifyPayment(@RequestParam("paymentId") String paymentId,
			@RequestParam("orderId") String razorpayOrderId, @RequestParam("signature") String signature,
			@RequestParam("productId") long productId, @RequestParam("buyQuantity") int quantity, Model model,
			RedirectAttributes attributes) {
		try {
			Users merchant = (Users) session.getAttribute("loggedInMerchant");
			Products product = productRepo.findById(productId).orElseThrow();

			// Save Order
			Orders order = new Orders();
			order.setProductName(product.getProductName());
			order.setPricePerUnit(product.getPricePerUnit());
			order.setQuantity(quantity);
			order.setFarmer(product.getFarmer());
			order.setMerchant(merchant);
			order.setOrderStatus(OrderStatus.CONFIRMED);
			order.setOrderDate(LocalDateTime.now());
			orderRepo.save(order);

			// Save Payment
			Payment payment = new Payment();
			payment.setOrder(order);

			long transactionId = System.currentTimeMillis();

			payment.setTransactionId(transactionId);
			payment.setPayId(paymentId);
			payment.setAmount(product.getPricePerUnit() * quantity);
			payment.setPaymentMode("Online");
			payment.setPaymentDate(LocalDateTime.now());

			paymentRepo.save(payment);

			// Update Quantity
			if (product != null) {
				int remainingQty = product.getQuantity() - order.getQuantity();
				if (remainingQty <= 0) {
					remainingQty = 0;
					product.setStatus(ProductStatus.OUT_OF_STOCK);
				}
				product.setQuentity(remainingQty);

				productRepo.save(product);
			}

			// sendAutoEmail.SendOrderConfirmationEmail(order);

			attributes.addFlashAttribute("msg", true);
			attributes.addFlashAttribute("transactionId", transactionId);
			attributes.addFlashAttribute("quantity", quantity);

			return "redirect:/Merchant/BuyProduct?id=" + productId;
		} catch (Exception e) {
			attributes.addFlashAttribute("error", "Payment verification failed: " + e.getMessage());
			return "redirect:/Merchant/BuyProduct?id=" + productId;
		}
	}

	@GetMapping("/ChangePassword")
	public String ShowChangePassword() {
		if (session.getAttribute("loggedInMerchant") == null) {
			return "redirect:/ChangePassword";
		}
		return "Merchant/ChangePassword";
	}

	@PostMapping("/ChangePassword")
	public String ChangePassword(HttpServletRequest request, RedirectAttributes attributes) {
		try {
			String oldPass = request.getParameter("oldPassword");
			String newPass = request.getParameter("newPassword");
			String confirmPass = request.getParameter("confirmPassword");

			if (!newPass.equals(confirmPass)) {
				attributes.addFlashAttribute("msg", "New Password And confirm Password Are Not same!");
				return "redirect:/Merchant/ChangePassword";
			}
			Users Merchant = (Users) session.getAttribute("loggedInMerchant");

			if (oldPass.equals(Merchant.getPassword())) {
				Merchant.setPassword(confirmPass);
				userRepo.save(Merchant);
				session.removeAttribute("loggedInMerchant");
				attributes.addFlashAttribute("msg", "Password Change Succsfully");
				return "redirect:/MerchantLogin";

			} else {
				attributes.addFlashAttribute("msg", "Invalid old Password!");
			}

			return "redirect:/Merchant/ChangePassword";

		} catch (Exception e) {
			attributes.addFlashAttribute("msg", "Error :" + e.getMessage());
			return "redirect:/Merchant/ChangePassword";
		}
	}

	// logout
	@GetMapping("/Logout")
	public String logout(RedirectAttributes attributes) {
		session.removeAttribute("loggedinMerchant");
		attributes.addFlashAttribute("msg", "Logged Out Successfully!");
		return "redirect:/MerchantLogin";
	}
}
