package server;

import global.MessageStatus;
import global.data.Campus;
import global.data.Course;
import global.data.MeetTime;
import global.data.Term;
import global.data.UserType;
import global.LinkedList;

import client.Client;

import java.time.LocalTime;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class test_server {

    static Client client;

    @BeforeAll
    static void setUp() {
        client = new Client("localhost", 7777);
        TestHelpers.StartClient();
    }

    @Test
    void testRegisterAdmin() {
        TestHelpers.RegisterUser("admin_test", "", UserType.ADMIN, client);
    }

    @Test
    void testAdminLogin() {
        var user = TestHelpers.Login("admin_test", "", client);
        assertNotNull(user);
    }

    @Test
    void testAddCampus() {
        TestHelpers.AddCampus("TestCampus", client);
        var campuses = TestHelpers.GetCampuses(client);
        boolean found = false;
        for (Campus c : campuses) {
            if (c.name().equals("TestCampus")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAddDepartment() {
        TestHelpers.AddDepartment("TestCampus", "CS", client);
    }

    @Test
    void testAddCourse() {
        LinkedList<Course> reqs = new LinkedList<>();
        var course = invokeAddCourse("TestCourse", 100, "TestCampus", "CS", reqs);
        assertNotNull(course);
    }

    private Course invokeAddCourse(String name, int num, String campus, String dept, LinkedList<Course> reqs) {
        try {
            var method = TestHelpers.class.getDeclaredMethod(
                "AddCourse",
                String.class, int.class, int.class,
                String.class, String.class,
                LinkedList.class, Client.class
            );
            method.setAccessible(true);
            return (Course) method.invoke(null, name, num, 3, campus, dept, reqs, client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddSection() {
        var course = invokeAddCourse("TestSectionCourse", 101, "TestCampus", "CS", new LinkedList<>());
        MeetTime[] meetTimes = new MeetTime[]{
            new MeetTime(MeetTime.Day.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0))
        };

        try {
            var method = TestHelpers.class.getDeclaredMethod(
                "AddSection",
                Course.class, String.class, String.class,
                Term.class, String.class,
                int.class, MeetTime[].class,
                Client.class
            );
            method.setAccessible(true);
            method.invoke(null, course, "TestCampus", "CS",
                new Term(Term.Season.FALL, 2025),
                "Instructor", 20, meetTimes, client
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSearchCourses() {
        try {
            var method = TestHelpers.class.getDeclaredMethod(
                "SearchCourses",
                String.class, String.class, String.class,
                Term.class, Client.class
            );
            method.setAccessible(true);
            var sections = (LinkedList<?>) method.invoke(
                null, "Test", "TestCampus", "CS",
                new Term(Term.Season.FALL, 2025), client
            );
            assertNotNull(sections);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testEnrollDrop() {
        var course = invokeAddCourse("EnrollCourse", 202, "TestCampus", "CS", new LinkedList<>());
        MeetTime[] mt = new MeetTime[]{
            new MeetTime(MeetTime.Day.TUESDAY, LocalTime.of(8, 0), LocalTime.of(9, 0))
        };
        int sectionId;

        try {
            var addSection = TestHelpers.class.getDeclaredMethod(
                "AddSection", Course.class, String.class, String.class,
                Term.class, String.class, int.class,
                MeetTime[].class, Client.class
            );
            addSection.setAccessible(true);
            addSection.invoke(null, course, "TestCampus", "CS",
                new Term(Term.Season.FALL, 2025),
                "Prof", 20, mt, client
            );

            var browse = TestHelpers.class.getDeclaredMethod(
                "SearchCourses", String.class, String.class, String.class,
                Term.class, Client.class
            );
            browse.setAccessible(true);

            var list = (LinkedList<?>) browse.invoke(
                null, "EnrollCourse", "TestCampus", "CS",
                new Term(Term.Season.FALL, 2025), client
            );

            sectionId = ((global.data.Section) list.Get(0)).getId();

            TestHelpers.RegisterUser("student_test", "", UserType.STUDENT, client);
            TestHelpers.Login("student_test", "", client);

            var enrollMethod = TestHelpers.class.getDeclaredMethod(
                "Enroll", int.class, Term.class, Client.class
            );
            enrollMethod.setAccessible(true);
            Object enrolled = enrollMethod.invoke(
                null, sectionId, new Term(Term.Season.FALL, 2025), client
            );
            assertNotNull(enrolled);

            var dropMethod = TestHelpers.class.getDeclaredMethod(
                "Drop", int.class, Term.class, Client.class
            );
            dropMethod.setAccessible(true);
            Object dropped = dropMethod.invoke(
                null, sectionId, new Term(Term.Season.FALL, 2025), client
            );
            assertNotNull(dropped);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            var method = TestHelpers.class.getDeclaredMethod("Logout", Client.class);
            method.setAccessible(true);
            method.invoke(null, client);
        } catch (Exception ignored) {
        }
    }
}

