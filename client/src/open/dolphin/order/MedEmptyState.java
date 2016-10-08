package open.dolphin.order;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * EmptyState
 * 
 * @author Minagawa,Kazushi
 *
 */
public class MedEmptyState extends AbstractMedTableState {

    /**
     *
     * @param setTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     * @param stateLabel
     */
    public MedEmptyState(JTable setTable, JButton deleteBtn, JButton clearBtn,
            JTextField stampNameField, JLabel stateLabel) {
        super(setTable, deleteBtn, clearBtn, stampNameField, stateLabel);
    }

    /**
     *
     */
    @Override
    public void enter() {
        deleteBtn.setEnabled(false);
        clearBtn.setEnabled(false);
        stampNameField.setText(PharmaceuticalsTablePanel.DEFAULT_STAMP_NAME);
        stateLabel.setText("医薬品を入力してください。");
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean isValidModel() {
        return false;
    }
}
