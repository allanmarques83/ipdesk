package client;

import javax.swing.UIManager;
import javax.swing.ToolTipManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import client.gui.Stage;

public class Main
{
    public Main() {
        
        applyNimbusLookAndFeel();
        
        new Stage();
    }

    private void applyNimbusLookAndFeel() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        new Main();
    }
}