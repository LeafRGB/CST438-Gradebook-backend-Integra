package com.cst438.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
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
	
	//get by primary key
	@GetMapping("/assignment/getByID/{id}")
	public AssignmentDTO[] getCourse(@PathVariable("id") int id) {
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		AssignmentDTO[] result = new AssignmentDTO[1];
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			if(as.getId() == id) {
				AssignmentDTO dto = new AssignmentDTO(
						as.getId(), 
						as.getName(), 
						as.getDueDate().toString(), 
						as.getCourse().getTitle(), 
						as.getCourse().getCourse_id());
				result[0]=dto;
				break;
			}
			
		}
		return result;
	}
	// delete a course
	//  DELETE  /course/12389
	//  DELETE  /course/12389?force=yes
	@DeleteMapping("/assignment/delete/{id}")
	public void deleteCourse(@PathVariable("id") int id) {
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			if(as.getId() == id) {
				assignmentRepository.delete(as);
			}
			
		}
		// @RequestParam("force") Optional<String> force)
	}
	
	@PutMapping("/assignment/update/{id}")
	public void updateAssignment(@RequestBody AssignmentDTO assignmentDTO, @PathVariable("id") int id) {
		String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		for (int i=0; i<assignments.size(); i++) {
			Assignment as = assignments.get(i);
			if(as.getId() == i) {
				as.setDueDate(Date.valueOf(assignmentDTO.dueDate()));
		        as.setName(assignmentDTO.assignmentName());
		        Course course = courseRepository.findById(assignmentDTO.courseId()).orElse(null);
		        if(course != null) {
		        	as.setCourse(course);
		        }
		        assignmentRepository.save(as);
		        break;
			}
			
		}
	}

	@PutMapping("/assignment/new/")
	public void createAssignment(@RequestBody AssignmentDTO assignmentDTO, int id, String name, Date date, int courseID) {
		//String instructorEmail = "dwisneski@csumb.edu";  // user name (should be instructor's email) 
		//List<Assignment> assignments = assignmentRepository.findByEmail(instructorEmail);
		Course course = courseRepository.findById(assignmentDTO.courseId()).orElse(null);
		String courseName = "";
		
        if(course != null) {
        	courseName = course.getTitle();
        }
		
		Assignment newAssignment = new Assignment();
		newAssignment.setCourse(course);
		newAssignment.setDueDate(date);
		newAssignment.setId(id);
		newAssignment.setName(courseName);
		assignmentRepository.save(newAssignment);
		
	}
	                       
}
