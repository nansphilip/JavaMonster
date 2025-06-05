package com.fantasyhospital.controller.ui;

import com.fantasyhospital.Simulation;
import com.fantasyhospital.config.StageManager;
import com.fantasyhospital.controller.ConsoleLogController;
import com.fantasyhospital.controller.CounterController;
import com.fantasyhospital.controller.HospitalStructureController;
import com.fantasyhospital.util.LogsUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ToolbarControllerTest {

    @Mock
    private ConsoleLogController consoleLogController;

    @Mock
    private Simulation simulation;

    @Mock
    private StageManager stageManager;

    @Mock
    private HospitalStructureController hospitalStructureController;

    @Mock
    private CounterController counterController;

    private ToolbarController controller;

    @BeforeEach
    void setUp() {
        controller = new ToolbarController(stageManager, simulation, consoleLogController, hospitalStructureController, counterController);
    }

    @Test
    void clearLog() {
        try (MockedStatic<LogsUtils> mockedLogsUtils = Mockito.mockStatic(LogsUtils.class)) {

            controller.clearLog(null);

            verify(consoleLogController).clearConsole();
            mockedLogsUtils.verify(LogsUtils::clearLogFile);
        }
    }
}

//@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
//class ToolBarClearLogButtonTest {
//
//    private ToolbarController controller;
//
//    private Button clearButton;
//
//    private Stage stage;
//
//    @Mock
//    private ConsoleLogController consoleLogController;
//
//    @BeforeEach
//    void setUp() {
//        controller = new ToolbarController(null, null, consoleLogController, null, null);
//    }
//
//    @Test
//    void clearLogButtonWithFXMLLoader(FxRobot robot) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/toolbar.fxml"));
//        Parent root = loader.load();
//
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        stage.show();
//
//        Button clearButton = (Button) root.lookup("Button[text='Clear Log']");
//        robot.clickOn(clearButton);
//
//        verify(consoleLogController).clearConsole();
//    }
//}