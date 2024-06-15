import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class generadorContrasena extends JFrame {

    private static final String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMEROS = "0123456789";
    private static final String SIMBOLOS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/";
    private static final String AMBIGUOS = "0O1lI|";

    private JTextField longitudField;
    private JTextField cantidadField;
    private JCheckBox incluirMayusculas;
    private JCheckBox incluirMinusculas;
    private JCheckBox incluirNumeros;
    private JCheckBox incluirSimbolos;
    private JCheckBox evitarAmbiguos;
    private JTextArea resultadoArea;

    public generadorContrasena() {
        setTitle("Generador de Contraseñas");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new GridLayout(7, 2));

        panelConfiguracion.add(new JLabel("Longitud de la contraseña:"));
        longitudField = new JTextField();
        panelConfiguracion.add(longitudField);

        panelConfiguracion.add(new JLabel("Cantidad de contraseñas:"));
        cantidadField = new JTextField();
        panelConfiguracion.add(cantidadField);

        incluirMayusculas = new JCheckBox("Incluir mayúsculas");
        panelConfiguracion.add(incluirMayusculas);

        incluirMinusculas = new JCheckBox("Incluir minúsculas");
        panelConfiguracion.add(incluirMinusculas);

        incluirNumeros = new JCheckBox("Incluir números");
        panelConfiguracion.add(incluirNumeros);

        incluirSimbolos = new JCheckBox("Incluir símbolos");
        panelConfiguracion.add(incluirSimbolos);

        evitarAmbiguos = new JCheckBox("Evitar caracteres ambiguos");
        panelConfiguracion.add(evitarAmbiguos);

        add(panelConfiguracion, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton generarButton = new JButton("Generar");
        generarButton.setPreferredSize(new Dimension(100, 30)); // Establece el tamaño preferido del botón
        generarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarContraseñas();
            }
        });
        buttonPanel.add(generarButton);

        JButton exportarButton = new JButton("Exportar a TXT");
        exportarButton.setPreferredSize(new Dimension(120, 30)); // Establece el tamaño preferido del botón
        exportarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportarATexto();
            }
        });
        buttonPanel.add(exportarButton);

        add(buttonPanel, BorderLayout.CENTER);

        resultadoArea = new JTextArea(10, 30); // Aumenta el tamaño del área de texto
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void generarContraseñas() {
        try {
            int longitud = Integer.parseInt(longitudField.getText());
            int cantidad = Integer.parseInt(cantidadField.getText());
            boolean mayusculas = incluirMayusculas.isSelected();
            boolean minusculas = incluirMinusculas.isSelected();
            boolean numeros = incluirNumeros.isSelected();
            boolean simbolos = incluirSimbolos.isSelected();
            boolean ambiguos = evitarAmbiguos.isSelected();

            List<String> contraseñas = generarContrasenas(cantidad, longitud, mayusculas, minusculas, numeros, simbolos, ambiguos);
            StringBuilder resultado = new StringBuilder();
            for (int i = 0; i < contraseñas.size(); i++) {
                resultado.append("Contraseña ").append(i + 1).append(": ").append(contraseñas.get(i)).append("\n");
            }
            resultadoArea.setText(resultado.toString());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarATexto() {
        String resultado = resultadoArea.getText();
        if (resultado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay contraseñas para exportar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile() + ".txt")) {
                writer.write(resultado);
                JOptionPane.showMessageDialog(this, "Contraseñas exportadas con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al exportar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static List<String> generarContrasenas(int cantidad, int longitud, boolean incluirMayusculas, boolean incluirMinusculas, boolean incluirNumeros, boolean incluirSimbolos, boolean evitarAmbiguos) {
        List<String> contraseñas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            contraseñas.add(generarContrasena(longitud, incluirMayusculas, incluirMinusculas, incluirNumeros, incluirSimbolos, evitarAmbiguos));
        }
        return contraseñas;
    }

    private static String generarContrasena(int longitud, boolean incluirMayusculas, boolean incluirMinusculas, boolean incluirNumeros, boolean incluirSimbolos, boolean evitarAmbiguos) {
        StringBuilder caracteres = new StringBuilder();

        if (incluirMayusculas) {
            caracteres.append(MAYUSCULAS);
        }
        if (incluirMinusculas) {
            caracteres.append(MINUSCULAS);
        }
        if (incluirNumeros) {
            caracteres.append(NUMEROS);
        }
        if (incluirSimbolos) {
            caracteres.append(SIMBOLOS);
        }

        if (evitarAmbiguos) {
            for (char c : AMBIGUOS.toCharArray()) {
                int index = caracteres.indexOf(String.valueOf(c));
                if (index != -1) {
                    caracteres.deleteCharAt(index);
                }
            }
        }

        if (caracteres.length() == 0) {
            throw new IllegalArgumentException("Debe incluir al menos un tipo de caracteres.");
        }

        Random random = new Random();
        StringBuilder contrasena = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            contrasena.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return contrasena.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new generadorContrasena().setVisible(true);
            }
        });
    }
}
