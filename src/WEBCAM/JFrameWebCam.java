package WEBCAM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.sarxos.webcam.Webcam;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

/**
 * @author Marcos Vinicius Angeli Costa.
 *
 * @mail marcosvinicios4132@gmail.com
 */
public class JFrameWebCam {

	private JFrame frame;

	//Busca WebCans
	private WebCamProvider webCamProvider;
	
	// Lbl onde será mostrada a imagem
	private JLabel fotoWebCam;

	// Lbls ppra representarem os botões
	private JPanel panelStart;
	private JLabel lblStart;
	
	private JPanel panelStop;
	private JLabel lblStop;
	
	private JPanel panelCaptura;
	private JLabel lblCaptura;
	
//	private JComboBox comboBoxWebCam;
	public static JComboBox comboBoxWebCam;
	
	//Instancia da webCam
	private static Webcam webCam;
	private JTextField textFieldPathImagem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new FlatDarkLaf());
					JFrameWebCam window = new JFrameWebCam();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JFrameWebCam() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 333, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().setBackground(new Color(92, 92, 92));
		frame.getContentPane().add(getLblFotoWebCam());
		frame.getContentPane().add(getPanelStart());
		frame.getContentPane().add(getPanelStop());
		frame.getContentPane().add(getPanelCaptura());
		frame.getContentPane().add(getComboBoxWebCam());
		frame.getContentPane().add(getTextFieldPath());
	}

	private JLabel getLblFotoWebCam() {
		if (fotoWebCam == null) {
			fotoWebCam = new JLabel("");
			fotoWebCam.setHorizontalAlignment(SwingConstants.CENTER);
			fotoWebCam.setBounds(10, 11, 297, 209);
			fotoWebCam.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		}
		return fotoWebCam;
	}
	
	/**************************************
	 * 
	 * Inicializa os componentes responsáveis
	 * 		 pela captura da imagem
	 *
	 **************************************/
	private JPanel getPanelStart() {
		if(panelStart == null) {
			panelStart = new JPanel();
			panelStart.setBackground(new Color(92, 92, 92));
			panelStart.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					changeColor(panelStart, new Color(152, 250, 172));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					changeColor(panelStart, new Color(18, 138, 42));
					webCamProvider.ligaWebCam(fotoWebCam, webCam);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					changeColor(panelStart, new Color(92, 92, 92));
				}
			});
			panelStart.setBounds(10, 227, 35, 35);
			panelStart.setLayout(new BorderLayout(0, 0));
			panelStart.add(getLblStart(), BorderLayout.CENTER);
		}
		return panelStart;
	}
	
	private JLabel getLblStart() {
		if (lblStart == null) {
			lblStart = new JLabel("");
			lblStart.setIcon(new ImageIcon("./db/resources/images/start.png"));
			lblStart.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblStart;
	}
	
	/**************************************
	 * 
	 * 		   captura A imagem
	 *
	 **************************************/
	private JPanel getPanelCaptura() {
		if(panelCaptura == null) {
			panelCaptura = new JPanel();
			panelCaptura.setBackground(new Color(92, 92, 92));
			panelCaptura.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					changeColor(panelCaptura, new Color(152, 250, 172));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					changeColor(panelCaptura, new Color(18, 138, 42));
					webCamProvider.capturaImagem(textFieldPathImagem.getText());
				}
				@Override
				public void mouseExited(MouseEvent e) {
					changeColor(panelCaptura, new Color(92, 92, 92));
				}
			});
			panelCaptura.setBounds(274, 286, 35, 35);
			panelCaptura.setLayout(new BorderLayout(0, 0));
			panelCaptura.add(getLblCaptura(), BorderLayout.CENTER);
		}
		return panelCaptura;
	}
	
	private JLabel getLblCaptura() {
		if (lblCaptura == null) {
			lblCaptura = new JLabel("");
			lblCaptura.setIcon(new ImageIcon("./db/resources/images/takePhoto.png"));
			lblCaptura.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblCaptura;
	}
	
	/**************************************
	 * 
	 * Finaliza os componentes responsáveis
	 * 		 pela captura da imagem
	 *
	 **************************************/
	private JPanel getPanelStop() {
		if(panelStop == null) {
			panelStop = new JPanel();
			panelStop.setBackground(new Color(92, 92, 92));
			panelStop.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					changeColor(panelStop, new Color(242, 133, 133));
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					changeColor(panelStop, new Color(181, 40, 40));
					webCamProvider.closeAndClearComponents(fotoWebCam).start();
				}
				@Override
				public void mouseExited(MouseEvent e) {
					changeColor(panelStop, new Color(92, 92, 92));
				}
			});
			panelStop.setBounds(51, 227, 35, 35);
			panelStop.setLayout(new BorderLayout(0, 0));
			panelStop.add(getLblStop(), BorderLayout.CENTER);
		}
		return panelStop;
	}
	
	private JLabel getLblStop() {
		if (lblStop == null) {
			lblStop = new JLabel("");
			lblStop.setIcon(new ImageIcon("./db/resources/images/stop.png"));
			lblStop.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblStop;
	}
	
	private JTextField getTextFieldPath() {
		if(textFieldPathImagem == null) {
			textFieldPathImagem = new JTextField();
			textFieldPathImagem.setBounds(89, 227, 218, 20);
			textFieldPathImagem.setColumns(10);
		}
		return textFieldPathImagem;
	}
	
	private JComboBox getComboBoxWebCam() {
		if(comboBoxWebCam == null) {
				
			comboBoxWebCam = new JComboBox<>();
			comboBoxWebCam.setBounds(140, 253, 167, 22);
			webCamProvider = new WebCamProvider(comboBoxWebCam);
			webCamProvider.getListWebCam().forEach(comboBoxWebCam::addItem);
			if(webCam == null)
				getWebCamSelected(comboBoxWebCam.getSelectedItem());
			comboBoxWebCam.addActionListener(e -> getWebCamSelected(comboBoxWebCam.getSelectedItem()) );
		}
		return comboBoxWebCam;
	}
	
	private void getWebCamSelected(Object objWebCam) {
		webCam = (Webcam) objWebCam;
	}
	
	/***************************************
	 * 
	 * 				Others
	 * 
	 ***************************************/
	
	private void changeColor(JPanel panel, Color color) {
		panel.setBackground(color);
	}
}
