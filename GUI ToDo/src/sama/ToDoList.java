package sama;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ToDoList extends JFrame {
    JTextField taskField;
    JButton addButton, settingsButton;
    JPanel tasksPanel;
    JLabel doneLabel;
    JScrollPane scrollPane;
    Font currentFont = new Font("Arial", Font.PLAIN, 16);

    JList<String> tipList;
    JTextArea tipArea;
    DefaultListModel<String> listModel;

    public ToDoList() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 400);
        this.setLayout(new BorderLayout());
        this.setTitle("To_Do List");

        // Menu setup
        JMenuBar menu = new JMenuBar();
        JMenu help = new JMenu("Edit");
        JMenu edit = new JMenu("Help");

        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem clearItem = new JMenuItem("Clear All Tasks");
        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem howToItem = new JMenuItem("How to Use");

        help.add(exitItem);
        help.add(clearItem);
        edit.add(aboutItem);
        edit.add(howToItem);
        menu.add(edit);
        menu.add(help);
        this.setJMenuBar(menu);

        exitItem.addActionListener(e -> System.exit(0));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "To-Do List App\nDeveloped by Eng.Sama Ahmed",
                "By CSIS O6U University", JOptionPane.INFORMATION_MESSAGE));
        howToItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                """
                1) Add tasks from the text field.
                2) Click a task to mark it as done.
                3) Use 'Clear' to remove all tasks.
                4) Use Settings to change fonts/background.
                """, "How to Use", JOptionPane.INFORMATION_MESSAGE));
        clearItem.addActionListener(e -> {
            tasksPanel.removeAll();
            tasksPanel.revalidate();
            tasksPanel.repaint();
            doneLabel.setText("All tasks cleared.");
        });

        // Input panel
        taskField = new JTextField("Enter Task");
        taskField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (taskField.getText().equals("Enter Task")) {
                    taskField.setText("");
                }
            }
        });
        addButton = new JButton("Add");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        settingsButton = new JButton("Settings");

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(taskField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(settingsButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(inputPanel, BorderLayout.NORTH);

        // Tasks panel
        tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(tasksPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Tasks"));

        JButton removeDoneButton = new JButton("Remove Done");
        removeDoneButton.setBackground(new Color(231, 76, 60));
        removeDoneButton.setForeground(Color.WHITE);
        removeDoneButton.setFont(new Font("Arial", Font.BOLD, 14));
        removeDoneButton.addActionListener(e -> removeDoneTasks());

        // Tips List and TextArea
        listModel = new DefaultListModel<>();
        listModel.addElement("Tip 1");
        listModel.addElement("Tip 2");
        listModel.addElement("Tip 3");

        tipList = new JList<>(listModel);
        tipList.setBorder(BorderFactory.createTitledBorder("Tips"));
        tipList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tipArea = new JTextArea();
        tipArea.setEditable(false);
        tipArea.setLineWrap(true);
        tipArea.setWrapStyleWord(true);
        tipArea.setFont(new Font("Serif", Font.PLAIN, 14));
        tipArea.setBorder(BorderFactory.createTitledBorder("Tip Detail"));
        tipArea.setPreferredSize(new Dimension(200, 100));

        tipList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = tipList.getSelectedValue();
                switch (selected) {
                    case "Tip 1" -> tipArea.setText("Start your day by identifying the top 3 tasks.");
                    case "Tip 2" -> tipArea.setText("Review completed tasks at the end of the day.");
                    case "Tip 3" -> tipArea.setText("Break large tasks into smaller steps.");
                    default -> tipArea.setText("");
                }
            }
        });

        JPanel tipPanel = new JPanel(new BorderLayout(5, 5));
        tipPanel.add(new JScrollPane(tipList), BorderLayout.NORTH);
        tipPanel.add(new JScrollPane(tipArea), BorderLayout.CENTER);

        // Center layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(removeDoneButton, BorderLayout.SOUTH);
        centerPanel.add(tipPanel, BorderLayout.EAST);
        this.add(centerPanel, BorderLayout.CENTER);

        // Done Label
        doneLabel = new JLabel(" ");
        doneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        doneLabel.setForeground(new Color(39, 174, 96));
        doneLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(doneLabel, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> addTask());
        settingsButton.addActionListener(e -> openSettings());

        this.setVisible(true);
    }

    private void addTask() {
        String text = taskField.getText().trim();
        if (!text.isEmpty()) {
            JCheckBox checkBox = new JCheckBox(text);
            checkBox.setFont(currentFont);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    doneLabel.setText("Mission Done: " + checkBox.getText());
                } else {
                    doneLabel.setText(" ");
                }
            });
            tasksPanel.add(checkBox);
            taskField.setText("");
            tasksPanel.revalidate();
            tasksPanel.repaint();
        }
    }

    private void removeDoneTasks() {
        Component[] components = tasksPanel.getComponents();
        java.util.List<Component> toRemove = new ArrayList<>();

        for (Component comp : components) {
            if (comp instanceof JCheckBox cb && cb.isSelected()) {
                toRemove.add(cb);
            }
        }

        for (Component c : toRemove) {
            tasksPanel.remove(c);
        }

        tasksPanel.revalidate();
        tasksPanel.repaint();
        doneLabel.setText(" ");
    }

    private void openSettings() {
        JFrame settings = new JFrame("Settings");
        settings.setSize(350, 200);
        settings.setLayout(new GridLayout(3, 1, 10, 10));
        settings.setLocationRelativeTo(this);

        JSlider fontSlider = new JSlider(12, 30, currentFont.getSize());
        fontSlider.setPaintTicks(true);
        fontSlider.setPaintLabels(true);
        fontSlider.setMajorTickSpacing(6);
        fontSlider.setBorder(BorderFactory.createTitledBorder("Font Size"));

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontBox = new JComboBox<>(fonts);
        fontBox.setSelectedItem(currentFont.getFamily());
        fontBox.setBorder(BorderFactory.createTitledBorder("Font Family"));

        String[] colors = {"White", "Light Gray", "Pink", "Cyan", "Yellow"};
        JComboBox<String> colorBox = new JComboBox<>(colors);
        colorBox.setBorder(BorderFactory.createTitledBorder("Background Color"));

        fontSlider.addChangeListener(e ->
            updateFont(fontBox.getSelectedItem().toString(), fontSlider.getValue()));

        fontBox.addActionListener(e ->
            updateFont(fontBox.getSelectedItem().toString(), fontSlider.getValue()));

        colorBox.addActionListener(e ->
            updateBackground(colorBox.getSelectedItem().toString()));

        settings.add(fontSlider);
        settings.add(fontBox);
        settings.add(colorBox);
        settings.setVisible(true);
    }

    private void updateFont(String name, int size) {
        currentFont = new Font(name, Font.PLAIN, size);
        for (Component comp : tasksPanel.getComponents()) {
            if (comp instanceof JCheckBox cb) {
                cb.setFont(currentFont);
            }
        }
        doneLabel.setFont(new Font(name, Font.BOLD, 16));
    }

    private void updateBackground(String color) {
        Color bg;
        switch (color) {
            case "Light Gray" -> bg = Color.LIGHT_GRAY;
            case "Pink" -> bg = Color.PINK;
            case "Cyan" -> bg = Color.CYAN;
            case "Yellow" -> bg = Color.YELLOW;
            default -> bg = Color.WHITE;
        }
        this.getContentPane().setBackground(bg);
        tasksPanel.setBackground(bg);
        scrollPane.getViewport().setBackground(bg);
    }

    public static void main(String[] args) {
        new ToDoList();
    }
}
