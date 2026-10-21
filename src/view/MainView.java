package view;
import controller.MainController;
import model.User;
import util.AppConstants;

import javax.swing.*;
import java.awt.*;

/**
 * MainView.java
 * Entry-point for the CampusPulse user interface.
 *
 * TODO [Ananya Raju] – Full UI and dashboard implementation
 *                      Responsible for rendering screens, menus, and forms.
 */
public class MainView {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private MainController controller;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public MainView() {
        frame = new JFrame(AppConstants.APP_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        frame.setContentPane(mainPanel);
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    // -------------------------------------------------------------------------
    // UI placeholder methods
    // TODO [Ananya Raju] – Implement all display methods below
    // -------------------------------------------------------------------------

    /**
     * Displays the main login screen.
     * TODO [Ananya Raju] – Implement login UI
     */
    
    /**
     * Displays the main login screen.
     * TODO [Ananya Raju] – Implement login UI
     */
    public void showLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(AppConstants.APP_NAME + " - Login",
                SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(22);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(22);

        JButton loginButton = new JButton("Login");
        JLabel statusLabel = new JLabel("Enter your email and password.");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        gbc.gridy++;
        panel.add(statusLabel, gbc);

        loginButton.addActionListener(e -> {
            if (controller == null) {
                showError("The controller has not been initialized.");
                return;
            }

            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both email and password.");
                return;
            }

            boolean success = controller.login(email, password);

            if (success) {
                User user = controller.getCurrentUser();

                if (user == null) {
                    showError("Login succeeded, but no user was returned.");
                    return;
                }

                String role = user.getRole();

                if (AppConstants.ROLE_STUDENT.equals(role)) {
                    showStudentDashboard();
                } else if (AppConstants.ROLE_VOLUNTEER.equals(role)) {
                    showVolunteerDashboard();
                } else if (AppConstants.ROLE_STAFF.equals(role)) {
                    showStaffDashboard();
                } else if (AppConstants.ROLE_ADMIN.equals(role)) {
                    showAdminPanel();
                } else {
                    showError("Unknown user role.");
                }
            } else {
                statusLabel.setText("Login failed. Please check your details.");
            }
        });

        mainPanel.add(panel, "LOGIN");
        cardLayout.show(mainPanel, "LOGIN");
        frame.setVisible(true);
    }


    /**
     * Displays the student dashboard.
     * TODO [Ananya Raju] – Implement student dashboard UI
     */
    
    /**
     * Displays the student dashboard.
     * TODO [Ananya Raju] – Implement student dashboard UI
     */
    public void showStudentDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                AppConstants.TITLE_STUDENT_DASHBOARD,
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton resourcesButton = new JButton("Browse Resource Catalog");
        resourcesButton.addActionListener(e -> showResourceCatalog());

        JButton requestsButton = new JButton("View Requests");
        requestsButton.addActionListener(e -> showRequestList());

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            if (controller != null) {
                controller.logout();
            }
            showLoginScreen();
        });

        buttonPanel.add(resourcesButton);
        buttonPanel.add(requestsButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(panel, "STUDENT_DASHBOARD");
        cardLayout.show(mainPanel, "STUDENT_DASHBOARD");
    }


    /**
     * Displays the volunteer dashboard.
     * TODO [Ananya Raju]   – Implement volunteer dashboard UI
     * TODO [Ashish K Biju] – Provide volunteer-specific data to populate the dashboard
     */
    
    /**
     * Displays the volunteer dashboard.
     * TODO [Ananya Raju]   – Implement volunteer dashboard UI
     * TODO [Ashish K Biju] – Provide volunteer-specific data to populate the dashboard
     */
    public void showVolunteerDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                AppConstants.TITLE_VOLUNTEER_DASHBOARD,
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JLabel message = new JLabel(
                "Welcome to your volunteer dashboard.",
                SwingConstants.CENTER
        );
        panel.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton requestsButton = new JButton("View Requests");
        requestsButton.addActionListener(e -> showRequestList());

        JButton resourcesButton = new JButton("Resource Catalog");
        resourcesButton.addActionListener(e -> showResourceCatalog());

        JButton logoutButton = new JButton(AppConstants.LABEL_LOGOUT);
        logoutButton.addActionListener(e -> {
            if (controller != null) {
                controller.logout();
            }
            showLoginScreen();
        });

        buttonPanel.add(requestsButton);
        buttonPanel.add(resourcesButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "VOLUNTEER_DASHBOARD");
        cardLayout.show(mainPanel, "VOLUNTEER_DASHBOARD");
    }


    /**
     * Displays the staff dashboard.
     * TODO [Ananya Raju]     – Implement staff dashboard UI
     * TODO [Ardra K Gireesh] – Provide resource catalog data to populate the dashboard
     */
    
    /**
     * Displays the staff dashboard.
     * TODO [Ananya Raju]     – Implement staff dashboard UI
     * TODO [Ardra K Gireesh] – Provide resource catalog data to populate the dashboard
     */
    public void showStaffDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                AppConstants.TITLE_STAFF_DASHBOARD,
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JLabel message = new JLabel(
                "Welcome to your staff dashboard.",
                SwingConstants.CENTER
        );
        panel.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton resourcesButton = new JButton("Resource Catalog");
        resourcesButton.addActionListener(e -> showResourceCatalog());

        JButton requestsButton = new JButton("View Requests");
        requestsButton.addActionListener(e -> showRequestList());

        JButton logoutButton = new JButton(AppConstants.LABEL_LOGOUT);
        logoutButton.addActionListener(e -> {
            if (controller != null) {
                controller.logout();
            }
            showLoginScreen();
        });

        buttonPanel.add(resourcesButton);
        buttonPanel.add(requestsButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "STAFF_DASHBOARD");
        cardLayout.show(mainPanel, "STAFF_DASHBOARD");
    }


    /**
     * Displays the admin panel.
     * TODO [Ananya Raju] – Implement admin panel UI
     * TODO [Alan Saji]   – Provide admin-specific data and actions
     */
    
    /**
     * Displays the admin panel.
     * TODO [Ananya Raju] – Implement admin panel UI
     * TODO [Alan Saji]   – Provide admin-specific data and actions
     */
    public void showAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                AppConstants.TITLE_ADMIN_PANEL,
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JLabel message = new JLabel(
                "Welcome to the Admin Panel.",
                SwingConstants.CENTER
        );
        panel.add(message, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton resourcesButton = new JButton("Resource Catalog");
        resourcesButton.addActionListener(e -> showResourceCatalog());

        JButton requestsButton = new JButton("View Requests");
        requestsButton.addActionListener(e -> showRequestList());

        JButton logoutButton = new JButton(AppConstants.LABEL_LOGOUT);
        logoutButton.addActionListener(e -> {
            if (controller != null) {
                controller.logout();
            }
            showLoginScreen();
        });

        buttonPanel.add(resourcesButton);
        buttonPanel.add(requestsButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "ADMIN_PANEL");
        cardLayout.show(mainPanel, "ADMIN_PANEL");

        // Original placeholder:
        // System.out.println("[MainView] showAdminPanel() — not yet implemented");
    }


    /**
     * Displays the resource catalog browsing screen.
     * TODO [Ananya Raju]     – Implement resource catalog UI
     * TODO [Ardra K Gireesh] – Connect with ResourceController / resource data
     */
    
    /**
     * Displays the resource catalog browsing screen.
     * TODO [Ananya Raju]     – Implement resource catalog UI
     * TODO [Ardra K Gireesh] – Connect with ResourceController / resource data
     */
    public void showResourceCatalog() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                AppConstants.TITLE_RESOURCE_CATALOG,
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton(AppConstants.LABEL_SEARCH);

        searchPanel.add(new JLabel("Search resources:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton(AppConstants.LABEL_BACK);
        backButton.addActionListener(e -> {
            User user = controller != null
                    ? controller.getCurrentUser()
                    : null;

            if (user == null) {
                showLoginScreen();
            } else if (AppConstants.ROLE_STUDENT.equals(user.getRole())) {
                showStudentDashboard();
            } else if (AppConstants.ROLE_VOLUNTEER.equals(user.getRole())) {
                showVolunteerDashboard();
            } else if (AppConstants.ROLE_STAFF.equals(user.getRole())) {
                showStaffDashboard();
            } else if (AppConstants.ROLE_ADMIN.equals(user.getRole())) {
                showAdminPanel();
            } else {
                showLoginScreen();
            }
        });

        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "RESOURCE_CATALOG");
        cardLayout.show(mainPanel, "RESOURCE_CATALOG");

        // Original placeholder:
        // System.out.println("[MainView] showResourceCatalog() — not yet implemented");
    }


    /**
     * Displays a list of open requests (general / emergency).
     * TODO [Ananya Raju]   – Implement request list UI
     * TODO [Ashish K Biju] – Connect with request data
     */
    
    /**
     * Displays a list of open requests (general / emergency).
     * TODO [Ananya Raju]   – Implement request list UI
     * TODO [Ashish K Biju] – Connect with request data
     */
    public void showRequestList() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                AppConstants.TITLE_REQUEST_LIST,
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JTextArea requestArea = new JTextArea();
        requestArea.setEditable(false);
        requestArea.setLineWrap(true);
        requestArea.setWrapStyleWord(true);
        requestArea.setText(
                "Requests will appear here when request data is connected."
        );

        JScrollPane scrollPane = new JScrollPane(requestArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton(AppConstants.LABEL_BACK);
        backButton.addActionListener(e -> {
            User user = controller != null
                    ? controller.getCurrentUser()
                    : null;

            if (user == null) {
                showLoginScreen();
            } else if (AppConstants.ROLE_STUDENT.equals(user.getRole())) {
                showStudentDashboard();
            } else if (AppConstants.ROLE_VOLUNTEER.equals(user.getRole())) {
                showVolunteerDashboard();
            } else if (AppConstants.ROLE_STAFF.equals(user.getRole())) {
                showStaffDashboard();
            } else if (AppConstants.ROLE_ADMIN.equals(user.getRole())) {
                showAdminPanel();
            } else {
                showLoginScreen();
            }
        });

        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(panel, "REQUEST_LIST");
        cardLayout.show(mainPanel, "REQUEST_LIST");

        // Original placeholder:
        // System.out.println("[MainView] showRequestList() — not yet implemented");
    }


    /**
     * Shows an informational message dialog.
     * TODO [Ananya Raju] – Implement message display
     *
     * @param message the message to display
     */
    
    /**
     * Shows an informational message dialog.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                AppConstants.APP_NAME,
                JOptionPane.INFORMATION_MESSAGE
        );

        // Original placeholder:
        // System.out.println("[MainView] Message: " + message);
    }

    /**
     * Shows an error message dialog.
     * TODO [Ananya Raju] – Implement error display
     *
     * @param error the error description
     */
    
    /**
     * Shows an error message dialog.
     *
     * @param error the error description
     */
    public void showError(String error) {
        JOptionPane.showMessageDialog(
                frame,
                error,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        // Original placeholder:
        // System.err.println("[MainView] Error: " + error);
    }

}