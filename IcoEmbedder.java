import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IcoEmbedder {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Upload File");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(175,175);
            frame.setLayout(null);

            final File[] icoFile = {null};
            final File[] pdfFile = {null};
            final int[] offset = {0};

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBounds(0,0,150,200);

            JButton uploadBtn = new JButton("Upload .ico");
            JButton pdfBtn = new JButton("Upload .pdf");
            JButton embedButton = new JButton("Embed Files");

            uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            pdfBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            embedButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(Box.createVerticalStrut(10));
            panel.add(uploadBtn);
            panel.add(Box.createVerticalStrut(10));
            panel.add(pdfBtn);
            panel.add(Box.createVerticalStrut(10));
            panel.add(embedButton);

            frame.add(panel);

            uploadBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select a .ico file");
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Icon Files", "ico"));

                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    icoFile[0] = chooser.getSelectedFile();
                }
            });

            pdfBtn.addActionListener(e -> {
                JFileChooser pdFileChooser = new JFileChooser();
                pdFileChooser.setDialogTitle("Select a .pdf file");
                pdFileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Pdf Files", "pdf"));


                if (pdFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    pdfFile[0] = pdFileChooser.getSelectedFile();

                    try {
                        byte[] pdfData = Files.readAllBytes(pdfFile[0].toPath());
                        offset[0] = pdfData.length + 22;

                        JOptionPane.showMessageDialog(frame,
                        "Pdf uploaded");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error reading PDF: " + ex.getMessage());
                    }

                }
            });

            embedButton.addActionListener(e -> {

                if (icoFile[0] == null || pdfFile[0] == null) {
                    JOptionPane.showMessageDialog(frame,
                    "Please upload both ICO and PDF files!");
                    return;
                }
                try {
                    byte[] icoData = Files.readAllBytes(icoFile[0].toPath());
                    byte[] pdfData = Files.readAllBytes(pdfFile[0].toPath());

                    int pdfOffset = pdfData.length + 22;

                    byte[] offsetBytes = ByteBuffer.allocate(4)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putInt(pdfOffset)
                        .array();
                    System.arraycopy(offsetBytes, 0, icoData, 18, 4);

                    File newIco = new File(icoFile[0].getParent(), "embedded_" + icoFile[0].getName());
                    try (FileOutputStream fos = new FileOutputStream(newIco)){
                        fos.write(icoData, 0, 22);
                        fos.write(pdfData);
                        fos.write(icoData, 22, icoData.length - 22);
                    }
                    JOptionPane.showMessageDialog(frame, "Embedded ICO created");
                    

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error embedding files: " + ex.getMessage());
                }
            });
            

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.setVisible(true);
        });
    }
}