package com.cst438.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@CrossOrigin 
public class AssignmentController {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/assignment")
	public AssignmentDTO[] getAllAssignmentsForInstructor() {
		// get all assignments for this instructor
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		AssignmentDTO[] result = new AssignmentDTO[assignments.size()];
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			AssignmentDTO dto = new AssignmentDTO(
					as.getId(), 
					as.getName(), 
					as.getDueDate().toString(), 
					as.getCourse().getTitle(), 
					as.getCourse().getCourse_id());
			result[i]=dto;
		}
		return result;
	}
	
	// TODO create CRUD methods for Assignment
	
	// Rest API for adding an assignment 
	@PostMapping("/assignment/add")
	public Assignment addAssignment(@RequestBody Assignment a) {
		Assignment assignment = new Assignment();
		Course course = new Course();
		
		// retrieve course
		course.setCourse_id(a.getCourse().getCourse_id());
		course.setTitle(a.getCourse().getTitle());
		
		courseRepository.save(course);
		
		// create new assignment by entering course_id, name, due date
		
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);	
		
		String dueDateStr = simpleDateFormat.format(a.getDueDate());
		
	    try {
	        simpleDateFormat.parse(dueDateStr);
	        assignment.setCourse(a.getCourse());
	        assignment.setName(a.getName());
	        assignment.setDueDate(a.getDueDate());
	        assignmentRepository.save(assignment);
	    } catch (ParseException e) {
	        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The due date for the assignment does not have a valid format. Use yyyy-MM-dd.");
	    }
		return assignment;
	}
	
	// Rest API for updating an assignment
	@PutMapping("/assignment/update")
	public Assignment updateAssignment(@RequestBody Assignment a) {
		// Modify assignment name & due date
		Assignment assignment = assignmentRepository.findById(a.getId()).orElse(null);
		if (assignment != null) {
			assignment.setName(a.getName());
			assignment.setDueDate(a.getDueDate());
			assignmentRepository.save(assignment);
		} else {
			return null;
		}
		return assignment;
	}	
	
	// Rest API for deleting an assignment
	@DeleteMapping("/assignment/delete")
	public void deleteAssignment(@RequestParam("id") Integer id) {
		
		Assignment assignment = assignmentRepository.findById(id).orElse(null);
		
		
		for (int i = 0; i < assignmentGrades.size(); i++) {
			
		}
		
		if (assignment != null) {
			if(assignment) {
				assignmentRepository.deleteById(id);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Warning...the assignment has grades.");
			}
		}
		
	}

}
