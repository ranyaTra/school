package tn.m104.rh.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.m104.rh.entity.Student;
import tn.m104.rh.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    Student student = new Student(1, "name1", "address1", 20.00);
    List<Student> listStudents = new ArrayList<Student>() {{
        add(new Student(2, "name2", "address2", 30.00));
        add(new Student(3, "name3", "address3", 10.00));
    }};

    // Test 1: Récupérer tous les étudiants (READ ALL)
    @Test
    @Order(1)
    public void testGetStudents() {
        // Arrange
        Mockito.when(studentRepository.findAll()).thenReturn(listStudents);

        // Act
        List<Student> result = studentService.getStudents();

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("name2", result.get(0).getName());
        Assertions.assertEquals("address3", result.get(1).getAddress());
    }

    // Test 2: Ajouter un étudiant (CREATE)
    @Test
    @Order(2)
    public void testRegisterStudent() {
        // Arrange
        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(student);

        // Act
        Student result = studentService.registerStudent(student);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getRollNumber());
        Assertions.assertEquals("name1", result.getName());
        Assertions.assertEquals(20.00, result.getPercentage());

        // Vérification que save() a été appelé 1 fois
        Mockito.verify(studentRepository, Mockito.times(1))
                .save(Mockito.any(Student.class));
    }

    // Test 3: Mettre à jour un étudiant (UPDATE)
    @Test
    @Order(3)
    public void testUpdateStudent() {
        // Arrange
        Student updatedStudent = new Student(1, "updatedName", "updatedAddress", 25.00);

        // Simuler findById pour retourner l'étudiant existant
        Mockito.when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        // Simuler save pour retourner l'étudiant mis à jour
        Mockito.when(studentRepository.save(Mockito.any(Student.class)))
                .thenReturn(updatedStudent);

        // Act
        Student result = studentService.updateStudent(updatedStudent);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("updatedName", result.getName());
        Assertions.assertEquals("updatedAddress", result.getAddress());
        Assertions.assertEquals(25.00, result.getPercentage());

        // Vérifications
        Mockito.verify(studentRepository, Mockito.times(1)).findById(1);
        Mockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any(Student.class));
    }

    // Test 4: Supprimer un étudiant (DELETE)
    @Test
    @Order(4)
    public void testDeleteStudent() {
        // Arrange
        Integer studentId = 1;

        // Configurer le mock pour ne rien faire quand deleteById est appelé
        Mockito.doNothing().when(studentRepository).deleteById(studentId);

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        // Vérifie que deleteById a été appelé exactement 1 fois avec le bon ID
        Mockito.verify(studentRepository, Mockito.times(1))
                .deleteById(studentId);
    }

    // Test bonus: Simulation d'erreur (optionnel)
    @Test
    @Order(5)
    public void testGetStudentsWithEmptyList() {
        // Arrange: Simuler une liste vide
        Mockito.when(studentRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Student> result = studentService.getStudents();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
        Assertions.assertTrue(result.isEmpty());
    }
}