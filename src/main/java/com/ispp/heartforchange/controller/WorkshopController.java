package com.ispp.heartforchange.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispp.heartforchange.dto.AttendanceDTO;
import com.ispp.heartforchange.dto.BeneficiaryDTO;
import com.ispp.heartforchange.dto.TaskDTO;
import com.ispp.heartforchange.security.jwt.JwtUtils;
import com.ispp.heartforchange.service.impl.TaskServiceImpl;

@RestController
@RequestMapping("/workshops")
public class WorkshopController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	private TaskServiceImpl taskService;
	private JwtUtils jwtUtils;

	/*
	 * Dependency injection
	 */
	public WorkshopController(TaskServiceImpl volunteerServiceImpl, JwtUtils jwtUtils) {
		super();
		this.taskService = volunteerServiceImpl;
		this.jwtUtils = jwtUtils;
	}

	/*
	 * Get Workshop by id
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getTaskById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			TaskDTO task = taskService.getById(jwt, id);
			return ResponseEntity.ok(task);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Save workshop
	 * 
	 * @Param TaskDTO
	 * 
	 * @Return ResponseEntity
	 */

	@PostMapping("/new")
	public ResponseEntity<?> save(HttpServletRequest request,  @RequestBody TaskDTO task) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			TaskDTO taskSave = taskService.saveTaller(jwt, task);
			logger.info("Workshop saved with name=={}", taskSave.getName());
			return ResponseEntity.ok(taskSave);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Get all Workshop by ong
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */

	@GetMapping("/ong/get")
	public ResponseEntity<?> getByOng(HttpServletRequest request) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		List<TaskDTO> tasks = taskService.getTallerByOng(jwt);
		return ResponseEntity.ok(tasks);
	}

	/*
	 * Update Workshop
	 * 
	 * @Param HttpServletRequest
	 * 
	 * @Return ResponseEntity
	 */

	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(HttpServletRequest request, @RequestBody TaskDTO task,
			@PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			TaskDTO taskupdate = taskService.updateTaller(jwt, id, task);
			logger.info("Workshop update with name=={}", taskupdate.getName());
			return ResponseEntity.ok(taskupdate);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Delete Workshop
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 */

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteWorkshop(@PathVariable("id") Long id, HttpServletRequest request) {
		String jwt = null;
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT not valid", HttpStatus.BAD_REQUEST);
		}

		try {
			taskService.deleteTaller(jwt, id);
			return ResponseEntity.ok("Workshop deleted");
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * get beneficiaries in a workshop
	 * 
	 * @Param Long id
	 * 
	 * @Return ResponseEntity
	 * 
	 */

	@GetMapping("/get/{id}/attendances/beneficiaries")
	public ResponseEntity<?> getBeneficiariesById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<BeneficiaryDTO> attendances = taskService.getAllBeneficiariesByTask(jwt, id);
			return ResponseEntity.ok(attendances);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/get/{id}/attendances/list")
	public ResponseEntity<?> getAttendancesById(HttpServletRequest request, @PathVariable("id") Long id) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAllAttendancesByWorkshop(jwt, id);
			return ResponseEntity.ok(attendances);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/get/{idPerson}/{idTask}/petition/state/") 
	public ResponseEntity<?> getStatePetitionByONG(HttpServletRequest request, @PathVariable("idPerson") Long idPerson, @PathVariable("idTask") Long idTask) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			AttendanceDTO attendances = taskService.getPetitionStateByONG(jwt, idPerson, idTask);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/beneficiary/get/{id}/attendances")
	public ResponseEntity<?> getAttendancesByPersonId(HttpServletRequest request, @PathVariable("id") Long id ) {
		String jwt = null;

		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
			jwt = headerAuth.substring(7, headerAuth.length());
		}
		if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
			return new ResponseEntity<String>("JWT no valid to refresh", HttpStatus.BAD_REQUEST);
		}

		try {
			List<AttendanceDTO> attendances = taskService.getAllAttendancesByBeneficiary(jwt, id);
			return ResponseEntity.ok(attendances);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
