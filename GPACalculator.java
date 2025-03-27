import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GPACalculator extends JFrame {
    private List<CoursePanel> coursePanels = new ArrayList<>();
    private JPanel coursesPanel;
    private JLabel gpaLabel;
    private DecimalFormat df = new DecimalFormat("0.00");

    public GPACalculator() {
        setTitle("GPA Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("GPA Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        
        // GPA display
        gpaLabel = new JLabel("GPA: 0.00");
        gpaLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gpaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(gpaLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Courses scroll panel
        coursesPanel = new JPanel();
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(coursesPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Courses"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Course");
        JButton calculateButton = new JButton("Calculate GPA");
        JButton clearButton = new JButton("Clear All");
        
        addButton.addActionListener(e -> addCoursePanel());
        calculateButton.addActionListener(e -> calculateGPA());
        clearButton.addActionListener(e -> clearAllCourses());
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(calculateButton);
        buttonsPanel.add(clearButton);
        
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Add initial course
        addCoursePanel();
        
        setContentPane(mainPanel);
    }
    
    private void addCoursePanel() {
        CoursePanel coursePanel = new CoursePanel(this);
        coursePanels.add(coursePanel);
        coursesPanel.add(coursePanel);
        coursesPanel.revalidate();
    }
    
    private void removeCoursePanel(CoursePanel panel) {
        coursePanels.remove(panel);
        coursesPanel.remove(panel);
        coursesPanel.revalidate();
        coursesPanel.repaint();
        calculateGPA();
    }
    
    private void clearAllCourses() {
        coursePanels.clear();
        coursesPanel.removeAll();
        coursesPanel.revalidate();
        coursesPanel.repaint();
        gpaLabel.setText("GPA: 0.00");
    }
    
    void calculateGPA() {
        double totalGradePoints = 0;
        int totalCreditHours = 0;
        
        for (CoursePanel panel : coursePanels) {
            try {
                String grade = panel.getGrade();
                int creditHours = panel.getCreditHours();
                
                double gradePoints = convertGradeToPoints(grade);
                totalGradePoints += gradePoints * creditHours;
                totalCreditHours += creditHours;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid credit hours for all courses", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (totalCreditHours > 0) {
            double gpa = totalGradePoints / totalCreditHours;
            gpaLabel.setText("GPA: " + df.format(gpa));
        } else {
            gpaLabel.setText("GPA: 0.00");
        }
    }
    
    private double convertGradeToPoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A+": return 4.0;
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "E": return 0.0;
            default: return 0.0;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            GPACalculator calculator = new GPACalculator();
            calculator.setVisible(true);
        });
    }
    
    private class CoursePanel extends JPanel {
        private JTextField nameField;
        private JComboBox<String> gradeComboBox;
        private JTextField creditHoursField;
        private GPACalculator parent;
        
        public CoursePanel(GPACalculator parent) {
            this.parent = parent;
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            setBorder(BorderFactory.createEtchedBorder());
            setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
            
            // Course name
            nameField = new JTextField(15);
            nameField.setToolTipText("Course name");
            
            // Grade dropdown
            String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "E"};
            gradeComboBox = new JComboBox<>(grades);
            gradeComboBox.setSelectedIndex(0);
            
            // Credit hours
            creditHoursField = new JTextField("3", 3);
            creditHoursField.setToolTipText("Credit hours");
            
            // Remove button
            JButton removeButton = new JButton("×");
            removeButton.setFont(new Font("Arial", Font.BOLD, 14));
            removeButton.setForeground(Color.RED);
            removeButton.setContentAreaFilled(false);
            removeButton.setBorder(BorderFactory.createEmptyBorder());
            removeButton.setToolTipText("Remove course");
            removeButton.addActionListener(e -> parent.removeCoursePanel(this));
            
            // Add components to panel
            add(new JLabel("Course:"));
            add(nameField);
            add(new JLabel("Grade:"));
            add(gradeComboBox);
            add(new JLabel("Credits:"));
            add(creditHoursField);
            add(removeButton);
        }
        
        public String getCourseName() {
            return nameField.getText();
        }
        
        public String getGrade() {
            return (String) gradeComboBox.getSelectedItem();
        }
        
        public int getCreditHours() throws NumberFormatException {
            return Integer.parseInt(creditHoursField.getText());
        }
    }
}