package assignmentds;

public class Destination {
        private String name;
        private double x;
        private double y;
        private double distance;

        public Destination(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public void calculateDistance(double userX, double userY) {
            this.distance = Math.sqrt(Math.pow(this.x - userX, 2) + Math.pow(this.y - userY, 2));
        }

        @Override
        public String toString() {
            return String.format("[%s] %s - %.2f km away", name, distance);
        }
    }
