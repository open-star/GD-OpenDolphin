/*
 * RunApp.java
 */

package run;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class RunApp extends SingleFrameApplication {

	public RunApp() {
		try {
            //起動チェック
            final FileOutputStream fos = new FileOutputStream(new File("lock"));
            final FileChannel fc = fos.getChannel();
            final FileLock lock = fc.tryLock();

            if (lock == null) {
                JOptionPane.showMessageDialog(null, "既に OpenDolphin は起動しています.", "エラー", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                //ロック開放処理を登録
                Runtime.getRuntime().addShutdownHook(
                        new Thread() {

                            @Override
                            public void run() {
                                try {
                                    if (lock != null && lock.isValid()) {
                                        lock.release();
                                        fc.close();
                                        fos.close();
                                    }
                                } catch (IOException ex) {
                                    System.out.println(ex.toString());
                                }
                            }
                        });
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
	}

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new RunView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of RunApp
     */
    public static RunApp getApplication() {
        return Application.getInstance(RunApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(RunApp.class, args);
    }
}
