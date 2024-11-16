package com.finanzas.controlador;

import com.finanzas.modelo.Transaccion;
import com.finanzas.modelo.TipoTransaccion;
import com.finanzas.modelo.Resumen;
import com.finanzas.servicio.FinanzasService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class MainController implements Initializable {
    
    @FXML private TextField montoField;
    @FXML private ComboBox<TipoTransaccion> tipoComboBox;
    @FXML private ComboBox<String> categoriaComboBox;
    @FXML private Label balanceLabel;
    @FXML private Label ingresosLabel;
    @FXML private Label gastosLabel;
    @FXML private TableView<Transaccion> transaccionesTable;
    @FXML private TableColumn<Transaccion, LocalDateTime> fechaColumn;
    @FXML private TableColumn<Transaccion, String> tipoColumn;
    @FXML private TableColumn<Transaccion, BigDecimal> montoColumn;
    @FXML private TableColumn<Transaccion, String> categoriaColumn;

    private final FinanzasService finanzasService = new FinanzasService();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar ComboBox de tipo
        tipoComboBox.setItems(FXCollections.observableArrayList(TipoTransaccion.values()));
        tipoComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TipoTransaccion tipo) {
                return tipo == null ? null : tipo.getDescripcion();
            }

            @Override
            public TipoTransaccion fromString(String string) {
                return null; // No necesario para este caso
            }
        });

        // Configurar las categorías según el tipo seleccionado
        tipoComboBox.setOnAction(e -> actualizarCategorias());

        // Configurar columnas de la tabla
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        fechaColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime fecha, boolean empty) {
                super.updateItem(fecha, empty);
                if (empty || fecha == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(fecha));
                }
            }
        });

        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        montoColumn.setCellValueFactory(new PropertyValueFactory<>("monto"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Formatear montos con color según tipo
        montoColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal monto, boolean empty) {
                super.updateItem(monto, empty);
                if (empty || monto == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    Transaccion transaccion = getTableView().getItems().get(getIndex());
                    setText(String.format("$%.2f", monto));
                    setTextFill(transaccion.getTipo() == TipoTransaccion.INGRESO ? 
                               Color.GREEN : Color.RED);
                }
            }
        });

        // Cargar datos iniciales
        actualizarTabla();
        actualizarResumen();
    }

    @FXML
    private void agregarTransaccion() {
        try {
            // Validar entrada
            if (montoField.getText().isEmpty() || tipoComboBox.getValue() == null || 
                categoriaComboBox.getValue() == null) {
                mostrarError("Por favor complete todos los campos");
                return;
            }

            BigDecimal monto = new BigDecimal(montoField.getText());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarError("El monto debe ser mayor que cero");
                return;
            }

            // Crear y guardar la transacción
            Transaccion transaccion = new Transaccion(
                monto,
                tipoComboBox.getValue(),
                categoriaComboBox.getValue(),
                ""  // Descripción opcional
            );

            finanzasService.agregarTransaccion(transaccion);

            // Limpiar campos y actualizar vista
            limpiarCampos();
            actualizarTabla();
            actualizarResumen();

        } catch (NumberFormatException e) {
            mostrarError("Por favor ingrese un monto válido");
        } catch (Exception e) {
            mostrarError("Error al agregar la transacción: " + e.getMessage());
        }
    }

    @FXML
    private void mostrarResumenMensual() {
        Resumen resumen = finanzasService.obtenerResumenMensual(
            LocalDateTime.now().getMonthValue(),
            LocalDateTime.now().getYear()
        );
        mostrarResumenDialog(resumen, "Resumen Mensual");
    }

    @FXML
    private void mostrarResumenAnual() {
        Resumen resumen = finanzasService.obtenerResumenAnual(
            LocalDateTime.now().getYear()
        );
        mostrarResumenDialog(resumen, "Resumen Anual");
    }

    private void actualizarCategorias() {
        if (tipoComboBox.getValue() == null) {
            categoriaComboBox.setItems(FXCollections.observableArrayList());
            return;
        }

        categoriaComboBox.setItems(FXCollections.observableArrayList(
            tipoComboBox.getValue() == TipoTransaccion.INGRESO ?
            FinanzasService.CATEGORIAS_INGRESO :
            FinanzasService.CATEGORIAS_GASTO
        ));
    }

    private void actualizarTabla() {
        ObservableList<Transaccion> transacciones = FXCollections.observableArrayList(
            finanzasService.obtenerUltimasTransacciones(10)
        );
        transaccionesTable.setItems(transacciones);
    }

    private void actualizarResumen() {
        Resumen resumen = finanzasService.obtenerResumenMensual(
            LocalDateTime.now().getMonthValue(),
            LocalDateTime.now().getYear()
        );

        balanceLabel.setText(String.format("$%.2f", resumen.getBalance()));
        balanceLabel.setTextFill(resumen.getBalance().compareTo(BigDecimal.ZERO) >= 0 ? 
                                Color.GREEN : Color.RED);

        ingresosLabel.setText(String.format("$%.2f", resumen.getTotalIngresos()));
        gastosLabel.setText(String.format("$%.2f", resumen.getTotalGastos()));
    }

    private void limpiarCampos() {
        montoField.clear();
        tipoComboBox.setValue(null);
        categoriaComboBox.setValue(null);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarResumenDialog(Resumen resumen, String titulo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        
        String contenido = String.format("""
            Total Ingresos: $%.2f
            Total Gastos: $%.2f
            Balance: $%.2f
            
            Desglose de Ingresos:
            %s
            
            Desglose de Gastos:
            %s
            """,
            resumen.getTotalIngresos(),
            resumen.getTotalGastos(),
            resumen.getBalance(),
            formatearDesglose(resumen.getIngresosPorCategoria()),
            formatearDesglose(resumen.getGastosPorCategoria())
        );
        
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private String formatearDesglose(Map<String, BigDecimal> desglose) {
        StringBuilder sb = new StringBuilder();
        desglose.forEach((categoria, monto) -> 
            sb.append(String.format("  %s: $%.2f%n", categoria, monto))
        );
        return sb.toString();
    }
}