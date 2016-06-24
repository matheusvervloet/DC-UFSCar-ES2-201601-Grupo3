package net.sf.jabref.es2test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;

import net.sf.jabref.JabRefMain;
import net.sf.jabref.gui.AWTExceptionHandler;
import net.sf.jabref.gui.ImportInspectionDialog;
import net.sf.jabref.gui.JabRefFrame;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.dependency.jsr305.Nonnull;
import org.assertj.swing.fixture.AbstractWindowFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.image.ScreenshotTaker;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import static org.assertj.swing.finder.WindowFinder.findDialog;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

public class ImportDuplicateTest extends AssertJSwingJUnitTestCase {

    private AWTExceptionHandler awtExceptionHandler;

    @Override
    protected void onSetUp() {
        awtExceptionHandler = new AWTExceptionHandler();
        awtExceptionHandler.installExceptionDetectionInEDT();
        application(JabRefMain.class).start();

        robot().waitForIdle();

        robot().settings().timeoutToFindSubMenu(1_000);
        robot().settings().delayBetweenEvents(50);
    }

    private void exitJabRef(FrameFixture mainFrame) {
        mainFrame.menuItemWithPath("File", "Quit").click();
        awtExceptionHandler.assertNoExceptions();
    }

    private String getTestFilePath(String fileName) {
        return new File(this.getClass().getClassLoader().getResource(fileName).getFile()).getAbsolutePath();
    }

    private void importBibIntoNewDatabase(FrameFixture mainFrame, String path) {
        // have to replace backslashes with normal slashes b/c assertJ can't type the former one on windows
        path = path.replace("\\", "/");

        mainFrame.menuItemWithPath("File", "Import into new database").click();
        JFileChooserFixture openFileDialog = mainFrame.fileChooser();
        robot().settings().delayBetweenEvents(1);
        openFileDialog.fileNameTextBox().enterText(path);
        robot().settings().delayBetweenEvents(1_000);
        openFileDialog.approve();
        robot().settings().delayBetweenEvents(50);
    }

    private void importBibIntoCurrentDatabase(FrameFixture mainFrame, String path) {
        // have to replace backslashes with normal slashes b/c assertJ can't type the former one on windows
        path = path.replace("\\", "/");

        mainFrame.menuItemWithPath("File", "Import into current database").click();
        JFileChooserFixture openFileDialog = mainFrame.fileChooser();
        robot().settings().delayBetweenEvents(1);
        openFileDialog.fileNameTextBox().enterText(path);
        robot().settings().delayBetweenEvents(1_000);
        openFileDialog.approve();
        robot().settings().delayBetweenEvents(50);
    }

    private void saveDatabase(FrameFixture mainFrame, String path) {
        // have to replace backslashes with normal slashes b/c assertJ can't type the former one on windows
        path = path.replace("\\", "/");

        mainFrame.menuItemWithPath("File", "Save database").click();
        JFileChooserFixture openFileDialog = mainFrame.fileChooser();
        robot().settings().delayBetweenEvents(1);
        openFileDialog.fileNameTextBox().enterText(path);
        robot().settings().delayBetweenEvents(1_000);
        openFileDialog.approve();
        robot().settings().delayBetweenEvents(50);
    }

    @Test
    public void testDuplicateEntries() throws IOException {
        FrameFixture mainFrame = findFrame(JabRefFrame.class).withTimeout(10_000).using(robot());
        String path = getTestFilePath("net/sf/jabref/es2test/es2bib.bib");
        importBibIntoNewDatabase(mainFrame, path);
        importBibIntoCurrentDatabase(mainFrame, path);

        DialogFixture importInspectionDialog = findDialog(ImportInspectionDialog.class).withTimeout(10_000)
                .using(robot());
        takeScreenshot(importInspectionDialog, "ImportInspectionDialog1");

        importInspectionDialog.button(new GenericTypeMatcher<JButton>(JButton.class) {

                    @Override
                    protected boolean isMatching(@Nonnull JButton jButton) {
                        return "OK".equals(jButton.getText());
                    }
                }).click();
        robot().settings().delayBetweenEvents(100);
        takeScreenshot(importInspectionDialog, "ImportInspectionDialog2");
        importInspectionDialog.button(new GenericTypeMatcher<JButton>(JButton.class) {

                    @Override
                    protected boolean isMatching(@Nonnull JButton jButton) {
                        return "No".equals(jButton.getText());
                    }
                }).click();

        takeScreenshot(mainFrame, "MainFrame");

        exitJabRef(mainFrame);
    }

    private void takeScreenshot(AbstractWindowFixture<?, ?, ?> dialog, String filename) throws IOException {
        ScreenshotTaker screenshotTaker = new ScreenshotTaker();
        String path = getTestFilePath("net/sf/jabref/es2test/es2bib.bib");
        Path folder = Paths.get(path.substring(0, path.length() - 10));
        // Create build/srceenshots folder if not present
        if (!Files.exists(folder)) {
            Files.createDirectory(folder);
        }
        Path file = folder.resolve(filename + ".png").toAbsolutePath();
        // Delete already present file
        if (Files.exists(file)) {
            Files.delete(file);
        }
        screenshotTaker.saveComponentAsPng(dialog.target(), file.toString());
    }
}
