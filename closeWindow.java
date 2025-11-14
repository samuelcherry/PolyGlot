import javax.swing.*;
import java.io.File;

public class closeWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Upload File");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400,300);
            frame.setLayout(null);

            JButton uploadBtn = new JButton("Upload .ico");
            uploadBtn.setBounds(50,50,150,40);

            uploadBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select a .ico file");
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Icon Files", "ico"));

                int result = chooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selected = chooser.getSelectedFile();
                    try {
                        byte[] data = java.nio.file.Files.readAllBytes(selected.toPath());
                        int length = Math.min(22, data.length);
                        StringBuilder hex = new StringBuilder();
                        for (int i = 0; i < length; i++) {
                            hex.append(String.format("%02X ", data[i]));
                        }
                        JOptionPane.showMessageDialog(frame,
                        "First" + length + "bytes in hex \n" + hex.toString(),
                        "Hex Preview",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                        "Error reading files: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton pdfBtn = new JButton("Upload PDF");
            pdfBtn.setBounds(50,120,150,40);
            pdfBtn.addActionListener(e -> {
                JFileChooser pdFileChooser = new JFileChooser();
                pdFileChooser.setDialogTitle("Select a .pdf file");
                pdFileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Pdf Files", "pdf"));

                int result = pdFileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selected = pdFileChooser.getSelectedFile();

                    try {
                        byte[] data = java.nio.file.Files.readAllBytes(selected.toPath());
                        String offset = String.format("%02X ", data.length+22);

                        JOptionPane.showMessageDialog(frame,
                        "Size of PDF: "  + data.length + " bytes \n"
                        + "with an offset of " + offset,
                        
                        "Hex Preview",
                        JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,
                        "Error reading file: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    }

                }
            });

            frame.add(uploadBtn);
            frame.add(pdfBtn);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}