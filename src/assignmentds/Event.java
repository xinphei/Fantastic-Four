package assignmentds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class Event {
    private static List<Event> events = new ArrayList<>();
    // Fields
    private String title;
    private String description;
    private String venue;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructor
    public Event(String title, String description, String venue, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public static List<Event> getEvents() {
        return events;
    }

    public static void addEvent(Event event) {
        events.add(event);
    }

    // Override toString() method to display event details
    @Override
    public String toString() {
        return "Event title\t: " + title +
                "\nDescription\t: " + description +
                "\nVenue\t\t: " + venue +
                "\nDate\t\t: " + date +
                "\nTime\t\t: " + startTime + " - " + endTime +
                "\n";
    }
    
    public static void writeEventsToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            for (Event event : events) {
                // Writing each event's details separated by commas
                writer.println(event.getTitle() + "," +
                        event.getDescription() + "," +
                        event.getVenue() + "," +
                        event.getDate() + "," +
                        event.getStartTime() + "," +
                        event.getEndTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to read events from a text file
    public static void readEventsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Splitting each line by tab to extract event details
                String[] parts = line.split("\t");
                String title = parts[0];
                String description = parts[1];
                String venue = parts[2];
                LocalDate date = LocalDate.parse(parts[3]);
                LocalTime startTime = LocalTime.parse(parts[4]);
                LocalTime endTime = LocalTime.parse(parts[5]);
                events.add(new Event(title, description, venue, date, startTime, endTime));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeEvents() {
        // Initialize events
        //April
        events.add(new Event("History Lecture Series", "Presentation on historical events", "History Department", LocalDate.of(2024, 4, 28), LocalTime.of(14, 0), LocalTime.of(16, 0)));
        events.add(new Event("Educational Seminar", "Exploring new teaching methodologies", "Auditorium A", LocalDate.of(2024, 4, 30), LocalTime.of(9, 0), LocalTime.of(12, 0)));
        //May
        events.add(new Event("STEM Workshop", "Hands-on activities for Science, Technology, Engineering, and Math", "Science Lab", LocalDate.of(2024, 5, 9), LocalTime.of(13, 0), LocalTime.of(16, 0)));
        events.add(new Event("Professional Development Session", "Improving educator skills", "Conference Room B", LocalDate.of(2024, 5, 18), LocalTime.of(10, 0), LocalTime.of(12, 0)));
        events.add(new Event("School Orchestra Concert", "Performance by school orchestra members", "Auditorium", LocalDate.of(2024, 5, 18), LocalTime.of(18, 0), LocalTime.of(20, 0)));
        events.add(new Event("Guest Lecture on Pedagogy", "Insights from experienced educators", "Lecture Hall", LocalDate.of(2024, 5, 25), LocalTime.of(14, 0), LocalTime.of(16, 0)));
        //June
        events.add(new Event("Mathematics Symposium", "Exploring advanced math concepts", "Mathematics Department", LocalDate.of(2024, 6, 5), LocalTime.of(9, 0), LocalTime.of(13, 0)));
        events.add(new Event("Technology Integration Workshop", "Incorporating technology into the classroom", "Computer Lab", LocalDate.of(2024, 6, 10), LocalTime.of(10, 0), LocalTime.of(12, 0)));
        events.add(new Event("Science Fair Preparation Meeting", "Planning for the upcoming science fair", "Principal's Office", LocalDate.of(2024, 6, 15), LocalTime.of(15, 0), LocalTime.of(17, 0)));
        events.add(new Event("Engineering Design Challenge", "Engaging students in hands-on engineering projects", "Engineering Wing", LocalDate.of(2024, 6, 20), LocalTime.of(13, 0), LocalTime.of(15, 0)));
        events.add(new Event("Professional Learning Community Meeting", "Collaborative discussion among educators", "Staff Lounge", LocalDate.of(2024, 6, 25), LocalTime.of(11, 0), LocalTime.of(13, 0)));
        //July
        events.add(new Event("STEM Education Conference", "Sharing best practices in STEM education", "Convention Center", LocalDate.of(2024, 7, 1), LocalTime.of(9, 0), LocalTime.of(17, 0)));
        events.add(new Event("Language Arts Workshop", "Improving literacy instruction strategies", "Language Arts Department", LocalDate.of(2024,7, 6), LocalTime.of(10, 0), LocalTime.of(12, 0)));
        events.add(new Event("In-Service Training Session", "Training session for new curriculum implementation", "Training Room", LocalDate.of(2024, 7, 12), LocalTime.of(13, 0), LocalTime.of(15, 0)));
        events.add(new Event("Parent-Teacher Conference", "Meeting with parents to discuss student progress", "Classroom 101", LocalDate.of(2024, 7, 20), LocalTime.of(16, 0), LocalTime.of(18, 0)));
        events.add(new Event("Educational Technology Expo", "Showcasing innovative educational technologies", "Exhibition Hall", LocalDate.of(2024, 7, 27), LocalTime.of(10, 0), LocalTime.of(16, 0)));
        //August
        events.add(new Event("Seminar on Creative Writing", "Explore techniques for creative writing", "Library", LocalDate.of(2024, 8, 10), LocalTime.of(14, 0), LocalTime.of(16, 0)));
        events.add(new Event("Workshop on Classroom Management", "Learn effective classroom management strategies", "Teacher's Lounge", LocalDate.of(2024, 8, 15), LocalTime.of(9, 0), LocalTime.of(12, 0)));
        events.add(new Event("Geography Field Trip", "Exploration of geographical landmarks", "Outdoor", LocalDate.of(2024, 8, 26), LocalTime.of(9, 0), LocalTime.of(16, 0)));
        //September
        events.add(new Event("Music Education Symposium", "Discuss music education trends and practices", "Music Room", LocalDate.of(2024, 9, 1), LocalTime.of(13, 0), LocalTime.of(17, 0)));
        events.add(new Event("Art Exhibition", "Display artworks created by students", "Art Gallery", LocalDate.of(2024, 9, 7), LocalTime.of(10, 0), LocalTime.of(15, 0)));
        events.add(new Event("Workshop on Project-Based Learning", "Explore project-based learning methodologies", "Classroom 102", LocalDate.of(2024, 9, 14), LocalTime.of(9, 0), LocalTime.of(12, 0)));
        events.add(new Event("Educational Leadership Summit", "Discuss leadership strategies for educators", "Conference Room A", LocalDate.of(2024, 9, 28), LocalTime.of(10, 0), LocalTime.of(16, 0)));
        //October
        events.add(new Event("Health Education Workshop", "Explore health education curriculum", "Health Room", LocalDate.of(2024, 10, 1), LocalTime.of(13, 0), LocalTime.of(15, 0)));
        events.add(new Event("Physical Education Workshop", "Demonstration of physical education activities", "Gymnasium", LocalDate.of(2024, 10, 15), LocalTime.of(10, 0), LocalTime.of(12, 0)));
        events.add(new Event("Seminar on Student Assessment", "Discuss different methods of student assessment", "Auditorium B", LocalDate.of(2024, 10, 21), LocalTime.of(14, 0), LocalTime.of(16, 0)));
        events.add(new Event("Educator Networking Event", "Building connections among educators", "Cafeteria", LocalDate.of(2024, 10, 30), LocalTime.of(16, 0), LocalTime.of(18, 0)));
        //November
        events.add(new Event("Drama Club Performance", "Dramatic performances by drama club members", "Theater", LocalDate.of(2024, 11, 1), LocalTime.of(17, 0), LocalTime.of(19, 0)));
        events.add(new Event("Educational Psychology Conference", "Discuss psychology concepts in education", "Psychology Department", LocalDate.of(2024, 11, 12), LocalTime.of(9, 0), LocalTime.of(17, 0)));
        events.add(new Event("Coding Bootcamp", "Intensive coding training for educators", "Computer Lab", LocalDate.of(2024, 11, 22), LocalTime.of(11, 0), LocalTime.of(16, 0)));
        //December
        events.add(new Event("Workshop on Differentiated Instruction", "Learn strategies for differentiated instruction", "Classroom 103", LocalDate.of(2024, 12, 4), LocalTime.of(10, 0), LocalTime.of(13, 0)));
        events.add(new Event("Special Education Symposium", "Discuss special education practices and policies", "Special Education Department", LocalDate.of(2024, 12, 14), LocalTime.of(13, 0), LocalTime.of(16, 0)));
        events.add(new Event("Technology in Music Education Workshop", "Explore technology integration in music education", "Music Room", LocalDate.of(2024, 12, 19), LocalTime.of(10, 0), LocalTime.of(12, 0)));
        events.add(new Event("Literature Circle", "Discussion on classic literature works", "Library", LocalDate.of(2024, 12, 31), LocalTime.of(15, 0), LocalTime.of(17, 0)));
    }


}
