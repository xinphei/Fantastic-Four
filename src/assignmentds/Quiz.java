package assignmentds;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
        private static List<Quiz> quizzes = new ArrayList<>();
        private String title;
        private String description;
        private String theme;
        private String quizizzLink;

        // Constructor
        public Quiz(String title, String description, String theme, String quizizzLink) {
            this.title = title;
            this.description = description;
            this.theme = theme;
            this.quizizzLink = quizizzLink;
        }

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getQuizizzLink() {
            return quizizzLink;
        }

        public void setQuizizzLink(String quizizzLink) {
            this.quizizzLink = quizizzLink;
        }

        public static List<Quiz> getQuizzes() {
            return quizzes;
        }

        // Method to add a new event to the list
        public static void addQuiz(Quiz q) {
            quizzes.add(q);
        }

        // Override toString() method to display quiz details
        @Override
        public String toString() {
            return "Quiz title\t: " + title +
                    "\nDescription\t: " + description +
                    "\nTheme\t\t: " + theme +
                    "\nQuizizzLink\t: " + quizizzLink +
                    "\n";
        }

        public static void initializeQuiz(){
            //Science
            quizzes.add(new Quiz("Biology Basics Quiz", "Test your understanding of fundamental concepts in biology", "Science", "https://quizizz.com/admin/quiz/598b4787c2324e1000f326f7?source=quiz_share"));
            quizzes.add(new Quiz("Chemistry Equation", "Assess your knowledge of basic equation you should know in chemistry", "Science", "https://quizizz.com/admin/quiz/62b47c82cf1fad001d95bcfe?source=quiz_share"));
            quizzes.add(new Quiz("Physics Concepts Quiz", "Test your understanding of fundamental concepts in physics", "Science", "https://quizizz.com/admin/quiz/6324d0a6707611001d395fec?source=quiz_share"));
            quizzes.add(new Quiz("Earth Science Quiz", "Assess your knowledge of geological and environmental science", "Science", "https://quizizz.com/admin/quiz/54bfa1971562dd2b11ed2f06?source=quiz_share"));
            quizzes.add(new Quiz("AFoundation of Astronomy", "Test your understanding of celestial bodies and the universe", "Science", "https://quizizz.com/admin/quiz/5dbf2594e78c80001ae3847a?source=quiz_share"));
            quizzes.add(new Quiz("Ecology Quiz", "Assess your knowledge of ecosystems, biodiversity, and conservation", "Science", "https://quizizz.com/admin/quiz/633c20f815c414001d468bbc?source=quiz_share"));
            quizzes.add(new Quiz("Genetics Quiz", "Test your understanding of genetic principles and inheritance", "Science", "https://quizizz.com/admin/quiz/63c94bc528c33a001e7e2e2a?source=quiz_share"));
            quizzes.add(new Quiz("Microbiology Quiz", "Assess your knowledge of microorganisms and their role in biology", "Science", "https://quizizz.com/admin/quiz/5f1fde4bc237dd001b86fdd0?source=quiz_share"));
            quizzes.add(new Quiz("Environmental Science Quiz", "Test your understanding of environmental issues and sustainability", "Science", "https://quizizz.com/admin/quiz/5a202b014fe02f1400128919?source=quiz_share"));
            quizzes.add(new Quiz("Anatomy and Physiology Quiz", "Assess your knowledge of the structure and function of the human body", "Science", "https://quizizz.com/admin/quiz/613a4c657ade94001e903ef9?source=quiz_share"));

            //Technology
            quizzes.add(new Quiz("Learn Educational Technology", "Test your knowledge of educational technology tools", "Technology", "https://quizizz.com/admin/quiz/5a2bc56baccfd01200e2cb4c?source=quiz_share"));
            quizzes.add(new Quiz("Coding Basics Quiz", "Assess your understanding of basic programming concepts", "Technology", "https://quizizz.com/admin/quiz/5e691b4ee2a30d001c4abf2e?source=quiz_share"));
            quizzes.add(new Quiz("Digital Security Quiz", "Test your knowledge of online security measures", "Technology", "https://quizizz.com/admin/quiz/5e612c183a75c2001b0ccc3c?source=quiz_share"));
            quizzes.add(new Quiz("Web Development Fundamentals", "Assess your understanding of web development concepts", "Technology", "https://quizizz.com/admin/quiz/659b43845f0d97b1befe0b4f?source=quiz_share"));
            quizzes.add(new Quiz("Mobile App Development Quiz", "Test your knowledge of mobile app development practices", "Technology", "https://quizizz.com/admin/quiz/656ab0716454d54265146ba9?source=quiz_share"));
            quizzes.add(new Quiz("Cybersecurity Basics", "Test your understanding of cybersecurity fundamentals", "Technology", "https://quizizz.com/admin/quiz/5d7b7cdf780414001cbcf4a4?source=quiz_share"));
            quizzes.add(new Quiz("Networking Concepts", "Assess your knowledge of computer networking principles", "Technology", "https://quizizz.com/admin/quiz/5cc2f721d25ae9001b97d178?source=quiz_share"));
            quizzes.add(new Quiz("Understand Artificial Intelligence", "Test your understanding of AI concepts and applications", "Technology", "https://quizizz.com/admin/quiz/60f66d501c90c6001b9b9b0a?source=quiz_share"));
            quizzes.add(new Quiz("Database Management Quiz", "Assess your knowledge of database management systems", "Technology", "https://quizizz.com/admin/quiz/6491ea3360c3aa001e66e9e2?source=quiz_share"));
            quizzes.add(new Quiz("About Internet of Things (IoT)", "Test your understanding of IoT technologies and applications", "Technology", "https://quizizz.com/admin/quiz/62df9156408ba7001ddd1736?source=quiz_share"));
            quizzes.add(new Quiz("STEM Concepts Quiz", "Assess your understanding of Science, Technology, Engineering, and Math", "Science", "https://quizizz.com/admin/quiz/5df2c75facc205001cbaee32?source=quiz_share"));

            //Engineering
            quizzes.add(new Quiz("Mechanical Engineering Basics", "Test your understanding of fundamental concepts in mechanical engineering", "Engineering", "https://quizizz.com/admin/quiz/602b2b9f23798e001bdbdd9e?source=quiz_share"));
            quizzes.add(new Quiz("Electrical Engineering Fundamentals", "Assess your knowledge of basic principles in electrical engineering", "Engineering", "https://quizizz.com/admin/quiz/5fdd7f282a606b001b4cb223?source=quiz_share"));
            quizzes.add(new Quiz("Civil Engineering Materials", "Test your understanding on the materials used in civil engineering", "Engineering", "https://quizizz.com/admin/quiz/5fcd823d41c265001b3d3de7?source=quiz_share"));
            quizzes.add(new Quiz("Chemical Engineering Quiz", "Assess your knowledge of chemical engineering principles and processes", "Engineering", "https://quizizz.com/admin/quiz/5e98b8c8e46a11001bb9ef95?source=quiz_share"));
            quizzes.add(new Quiz("Be an Aerospace Engineer", "Assess your knowledge of aerospace engineering principles and technologies", "Engineering", "https://quizizz.com/admin/quiz/626fec30db9742001d05bf41?source=quiz_share"));
            quizzes.add(new Quiz("Environmental Engineering Basics", "Test your understanding of environmental engineering principles and practices", "Engineering", "https://quizizz.com/admin/quiz/6155dc0683d8af001d257c85?source=quiz_share"));
            quizzes.add(new Quiz("Biomedical Engineering", "Assess your knowledge of biomedical engineering concepts and applications", "Engineering", "https://quizizz.com/admin/quiz/5e7038c3b6e81c001b2d826b?source=quiz_share"));
            quizzes.add(new Quiz("Industrial Engineering", "Test your understanding of industrial engineering principles and methodologies", "Engineering", "https://quizizz.com/admin/quiz/635500116c14fb001d0c01b4?source=quiz_share"));
            quizzes.add(new Quiz("Learn Robotics", "Test your understanding of robotics engineering concepts and applications", "Engineering", "https://quizizz.com/admin/quiz/64e8b6b08353fb274e5f3ea1?source=quiz_share"));
            quizzes.add(new Quiz("Materials Engineering", "Assess your knowledge of materials engineering principles and properties", "Engineering", "https://quizizz.com/admin/quiz/641d9419afde6d001d193d73?source=quiz_share"));

            //Mathematics
            quizzes.add(new Quiz("Differentiation", "Quiz on differentiation", "Mathematics", "https://quizizz.com/admin/quiz/57a279811d1729373f27ee29?source=quiz_share"));
            quizzes.add(new Quiz("Mathematics Challenge", "Challenge your math skills with this quiz", "Mathematics", "https://quizizz.com/admin/quiz/5edcd753a52e5b001b851802?source=quiz_share"));
            quizzes.add(new Quiz("Algebra Basics", "Test your understanding of fundamental algebra concepts", "Mathematics", "https://quizizz.com/admin/quiz/5e223e93b937e8001c040cc2?source=quiz_share"));
            quizzes.add(new Quiz("Geometry", "Assess your knowledge of basic geometry principles and shapes", "Mathematics", "https://quizizz.com/admin/quiz/5e83ee0aff87b2001d548b8f?source=quiz_share"));
            quizzes.add(new Quiz("Trigonometry", "Test your understanding of trigonometric functions and identities", "Mathematics", "https://quizizz.com/admin/quiz/5dbcf4c74be111001a207ce5?source=quiz_share"));
            quizzes.add(new Quiz("Calculus Fundamentals", "Assess your knowledge of basic calculus concepts and techniques", "Mathematics", "https://quizizz.com/admin/quiz/5ee0840088ebd6001b84dd53?source=quiz_share"));
            quizzes.add(new Quiz("Probability and Statistics Quiz", "Test your understanding of probability and statistical concepts", "Mathematics", "https://quizizz.com/admin/quiz/5e6e6f2f3ba941001b3231d5?source=quiz_share"));
            quizzes.add(new Quiz("Number Theory", "Assess your knowledge of properties of numbers and number theory", "Mathematics", "https://quizizz.com/admin/quiz/5dc1ba0712419d001c4be338?source=quiz_share"));
            quizzes.add(new Quiz("Logic", "Test your understanding of logical reasoning and mathematical logic", "Mathematics", "https://quizizz.com/admin/quiz/5eca826fb2071500205d424d?source=quiz_share"));
            quizzes.add(new Quiz("Discrete Mathematics Quiz", "Assess your knowledge of discrete mathematical structures and concepts", "Mathematics", "https://quizizz.com/admin/quiz/5ee2323c1243c4001be17227?source=quiz_share"));
        }
    }